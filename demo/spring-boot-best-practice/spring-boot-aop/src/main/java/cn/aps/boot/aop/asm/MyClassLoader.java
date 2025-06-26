package cn.aps.boot.aop.asm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/5/12 15:38
 */
public class MyClassLoader extends ClassLoader {
//    private final String classPath;
//
//    public MyClassLoader(String classPath) {
//        this.classPath = classPath;
//    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String path = name.replace('.', '/') + ".class";
            InputStream in = getClass().getClassLoader().getResourceAsStream(path);
            if (in == null) {
                throw new ClassNotFoundException("找不到类: " + path);
            }
            byte[] bytes = in.readAllBytes();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("加载类失败: " + name, e);
        }
    }
}
