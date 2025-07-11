package cn.aps.boot.aop.attach;
import com.sun.tools.attach.VirtualMachine;


/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/7/9 10:04
 */
public class AttachLauncher {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("用法: AttachLauncher <pid> <agent.jar> <args>");
            return;
        }
        String pid = args[0];
        String agentJar = args[1];
        String agentArgs = args[2]; // eg: class=xxx;method=xxx

        VirtualMachine vm = VirtualMachine.attach(pid);
        vm.loadAgent(agentJar, agentArgs);
        vm.detach();
    }
}
