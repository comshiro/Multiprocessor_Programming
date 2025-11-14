package org.example;

public class exemplificare_DoubleLockedQueue {
    public static void main(String[] args) {
        DoubleLockBasedQueue queue = new DoubleLockBasedQueue();

        // doi producatori
        Thread producer1 = new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                queue.enq(i);
                try { Thread.sleep(20); } catch (InterruptedException e) {}
            }
        }, "Producer-1");

        Thread producer2 = new Thread(() -> {
            for (int i = 101; i <= 120; i++) {
                queue.enq(i);
                try { Thread.sleep(30); } catch (InterruptedException e) {}
            }
        }, "Producer-2");

        // doi consumatori
        Thread consumer1 = new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                int val = queue.deq();
                System.out.println("Consumer-1 a procesat: " + val);
                try { Thread.sleep(50); } catch (InterruptedException e) {}
            }
        }, "Consumer-1");

        Thread consumer2 = new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                int val = queue.deq();
                System.out.println("Consumer-2 a procesat: " + val);
                try { Thread.sleep(40); } catch (InterruptedException e) {}
            }
        }, "Consumer-2");

        // pornim toate thread-urile
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
    }
}