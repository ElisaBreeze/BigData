package org.example;

public class Blocking {
    public double[][] multiply(double[][] A, double[][] B, int blockSize) {
        int n = A.length;
        double[][] C = new double[n][n];

        // Divide into Blocks and multiply
        for (int i = 0; i < n; i += blockSize) {
            for (int j = 0; j < n; j += blockSize) {
                for (int k = 0; k < n; k += blockSize) {
                    for (int ii = i; ii < Math.min(i + blockSize, n); ii++) {
                        for (int jj = j; jj < Math.min(j + blockSize, n); jj++) {
                            for (int kk = k; kk < Math.min(k + blockSize, n); kk++) {
                                C[ii][jj] += A[ii][kk] * B[kk][jj];
                            }
                        }
                    }
                }
            }
        }
        return C;
    }
}
