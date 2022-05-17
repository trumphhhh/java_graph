package day03;

import groovy.lang.GroovyShell;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

// -XX:MaxMetaspaceSize=24m
// 模拟不断生成类, 但类无法卸载的情况
public class TestOomTooManyClass {

    //GroovyShell属性  GroovyClassLoader loader;只要类加载器、类、类的实例对象存在，类就不能卸载，导致元空间的内存不能释放
//    static GroovyShell shell = new GroovyShell();  //静态对象变量，生命周期很长-->根对象-->根对象不能垃圾回收-->类加载器不能回收-->元空间的内存不能释放

    public static void main(String[] args) {
        AtomicInteger c = new AtomicInteger();
        while (true) {
            try (FileReader reader = new FileReader("script")) {
                GroovyShell shell = new GroovyShell();  //把上面的静态变量改为局部变量，GroovyShell对象不使用了，既可以回收
                shell.evaluate(reader);
                System.out.println(c.incrementAndGet());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
