
#include <cstdlib>
#include <cstdint>
#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <chrono>

#include "matrix.h"

using namespace std;


int main(int argc, char* argv[]) {
    if (argc != 4) {
        cout << "Must invoke program with 3 arguments." << endl;
        exit(1);
    }

    matrix<double> mtx_a = matrix<double>::parse_matrix_file(argv[1]);
    matrix<double> mtx_b = matrix<double>::parse_matrix_file(argv[2]);

    // check that the # of columns in a === # of rows in b
    if (mtx_a.cols() != mtx_b.rows()) {
        cout << "Number of columns in first matrix does not equal the number of rows in second matrix." << endl;
        exit(1);
    }
    cout << "Matrix A:" << endl;
    matrix<double>::print_matrix(mtx_a);
    cout << "Matrix B:" << endl;
    matrix<double>::print_matrix(mtx_b);

    cout << "Matrix C = A * B:" << endl;
    // measure the time it takes to execute the matrix_mult
    auto begin = chrono::high_resolution_clock::now();
    matrix<double> mtx_c = matrix<double>::matrix_mult(mtx_a, mtx_b, atoi(argv[3]));
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - begin).count();
    matrix<double>::print_matrix(mtx_c);
    cout    << "The time taken to multiply with " << argv[3] << " threads is "
            << chrono::duration_cast<chrono::milliseconds>(end - begin).count()
            << " ms" << endl;
}
