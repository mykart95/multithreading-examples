package com.threadcommunications;

import java.util.ArrayList;
import java.util.List;

public class ProducerConsumerProblem {
    public static void main(String[] args) {
        Processor processor=new Processor();
        Thread t1=new Thread(()->{
            try {
                processor.producer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2=new Thread(()->{
            try {
                processor.consumer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();
    }

}

class Processor{
    private static List<Integer> list=new ArrayList<>();
    private static int UPPER_LIMIT=5;
    private static int LOWER_LIMIT=0;
    private final Object lock = new Object();
    private int value=0;

    public void producer() throws InterruptedException {
        synchronized (lock){
            while (true){
                if(list.size()==UPPER_LIMIT){
                    System.out.println("Waiting for items to removed...");
                    lock.wait();
                }else {
                    System.out.println("Adding: "+value);
                    list.add(value);
                    value++;
                    // we can call the notify - because the other thread will be notified
                    // only when it is in a waiting state
                    lock.notify();
                    //do other tasks
                }
                Thread.sleep(500);
            }
        }
    }

    public void consumer() throws InterruptedException {
        synchronized (lock){
            while (true){
                if(list.size()==LOWER_LIMIT){
                    System.out.println("Waiting for items to Adding...");
                    value=0;
                    lock.wait();
                }else {
                    System.out.println("removing : "+list.remove(list.size()-1));
                    lock.notify();
                    //do furthur operations Once list is full thread will under waiting state but its already notified other thread
                }
                Thread.sleep(500);
            }
        }
    }
}
