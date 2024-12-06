package cn.aps.boot.demo.java8.abs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/8/29 10:15
 */
public class Main {
    public static void main(String[] args) {
//        //手动new具体实现
//        Pay pay = new ZhifubaoPay();
//        pay.initPay();
//        pay.sendPay("zhangsan","lisi",1);
//
//        //通过工厂方式获取具体实现
//        Pay weixin = PayFactory.getPay(PayType.WEIXIN);
//        weixin.initPay();
//        weixin.sendPay("zhangsan","lisi",1);
//
//        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
//        System.out.println(runtimeMXBean.getName());

        String sqlParams = "param:['fdsafad',TO_DATA(dsfdsas),'fdasaf,fdafasd,fdafa','test','dfas'fdasf'fdasfasd']";

        List<String> paramsList = parseSQLParams5(sqlParams);

        for (String param : paramsList) {
            System.out.println(param);
        }
    }


    public static List<String> parseSQLParams5(String sqlParams) {
        List<String> paramsList = new ArrayList<>();

        // 使用正则表达式匹配参数
        Pattern pattern =  Pattern.compile("'[^']*'|[^,\\[\\]]+");
        Matcher matcher = pattern.matcher(sqlParams);

        // 跳过第一个匹配项，因为它是 'param:'
        if (matcher.find()) {
            matcher.group();
        }

        while (matcher.find()) {
            String param = matcher.group().replaceAll("^'|'$", ""); // 去除首尾单引号
            paramsList.add("'" + param + "'");
        }

        return paramsList;
    }

    public static List<String> parseSQLParams4(String sqlParams) {
        List<String> paramsList = new ArrayList<>();

        // 使用正则表达式匹配参数
        Pattern pattern = Pattern.compile("'[^']*'|\\w+\\(.*?\\)|[^,\\[\\]]+");
        Matcher matcher = pattern.matcher(sqlParams);

        // 跳过第一个匹配项，因为它是 'param:'
        if (matcher.find()) {
            matcher.group();
        }

        while (matcher.find()) {
            String param = matcher.group().replaceAll("^'|'$", ""); // 去除首尾单引号
            paramsList.add("'" + param + "'");
        }

        return paramsList;
    }

    public static List<String> parseSQLParams3(String sqlParams) {
        List<String> paramsList = new ArrayList<>();

        // 使用正则表达式匹配参数
        Pattern pattern = Pattern.compile("'[^']*'|\\w+\\(.*?\\)|[^,\\[\\]]+");
        Matcher matcher = pattern.matcher(sqlParams);

        while (matcher.find()) {
            String param = matcher.group().replaceAll("^'|'$", ""); // 去除首尾单引号
            paramsList.add("'" + param + "'");
        }

        return paramsList;
    }
    public static List<String> parseSQLParams2(String sqlParams) {
        List<String> paramsList = new ArrayList<>();

        // 使用正则表达式匹配参数
        Pattern pattern = Pattern.compile("'(.*?)'|\\w+\\(.*?\\)|\\w+");
        Matcher matcher = pattern.matcher(sqlParams);

        while (matcher.find()) {
            String param = matcher.group().replaceAll("^'|'$", ""); // 去除首尾单引号
            paramsList.add("'" + param + "'");
        }

        return paramsList;
    }

    public static List<String> parseSQLParams(String sqlParams) {
        List<String> paramsList = new ArrayList<>();

        // 使用正则表达式匹配参数
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(sqlParams);

        while (matcher.find()) {
            paramsList.add(matcher.group(1));
        }

        return paramsList;
    }

    public static List<String> parseSQLParams1(String sqlParams) {
        List<String> paramsList = new ArrayList<>();

        // 使用正则表达式匹配参数
        Pattern pattern = Pattern.compile("'[^']*'|\\w+\\([\\w,']*\\)|\\w+");
        Matcher matcher = pattern.matcher(sqlParams);

        while (matcher.find()) {
            paramsList.add(matcher.group().replaceAll("'",""));
        }

        return paramsList;
    }
}
