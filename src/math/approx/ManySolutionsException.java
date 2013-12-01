
package math.approx;

/**
 *
 * @author Grzegorz Los
 */
public class ManySolutionsException extends Exception
{
    public ManySolutionsException(double[] onesol)
    {
        exampleSolution = onesol;
    }
    
    public final double[] exampleSolution;
}
