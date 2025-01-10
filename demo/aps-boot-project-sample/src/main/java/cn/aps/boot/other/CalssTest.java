package cn.aps.boot.other;

import org.springframework.context.annotation.Bean;

import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/12/25 10:02
 */
public class CalssTest {
    public static Map<Class,Object> map = new HashMap<Class,Object>();
    public static void main(String[] args) {
        map.put(Test.class,"llll");
        Object o = map.get(Test.class);
        System.out.println(o);
        Class<Test> testClass = Test.class;
        System.out.println(testClass.getName());
        ProtectionDomain protectionDomain = testClass.getProtectionDomain();
        System.out.println(protectionDomain.getCodeSource());
    }
    class Test {
        private String name = "test";
    }
}
