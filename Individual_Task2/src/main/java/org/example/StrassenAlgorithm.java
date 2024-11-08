package org.example;

public class StrassenAlgorithm {
    public double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        if (n == 1) {
            double[][] result = new double[1][1];
            result[0][0] = A[0][0] * B[0][0];
            return result;
        }

        int newSize = n / 2;
        double[][] a11 = new double[newSize][newSize];
        double[][] a12 = new double[newSize][newSize];
        double[][] a21 = new double[newSize][newSize];
        double[][] a22 = new double[newSize][newSize];

        double[][] b11 = new double[newSize][newSize];
        double[][] b12 = new double[newSize][newSize];
        double[][] b21 = new double[newSize][newSize];
        double[][] b22 = new double[newSize][newSize];

        split(A, a11, 0, 0);
        split(A, a12, 0, newSize);
        split(A, a21, newSize, 0);
        split(A, a22, newSize, newSize);
        split(B, b11, 0, 0);
        split(B, b12, 0, newSize);
        split(B, b21, newSize, 0);
        split(B, b22, newSize, newSize);

        double[][] m1 = multiply(add(a11, a22), add(b11, b22));
        double[][] m2 = multiply(add(a21, a22), b11);
        double[][] m3 = multiply(a11, subtract(b12, b22));
        double[][] m4 = multiply(a22, subtract(b21, b11));
        double[][] m5 = multiply(add(a11, a12), b22);
        double[][] m6 = multiply(subtract(a21, a11), add(b11, b12));
        double[][] m7 = multiply(subtract(a12, a22), add(b21, b22));

        double[][] c11 = add(subtract(add(m1, m4), m5), m7);
        double[][] c12 = add(m3, m5);
        double[][] c21 = add(m2, m4);
        double[][] c22 = add(subtract(add(m1, m3), m2), m6);

        double[][] result = new double[n][n];
        combine(c11, result, 0, 0);
        combine(c12, result, 0, newSize);
        combine(c21, result, newSize, 0);
        combine(c22, result, newSize, newSize);

        return result;
    }

    public static void split(double[][] source, double[][] target, int rowOffset, int colOffset) {
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target.length; j++) {
                target[i][j] = source[i + rowOffset][j + colOffset];
            }
        }
    }

    public static void combine(double[][] source, double[][] target, int rowOffset, int colOffset) {
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source.length; j++) {
                target[i + rowOffset][j + colOffset] = source[i][j];
            }
        }
    }

    public static double[][] add(double[][] A, double[][] B) {
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }

    public static double[][] subtract(double[][] A, double[][] B) {
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }

}
