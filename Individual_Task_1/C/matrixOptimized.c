#include <stdio.h>
#include <stdlib.h>

#define n 10
double a[n][n];
double b[n][n];
double c[n][n];

int main() {
    for (int i = 0; i<n; ++i) {
        for (int j = 0; j < n; ++j) {
            a[i][j] = (double) rand() / RAND_MAX;
            b[i][j] = (double) rand() / RAND_MAX;
            c[i][j] = 0;
        }
    }
    for (int i = 0; i<n; ++i) {
        for (int k = 0; k<n; ++k) {
            for (int j = 0; j< n; ++j) {
                c[i][j] += a[i][k] * b[k][j];
            }
        }
    }
}