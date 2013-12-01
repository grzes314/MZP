package math.utils;

import java.util.Random;
import math.matrices.DimensionException;
import math.matrices.Matrix;
import math.matrices.NotPositiveDefiniteMatrixException;
import math.matrices.Vector;

/**
*
* @author Grzegorz Los
*/
public class RandomTools
{
    public RandomTools()
    {
        r = new Random(System.currentTimeMillis());
    }

    public void setSeed(long seed)
    {
        r.setSeed(seed);
    }

    /**
     * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and the
     * specified value (exclusive).
     * @param n the bound on the random number to be returned. Must be positive. 
     * @return the pseudorandom, uniformly distributed int value between 0 (inclusive) and n (exclusive).
     */
    public int uniformInt(int n)
    {
        return r.nextInt(n);
    }

    /**
     * Returns pseudorandom value uniformly distributed at the interval (0,1).
     * @return pseudorandom value uniformly distributed at the interval (0,1).
     */
    public double uniform()
    {
        return r.nextDouble();
    }

    /**
     * Returns Vector of n independent pseudorandom values uniformly distributed
     * at the interval (0,1).
     * @param n number of samples.
     * @return Vector of n pseudorandom values uniformly distributed at the interval (0,1).
     */
    public Vector uniform(int n)
    {
        return uniform(n, 0, 1);
    }
    
    /**
     * Returns pseudorandom value uniformly distributed at the interval (a,b).
     * @param a beginning of the interval.
     * @param b end of the interval.
     * @return pseudorandom value uniformly distributed at the interval (a,b).
     */
    public double uniform(double a, double b)
    {
        if (a > b)
            throw new IllegalArgumentException();
        return (b - a)*uniform() + a;
    }

    /**
     * Returns Vector of n independent pseudorandom values uniformly distributed
     * at the interval (a,b).
     * @param a beginning of the interval.
     * @param b end of the interval.
     * @return Vector of n pseudorandom values uniformly distributed at the interval (a,b).
     */
    public Vector uniform(int n, double a, double b)
    {
        Vector res = new Vector(n);
        for (int row = 1; row <= n; ++row)
            res.set(row, uniform(a,b));
        return res;
    }

    /**
     * Returns pseudorandom value with standard normal distribution.
     * @return pseudorandom value with standard normal distribution.
     */
    public double normal()
    {
        return r.nextGaussian();
    }
    
    /**
     * Returns Vector of n independent pseudorandom values with standard normal distribution.
     * @param n number of samples.
     * @return Vector of n independent pseudorandom values with standard normal distribution.
     */
    public Vector normal(int n)
    {
        return normal(n, 0, 1);
    }

    /**
     * Returns pseudorandom value with normal distribution with specified mean and variance.
     * @param mean of the distribution.
     * @param var variance of the distribution.
     * @return pseudorandom value with normal distribution with specified mean and variance.
     */
    public double normal(double mean, double var)
    {
        return mean + Math.sqrt(var) * normal();
    }
    
    /**
     * Returns Vector of n independent pseudorandom values with normal distribution with
     * specified mean and variance.
     * @param n number of values in sample.
     * @param mean of the distribution.
     * @param var variance of the distribution.
     * @return Vector of n independent pseudorandom values with normal distribution with
     * specified mean and variance.
     */
    public Vector normal(int n, double mean, double var)
    {
        Vector res = new Vector(n);
        for (int row = 1; row <= n; ++row)
            res.set(row, normal(mean, var));
        return res;        
    }
    
    /**
     * Produces vector of correlated pseudorandom variables from normal distribution described
     * by given mean and covariance matrix. Size of the resulting vector is the same as the
     * vector of the mean. Matrix of covariance must be square, positive-definite matrix of the
     * same size as mean vector. Otherwise exception will be thrown.
     * @param mean mean of the distribution.
     * @param covar matrix of covariance.
     * @return Vector of n ependent pseudorandom values with normal distribution with
     * specified mean and covariance.
     * @throws NotPositiveDefiniteMatrixException if given matrix is not positive-definite.
     * @throws DimensionException if dimension of the matrix is not the same as dimension of
     * the mean vector. 
     */
    public Vector normal(Vector mean, Matrix covar)
            throws NotPositiveDefiniteMatrixException, DimensionException
    {
        if (mean.getRows() != covar.getRows())
            throw new DimensionException("Sizes of mean vector and covariation matrix "
                    + "are not consistent.");
        Matrix L = covar.cholesky();
        return mean.add( L.mult( normal(mean.getRows()) ) );
    }
    
    private Random r;
}
