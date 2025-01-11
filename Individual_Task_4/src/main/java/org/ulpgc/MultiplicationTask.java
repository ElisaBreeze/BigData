package org.ulpgc;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class MultiplicationTask implements Callable<Integer>, Serializable {

    private final int[] rowA;
    private final int[] columnB;

    // Constructor that initializes the task with a row from Matrix A and a column from Matrix B
    public MultiplicationTask(int[] rowA, int[] columnB) {
        this.rowA = rowA;
        this.columnB = columnB;
    }

    // Calculates the multiplication of the row of A and column of B and returns the result
    @Override
    public Integer call() {
        int result = 0;

        // Iterates through the row and column for calculating the dot product
        for (int k = 0; k < rowA.length; k++) {
            result += rowA[k] * columnB[k]; // Multiplies the elements and adds it to the result
        }
        return result;
    }
}
