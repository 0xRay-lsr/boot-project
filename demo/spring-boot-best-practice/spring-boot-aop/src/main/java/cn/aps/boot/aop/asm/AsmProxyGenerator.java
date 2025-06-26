package cn.aps.boot.aop.asm;

import org.objectweb.asm.*;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Paths;

import static org.objectweb.asm.Opcodes.*;

/**
 * ASM代理生成器，用于生成增强版的HelloService类
 */
public class AsmProxyGenerator {
    private static final String TARGET_CLASS = "cn/aps/boot/aop/asm/EnhancedHelloService";
    private static final String SYSTEM_OUT = "java/lang/System";
    private static final String PRINT_STREAM = "java/io/PrintStream";

    public static void main(String[] args) throws Exception {
        // 1. 读取原始类
        ClassReader reader = new ClassReader("cn.aps.boot.aop.asm.HelloService");
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        // 2. 创建ClassVisitor修改类
        ClassVisitor visitor = new ClassVisitor(ASM9, writer) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                // 修改类名
                super.visit(version, access, TARGET_CLASS, signature, superName, interfaces);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if ("sayHello".equals(name)) {
                    return new MethodEnhancer(ASM9, methodVisitor);
                }
                return methodVisitor;
            }
        };

        // 3. 处理类
        reader.accept(visitor, ClassReader.EXPAND_FRAMES);
        byte[] bytes = writer.toByteArray();

        // 4. 获取输出路径
        URL url = AsmProxyGenerator.class.getResource("/cn/aps/boot/aop/asm/");
        File dir = Paths.get(url.toURI()).toFile();
        File outFile = new File(dir, "EnhancedHelloService.class");

        // 5. 写入文件
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            fos.write(bytes);
        }
        System.out.println("生成成功：" + outFile.getAbsolutePath());
    }

    /**
     * 方法增强器
     */
    private static class MethodEnhancer extends MethodVisitor {
        private final MethodVisitor mv;

        public MethodEnhancer(int api, MethodVisitor mv) {
            super(api, mv);
            this.mv = mv;
        }

        @Override
        public void visitCode() {
            super.visitCode();
            // 在方法开始前插入日志
            insertPrintln("before Hello World");
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == RETURN) {
                // 在方法返回前插入日志
                insertPrintln("after Hello World");
            }
            super.visitInsn(opcode);
        }

        private void insertPrintln(String message) {
            mv.visitFieldInsn(GETSTATIC, SYSTEM_OUT, "out", "L" + PRINT_STREAM + ";");
            mv.visitLdcInsn(message);
            mv.visitMethodInsn(INVOKEVIRTUAL, PRINT_STREAM, "println", "(Ljava/lang/String;)V", false);
        }
    }
}
