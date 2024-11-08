package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import java.util.Random;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)

public class Benchmark {

    @State(Scope.Thread)
    public static class Operands {

        @Param({"16", "128", "1024"})
        public int matrixSize;

        @Param({"0.0", "0.5", "0.9"})
        public double sparsity;

        public int blockSize = 10;

        private double[][] a;
        private double[][] b;
        private double[][] c;

        @Setup
        public void setup() {

            a = new double[matrixSize][matrixSize];
            b = new double[matrixSize][matrixSize];
            c = new double[matrixSize][matrixSize];
            Random random = new Random();

            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    if (random.nextDouble() < sparsity) {
                        a[i][j] = 0;
                        b[i][j] = 0;
                    } else {
                        a[i][j] = random.nextDouble();
                        b[i][j] = random.nextDouble();
                    }
                }
            }

        }
            }

    private long memoryUsageTotal;
    private String benchmarkName;

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkBasicMatrixMultiplication(Operands operands) {
        benchmarkName = "BasicMatrixMultiplication";

        new BasicMatrixMultiplication().multiply(operands.a, operands.b, operands.c, operands.matrixSize);

        Runtime runtime = Runtime.getRuntime();
        memoryUsageTotal = runtime.totalMemory() - runtime.freeMemory();


    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkAccessOptimized(Operands operands) {
        benchmarkName = "AccessOptimized";

        new AccessOptimization().multiply(operands.a, operands.b, operands.c, operands.matrixSize);

        Runtime runtime = Runtime.getRuntime();
        memoryUsageTotal = runtime.totalMemory() - runtime.freeMemory();


    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkLoopUnrolling(Operands operands) {
        benchmarkName = "LoopUnrolling";

        new LoopUnrolling().multiply(operands.a, operands.b, operands.c, operands.matrixSize);

        Runtime runtime = Runtime.getRuntime();
        memoryUsageTotal = runtime.totalMemory() - runtime.freeMemory();


    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkBlocking(Operands operands) {
        benchmarkName = "Blocking";

        new Blocking().multiply(operands.a, operands.b, operands.blockSize);

        Runtime runtime = Runtime.getRuntime();
        memoryUsageTotal = runtime.totalMemory() - runtime.freeMemory();


    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkStrassen(Operands operands) {
        benchmarkName = "Strassen";

        new StrassenAlgorithm().multiply(operands.a, operands.b);

        Runtime runtime = Runtime.getRuntime();
        memoryUsageTotal = runtime.totalMemory() - runtime.freeMemory();


    }

    @org.openjdk.jmh.annotations.Benchmark
    public void benchmarkCSCSparseMultiplication(Operands operands) {
        benchmarkName = "CSCSparseMultiplication";

        SparseMatrixCSCMultiplication.CSCMatrix cscA = SparseMatrixCSCMultiplication.convertToCSC(operands.a);
        SparseMatrixCSCMultiplication.CSCMatrix cscB = SparseMatrixCSCMultiplication.convertToCSC(operands.b);
        SparseMatrixCSCMultiplication.CSCMatrix result = cscA.multiply(cscB);

        Runtime runtime = Runtime.getRuntime();
        memoryUsageTotal = runtime.totalMemory() - runtime.freeMemory();

    }

    @org.openjdk.jmh.annotations.Benchmark
    public void SparseMatrixCSRMultiplication(Operands operands) {
        benchmarkName = "CSRMultiplication";

        SparseMatrixCSRMultiplication.CSRMatrix csrA = SparseMatrixCSRMultiplication.convertToCSR(operands.a);
        SparseMatrixCSRMultiplication.CSRMatrix csrB = SparseMatrixCSRMultiplication.convertToCSR(operands.b);
        SparseMatrixCSRMultiplication.CSRMatrix result = csrA.multiply(csrB);

        Runtime runtime = Runtime.getRuntime();
        memoryUsageTotal = runtime.totalMemory() - runtime.freeMemory();

    }

    @TearDown(Level.Trial)
    public void tearDown(Operands operands){

        System.out.println("Memory Used: " + memoryUsageTotal +  " bytes");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("memoryUsage_results.csv", true))) {

            writer.write(String.format("%s, %d, %f, %d bytes\n", benchmarkName, operands.matrixSize,operands.sparsity, memoryUsageTotal));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
