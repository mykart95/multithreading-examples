package com.threadcommunications;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Livelock {
    private Lock lock1=new ReentrantLock();
    private Lock lock2= new ReentrantLock();

    private void worker1(){
        while(true){
            try {
                lock1.tryLock(50, TimeUnit.MILLISECONDS);
                System.out.println("Worker1 acquired the lock1...");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Worker1 try to acquire lock2...");
            if(lock2.tryLock()){
                System.out.println("Worker1 acquires the lock2...");
                lock2.unlock();
            }else {
                System.out.println("Worker1 cannot acquire lock2...");
                continue;
            }
            break;
        }
        lock1.unlock();
        lock2.unlock();
    }

    private void worker2() {
        while (true){
            try {
                lock2.tryLock(50, TimeUnit.MILLISECONDS);
                System.out.println("Worker2 acquires lock2...");
                Thread.sleep(3000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Worker2 tries to get lock1...");
            if (lock1.tryLock()){
                System.out.println("Worker2 acquires the lock1...");
                lock1.unlock();
            }else {
                System.out.println("Worker2 cannot acquires the lock1");
                continue;
            }
            break;
        }
        lock1.unlock();
        lock2.unlock();
    }

    public static void main(String[] args) {
        Livelock livelock=new Livelock();
        new Thread(livelock::worker1,"Worker1").start();
        new Thread(livelock::worker2, "Worker2").start();
    }
}
