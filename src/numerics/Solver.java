
package numerics;

/**
 *
 * @author Grzegorz Los
 */
public interface Solver
{
    public void setFunction(Function f);
    public double findRoot(Interval i) throws UnsatisfiedConditionsException;
    public double solve(Interval i, double y) throws UnsatisfiedConditionsException;
    public void setAccuracy(double eps);
}
