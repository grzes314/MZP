package math.approx;

import math.utils.Numerics;
import java.util.Arrays;


/**
 * Class representing polynomials.
 * @author Grzegorz Los
 */
public class Polynomial
{
    /**
     * Constructor creating a const polymomial.
     * @param c constant to which the polynomial will be equal.
     */
    public Polynomial(double c)
    {
        deg = 0;
        a = new double[1];
        a[0] = c;
    }

    /**
     * Constructor creating polynomial of given degree with given coefficients.
     * Array b should have at least deg+1 elements. b[0] will be 0th coefficient
     * and so on. If array is longer than deg+1 then additional elements
     * are ignored.
     * @param deg degree of the polynomial.
     * @param b array with coefficients of the polynomial.
     */
    public Polynomial(int deg, double[] b)
    {
        this.deg = deg;
        a = new double[deg+1];
        for (int i=0; i<=deg; ++i)
            a[i] = b[i];
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Polynomial other = (Polynomial) obj;
        if (deg != other.deg)
            return false;
        for (int i = 0; i <= deg; ++i)
            if (!Numerics.doublesEqual(a[i], other.a[i]))
                return false;
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + this.deg;
        hash = 89 * hash + Arrays.hashCode(this.a);
        return hash;
    }

    public double getCoeff(int i)
    {
        return a[i];
    }

    public int getDeg()
    {
        return deg;
    }
    
    /**
     * Returns value of the polynomial for given argument.
     * @param arg argument.
     * @return value of the polynomial.
     */
    public double value(double arg)
    {
        double res = a[deg];
        for (int i=deg-1; i>=0; --i)
            res = res*arg + a[i];
        return res;
    }
    
    /**
     * Returns root of the function in the interval [beg, end] if it exists.
     * The polynomial must have values of diffrent signs at the beginning
     * and at the end of the interval.
     * It there are many roots in the interval then it is not specified
     * which one will be found.
     * @param beg beginning of the interval.
     * @param end ending of the interval.
     * @return root of the polynomial.
     * @throws NoSolutionException if solution in given interval does not exist.
     */
    public double solve(double beg, double end) throws NoSolutionException
    {
        return solve(beg, end, 0, 1e-10);
    }
    
    /**
     * Find solution of P(x) = y (where P is this polynomial)
     * in the interval [beg, end], if the solution exists.
     * The polynomial must satisfy P(beg) < y < P(end) or P(end) > y > P(beg),
     * otherwise the solution won't be found even if it exists.
     * It there are many solutions in the interval then it is not specified
     * which one will be found.
     * @param beg beginning of the interval.
     * @param end ending of the interval.
     * @param y target value.
     * @return solution of P(x)=y.
     * @throws NoSolutionException if solution in given interval does not exist.
     */
    public double solve(double beg, double end, double y) throws NoSolutionException
    {
        return solve(beg, end, y, 1e-10);
    }
           
    /**
     * Find solution of P(x) = y (where P is this polynomial)
     * in the interval [beg, end], if the solution exists.
     * It there are many solutions in the interval then it is not specified
     * which one will be found. It uses bisection method.
     * @param beg beginning of the interval.
     * @param end ending of the interval.
     * @param y target value.
     * @param eps algorithm stops when the interval is shorter than eps.
     * @return solution of P(x)=y.
     * @throws NoSolutionException if solution in given interval does not exist.
     */     
    private double solve(double beg, double end, double y, double eps)
            throws NoSolutionException
    {
        double v1 = value(beg) - y;
        double v2 = value(end) - y;
        if (v1 == 0d) return beg;
        else if (v2 == 0d) return end;
        else if (v1*v2 > 0) throw new NoSolutionException();
        
        while (end - beg > eps)
        {
            double mid = 0.5*(beg + end);
            double v = value(mid) - y;
            if ( v == 0d) return mid;
            if ( (v1 > 0 && v > 0) || (v1 < 0 && v < 0) ) {
                beg = mid;
                v1 = v;
            } else {
                end = mid;
                // v2 = v; assigment not necessary
            }            
        }
        return beg;
    }
    
    /**
     * Degree of the polynomial.
     */
    private int deg;
    
    /**
     * Coefficients of the polynomial.
     */
    private double[] a;
}
