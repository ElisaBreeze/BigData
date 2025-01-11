package org.ulpgc;

import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Utils {

    // Function to generate a square matrix with random integers (0-9)
    public static int[][] generateRandomMatrix(int size) {
        Random random = new Random();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }

    // Wait until the Hazelcast cluster is ready with at least 3 nodes connected
    public static void waitForCluster(HazelcastInstance instance) throws InterruptedException {
        while (instance.getCluster().getMembers().size() < 3) { // Checks if cluster has at least 3 members
            Thread.sleep(1000); // Waits for 1 second before checking again, to give them time to connect
        }
        System.out.println("All Nodes are ready.");
    }

    // Distributes the multiplication tasks
    // Gathers the results into a result matrix
    public static Integer[][] getResultMatrix(HazelcastInstance hazelcastInstance, int rowsA, int colsB,
                                              int[][] matrixA, int[][] matrixB, IExecutorService executorService) {
        // Get the local member, which will be the coordinator
        Member coordinator = hazelcastInstance.getCluster().getLocalMember();

        // Get all the cluster members except the coordinator and put them in a List
        List<Member> members = hazelcastInstance.getCluster().getMembers()
                .stream()
                .filter(member -> !member.equals(coordinator)) // Excludes the coordinator
                .toList();

        if (members.isEmpty()) { // Makes sure there are nodes to distribute to
            throw new IllegalStateException("Not enough nodes to process the tasks");
        }

        // Initializes the result matrix
        Integer[][] resultMatrix = new Integer[rowsA][colsB];

        // Distributes the multiplication tasks
        taskDistribution(rowsA, colsB, matrixA, matrixB, executorService, members, resultMatrix);

        return resultMatrix;
    }

    private static void taskDistribution(int rowsA, int colsB, int[][] matrixA, int[][] matrixB, IExecutorService executorService, List<Member> members, Integer[][] resultMatrix) {
        // Iterates over each element of the result matrix
        // Assigns the calculation of that element as a task and distributes it to a member of the cluster
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                // Creates a task to calculate one element
                MultiplicationTask task = new MultiplicationTask(matrixA[i], getColumn(matrixB, j));

                // Selects a member of the cluster for the task
                Member targetMember = members.get((i * colsB + j) % members.size());
                System.out.println("Sending task for position (" + i + "," + j + ") to node: " + targetMember.getAddress());

                // Submits the task to the member and saves the result
                Future<Integer> future = executorService.submitToMember(task, targetMember);

                // Stores the result in the result matrix
                storeResult(future, resultMatrix, i, j);
            }
        }
    }

    // Gets a specific column from a matrix
    public static int[] getColumn(int[][] matrix, int colIndex) {
        int[] column = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][colIndex]; // Copies the value from the specified column into the column variable
        }
        return column;
    }

    // Stores the result of a task into the result matrix
    private static void storeResult(Future<Integer> future, Integer[][] resultMatrix, int i, int j) {
        try {
            Integer result = future.get(); // Gets the computed result
            resultMatrix[i][j] = result; // Stores the result in its position
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Prints the initial matrices (int[][] format)
    public static void showMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " "); // Prints each value separated by a space
            }
            System.out.println(); // Moves to the next line after each row
        }
    }

    // Prints the final result matrix (Integer[][] format)
    public static void printMatrixResult(int rowsA, int colsB, Integer[][] resultMatrix) {
        System.out.println("Matrix Result:");
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                System.out.print(resultMatrix[i][j] + " "); // Prints each value separated by a space
            }
            System.out.println(); // Moves to the next line after each row
        }
    }
}
