
package circle;

/**
 *
 * @author Grzegorz Los
 */
public class Parameters
{
    public Parameters(double h, double t) throws InvalidParamsException
    {
        this.h = h;
        this.t = t;
        ensureParamsOK();
    }
    
    private void ensureParamsOK() throws InvalidParamsException
    {
        if (t / h > 1e7)
            throw new InvalidParamsException("Given parameters require " +
                String.format("%.0f", t/h) +
                " steps, which is to large number.");
    }
    
    public int getNumberOfSteps()
    {
        return new Double(Math.ceil(t / h)).intValue();
    }
    
    public final double h;
    public final double t;

}
