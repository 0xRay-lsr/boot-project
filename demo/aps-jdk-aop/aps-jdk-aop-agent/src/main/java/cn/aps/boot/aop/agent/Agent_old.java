package cn.aps.boot.aop.agent;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : agent字节码增强
 * @Author : lishirui
 * @Date ：2025/7/9 09:56
 */
public class Agent_old {
    private static MyTransformer transformer;

    /**
     * java -javaagent:aps-jdk-aop-agent-1.0-SNAPSHOT.jar="class=cn.aps.boot.aop.demo.TargetApp;method=sayHello;desc=(Ljava/lang/String;)Ljava/lang/String;" \
     *      -cp aps-jdk-aop-demo-1.0-SNAPSHOT.jar cn.aps.boot.aop.demo.TargetApp
     * @param agentArgs
     * @param inst
     * @throws Exception
     */
    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        System.out.println("[Agent] premain() called");
        agentmain(agentArgs, inst);
    }


    /**
     * 通过attach机制实现动态加载
     * @param agentArgs
     * @param inst
     * @throws Exception
     */
    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        System.out.println("[Agent] agentmain() called (动态 attach) params : "+ agentArgs);
//        attachLogic(inst);
        try {
            Map<String, String> argMap = parseArgs(agentArgs); // class=xxx;method=xxx
            String methodDesc = argMap.getOrDefault("desc", "");
            String classKeyword = argMap.getOrDefault("class", "");
            String methodKeyword = argMap.getOrDefault("method", "");

            transformer = new MyTransformer(classKeyword, methodKeyword, methodDesc);
            inst.addTransformer(transformer, true);

            for (Class<?> clazz : inst.getAllLoadedClasses()) {
                if (clazz.getName().contains(classKeyword) && inst.isModifiableClass(clazz)) {
                    System.out.println("[Agent] Retransforming loaded class: " + clazz.getName());
                    inst.retransformClasses(clazz);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private static void attachLogic(Instrumentation inst) throws Exception {
        String classNameKeyword = "TargetApp";
        String methodNameKeyword = "sayHello";

        transformer = new MyTransformer(classNameKeyword, methodNameKeyword, null);
        inst.addTransformer(transformer, true);

        for (Class<?> clazz : inst.getAllLoadedClasses()) {
            if (inst.isModifiableClass(clazz) && clazz.getName().contains(classNameKeyword)) {
                System.out.println("[Agent] Retransforming loaded class: " + clazz.getName());
                inst.retransformClasses(clazz);
            }
        }
    }

    // 提供 Transformer 卸载入口（可通过外部控制调用）
    public static void removeTransformer(Instrumentation inst) {
        if (transformer != null) {
            inst.removeTransformer(transformer);
            System.out.println("[Agent] Transformer 已卸载");
        }
    }
}
