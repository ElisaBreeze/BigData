import random
from memory_profiler import profile
import numpy as np

#@profile 
def matrix_multi():

    n = 10

    A = [[random.random() for _ in range(n)] for _ in range(n)]
    B = [[random.random() for _ in range(n)] for _ in range(n)]
    C = [[0 for _ in range(n)] for _ in range(n)]

    for i in range(n):
        for j in range(n):
            for k in range(n):
                C[i][j] += A[i][k]*B[k][j]
    return C

matrix_multi()


@profile
def matrix_multiNumpy():
    n = 1000

    A = [[random.random() for _ in range(n)] for _ in range(n)]
    B = [[random.random() for _ in range(n)] for _ in range(n)]

    return np.dot(A, B)


matrix_multiNumpy()


