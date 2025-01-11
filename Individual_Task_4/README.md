# Matrix Multiplication with Distributed Processing Using Hazelcast

This project demonstrates matrix multiplication using Hazelcast for distributed computing. The program divides the matrix multiplication task into smaller tasks, which are then processed by multiple Hazelcast nodes in a cluster. The result is computed efficiently by utilizing distributed nodes.


## Requirements:

Java 17 or higher
Maven
Docker Desktop (for Docker Compose support)


## Overview:

This project uses Hazelcast to perform distributed matrix multiplication.s The code includes the following:

### Matrix Generation: 
Random matrices are generated for testing.
### Distributed Tasks: 
Each element of the resulting matrix is calculated on different nodes in the Hazelcast cluster.
### Executor Service: 
Hazelcast's IExecutorService is used to distribute tasks across the nodes in the Hazelcast cluster.
### Cluster Setup:
Hazelcast nodes process tasks concurrently, with the main node coordinating computations and other nodes executing tasks.
### Task Status Monitoring: 
Uses Imap to track the completion status of the distributed tasks across the nodes.
### Serialization: 
Integer data is used for task distribution, ensuring proper serialization across nodes.

## Performance Consideration:

While int[][] is used for matrix representation and initial calculations to avoid the overhead of using Integer where not necessary, Integer is used during task execution because it ensures proper serialization in distributed environments (Hazelcast uses serialization to send data between nodes).

## Project Structure:

The project consists of:

### MatrixMultiplication.java: 
This is the main class for initializing Hazelcast. It waits for the cluster to be ready and generates matrices, then it distributes tasks to the cluster nodes, and collects the results to finally show the result Matrix.

### Utils.java: 
A utility class for helper functions like: 
- matrix generation (generateRandomMatrix)
- waiting for the cluster to be ready (waitForCluster)
- Distributing the tasks amongst the nodes (taskDistribution)
- Extracting matrix columns (getColumn)
- Storing the result into the result matrix (storeResult)
- showing matrices (showMatrix and printMatrixResult)

### MultiplicationTask.java: 
A task class that performs multiplication of two vectors (rows of the first matrix and columns of the second matrix).
It implements Callable<Integer> for matrix multiplication of 1 element (dot product).

### Docker Configuration: 
- Dockerfile: A file that defines the environment for running the Java application within Docker containers.
- docker-compose.yml: A configuration file to set up a Hazelcast cluster using Docker Compose.


## How to Run the Project:

### 1. Build the project
   Run the following Maven command to build the project:

mvn clean install

### 2. Set up Docker Containers
Ensure Docker Desktop is running. If you are using Docker Desktop, Docker Compose should be integrated. Otherwise, ensure you have docker-compose installed.

Build the Docker images and start the containers with:

docker-compose build
docker-compose up

This command will:

Build the Docker images defined in the Dockerfile.
Start the containers as defined in the docker-compose.yml file.

### 3. Running the application
Once the containers are up and running, the Hazelcast nodes will form a cluster. The main node will distribute matrix multiplication tasks across the cluster. The results will be printed in the terminal.

### 5. Shutdown
After the task completes, you can shut down the cluster using: 
docker-compose down

## Why use Docker Compose:
Docker Compose is a tool that helps you define and manage multi-container Docker applications. With the docker-compose.yml file, you can specify services, networks, and volumes for a complete environment.
If using Docker Desktop, Docker Compose is integrated, so no further installation is necessary.
If not using Docker Desktop, ensure that docker-compose is installed on your system.

## Docker Compose Configuration

In the docker-compose.yml, the services are defined as:

3 nodes that connect to each other: hazelcast-node-1, hazelcast-node-2 and hazelcast-node-3.
In this project, it is set up such that the first node in the cluster acts as the "coordinator" and the rest are treated as "workers".
The Coordinator is used for task distribution, and worker nodes are those who perform matrix multiplication calculations.
The nodes communicate through a network named hazelcast-cluster (bridge driver), which simulated a distributed environment locally using Docker.

Docker Compose allows running multiple nodes on a single computer for testing distributed systems.
This is particularly useful in scenarios like mine, where access to multiple physical devices is limited or unavailable, 
as it provides an efficient way to simulate a distributed environment locally.

## Conclusion

This project demonstrates the power of distributed computing using Hazelcast.
By leveraging Hazelcast IExecutorService and IMap, tasks are efficiently distributed
and monitored, showcasing a practical approach to parallelization computationally intensive operations like matrix multiplication.