/*
授权声明：
本源码系《Java多线程编程实战指南（核心篇）》一书（ISBN：978-7-121-31065-2，以下称之为“原书”）的配套源码，
欲了解本代码的更多细节，请参考原书。
本代码仅为原书的配套说明之用，并不附带任何承诺（如质量保证和收益）。
以任何形式将本代码之部分或者全部用于营利性用途需经版权人书面同意。
将本代码之部分或者全部用于非营利性用途需要在代码中保留本声明。
任何对本代码的修改需在代码中以注释的形式注明修改人、修改时间以及修改内容。
本代码可以从以下网址下载：
https://github.com/Viscent/javamtia
http://www.broadview.com.cn/31065
*/
package com.mtia.ch5;

import com.mtia.util.Debug;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TerminatableTaskRunner implements TaskRunnerSpec {
    protected final BlockingQueue<Runnable> channel;
    // 线程停止标记
    protected volatile boolean inUse = true;
    // 待处理任务计数器
    public final AtomicInteger reservations = new AtomicInteger(0);
    private volatile Thread workerThread;

    public TerminatableTaskRunner(BlockingQueue<Runnable> channel) {
        this.channel = channel;
        this.workerThread = new WorkerThread();
    }

    public TerminatableTaskRunner() {
        this(new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void init() {
        final Thread t = workerThread;
        if (null != t) {
            t.start();
        }
    }

    @Override
    public void submit(Runnable task) throws InterruptedException {
        channel.put(task);
        reservations.incrementAndGet();
    }

    public void shutdown() {
        Debug.info("Shutting down service...");
        inUse = false;// 语句①
        final Thread t = workerThread;
        if (null != t) {
            t.interrupt();// 语句②
        }
    }

    public void cancelTask() {
        Debug.info("Canceling in progress task...");
        workerThread.interrupt();
    }

    class WorkerThread extends Thread {
        @Override
        public void run() {
            Runnable task = null;
            try {
                for (; ; ) {
                    // 线程不再被需要，且无待处理任务
                    if (!inUse && reservations.get() <= 0) {// 语句③
                        break;
                    }
                    task = channel.take();
                    try {
                        // 1、task已经停止，此时就会抛异常InterruptedException，那么run返回，线程停止
                        // 2、如果在调用shutdown()那一刻，目标线程正在执行task.run()且task.run()中的代码清空了线程中断标记，
                        //     那么channel.take()调用无法抛出InterruptedException（因为中断标记被清除了）。但是会通过inUse
                        //     来实现停止线程
                        // 3、task在run方法中需要判断Thread.currentThread().isInterrupted()，否则会一直阻塞
                        task.run();
                    }
                    catch (Throwable e) {
                        e.printStackTrace();
                    }
                    // 使待处理任务数减少1
                    reservations.decrementAndGet();// 语句④
                }
            }
            catch (InterruptedException e) {
                // 停止worker线程
                workerThread = null;
            }
            Debug.info("worker thread terminated.");
        }
    }
}