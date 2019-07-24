package com.websocket.demo.util;

/**
 * @author: Gruuy
 * @remark:
 * @date: Create in 16:01 2019/7/19
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class AddClass {
    private static String classString = "public class Student{        "
            + "       private String  studentId;                      "
            + "       public String getStudentId(){                   "
            + "           return studentId;                           "
            + "       }                                               "
            + "       public void setStudentId(String studentId){     "
            + "           this.studentId = studentId;                 "
            + "       }                                               "
            + "}                                                      ";

    private static void createStudent() throws Exception {
        //获取java编译器(javac)
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 通过 JavaCompiler 取得标准 StandardJavaFileManager 对象，StandardJavaFileManager 对象主要负责
        // 编译文件对象的创建，编译的参数等等，我们只对它做些基本设置比如编译 CLASSPATH 等。
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);
        //自定义fileManager(文件管理器) 把JavaCompiler转发给它，他需要继承ForwardingJavaFileManager 重写获取源码（.java）的方式
        ClassJavaFileManager classJavaFileManager = new ClassJavaFileManager(standardFileManager);
        //第二个参数和表示指定java文件的类型为.java，第一个参数是路径  通过URI加载Student.java（我们也一样  通过把.java编译为.class 才能运行）  classString用于存放源码
        StringObject stringObject = new StringObject(new URI("Student.java"), JavaFileObject.Kind.SOURCE, classString);
        //初始化编译
        //第一个参数是输出流
        //第二个是文件管理器  这里是我们自定义的  动态加载类的话就在这里面
        //第三个是一个诊断监听器  监听编译过程那种玩意
        //第四个是编译器选项  定义编译器配置的  不管
        //第五个是类要由注释处理的类的名称  这里表示没有类名
        //第六个是编译单元  你就理解成  你要编译啥玩意
        JavaCompiler.CompilationTask task = compiler.getTask(null, classJavaFileManager, null, null, null,
                Arrays.asList(stringObject));
        //如果编译成功    则开始创建对象
        if (task.call()) {
            //获取在文件管理器存起来的通过javac创建的文件管理器
            ClassJavaFileObject javaFileObject = classJavaFileManager.getClassJavaFileObject();
            //新建类加载器实例
            ClassLoader classLoader = new MyClassLoader(javaFileObject);
            //通过类名获取对象的所有方法接口
            Object student = classLoader.loadClass("Student").newInstance();
            //student对象
            System.out.println("student-->"+student);
            //通过接口对象获取set方法
            Method setStudentId = student.getClass().getMethod("setStudentId",String.class);
            //使用对象调用方法赋值
            Object obj1 = setStudentId.invoke(student, "tom");
            System.out.println("-->setStudentId-->"+setStudentId.toString()+"-->"+obj1);
            //通过接口对象获取get方法
            Method getStudentId = student.getClass().getMethod("getStudentId");
            //使用对象取值
            Object obj2 = getStudentId.invoke(student);
            System.out.println("-->getStudentId-->"+getStudentId.toString()+"-->"+obj2);

        }
    }

    /**    *自定义fileManager    */
    static class ClassJavaFileManager extends ForwardingJavaFileManager{
        private ClassJavaFileObject classJavaFileObject;
        public ClassJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }
        public ClassJavaFileObject getClassJavaFileObject() {
            return classJavaFileObject;
        }
        /**这个方法一定要自定义 */
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className,
                                                   JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            return (classJavaFileObject = new ClassJavaFileObject(className,kind));
        }
    }

    /**存储源文件*/
    static class StringObject extends SimpleJavaFileObject{
        /** 类的源文件 */
        private String content;
        public StringObject(URI uri, Kind kind, String content) {
            super(uri, kind);
            this.content = content;
        }
        /**使JavaCompiler可以从content获取java源码*/
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return this.content;
        }
    }

    /**class文件（不需要存到文件中）*/
    static class ClassJavaFileObject extends SimpleJavaFileObject{
        ByteArrayOutputStream outputStream;

        public ClassJavaFileObject(String className, Kind kind) {
            super(URI.create(className + kind.extension), kind);
            this.outputStream = new ByteArrayOutputStream();
        }
        @Override
        public OutputStream openOutputStream() throws IOException {
            return this.outputStream;
        }
        /**获取输出流为byte[]数组*/
        public byte[] getBytes(){
            return this.outputStream.toByteArray();
        }
    }

    /**自定义classloader*/
    static class MyClassLoader extends ClassLoader{
        private ClassJavaFileObject stringObject;
        public MyClassLoader(ClassJavaFileObject stringObject){
            this.stringObject = stringObject;
        }
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            //通过转成字节加载类
            byte[] bytes = this.stringObject.getBytes();
            return defineClass(name,bytes,0,bytes.length);
        }
    }


    public static void main(String[] args) throws Exception {
        createStudent();
    }

}