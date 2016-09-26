
#include <iostream>
#include <cstdint>
#include <random>
#include <omp.h>
#include <thread>

static const uint32_t   R               = 5;
static const uint32_t   NUM_POINTS      = 100000;
static const uint32_t   NUM_THREADS     = 8;

/* Function declarations */
double monte_carlo_pi(int s);
double rand_double(double min, double max);

using std::cout;
using std::endl;

int main(void) {
    double pi = monte_carlo_pi(NUM_POINTS);
    cout << "Calculated value of pi with " << NUM_POINTS << " points and " << NUM_THREADS << " threads is " << std::fixed << pi << endl;
}

/**
 * Calculates the value of pi by calculating pi = 4 * c/s, where c = # of points in s that are in the circle with radius R.
 * @param s the number of points to choose
 * @return the estimated value of pi
 */
double monte_carlo_pi(int s) {
    uint32_t c = 0;
    #pragma omp parallel for num_threads(NUM_THREADS)
    for (int i = 0; i < s; ++i) {
        double x = rand_double(0.0, R);
        double y = rand_double(0.0, R);
        if ((x * x) + (y * y) < (R * R)) {
            #pragma omp atomic
            ++c;
        }
    }
    return 4 * ((double)c / s);
}

/**
 * Thread-safe random double generator. Creates thread_local generators so we are guaranteed one generator per thread.
 * @param min the minimum bound of the random value
 * @param max the maximum bound of the random value
 * @return the random double
 */
double rand_double(double min, double max) {
    
    static thread_local std::mt19937* generator = nullptr;

    // if there's no generator then we create one
    if (!generator) {
        int seed = ((double)omp_get_thread_num() / omp_get_num_threads()) * NUM_POINTS;
        generator = new std::mt19937(seed);
    }
    std::uniform_real_distribution<double> distribution(min, max);
    return distribution(*generator);
}
