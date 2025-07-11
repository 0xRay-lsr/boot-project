package cn.aps.boot.aop.agent;

import cn.aps.boot.aop.agent.classz.StringBuilderSinkFactory;
import org.benf.cfr.reader.api.CfrDriver;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description : agent字节码增强
 * @Author : lishirui
 * @Date ：2025/7/9 09:56
 */
public class Agent {
    private static Instrumentation instGlobal;
    private static MyTransformer transformer;

    private static List<MyTransformer> myTransformerList = new ArrayList<>();
    // 使用 AtomicBoolean 确保服务器只启动一次
    private static final AtomicBoolean serversStarted = new AtomicBoolean(false);

    public static void premain(String agentArgs, Instrumentation inst) {
        instGlobal = inst;
        System.out.println("[Agent] premain() called");
        // existing premain code...
        if (serversStarted.compareAndSet(false, true)) {
            startSocketServer();
            startHttpControlServer();  // HTTP 控制接口
        }
    }
    private static final AtomicBoolean firstLoading = new AtomicBoolean(false);
    /**
     * 通过attach机制实现动态加载
     *
     * @param agentArgs
     * @param inst
     * @throws Exception
     */
    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        instGlobal = inst;
        System.out.println("[Agent] agentmain() called (动态 attach) params : " + agentArgs);
        if (agentArgs == null || agentArgs.equals("")) {
            if (firstLoading.compareAndSet(false, true)) {
                System.out.println("[Agent] first loading args is null , skip agentmain()");
                return;
            }else  {
                System.out.println("[Agent] please check params is empty , skip agentmain() !!!");
                return;
            }
        }
        try {
            Map<String, String> argMap = parseArgs(agentArgs); // class=xxx;method=xxx
            retransformClasses(argMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * java -javaagent:aps-jdk-aop-agent.jar -jar app.jar
     * echo "class=cn.aps.demo.TargetApp;method=sayHello;desc=(Ljava/lang/String;)Ljava/lang/String;" | nc localhost 11223
     */
    private static void startSocketServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(11223)) {
                System.out.println("[Agent] Socket server started on port 11223");
                while (true) {
                    try (Socket socket = serverSocket.accept()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String line = reader.readLine(); // 命令格式：class=...;method=...;desc=...
                        System.out.println("[Agent] Received socket command: " + line);
                        if (line != null && !line.trim().isEmpty()) {
                            Map<String, String> argMap = parseArgs(line);
                            retransformClasses(argMap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "AgentSocketServer").start();
    }

    /**
     * 增强指定方法
     *
     * @param argMap
     * @throws Exception
     */
    private static void retransformClasses(Map<String, String> argMap) throws Exception {
        String classKeyword = argMap.getOrDefault("class", "");
        String methodKeyword = argMap.getOrDefault("method", "");
        String methodDesc = argMap.getOrDefault("desc", "");
        transformer = new MyTransformer(classKeyword, methodKeyword, methodDesc);
        myTransformerList.add(transformer);
        instGlobal.addTransformer(transformer, true);
        for (Class<?> clazz : instGlobal.getAllLoadedClasses()) {
            if (clazz.getName().contains(classKeyword) && instGlobal.isModifiableClass(clazz)) {
                System.out.println("[Agent] Retransforming loaded class: " + clazz.getName());
                instGlobal.retransformClasses(clazz);
            }
        }
    }

    private static Map<String, String> parseArgs(String raw) {
        Map<String, String> map = new HashMap<>();
        if (raw == null || raw.isEmpty()) return map;
        for (String pair : raw.split(";")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) map.put(kv[0], kv[1]);
        }
        return map;
    }

    /**
     * # 增强某方法
     * curl "http://localhost:11414/retransform?class=cn.aps.demo.TargetApp&method=sayHello&desc=(Ljava/lang/String;)Ljava/lang/String;"
     * <p>
     * # 查看已加载类
     * curl http://localhost:11414/list
     * <p>
     * # 查看日志
     * curl http://localhost:11414/log
     * <p>
     * # 卸载增强
     * curl http://localhost:11414/remove
     * <p>
     * # 帮助菜单
     * curl http://localhost:11414/help
     */
    private static void startHttpControlServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(11414)) {
                System.out.println("[Agent] HTTP Control started on http://localhost:11414");

                while (true) {
                    Socket socket = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    String line = reader.readLine();
                    if (line == null || !line.startsWith("GET")) continue;

                    String path = line.split(" ")[1]; // 例如：/retransform?class=...&method=...&desc=...

                    String response;

                    try {
                        if (path.startsWith("/retransform")) {
                            Map<String, String> params = parseQuery(path);
                            retransformClasses(params);
                            response = "SUCCESS: Retransform done.";
                        } else if (path.startsWith("/list")) {
                            StringBuilder sb = new StringBuilder();
                            for (Class<?> clazz : instGlobal.getAllLoadedClasses()) {
                                sb.append(clazz.getName()).append("\n");
                            }
                            response = sb.toString();
                        } else if (path.startsWith("/log")) {
                            Path logPath = Paths.get(System.getProperty("user.dir"), "java-agent.log");
                            if (Files.exists(logPath)) {
                                response = Files.readString(logPath);
                            } else {
                                response = "Log file not found.";
                            }
                        } else if (path.startsWith("/remove")) {
                            removeTransformer(instGlobal);
                            response = "Transformer removed.";
                        } else if (line.contains("/jad")) {
                            response = handleJad(path);
                        } else {
                            response = """
                                    Agent HTTP 控制台可用命令：
                                    /retransform?class=xxx&method=yyy&desc=zzz  - 增强指定方法
                                    /list                                       - 查看已加载类
                                    /log                                        - 查看增强日志
                                    /remove                                     - 卸载当前增强逻辑
                                    /help                                       - 查看帮助信息
                                    """;
                        }
                    } catch (Exception e) {
                        response = "ERROR: " + e.getMessage();
                        e.printStackTrace();
                    }
                    // 输出 HTTP 响应
                    writer.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain; charset=UTF-8\r\n\r\n");
                    writer.write(response);
                    writer.flush();
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "AgentHttpControl").start();
    }

    /**
     * http://localhost:9999/jad?class=com.example.MyClass
     *
     * @param path
     * @return
     */
    public static String handleJad(String path) {
        try {
            Map<String, String> params = parseQuery(path);
            String className = params.get("class");

            if (className == null) {
                return "ERROR: class parameter is required";
            }

            Class<?> clazz = Class.forName(className);
            String internalName = className.replace('.', '/') + ".class";
            InputStream is = clazz.getClassLoader().getResourceAsStream(internalName);

            if (is == null) {
                return "ERROR: Cannot load class bytes for " + className;
            }

            // 保存 class 到临时文件
            byte[] classBytes = is.readAllBytes();
            java.nio.file.Path tempDir = java.nio.file.Files.createTempDirectory("cfr");
            java.nio.file.Path classFile = tempDir.resolve(className.substring(className.lastIndexOf('.') + 1) + ".class");
            java.nio.file.Files.write(classFile, classBytes);

            // 调用 CFR 反编译
            StringBuilder result = new StringBuilder();
            CfrDriver driver = new CfrDriver.Builder()
                    .withOutputSink(new StringBuilderSinkFactory(result))
                    .build();

            driver.analyse(Collections.singletonList(classFile.toAbsolutePath().toString()));
            return result.toString();
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }


    // GET 参数解析器
    private static Map<String, String> parseQuery(String url) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        int idx = url.indexOf('?');
        if (idx < 0) return map;
        String[] pairs = url.substring(idx + 1).split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8"));
            }
        }
        return map;
    }

    // 提供 Transformer 卸载入口（可通过外部控制调用）
    public static void removeTransformer(Instrumentation inst) {
        for (MyTransformer transformer : myTransformerList) {
            inst.removeTransformer(transformer);
            System.out.println("[Agent] Removed transformer: " + transformer);

            // 回滚已增强类
            String classKeyword = transformer.getTargetClassName(); // 你要在 MyTransformer 中保留这个字段
            for (Class<?> clazz : inst.getAllLoadedClasses()) {
                if (clazz.getName().contains(classKeyword) && inst.isModifiableClass(clazz)) {
                    try {
                        inst.retransformClasses(clazz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        myTransformerList.clear();
    }
}

