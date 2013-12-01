package math.utils;

import math.matrices.Vector;

public class Sequences
{
    /**
     * This class is not supposed to be subclassed.
     */
    private Sequences() {
    }

    public static Vector arithmetic(int length, double from, double step)
    {
        Vector vec = new Vector(length);
        for (int row = 1; row <= length; ++row)
            vec.set(row, from + (row-1)*step);
        return vec;
    }
    
    public static double sumOfArithmetic(int length, double from, double step)
    {
        return (from + (length-1)*step/2) * length;
    }
    
    public static Vector geometric(int length, double from, double q)
    {
        Vector vec = new Vector(length);
        double qn = from;
        for (int row = 1; row <= length; ++row)
        {
            vec.set(row, qn);
            qn *= q;
        }
        return vec;
    }
    
    public static double sumOfGeometric(int length, double from, double q)
    {
        return from * (1 - Math.pow(q, length)) / (1 - q);
    }
    
    public static Vector constant(int length, double c)
    {
        Vector vec = new Vector(length);
        for (int row = 1; row <= length; ++row)
            vec.set(row, c);
        return vec;
    }
    
    public static double sumOfConstant(int length, int c)
    {
        return c*length;
    }

    public static Vector squares(int length)
    {
        Vector v = new Vector(length);
        for (int row = 1; row <= length; ++row)
            v.set(row, row*row);
        return v;
    }

    public static double sumOfSquares(int n)
    {
        return (double)n * (n+1) * (2*n + 1) / 6;
    }
    
    public static Vector intersperse(int length)
    {
        Vector v = new Vector(length);
        for (int row = 1; row <= length; ++row)
            v.set(row, (row % 2 == 0 ? -1 : 1));
        return v;
    }
    
    public static double sumOfInterperse(int length)
    {
        return length % 2 == 0 ? 0 : 1;
    }
    
    public static Vector sineOfNatural(int length)
    {
        Vector v = new Vector(length);
        for (int row = 1; row <= length; ++row)
            v.set(row, Math.sin(row));
        return v;
    }

    public static Vector cosineOfNatural(int length)
    {
        Vector v = new Vector(length);
        for (int row = 1; row <= length; ++row)
            v.set(row, Math.cos(row));
        return v;
    }

    public static Vector tangentOfNatural(int length)
    {
        Vector v = new Vector(length);
        for (int row = 1; row <= length; ++row)
            v.set(row, Math.tan(row));
        return v;
    }

    public static Vector cotangentOfNatural(int length)
    {
        Vector v = new Vector(length);
        for (int row = 1; row <= length; ++row)
            v.set(row, 1./Math.tan(row));
        return v;
    }
}
