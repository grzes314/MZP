
package math.approx;

import static math.utils.Numerics.isZero;

/**
 * Class providing method for solving systems of linear equations.
 * @author Grzegorz Los
 */
public class Gauss
{
    /**
     * Implementation of Gauss elimination method for solving systems of 
     * linear equations. System is represented as an array of equations, where
     * equation is also an array. Each equation must have the same length. 
     * Last element of equation is a RHS of equal sign. Previous elements are
     * coefficients, system[i][j] is a coefficient in i'th equation standing 
     * by j'th variable (indices of equations and variables are 0-based). This
     * method guarantees that system given as an argument will not be modified.
     * @param system system of linear equations described above.
     * @return vector (an array in Java sense) with solution.
     * @throws NoSolutionException when system does not have any solutions.
     * @throws ManySolutionsException when system has infinitely many solutions,
     * in that case Exception holds one example solution.
     */
    public double[] solve(double[][] system)
            throws NoSolutionException, ManySolutionsException
    {
        return solve(system, false);
    }

    /**
     * An overloaded version. Allows user to decide whether the system given as
     * an argument may be modified during calculations. If user does not
     * need system after solving, then for efficiency he may want to skip
     * creating copy of the system.
     * @param system system of linear equations described above.
     * @param modifySystem indicates if the given argument may be modified 
     * during calculations.
     * @return vector (an array in Java sense) with solution.
     * @throws NoSolutionException when system does not have any solutions.
     * @throws ManySolutionsException when system has infinitely many solutions,
     * in that case Exception holds one example solution.
     */
    public double[] solve(double[][] system, boolean modifySystem)
            throws NoSolutionException, ManySolutionsException
    {
        ensureSystemIsValid(system);
        double[][] systemCopy = system;
        if (!modifySystem)
        {
            systemCopy = new double[system.length][];
            for (int i = 0; i < system.length; ++i)
                systemCopy[i] = system[i].clone();
        }
        GaussAux aux = new GaussAux(systemCopy);
        
        toTriangle(aux);
        return fromTriangle(aux);
    }
    
    /**
     * Checks if system given as an argument is valid. System is invalid when:
     * it is null, it has length 0, some equation is null, equation is
     * represented by less then two doubles, equations do not have the same
     * length.
     * @param system system of linear equations described above.
     * @throws InvalidArgumentException when system is invalid.
     */
    private void ensureSystemIsValid(double[][] system)
    {
        if (system == null)
            throw new InvalidArgumentException("System can not be null");
        if (system.length == 0)
            throw new InvalidArgumentException("System must have at least one equation");
        if (system[0] == null)
            throw new InvalidArgumentException("System has nulls as equations");
        int n = system[0].length;
        if (n < 2)
            throw new InvalidArgumentException("Equation must be "
                    + "represented by at least two doubles");
        for (int eq = 1; eq < system.length; ++eq)
        {
            if (system[eq] == null)
                throw new InvalidArgumentException("System has nulls as equations");
            if (system[eq].length != n)
                throw new InvalidArgumentException("Equations' lengths differ");
        }        
    }
    
    /**
     * Transforms system to row echelon form.
     * @param aux box with data.
     */
    private void toTriangle(GaussAux aux)
    {
        int eq = 0, var = 0;
        while (eq < aux.equations && var < aux.variables)
        {
            boolean ok = rearrange(aux, eq, var);
            if (ok) // variable var can be computed from equation eq
            {
                aux.eq4var[var] = eq;
                subtract(aux, eq, var);
                eq++;
            }
            else
            {
                aux.eq4var[var] = -1;
            }
            var++;
        }
        if (eq < aux.equations) // some rows have only zeros
            aux.rowWithZeros = eq;
    }
    
