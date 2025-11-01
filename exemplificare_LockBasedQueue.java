package org.example;

public class exemplificare_LockBasedQueue {
    public static void main(String[] args) {
        LockBasedQueue queue = new LockBasedQueue();

        // Thread care încearcă să scoată elemente
        Thread consumer1 = new Thread(() -> {
            System.out.println("Consumer 1 încearcă să scoată elemente...");
            for (int i = 0; i < 5; i++) {
                int val = queue.deq();
                System.out.println("Consumer 1 a scos: " + val);
            }
        }, "Consumer-1");

        // Alt consumer pentru a vedea interacțiunea
        Thread consumer2 = new Thread(() -> {
            System.out.println("Consumer 2 încearcă să scoată elemente...");
            for (int i = 0; i < 5; i++) {
                int val = queue.deq();
                System.out.println("Consumer 2 a scos: " + val);
            }
        }, "Consumer-2");

        // Thread care introduce elemente
        Thread producer1 = new Thread(() -> {
            System.out.println("Producer 1 încearcă să introducă elemente...");
            for (int i = 0; i < 15; i++) {  // 15 > QSIZE pentru a evidenția așteptarea
                queue.enq(i);
                System.out.println("Producer 1 a introdus: " + i);
            }
        }, "Producer-1");

        // Alt producer pentru a evidenția concurența
        Thread producer2 = new Thread(() -> {
            System.out.println("Producer 2 încearcă să introducă elemente...");
            for (int i = 100; i < 110; i++) {
                queue.enq(i);
                System.out.println("Producer 2 a introdus: " + i);
            }
        }, "Producer-2");

        // Pornim toate thread-urile
        consumer1.start();
        consumer2.start();
        producer1.start();
        producer2.start();
    }
}
