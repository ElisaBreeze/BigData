package org.ulpgc;

import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.linear.MatrixUtils;
import org.openjdk.jmh.annotations.*;
import java.util.Random;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@State(Scope.Thread)

public class Benchmark {

    @State(Scope.Thread)
    public static class Operands {

        @Param({"16", "128", "1024", "2048"})
        public int matrixSize;

        double[][] a;
        double[][] b;
        double[][] c;

        @Setup
        public void setup() {

            a = new double[matrixSize][matrixSize];
            b = new double[matrixSize][matrixSize];
            c = new double[matrixSize][matrixSize];
            Random random = new Random();

            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    a[i][j] = random.nextDouble();
                    b[i][j] = random.nextDouble();
                }
            }
        }
    }

    @State(Scope.Thread)
    public static class ThreadOperands {
        @Param({"2", "4", "8"})
        public int threads;
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkBasicMatrixMultiplication(Operands operands) {
        new BasicMatrixMultiplication().multiply(operands.a, operands.b, operands.c, operands.matrixSize);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkParallelMatrixMultiplication(Operands operands, ThreadOperands threadOperands) throws InterruptedException {

        new ParallelMatrixMultiplication().multiply(operands.a, operands.b, operands.c, operands.matrixSize, threadOperands.threads);

    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkMatrixMultiplicationVectorized(Operands operands) {
        new MatrixMultiplicationVectorized().multiply(MatrixUtils.createRealMatrix(operands.a), MatrixUtils.createRealMatrix(operands.b));

    }
}
