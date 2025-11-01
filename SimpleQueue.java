package org.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SimpleQueue<T>{
    private Lock lock = new ReentrantLock();
    private Condition full = lock.newCondition();
    private Condition empty = lock.newCondition();
    private T [] items;
    private int tail = 0, head = 0, count = 0;

    public SimpleQueue(int size)
    {
        items = (T[])new Object[size];
    }

    public void enq(T x) {
        lock.lock();
        try {
            while(count == items.length) //verifică dacă coada este plină
            {
                try { full.await(); } //eliberează lock-ul atomically și pune firul curent în
                // așteptare pe condiția full;  când firul este trezit (prin signal()/signalAll() de
                // pe aceeași Condition), el re-achiziționează lock-ul înainte de a reveni din await().
                catch (InterruptedException e) { }
                finally { }
            }
            System.out.println("Enqueuing " + x);
            items[tail] = x;
            if (++tail == items.length) { tail = 0; } //verificăm dacă am depășit capătul array-ului
            count++;
            if (count == 1) { //Semnalăm condiția empty numai în momentul în care coada a devenit nevidă
                empty.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public T deq() {
        lock.lock();
        try {
            while (count == 0)
            {
                try { empty.await(); }
                catch (InterruptedException e) { }
                finally { }
            }
            T x = items[head];
            if (++head == items.length) { head = 0; }
            count--;
            System.out.println("Dequeuing " + x);
            if (count == items.length - 1) {
                full.signal();
            }
            return x;
        } finally {
            lock.unlock();
        }
    }
}
