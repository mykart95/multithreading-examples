package com.threadcommunications;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Worker {
    private Lock lock=new ReentrantLock();
    private Condition condition=lock.newCondition();

    public void produce() throws InterruptedException {
        lock.lock();
        System.out.println("producer method...");
        condition.await();
        System.out.println("Again producer method...");
        lock.unlock();
    }

    public void consume() throws InterruptedException {
        Thread.sleep(2000);
        lock.lock();
        System.out.println("consumer method...");
        Thread.sleep(3000);
        condition.signal();
        lock.unlock();
    }
}
public class ReentrantLockExample {
    public static void main(String[] args) {
        Worker worker = new Worker();
        Thread t1 = new Thread(() -> {
            try {
                worker.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                worker.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();
    }
}
