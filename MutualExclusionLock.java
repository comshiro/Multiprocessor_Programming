package org.example;

public class MutualExclusionLock {
    private final int n;
    private final int THRESHOLD;
    private final boolean[] flag;
    private final boolean[] access;
    private final int[] label;
    private final Object normalizeLock = new Object();

    public MutualExclusionLock(int numberOfThreads) {
        this.n = numberOfThreads;

        this.THRESHOLD = Math.min(2*n,1); ///modificata ca sa se poata testa functionalitatea
        this.flag = new boolean[n];
        this.access = new boolean[n];
        this.label = new int[n];
        init();
    }

    private void init() {
        for (int k = 0; k < n; k++) {
            flag[k] = false;
            access[k] = false;
            label[k] = k + 1;
        }
    }

    private void normalizeLabels() {
        synchronized (normalizeLock) {
            int minOfBig = Integer.MAX_VALUE;
            int maxOfSmall = 0;


            for (int i = 0; i < n; i++) {
                if (label[i] >= n) {
                    if (minOfBig > label[i]) {
                        minOfBig = label[i];
                    }
                } else {
                    if (label[i] > maxOfSmall) {
                        maxOfSmall = label[i];
                    }
                }
            }


            if (minOfBig != Integer.MAX_VALUE) {
                for (int i = 0; i < n; i++) {
                    if (label[i] >= n) {
                        label[i] = label[i] - minOfBig + maxOfSmall + 1;
                    }
                }
            }
        }
    }

    public void lock(int i) {
        flag[i] = true;
        do {
            access[i] = false;


            while (true) {
                boolean canProceed = true;
                for (int j = 0; j < n; j++) {
                    if (j != i && flag[j] && label[j] <= label[i]) {

                        if (label[j] == label[i] && j < i) {
                            canProceed = false;
                            break;
                        } else if (label[j] < label[i]) {
                            canProceed = false;
                            break;
                        }
                    }
                }
                if (canProceed) break;

                Thread.yield();


            }

            access[i] = true;


            boolean otherHasAccess = false;
            for (int j = 0; j < n; j++) {
                if (j != i && access[j]) {
                    otherHasAccess = true;
                    break;
                }
            }

            if (!otherHasAccess) break;


            Thread.yield();

        } while (true);
    }

    public void unlock(int i) {
        synchronized (normalizeLock) {

            if (label[i] > THRESHOLD) {
                normalizeLabels();
            }


            int maxLabel = 0;
            for (int j = 0; j < n; j++) {
                if (label[j] > maxLabel) {
                    maxLabel = label[j];
                }
            }

            label[i] = maxLabel + 1;
            access[i] = false;
            flag[i] = false;
        }
    }


    public int getLabel(int i) {
        return label[i];
    }

    public boolean getFlag(int i) {
        return flag[i];
    }

    public boolean getAccess(int i) {
        return access[i];
    }


    public static void main(String[] args) throws InterruptedException {
        final int NUM_THREADS = 5;
        final int NUM_ITERATIONS = 10;
        final MutualExclusionLock lock = new MutualExclusionLock(NUM_THREADS);
        final int[] sharedCounter = {0};

        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int iter = 0; iter < NUM_ITERATIONS; iter++) {

                    lock.lock(threadId);

                    try {

                        int temp = sharedCounter[0];
                        System.out.println("Thread " + threadId + ", label: " + lock.getLabel(threadId));
                        sharedCounter[0] = temp + 1;
                    } finally {

                        lock.unlock(threadId);
                    }

                }
            });
            threads[i].start();
        }


        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("\nFinal counter value: " + sharedCounter[0]);
        System.out.println("Expected value: " + (NUM_THREADS * NUM_ITERATIONS));
        System.out.println("Mutual exclusion " +
                (sharedCounter[0] == NUM_THREADS * NUM_ITERATIONS ? "SUCCESS" : "FAILED"));
    }
}
