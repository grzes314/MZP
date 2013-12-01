
package math.approx;

import math.matrices.Matrix;
import math.matrices.UninvertibleMatrixException;
import math.matrices.Vector;

/**
 *
 * @author Grzegorz Los
 */
public class Regresser
{
    /**
     * Solves regression problem {@code Xb = Y}.
     * @param X matrix of predictors.
     * @param Y vector of observations.
     * @return Vector {@code b}.
     */
    public Vector regress(Matrix X, Vector Y) throws UnsupportedCaseException
    {
        ensureRegressionArgsOK(X, Y);
        ensureAllColsExceptFirstAreNotConst(X);
        Matrix tr = X.transpose();
        try {
            return (tr.mult(X)).getInverted().mult(tr).mult(Y);
        } catch (UninvertibleMatrixException ex) {
            throw new UnsupportedCaseException();
        }
    }
    
    private void ensureAllColsExceptFirstAreNotConst(Matrix X) throws UnsupportedCaseException
    {
        for (int col = 2; col <= X.getCols(); ++col)
        {
            if (isColConstant(X, col))
                throw new UnsupportedCaseException();
        }
    }
    private boolean isColConstant(Matrix X, int col)
    {
        double val = X.get(1, col);
        for (int row = 2; row <= X.getRows(); ++row)
            if (Math.abs(val - X.get(row, col)) > 0.0001)
                return false;
        return true;
    }

    private void ensureRegressionArgsOK(Matrix X, Vector Y)
    {
        if (X.getRows() != Y.getRows())
            throw new IllegalArgumentException("Number of rows in X and Y differ");
        if (X.getRows() < X.getCols())
            throw new IllegalArgumentException("Number of observations is smaller "
                    + "than number of predictors.");
    }

}
