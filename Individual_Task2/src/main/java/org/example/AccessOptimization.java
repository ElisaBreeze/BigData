package org.example;

public class AccessOptimization {
    public double[][] multiply(double[][] a, double[][] b, double[][] c, int n) {
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                double temp = a[i][k];
                for (int j = 0; j < n; j++) {
                    c[i][j] += temp * b[k][j];
                }
            }
        }
        return c;
    }
}


