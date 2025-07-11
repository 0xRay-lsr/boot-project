package cn.aps.boot.aop.agent;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @Description :  Transformer 插桩逻辑（基于 ASM）
 * @Author : lishirui
 * @Date ：2025/7/9 09:57
 */
public class MyTransformer implements ClassFileTransformer {
    private final String classNameKeyword;
    private final String methodNameKeyword;
    private final String methodDescKeyword;
    public MyTransformer(String classNameKeyword, String methodNameKeyword,String methodDescKeyword) {
        this.classNameKeyword = classNameKeyword;
        this.methodNameKeyword = methodNameKeyword;
        this.methodDescKeyword = methodDescKeyword;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String dottedClassName = className.replace('/', '.');
        if (!dottedClassName.contains(classNameKeyword)) return null;

        try {
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                    MethodVisitor mv = super.visitMethod(access, name, desc, sig, ex);
                    if (!name.equals(methodNameKeyword)) return mv;
                    if (methodDescKeyword != null && !methodDescKeyword.isEmpty() && !desc.equals(methodDescKeyword)) return mv;

                    return new MethodVisitor(Opcodes.ASM9, mv) {
                        @Override
                        public void visitCode() {
                            // 方法进入
                            mv.visitLdcInsn("Enter method: " + name);
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cn/aps/boot/aop/agent/LogWriter", "writeLog", "(Ljava/lang/String;)V", false);

                            // 打印线程名
                            mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                            mv.visitInsn(Opcodes.DUP);
                            mv.visitLdcInsn("Thread: ");
                            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "getName", "()Ljava/lang/String;", false);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cn/aps/boot/aop/agent/LogWriter", "writeLog", "(Ljava/lang/String;)V", false);

                            // 记录开始时间
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                            mv.visitVarInsn(Opcodes.LSTORE, 97); // start time

                            super.visitCode();
                        }

                        @Override
                        public void visitInsn(int opcode) {
                            if (opcode == Opcodes.ARETURN) {
                                mv.visitInsn(Opcodes.DUP);
                                mv.visitVarInsn(Opcodes.ASTORE, 99);

                                mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                                mv.visitInsn(Opcodes.DUP);
                                mv.visitLdcInsn("Return value: ");
                                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
                                mv.visitVarInsn(Opcodes.ALOAD, 99);
                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cn/aps/boot/aop/agent/LogWriter", "writeLog", "(Ljava/lang/String;)V", false);
                            } else if (opcode == Opcodes.ATHROW) {
                                mv.visitInsn(Opcodes.DUP);
                                mv.visitVarInsn(Opcodes.ASTORE, 98);

                                mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                                mv.visitInsn(Opcodes.DUP);
                                mv.visitLdcInsn("Exception thrown: ");
                                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
                                mv.visitVarInsn(Opcodes.ALOAD, 98);
                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cn/aps/boot/aop/agent/LogWriter", "writeLog", "(Ljava/lang/String;)V", false);
                            }

                            // 记录结束时间并打印耗时
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                            mv.visitVarInsn(Opcodes.LLOAD, 97);
                            mv.visitInsn(Opcodes.LSUB);
                            mv.visitVarInsn(Opcodes.LSTORE, 96);

                            mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                            mv.visitInsn(Opcodes.DUP);
                            mv.visitLdcInsn("Execution time (ns): ");
                            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
                            mv.visitVarInsn(Opcodes.LLOAD, 96);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cn/aps/boot/aop/agent/LogWriter", "writeLog", "(Ljava/lang/String;)V", false);

                            super.visitInsn(opcode);
                        }
                    };
                }
            };
            cr.accept(cv, 0);
            return cw.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getTargetClassName() {
        return this.classNameKeyword; // 你已传入的类关键字
    }
}
