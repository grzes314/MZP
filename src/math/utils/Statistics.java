package math.utils;

import math.matrices.DimensionException;
import math.matrices.Matrix;
import math.matrices.NoCorrelationException;
import math.matrices.Vector;

public class Statistics
{
    /**
     * This class is not supposed to be instantiated.
     */
    private Statistics()
    {
    }
    
    /**
     * Cumulative normal distribution function.
     * @param t will return Phi(t)
     * @return Phi(t)
     */
    public static  double cndf(double t)
    {
        return 0.5d * (1d + erf(t / Math.sqrt(2d)));
    }   
    
    /**
     * So called error function.
     * @see http://en.wikipedia.org/wiki/Error_function#Approximation_with_elementary_functions
     * @param d
     * @return
     */
    private static double erf(double x)
    {
        if (x < 0) return -erf(-x);
        double p = 0.3275911d, a1 = 0.254829592d, a2 = -0.284496736d,
                a3 = 1.421413741d, a4 = -1.453152027d, a5 = 1.061405429d;
        double t = 1d / (1d + p*x);
        double w = ((((a5*t + a4)*t + a3)*t + a2)*t + a1)*t;
        return 1d - w*Math.exp(-x*x);
    }

    /**
     * Returns sample mean.
     * @param v vector with a sample.
     * @return sample mean.
     */
    public static double mean(Vector v)
    {
        double sum = 0.0;
        for (int row = 1; row <= v.getRows(); ++row)
            sum += v.get(row);
        return sum / v.getRows();
    }
    
    /**
     * Computes means of all variables in given matrix. Columns of the matrix
     * correspond two variables. Rows of the matrix correspond to observations (or individuals).
     * @param m Matrix with observations.
     * @return vector containing mean of each column of the matrix.
     */
    public static Vector mean(Matrix m)
    {
        Vector res = new Vector(m.getCols());
        for (int row = 1; row <= res.getRows(); ++row)
            res.set(row, mean(m.getCol(row)));
        return res;
    }
    
    /**
     * Computes sample variance.
     * @param sample vector with observations.
     * @return sample variance.
     * @throws DimensionException if sample is has less than two elements.
     */
    public static double var(Vector sample)
    {
        ensureSampleHasMinimalSize(sample);
        int n = sample.getSize();
        double m = mean(sample);
        double res = 0;
        for (double el: sample)
            res += (el - m) * (el - m);
        return res / (n-1);
    }
    
    /**
     * Computes variances of all variables in given matrix. Columns of the matrix
     * correspond two variables. Rows of the matrix correspond to observations (or individuals).
     * @param m Matrix with observations.
     * @return vector containing variances of each column of the matrix.
     * @throws DimensionException if samples have less than two elements.
     */
    public static Vector var(Matrix m)
    {
        Vector res = new Vector( m.getCols() );
        for (int i = 1; i <= res.getSize(); ++i)
            res.set(i, var(m.getCol(i)));
        return res;
    }
    
    private static void ensureSampleHasMinimalSize(Vector sample)
    {
        if (sample.getSize() < 2)
            throw new DimensionException("Size of the sample should be at least 2");
    }
    
    /**
     * Computes covariance between two samples.
     * @param s1 vector with values of first variable.
     * @param s2 vector with values of second variable.
     * @return sample covariance.
     * @throws DimensionException if samples have different lengths or less than two elements.
     */
    public static double cov(Vector s1, Vector s2)
    {
        ensureSamplesHaveTheSameSize(s1, s2);
        ensureSampleHasMinimalSize(s1);
        int n = s1.getSize();
        double m1 = mean(s1);
        double m2 = mean(s2);
        double res = 0;
        for (int row = 1; row <= s1.getRows(); ++row)
            res += (s1.get(row) - m1) * (s2.get(row) - m2);
        return res / (n-1);
    }
    
    private static void ensureSamplesHaveTheSameSize(Vector s1, Vector s2)
    {
        if (s1.getSize() != s2.getSize())
            throw new DimensionException("Size of the samples should be the same");
    }
    
    /**
     * Computes covariance between all variables in given matrix. Columns of the matrix
     * correspond two variables. Rows of the matrix correspond to observations (or individuals).
     * @param m Matrix with observations.
     * @return covariance matrix.
     * @throws DimensionException if samples have less than two elements.
     */
    public static Matrix cov(Matrix m)
    {
        ensureSampleHasMinimalSize(m);
        int cols = m.getCols();
        Matrix res = new Matrix(cols, cols);
        Vector means = mean(m);
        for (int i = 1; i <= cols; ++i)
            for (int j = i; j <= cols; ++j)
                fillCovElement(m, res, means, i, j);
        return res;
    }

    private static void ensureSampleHasMinimalSize(Matrix m)
    {
        if (m.getRows() < 2)
            throw new DimensionException("Size of the sample should be at least 2");
    }
    
    private static void fillCovElement(Matrix data, Matrix cov, Vector means, int col1, int col2)
    {
        double sum = 0;
        for (int row = 1; row <= data.getRows(); ++row)
            sum += (data.get(row, col1) - means.get(col1)) * (data.get(row, col2) - means.get(col2));
        cov.set(col1, col2, sum / (data.getRows()-1));
        cov.set(col2, col1, sum / (data.getRows()-1));
    }
    
    /**
     * Computes correlation between two samples.
     * @param s1 vector with values of first variable.
     * @param s2 vector with values of second variable.
     * @return sample correlation.
     * @throws DimensionException if samples have less than two elements.
     * @throws NoCorrelationException if one of the samples have zero variance.
     */
    public static double corr(Vector s1, Vector s2)
    {
        double var1 = var(s1);
        double var2 = var(s2);
        ensureCorrelationExists( new Vector(new double[]{var1, var2}) );
        double cov = cov(s1, s2);
        return cov / Math.sqrt(var1 * var2);
    }
    
    private static void ensureCorrelationExists(Vector variances)
    {
        for (int i = 1; i <= variances.getSize(); ++i)
            if (math.utils.Numerics.isZero(variances.get(i)))
                throw new NoCorrelationException("Sample "  + i + " has zero variance");
    }
    
    /**
     * Computes correlation between all variables in given matrix. Columns of the matrix
     * correspond two variables. Rows of the matrix correspond to observations (or individuals).
     * @param m Matrix with observations.
     * @return correlation matrix.
     */
    public static Matrix corr(Matrix m)
    {
        Vector vars = var(m);
        ensureCorrelationExists(vars);
        int cols = m.getCols();
        Matrix corr = cov(m);
        for (int i = 1; i <= cols; ++i)
            for (int j = 1; j <= cols; ++j)
                corr.set(i, j, corr.get(i, j) / Math.sqrt( vars.get(i) * vars.get(j) ));
        return corr;
    }
}
