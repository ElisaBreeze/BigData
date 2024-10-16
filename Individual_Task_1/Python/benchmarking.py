from matrix_multiplication import matrix_multi, matrix_multiNumpy

def test_matrix_multiplication(benchmark):
    benchmark.extra_info['unit'] = 'ms'

    #comment the algorithm you don't want to analyse:
    #benchmark(matrix_multi)
    benchmark(matrix_multiNumpy)
