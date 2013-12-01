
package numerics;

/**
 *
 * @author Grzegorz Los
 */
public class Bisection implements Solver
{

    public Bisection()
    {
    }

    public Bisection(Function f)
    {
        this.f = f;
    }

    public Bisection(Function f, double eps)
    {
        this.f = f;
        this.eps = eps;
    }

    @Override
    public void setFunction(Function f)
    {
        this.f = f;
    }

    @Override
    public double findRoot(Interval i) throws UnsatisfiedConditionsException
    {
        return solve(i, 0);
    }

    @Override
    public double solve(Interval i, double y) throws UnsatisfiedConditionsException
    {
        initAuxValues(i, y);
        ensureSolveable();
        runMainLoop();
        return leftEnd;
    }

    private void initAuxValues(Interval i, double y)
    {
        leftEnd = i.beg;
        rightEnd = i.end;
        leftVal = f.eval(i.beg);
        rightVal = f.eval(i.end);
        targetVal = y;
    }

    private void ensureSolveable() throws UnsatisfiedConditionsException
    {
        if (sameSideOfTargetVal(leftVal, rightVal))
            throw new UnsatisfiedConditionsException();
    }
    
    @Override
    public void setAccuracy(double eps)
    {
        this.eps = eps;
    }

    private void runMainLoop()
    {
        while (rightEnd - leftEnd > eps)
        {
            double middle = (leftEnd + rightEnd) / 2;
            middleVal = f.eval(middle);
            if (sameSideOfTargetVal(leftVal, middleVal))
            {
                leftEnd = middle;
                leftVal = middleVal;
            }
            else
            {
                rightEnd = middle;
                rightVal = middleVal;                
            }          
        }
    }

    private boolean sameSideOfTargetVal(double x, double y)
    {
        return Math.signum(x - targetVal) == Math.signum(y - targetVal);
    }

    private Function f;
    private double leftEnd, rightEnd, leftVal, rightVal, middleVal, targetVal;
    private double eps = 1e-3;
}
