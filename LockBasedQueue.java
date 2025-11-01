package org.example;

import java.util.concurrent.locks.ReentrantLock;

public class LockBasedQueue {
    private static final int QSIZE = 12 ;
    int head = 0, tail = 0;
    int items [] = new int[ QSIZE ];
    ReentrantLock lock = new ReentrantLock();

    public void enq(int x) {
        lock.lock();
        try {
            while ( tail - head == QSIZE ) {};
            items [ tail % QSIZE ] = x;
            tail ++;
        } finally {
            lock.unlock();
        }
    }

    public int deq () {
        lock.lock();
        try {
            while ( tail == head ) {};
            int item = items [ head % QSIZE ];
            head ++;
            return item;
        } finally {
            lock.unlock();
        }
    }
}
