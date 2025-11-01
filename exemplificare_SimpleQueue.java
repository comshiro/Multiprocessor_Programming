package org.example;

public class exemplificare_SimpleQueue {
    public static void main(String[] args) throws InterruptedException {
        SimpleQueue<Integer> queue = new SimpleQueue<>(2);

        Thread consumer1 = new Thread(() -> {
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            for (int i = 0; i < 5; i++) {
                Integer val = queue.deq();
                System.out.println("Consumer1 got " + val);
            }
        });

        Thread consumer2 = new Thread(() -> {
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            for (int i = 0; i < 5; i++) {
                Integer val = queue.deq();
                System.out.println("Consumer2 got " + val);
            }
        });

        Thread producer1 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                queue.enq(i);
                System.out.println("Producer1 put " + i);
            }
        });

        Thread producer2 = new Thread(() -> {
            for (int i = 5; i < 7; i++) {
                queue.enq(i);
                System.out.println("Producer2 put " + i);
            }
        });

        //Producatorii pornesc înaintea consumatorilor
        producer1.start();
        producer2.start();

        consumer1.start();
        consumer2.start();

        consumer1.join(4000);
        consumer2.join(4000);

        System.out.println("\n=== Dacă programul nu termină -> semnal pierdut");
    }

}
