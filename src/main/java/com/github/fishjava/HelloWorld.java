package com.github.fishjava;

import java.io.FileOutputStream;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 看起其实也是非常简单，就是把class文件用一个数据结构来表示，然后修改它
 */
public class HelloWorld extends ClassLoader implements Opcodes {

    public static void main(final String args[]) throws Exception {

        //直接将二进制流加载到内存中
        //HelloWorld loader = new HelloWorld();
        //Class<?> exampleClass = loader.defineClass("com.github.fish.Monkey", code, 0, code.length);

        //通过反射调用main方法
        //exampleClass.getMethods()[0].invoke(null, new Object[]{null});
    }
}
