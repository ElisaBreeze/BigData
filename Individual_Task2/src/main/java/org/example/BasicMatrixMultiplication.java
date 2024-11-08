package org.example;

public class BasicMatrixMultiplication {

    public double[][] multiply(double[][] a, double[][] b, double[][] c, int n){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    c[i][j] += a[i][k] * b[k][j];

                }
            }
        }
        return c;
    }
}
