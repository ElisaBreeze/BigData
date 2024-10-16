package org.example;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import java.util.Random;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1) //so that it doesn't do more than one and compares better with the other languages
@State(Scope.Thread)

public class Benchmarking {

    @State(Scope.Thread)
    public static class Operands {
        private final int n = 1000;
        private final double[][] a = new double[n][n];
        private final double[][] b = new double[n][n];
        private final double[][] c = new double[n][n];

        @Setup
        public void setup() {
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    a[i][j] = random.nextDouble();
                    b[i][j] = random.nextDouble();
                    c[i][j] = 0;
                }
            }
        }
    }

    private long memoryUsageTotal;

    @Benchmark
    public void multiplication(Operands operands) {

        //new Matrix_Multiplication().execute(operands.a, operands.b, operands.c, operands.n);
        new Matrix_multi_optimized().execute(operands.a, operands.b, operands.c, operands.n);

        Runtime runtime = Runtime.getRuntime();
        memoryUsageTotal = runtime.totalMemory() - runtime.freeMemory();


    }

    @TearDown(Level.Trial)
    public void tearDown(){
        System.out.println("Memory Used: " + memoryUsageTotal +  " bytes");
    }


}



