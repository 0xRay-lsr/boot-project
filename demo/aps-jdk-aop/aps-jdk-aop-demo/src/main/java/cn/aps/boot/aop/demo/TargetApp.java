package cn.aps.boot.aop.demo;

/**
 * @Description : 目标类
 * @Author : lishirui
 * @Date ：2025/7/9 10:03
 */
public class TargetApp {

    public static void main(String[] args) throws Exception {
        System.out.println("[App] TargetApp running...");
        TargetApp targetApp = new TargetApp();
        targetApp.sayHello();
        targetApp.sayHello("lsr");
        Thread.sleep(120000); // 保持运行 120s 供 attach
        targetApp.sayHello();
        targetApp.sayHello("lsr2");
    }

    public void sayHello() {
        System.out.println("[App] Hello from TargetApp");
    }

    public String sayHello(String name) {
        System.out.println("[App] Hello from TargetApp with return value");
        return name;
    }
}
