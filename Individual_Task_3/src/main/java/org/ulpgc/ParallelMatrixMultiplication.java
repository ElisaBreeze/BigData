package org.ulpgc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class ParallelMatrixMultiplication {
    public static double[][] multiply(double[][] a, double[][] b, double[][] c, int n, int threads) throws InterruptedException {
        int numThreads = threads; // available cores
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < n; i++) {
            final int row = i; // row to process
            executor.submit(() -> {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        c[row][j] += a[row][k] * b[k][j];
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS); // wait until all tasks are finished
        return c;
    }
}
