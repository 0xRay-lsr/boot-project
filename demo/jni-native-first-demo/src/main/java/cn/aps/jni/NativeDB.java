package cn.aps.jni;

/**
 * @Description : 模拟实现java通过navicat调用c++库
 * @Author : lishirui
 * @Date ：2025/4/25 09:48
 */
public class NativeDB {

    static {
        System.loadLibrary("NativeDB");
    }

    public native boolean connect(String url);

    public native String query(String sql);

    public static void main(String[] args) {
        NativeDB nativeDB = new NativeDB();
        boolean connect = nativeDB.connect("jdbc:mysql://localhost:3306/test");
        if (connect) {
            String result = nativeDB.query("select * from user");
            System.out.println("查询结果为" + result);
        } else {
            System.out.println("<UNK>");
        }
    }
}
