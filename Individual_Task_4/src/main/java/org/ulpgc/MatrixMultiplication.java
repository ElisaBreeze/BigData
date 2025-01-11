package org.ulpgc;

import com.hazelcast.cluster.Member;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.map.IMap;
import java.util.concurrent.Future;

public class MatrixMultiplication {

    public static void main(String[] args) throws Exception {

        // Initialize Hazelcast
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        // Prints out the nodes that have connected to the cluster
        System.out.println("Node initiated: " + hazelcastInstance.getCluster().getLocalMember().getAddress());

        // Waits until all the nodes have connected correctly
        System.out.println("Waiting for nodes to connect...");
        Utils.waitForCluster(hazelcastInstance);

        int size = 64;  // size of the matrix
        // Created the matrices
        int[][] matrixA = Utils.generateRandomMatrix(size);
        int[][] matrixB = Utils.generateRandomMatrix(size);

        // Shows the matrices
        System.out.println("Matrix A:");
        Utils.showMatrix(matrixA);
        System.out.println("\nMatrix B:");
        Utils.showMatrix(matrixB);

        // Obtains the execution service from Hazelcast
        IExecutorService executorService = hazelcastInstance.getExecutorService("matrixExecutor");

        // Obtains an IMap to controle the status of the task
        IMap<String, Boolean> taskStatusMap = hazelcastInstance.getMap("taskStatusMap");
        taskStatusMap.put("taskCompleted", false);  // Task completed at the beginning false

        // Initializes the result matrix
        Integer[][] resultMatrix = new Integer[matrixA.length][matrixB[0].length];

        // Distributes the tasks and gets the results of each one using "futures"
        System.out.println("Distributing tasks...");
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                // Creates a task for every element of the matrix multiplication
                MultiplicationTask task = new MultiplicationTask(matrixA[i], Utils.getColumn(matrixB, j));

                // Sends the task to a worker node
                Future<Integer> future = executorService.submit(task);

                // Waits for the result of the task and stores the result in the result matrix
                Integer result = future.get();
                resultMatrix[i][j] = result;  // Assigns the result to the correct position
            }
        }

        // Coordinator Node marks the task completed and shows the result matrix
        Member coordinator = hazelcastInstance.getCluster().getMembers().iterator().next(); // The first member of the cluster is the coordinator
        if (hazelcastInstance.getCluster().getLocalMember().equals(coordinator)) {
            System.out.println("Finishing Task...");
            taskStatusMap.put("taskCompleted", true);  // Marks the task as completed
            // Only the coordinator node shows the result matrix
            System.out.println("Result Matrix:");
            Utils.printMatrixResult(matrixA.length, matrixB[0].length, resultMatrix);
        }

        // Waits until the tasks have been completed
        while (!taskStatusMap.get("taskCompleted")) {
            Thread.sleep(1000);  // Node waits for next task assignment
        }
        System.out.println("Task completed.");
    }
}
