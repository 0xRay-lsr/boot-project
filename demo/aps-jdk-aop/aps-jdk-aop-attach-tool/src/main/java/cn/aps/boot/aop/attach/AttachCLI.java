package cn.aps.boot.aop.attach;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @Description : 可持续 Attach JVM 并进行 Agent 增强的工具
 * 1. 列出所有的 JVM 进程
 * 2. 选择要 attach 的 JVM 进程
 * 3. 首次注入 Agent 时，输入 agent jar 的绝对路径
 * 4. 之后进入循环，持续接收 agentArgs 参数（例如 class=xxx;method=xxx）
 * 5. 执行 attach 操作并加载 agent (首次)，或发送新的 agentArgs (后续)
 * 6. 成功注入/发送指令后，等待下一次指令
 * 7. 输入 'q' 或 'quit' 退出 attach 工具
 * <p>
 * 注意：
 * 1. 确保目标 JVM 进程已经启动。
 * 2. Agent Jar 必须支持 `agentmain` 方法（如果后续 attach）或 `premain` 方法（如果启动时 attach）。
 * 3. Agent Jar 的 MANIFEST.MF 中需要包含 `Can-Retransform-Classes: true` 以支持类重定义。
 * 4. Agent Jar 内部需要处理 `agentArgs`，并使用 `Instrumentation.retransformClasses()` 进行类的字节码修改。
 * 5. 目标 JVM 需要运行在 JDK 环境下（而不是 JRE），或者至少包含 tools.jar。
 * @Author : lishirui
 * @Date ：2025/7/11 08:44
 */

public class AttachCLI {
    /**
     * 可用 JVM 进程：
     * [0] 12345 : org.springframework.boot.loader.JarLauncher
     * [1] 23456 : org.jetbrains.idea.Main
     * 请选择要 attach 的 JVM 序号：0
     * 请输入 agent jar 的绝对路径：/Users/lsr/Desktop/test-agent/aps-jdk-aop-agent.jar
     * 请输入 agentArgs 参数：class=cn.aps.TestController;method=sayHello
     * Agent 注入成功！
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        VirtualMachine vm = null;
        try {
            List<VirtualMachineDescriptor> vms = VirtualMachine.list();
            if (vms.isEmpty()) {
                System.out.println("当前没有可 attach 的 JVM 进程。");
                return;
            }

            System.out.println("可用 JVM 进程：");
            for (int i = 0; i < vms.size(); i++) {
                VirtualMachineDescriptor vmd = vms.get(i);
                System.out.printf("[%d] %s : %s%n", i, vmd.id(), vmd.displayName());
            }

            System.out.print("请选择要 attach 的 JVM 序号：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 清除换行符

            VirtualMachineDescriptor selectedVm = vms.get(choice);
            String targetPid = selectedVm.id();
            System.out.print("请输入 agent jar 的绝对路径：");
            String agentPath = scanner.nextLine();

            System.out.println("Attaching to PID: " + targetPid);
            vm = VirtualMachine.attach(targetPid);
            System.out.println("成功连接到 JVM (PID: " + targetPid + ", Display: " + selectedVm.displayName() + ")");
            // 加载 agent ，首次加载agent 不传递参数
            // vm.loadAgent(agentPath, agentArgs);
            vm.loadAgent(agentPath);
            System.out.println("Agent JAR '" + agentPath + "' 已成功注入到目标 JVM。");
            System.out.println("-------------------------------------------------------");
            System.out.println("Agent 内部的 Socket 服务器 (端口 11223) 和 HTTP 控制服务器 (端口 11414) 已启动。");
            System.out.println("您现在可以使用 'nc' 或 'curl' 命令来与 Agent 交互，进行持续增强。");
            System.out.println("例如：");
            System.out.println("  通过 Socket: echo \"class=cn.aps.demo.TargetApp;method=sayHello\" | nc localhost 11223");
            System.out.println("  通过 HTTP: curl \"http://localhost:11414/retransform?class=cn.aps.demo.TargetApp&method=sayHello\"");
            System.out.println("  更多 HTTP 命令请访问 http://localhost:11414/help");
            System.out.println("-------------------------------------------------------");
            while (true) {
                System.out.print("\n请输入 agentArgs 参数（例如 class=xxx;method=xxx），或输入 'q'/'quit' 退出：");
                String agentArgs = scanner.nextLine().trim();
                if ("q".equalsIgnoreCase(agentArgs) || "quit".equalsIgnoreCase(agentArgs)) {
                    System.out.println("检测到退出指令，程序将退出。");
                    break; // 退出循环
                }
                if (agentArgs.isEmpty()) {
                    System.out.println("AgentArgs 不能为空，请重新输入。");
                    continue;
                }
                try {
                    // 再次调用 loadAgent 来触发 Agent 的 agentmain 方法
                    // 这里的关键是 agentmain 方法需要处理多次调用时的逻辑，
                    // 并且利用 instrumentation 实例进行类的 retransformation。
                    // 传递 agentArgs 给目标 Agent，让它知道要增强哪个类。
                    vm.loadAgent(agentPath, agentArgs);
                    System.out.println("Agent 指令发送成功！参数: " + agentArgs);
                    // 给予一些时间让Agent完成操作
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (Exception e) {
                    System.err.println("发送 Agent 指令失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException ioException) {
            System.err.println("I/O 错误或无法连接到 JVM。请确保目标 JVM 允许 attach 且 agent path 正确。");
            ioException.printStackTrace();
        } catch (Exception e) {
            System.err.println("发生未知错误：" + e.getMessage());
            e.printStackTrace();
        } finally {
            // 在程序退出时进行 detach
            if (vm != null) {
                try {
                    // 尽管 Agent 的线程会继续运行，但断开 Attacher 与目标 JVM 的连接是一个好习惯。
                    vm.detach();
                    System.out.println("AttachCLI 已从目标 JVM 分离。");
                } catch (IOException e) {
                    System.err.println("从 JVM 分离失败：" + e.getMessage());
                }
            }
            scanner.close();
            System.out.println("AttachCLI 程序已结束。");
        }
    }
}