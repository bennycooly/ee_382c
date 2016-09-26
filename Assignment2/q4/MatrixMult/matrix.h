#pragma once

#include <cstdint>
#include <iostream>
#include <fstream>
#include <vector>
#include <string>

using namespace std;

/**
 * Matrix class to be used with MatrixMult. Provides functions to print and multiply matrices and not much more.
 */
template <typename T>
class matrix {
public:
    /* Constructors */
    matrix();
    matrix(uint32_t rows, uint32_t cols);

    /* Operators */
    vector<T>& operator[](uint32_t const index);
    vector<T> const& operator[](uint32_t index) const;

    /* Member functions */
    uint32_t rows() const;
    uint32_t cols() const;

    /* Static member functions */
    static matrix   parse_matrix_file(string const& filename);
    static void     print_matrix(matrix const& mtx);
    static matrix   matrix_mult(matrix const& mtx_a, matrix const& mtx_b);
    static matrix   matrix_mult(matrix const& mtx_a, matrix const& mtx_b, uint32_t num_threads);

private:
    uint32_t m_rows;
    uint32_t m_cols;
    vector<vector<T>> m_mtx;
};

/**
 * Empty constructor. Doesn't do anything.
 */
template <typename T>
matrix<T>::matrix() {

}

/**
 * Creates empty 2-D vector with the specified # of rows and columns.
 */
template <typename T>
matrix<T>::matrix(uint32_t rows, uint32_t cols) {
    vector<vector<T>> res_mtx;
    for (uint32_t i = 0; i < rows; ++i) {
        res_mtx.push_back(vector<T>(cols));
    }
    m_mtx = res_mtx;
    m_rows = rows;
    m_cols = cols;
}

/**
 * Returns the vector for the specific row. The second [] access is handled by the vector class.
 */
template <typename T>
vector<T>& matrix<T>::operator[](uint32_t const index) {
    return m_mtx[index];
}

template <typename T>
vector<T> const& matrix<T>::operator[](uint32_t index) const {
    return m_mtx[index];
}

/**
 * Returns the # of rows.
 */
template <typename T>
uint32_t matrix<T>::rows() const {
    return m_rows;
}

/**
 * Returns the # of columns.
 */
template <typename T>
uint32_t matrix<T>::cols() const {
    return m_cols;
}

/**
 * Parses the file with the given filename into a matrix.
 */
template <typename T>
matrix<T> matrix<T>::parse_matrix_file(string const& filename) {
    string line;
    ifstream file;
    file.open(filename);
    if (!file.is_open()) {
        cout << "Error opening file " << filename << endl;
        exit(1);
    }
    uint32_t num_rows, num_cols;
    file >> num_rows;
    file >> num_cols;

    matrix<T> res_mtx(num_rows, num_cols);
    for (uint32_t i = 0; i < num_rows; ++i) {
        for (uint32_t j = 0; j < num_cols; ++j) {
            int cur_elem;
            file >> cur_elem;
            res_mtx[i][j] = cur_elem;
        }
    }
    file.close();
    return res_mtx;
}

/**
 * Prints the matrix to stdout with the same format as the input matrix files.
 */
template <typename T>
void matrix<T>::print_matrix(matrix<T> const& mtx) {
    uint32_t num_rows = mtx.rows();
    uint32_t num_cols = mtx.cols();
    cout << num_rows << " " << num_cols << endl;
    cout << endl;
    for (uint32_t i = 0; i < num_rows; ++i) {
        for (uint32_t j = 0; j < num_cols; ++j) {
            cout << mtx[i][j] << " ";
        }
        cout << endl;
    }
    cout << endl;
}

/**
 * Multiplies two matrices together and returns the resulting matrix.
 */
template <typename T>
matrix<T> matrix<T>::matrix_mult(matrix<T> const& mtx_a, matrix<T> const& mtx_b) {
    matrix<T> res_mtx(mtx_a.rows(), mtx_b.cols());
    for (int i = 0; i < mtx_a.rows(); ++i) {
        for (uint32_t j = 0; j < mtx_b.cols(); ++j) {
            double sum = 0;
            for (uint32_t k = 0; k < mtx_a.cols(); ++k) {
                sum += mtx_a[i][k] * mtx_b[k][j];
            }
            res_mtx[i][j] = sum;
        }
    }
    return res_mtx;
}

/**
 * Multithreaded version of matrix_mult. Takes in an extra argument as the number of threads.
 */
template <typename T>
matrix<T> matrix<T>::matrix_mult(matrix<T> const& mtx_a, matrix<T> const& mtx_b, uint32_t num_threads) {
    matrix<T> res_mtx(mtx_a.rows(), mtx_b.cols());
    #pragma omp parallel for num_threads(num_threads)
    for (int i = 0; i < mtx_a.rows(); ++i) {
        for (uint32_t j = 0; j < mtx_b.cols(); ++j) {
            double sum = 0;
            for (uint32_t k = 0; k < mtx_a.cols(); ++k) {
                sum += mtx_a[i][k] * mtx_b[k][j];
            }
            res_mtx[i][j] = sum;
        }
    }
    return res_mtx;
}
