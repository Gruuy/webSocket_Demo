package com.websocket.demo;

import com.websocket.demo.Handler.TestHandler;
import com.websocket.demo.service.TestService;
import com.websocket.demo.service.serviceImpl.TestServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.Arrays;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
        TestService testService=new TestServiceImpl();
        TestHandler testHandler=new TestHandler(testService);

        /** 给类加载器，接口列表，以及实现了动态代理接口的实例 */
        TestService proxy=(TestService) Proxy.newProxyInstance(testService.getClass().getClassLoader(),testService.getClass().getInterfaces(),testHandler);
        proxy.test();
    }

    @Test
    public void createStudent(){
        //通过 ToolProvider 取得 JavaCompiler 对象，JavaCompiler 对象是动态编译工具的主要对象
//        JavaCompiler compiler= ToolProvider.getSystemJavaCompiler();
//        // 通过 JavaCompiler 取得标准 StandardJavaFileManager 对象，StandardJavaFileManager 对象主要负责
//        // 编译文件对象的创建，编译的参数等等，我们只对它做些基本设置比如编译 CLASSPATH 等。
//        StandardJavaFileManager standardJavaFileManager=compiler.getStandardFileManager(null,null,null);
//        ClassJavaFileManager classJavaFileManager = new ClassJavaFileManager(standardJavaFileManager);
//        StringObject stringObject = new StringObject(new URI("Student.java"), JavaFileObject.Kind.SOURCE, classString);
//        JavaCompiler.CompilationTask task = compiler.getTask(null,classJavaFileManager,null,null,null, Arrays.asList(stringObject));
//        if(task.call()){
//            ClassJavaFileObject javaFileObject =  classJavaFileManager.getClassJavaFileObject();
//            ClassLoader classLoader = new MyClassLoader(javaFileObject);
//            Object student = classLoader.loadClass("Student").newInstance();
//            Method getStudetnId = student.getClass().getMethod("getStudentId");
//            Object invoke = getStudetnId.invoke(student);
//            logger.info("class==={}",student);
//        }
    }

}
