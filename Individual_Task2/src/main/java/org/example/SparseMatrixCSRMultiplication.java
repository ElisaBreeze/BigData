package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SparseMatrixCSRMultiplication {

    // Sparse matrix in CSR format
    static class CSRMatrix {
        double[] values;          // Non-zero values
        int[] columnIndices;      // Column indices corresponding to values
        int[] rowPointers;        // Row pointers

        int rows, cols;           // Number of rows and columns in the matrix

        CSRMatrix(double[] values, int[] columnIndices, int[] rowPointers, int rows, int cols) {
            this.values = values;
            this.columnIndices = columnIndices;
            this.rowPointers = rowPointers;
            this.rows = rows;
            this.cols = cols;
        }

        // Method to multiply two CSR matrices
        public CSRMatrix multiply(CSRMatrix B) {
            if (this.cols != B.rows) {
                throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");
            }

            List<Double> resultValues = new ArrayList<>();
            List<Integer> resultColumnIndices = new ArrayList<>();
            List<Integer> resultRowPointers = new ArrayList<>();
            resultRowPointers.add(0);

            // Temporary array to store result for a single row in C
            double[] rowResult = new double[B.cols];

            // Perform CSR matrix multiplication (this * B)
            for (int i = 0; i < this.rows; i++) {
                // Clear rowResult
                Arrays.fill(rowResult, 0.0);

                // For each non-zero element in row i of this matrix
                for (int j = this.rowPointers[i]; j < this.rowPointers[i + 1]; j++) {
                    int colA = this.columnIndices[j];  // Column index in matrix A
                    double valA = this.values[j];      // Value of A at row i and column colA

                    // Multiply row of A by corresponding row in B (which is stored in CSR format)
                    for (int k = B.rowPointers[colA]; k < B.rowPointers[colA + 1]; k++) {
                        int colB = B.columnIndices[k];   // Column index in matrix B
                        double valB = B.values[k];       // Value of B at column colB
                        rowResult[colB] += valA * valB;
                    }
                }

                // Save the result of row i in CSR format
                int nonZeroCount = 0;
                for (int j = 0; j < B.cols; j++) {
                    if (rowResult[j] != 0.0) {
                        resultValues.add(rowResult[j]);
                        resultColumnIndices.add(j);
                        nonZeroCount++;
                    }
                }

                resultRowPointers.add(resultRowPointers.get(resultRowPointers.size() - 1) + nonZeroCount);
            }

            // Convert lists to arrays
            double[] resultValuesArray = resultValues.stream().mapToDouble(Double::doubleValue).toArray();
            int[] resultColumnIndicesArray = resultColumnIndices.stream().mapToInt(Integer::intValue).toArray();
            int[] resultRowPointersArray = resultRowPointers.stream().mapToInt(Integer::intValue).toArray();

            return new CSRMatrix(resultValuesArray, resultColumnIndicesArray, resultRowPointersArray, this.rows, B.cols);
        }
    }

    // Method to convert a sparse matrix to CSR format
    public static CSRMatrix convertToCSR(double[][] matrix) {
        List<Double> valuesList = new ArrayList<>();
        List<Integer> columnIndicesList = new ArrayList<>();
        List<Integer> rowPointersList = new ArrayList<>();

        int rows = matrix.length;
        int cols = matrix[0].length;

        rowPointersList.add(0);

        // Traverse the matrix and convert it into CSR format
        for (int i = 0; i < rows; i++) {
            int nonZeroCount = 0;
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] != 0) {
                    valuesList.add(matrix[i][j]);
                    columnIndicesList.add(j);
                    nonZeroCount++;
                }
            }
            rowPointersList.add(rowPointersList.get(rowPointersList.size() - 1) + nonZeroCount);
        }

        // Convert lists to arrays
        double[] values = valuesList.stream().mapToDouble(Double::doubleValue).toArray();
        int[] columnIndices = columnIndicesList.stream().mapToInt(Integer::intValue).toArray();
        int[] rowPointers = rowPointersList.stream().mapToInt(Integer::intValue).toArray();

        return new CSRMatrix(values, columnIndices, rowPointers, rows, cols);
    }
}