    /**
     * Computes solution from system in row echelon form.
     * @param aux box with data.
     * @return vector (an array in Java sense) with solution.
     * @throws NoSolutionException when system does not have any solutions.
     * @throws ManySolutionsException when system has infinitely many solutions,
     * in that case Exception holds one example solution.
     */
    private double[] fromTriangle(GaussAux aux)
            throws NoSolutionException, ManySolutionsException
    {
        ensureHasSolutions(aux);
        double result[] = new double[aux.variables];
        boolean oneSolution = true;
        for (int var = aux.variables - 1; var >= 0; --var)
        {
            int eq = aux.eq4var[var];
            if (eq < 0)
            {
                oneSolution = false;
                result[var] = 0;
            }
            else
            {
                double res = aux.system[eq][aux.variables];
                for (int j = aux.variables - 1; j > var; --j)
                    res -= aux.system[eq][j] * result[j];
                result[var] = res / aux.system[eq][var];
            }
        }
        if (oneSolution) return result;   
        else throw new ManySolutionsException(result);
    }

    /**
     * Finds an equation which will be used to eliminate next variable. If each
     * of the remaining equations has zero coefficient standing by variable
     * number 'var' the rearranging fails and false is returned.
     * @param aux box with data.
     * @param eq index of first equation taken into consideration.
     * @param var index of the variable to be eliminated.
     * @return true if and only if the equation with index 'eq' (maybe after
     * some swap) has the nonzero coefficient by variable number 'var'.
     */
    private static boolean rearrange(GaussAux aux, int eq, int var) 
    {
        double[][] system = aux.system;
        if ( !isZero(system[eq][var]) )
            return true;
        for (int j = eq+1; j < aux.equations; ++j)
        {
            if ( !isZero(system[j][var]) )
            {
                double[] pom = system[eq];
                system[eq] = system[j];
                system[j] = pom;
                return true;
            }
        }
        return false;
    }

    /**
     * Uses equation number 'eq' to eliminate variable number 'var' from the
     * system.
     * @param aux box with data.
     * @param eq index of the equation used to be multiplied and subtract
     * from the others in order to eliminate variable number 'var'.
     * @param var index of the variable to be eliminated.
     */
    private static void subtract(GaussAux aux, int eq, int var)
    {
        double[][] system = aux.system;
        for (int j = eq + 1; j < aux.equations; ++j)
        {
            double scalar = system[j][var] / system[eq][var];
            system[j][var] = 0.0;
            for (int k = var + 1; k <= aux.variables; ++k)
            {
                system[j][k] -= scalar * system[eq][k];
            }
        }
    }

    /**
     * Checks if the system has any solutions. Must be called when system is
     * already in row echelon form.
     * @param aux box with data.
     * @throws NoSolutionException when system does not have any solutions.
     */
    private void ensureHasSolutions(GaussAux aux) throws NoSolutionException
    {
        for (int eq = aux.rowWithZeros; eq < aux.equations; ++eq)
            if ( !isZero(aux.system[eq][aux.variables]) )
                throw new NoSolutionException();
    }
    
    /**
     * Print system to the standard output.
     * @param system the system to be printed.
     */
    private void print(double[][] system)
    {
        for (int i=0; i<system.length; ++i)
        {
            for (int j=0; j<system[i].length; ++j)
            {
                System.out.print(system[i][j] + " ");
            }
            System.out.println();
        }
    }
}

/**
 * Some intermediate data for Gauss elimination method.
 * @author Grzegorz Los
 */
class GaussAux
{
    GaussAux(double[][] system)
    {
        equations = system.length;
        variables = system[0].length - 1;
        this.system = system;
        eq4var = new int[variables];
        for (int i = 0; i < variables; ++i)
            eq4var[i] = -1;
        rowWithZeros = equations + 1;
    }
    
    /**
     * The number of variables in the system.
     */
    final int variables;
    
    /**
     * The number of equations in the system.
     */
    final int equations;
    
    /**
     * 
     */
    double[][] system;
    
    /**
     * Array holds information from which equations variables can be calculated.
     * Both equations and variables numbers are 0-based. Value eq4var[i] is the
     * number of equation from which ith variable can be calculated. If
     * eq4var[i] == -1, then ith variable can be any arbitrary value (solution
     * of the system is not unique).
     */
    int[] eq4var;
    
    /**
     * The first row whose coefficients are zero.
     */
    int rowWithZeros;
}
