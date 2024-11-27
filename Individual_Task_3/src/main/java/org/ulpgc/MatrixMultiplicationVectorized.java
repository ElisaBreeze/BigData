package org.ulpgc;

import org.apache.commons.math3.linear.RealMatrix;

public class MatrixMultiplicationVectorized {

    public static RealMatrix multiply(RealMatrix A, RealMatrix B) {
        if (A.getColumnDimension() != B.getRowDimension()) {
            throw new IllegalArgumentException("Dimensions do not match requirements");
        }
        return A.multiply(B);
    }

}

