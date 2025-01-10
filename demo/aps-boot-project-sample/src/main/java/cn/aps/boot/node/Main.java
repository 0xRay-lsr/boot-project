package cn.aps.boot.node;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/12/9 17:09
 */
public class Main {
    public static void main(String[] args) {
        Node root = new Node("1","root");

        //用户任务
        TaskNode task1 = new TaskNode("1","task1");
        root.addChild(task1);

        //排他网管关
        GatewayNode gateway1 = new GatewayNode("1","gateway1");
        TaskNode gatewayTask01 = new TaskNode("1","gatewayTask01");
        TaskNode gatewayTask02 = new TaskNode("1","gatewayTask02");
        gateway1.addChild(gatewayTask01);
        gateway1.addChild(gatewayTask02);
        root.addChild(gateway1);


        //复杂网关
        GatewayNode gateway2 = new GatewayNode("2","gateway2");
        //分支1 - 网关 - task
        TaskNode gatewayTask001 = new TaskNode("2","gatewayTask001");
        GatewayNode gateway001 = new GatewayNode("2","gateway001");
        TaskNode gatewayTask0001 = new TaskNode("2.1","gatewayTask0001");
        gateway001.addChild(gatewayTask0001);
        gatewayTask001.addChild(gateway001);
        gateway2.addChild(gatewayTask001);
        //分支2
        TaskNode gatewayTask002 = new TaskNode("2","gatewayTask002");
        gateway2.addChild(gatewayTask002);
        root.addChild(gateway2);
        try {
            System.out.println(JSON.toJSONString(root));
        }catch (Exception exception){

        }
        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1);
        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);
    }
}
