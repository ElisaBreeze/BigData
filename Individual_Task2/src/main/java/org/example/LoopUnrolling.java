package org.example;

public class LoopUnrolling {

    public double[][] multiply(double[][] a, double[][] b, double[][] c, int n) {
        for (int i = 0; i < n; i++) {               // Rows
            for (int j = 0; j < n; j++) {           // Columns
                for (int k = 0; k < n; k += 4) {    // Loop => processes 4 elements at a time
                    // Unroll loop
                    c[i][j] += a[i][k] * b[k][j]                           // core operation => takes element from row i and column k of A, and multiplies it by the element of row k and column j of matrix B, result sabes in row i column j of matrix C
                            + (k + 1 < n ? a[i][k + 1] * b[k + 1][j] : 0) // Conditional multiplication => checks if there is a second element, k+1<n ensures that we are still withing the matrix; if it is, the size of next element A is multiplied by next element B and added to C, if not, it adds 0
                            + (k + 2 < n ? a[i][k + 2] * b[k + 2][j] : 0) // Repeat the same for 3rd element
                            + (k + 3 < n ? a[i][k + 3] * b[k + 3][j] : 0); // Repeat the same for 4th element
                }
            }
        }
        return c;
    }
}
