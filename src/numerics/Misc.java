
package numerics;

/**
 *
 * @author Grzegorz Los
 */
public class Misc
{
    public static double absError(double a, double b)
    {
        return Math.abs(a-b);
    }
    
    public static double relError(double a, double b)
    {
        return Math.abs((a-b)/b);
    }
    
    public static boolean doublesEqual(double a, double b, double eps)
    {
        return Math.min( absError(a, b), relError(a, b) ) < eps;
    }
}
