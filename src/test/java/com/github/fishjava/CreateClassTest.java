package com.github.fishjava;

import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.FileOutputStream;

import static org.objectweb.asm.Opcodes.*;

/**
 * 使用ASM编写一个class文件
 */
public class CreateClassTest {
    @Test
    public void constructor() throws Exception{
        //定义一个叫做com.github.fishjava.Monkey的类
        ClassWriter cw;
        cw = new ClassWriter(0);

        /**
         * v9: jvm9 版本的class文件
         * ACC_PROTECTED: PROTECTED类型的文件
         * 然后是方法名
         */
        cw.visit(V9, ACC_PROTECTED, "com/github/fishjava/Monkey",
                null, "java/lang/Object", null);

        /**
         * 生成私有的构造方法
         * 用 '<init>' 表示
         * 返回一个表示这个方法的函数，我们就通过它来设置字节码
         */
        MethodVisitor mw = cw.visitMethod(ACC_PRIVATE, "<init>", "()V", null, null);

        /**
         * 构造函数初始化就是
         *
         *   - 将this（也就是当前对象入栈）
         *     this在本地变量表的索引为0，所以是 ALOAD 0
         *
         *   - 执行this的 init 方法
         *     - 调用父类的 init 方法
         *       也就四 invokespecial
         *
         *   - 返回结果
         *     因为是void的返回类型，使用RETURN指令
         */
        mw.visitVarInsn(ALOAD, 0);
        mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mw.visitInsn(RETURN);

        /**
         * 说明下栈的大小以及本地变量的个数
         * 参数数量不需要写，可以自动的词法分析出来
         */
        mw.visitMaxs(1, 1);
        mw.visitEnd();

        // 获取生成的class文件对应的二进制流
        byte[] code = cw.toByteArray();

        //将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream("Monkey.class");
        fos.write(code);
        fos.close();
    }

    @Test
    public void staticMethod() throws Exception{
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V9, ACC_PROTECTED, "com/github/fishjava/Counter",
                null, "java/lang/Object", null);

        MethodVisitor mw;

        //生成public static int add(int a, int b) 方法
        mw = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
                "add", "(II)I", null, null);

        /**
         * 这种情况下有两个本地变量a 和 b，它们在本地变量表中的索引依次为0 和 1
         *
         * 执行的时候先把两个本地变量压入栈，然后执行IADD
         * 再将结果返回就行了
         */
        mw.visitVarInsn(ILOAD, 0);
        mw.visitVarInsn(ILOAD, 1);
        mw.visitInsn(IADD);
        mw.visitInsn(IRETURN);
        mw.visitMaxs(2, 2);

        mw.visitEnd();

        // 获取生成的class文件对应的二进制流
        byte[] code = cw.toByteArray();


        //将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream("Counter.class");
        fos.write(code);
        fos.close();
    }

    @Test
    public void field() throws Exception{
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V9, ACC_PROTECTED, "com/github/fishjava/Fish",
                null, "java/lang/Object", null);

        /**
         * private int age;
         */
        FieldVisitor fieldVisitor = cw.visitField(ACC_PRIVATE, "age", "I", null, null);
        fieldVisitor.visitEnd();

        byte[] code = cw.toByteArray();
        FileOutputStream fos = new FileOutputStream("Fish.class");
        fos.write(code);
        fos.close();
    }
}
