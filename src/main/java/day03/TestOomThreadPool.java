package day03;

import day02.LoggerUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// -Xmx64m
// 模拟短信发送超时，但这时仍有大量的任务进入队列
public class TestOomThreadPool {
    public static void main(String[] args) {
        //线程池误用:Executors源码，newFixedThreadPool(int nThreads);没有救急线程，不需要设置生存时间，工作队列使用的是LinkedBlockingQueue,可以放无限个(整数最大值)任务
        //解决：不要用Executors工具类的newFixedThreadPool(),因为newFixedThreadPool()中的队列是无界队列，没有设置队列上限；建议使用线程池的构造方法【new ThreadPoolExecutors(...)】，设置有大小限制的工作队列
        //ExecutorService executor = Executors.newFixedThreadPool(2);
        
        //newCachedThreadPool()的实现，newFixedThreadPool(0，2147483647..,new SynchronousQueue()),最多放一个任务，但是没有核心线程，救急线程是整数最大值，对线程数量没有限制，任务繁多会造成内存溢出
        ExecutorService executor = Executors.newCachedThreadPool();  //虚拟机实验
        LoggerUtils.get().debug("begin...");
        while (true) {
            executor.submit(()->{
                try {
                    LoggerUtils.get().debug("send sms...");//前两个任务超时时，剩下的所有任务都会被加入工作/任务队列中LinkedBlockingQueue，任务对象所耗费的内存导致堆内存的耗尽
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
