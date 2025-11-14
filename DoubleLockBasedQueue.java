package org.example;
import java.util.concurrent.locks.ReentrantLock;

public class DoubleLockBasedQueue {
    int head = 0, tail = 0;
    final static int QSIZE = 10;
    int items [] = new int[ QSIZE ];
    ReentrantLock enqlock = new ReentrantLock();
    ReentrantLock deqlock = new ReentrantLock();

    public void enq(int x) {
        while ( tail - head == QSIZE ) {
            System.out.println(Thread.currentThread().getName() +
                    " așteaptă să introducă " + x + " (coada plină). head=" + head + ", tail=" + tail);
        };
        System.out.println(Thread.currentThread().getName() + " citește tail=" + tail);
        enqlock.lock();
        try {
            items [ tail % QSIZE ] = x;
            tail ++;
            System.out.println(Thread.currentThread().getName() + " modifică tail -> " + tail);
        } finally {
            enqlock.unlock();
        }
    }

    public int deq () {
        while ( tail == head ) {
            System.out.println(Thread.currentThread().getName() +
                    " așteaptă să scoată (coada goală). head=" + head + ", tail=" + tail);
        };
        System.out.println(Thread.currentThread().getName() + " citește head=" + head);
        deqlock.lock();
        try {
            int item = items [ head % QSIZE ];
            head ++;
            System.out.println(Thread.currentThread().getName() + " modifică head -> " + head);
            return item;
        } finally {
            deqlock.unlock();
        }
    }
}
