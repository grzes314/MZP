
package numerics;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author grzes
 */
public class BisectionTest
{
    
    public BisectionTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }
    
    @Before
    public void setUp()
    {
        f1 = new Function() {
            @Override public double eval(double x) {
                return (x-2)*(x-3)*(x-4);
            }
        };
        f2 = new Function() {
            @Override public double eval(double x) {
                return x*x;
            }
        };
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of findRoot method, of class Bisection.
     */
    @Test
    public void testFindRoot() throws Exception
    {
        System.out.println("findRoot");
        Interval i = new Interval(0, 8);
        Bisection instance = new Bisection(f1, eps);
        double result = instance.findRoot(i);
        boolean succ =  Math.abs(result - 2) < eps  ||
                        Math.abs(result - 3) < eps  ||
                        Math.abs(result - 4) < eps;
        assertTrue(succ);
    }

    /**
     * Test of solve method, of class Bisection.
     */
    @Test
    public void testSolve() throws Exception
    {
        System.out.println("solve");
        Interval i = new Interval(-4, -1);
        double y = 4;
        Bisection instance = new Bisection(f2, eps);
        double expResult = -2.0;
        double result = instance.solve(i, y);
        assertEquals(expResult, result, eps);
    }
    
    private Function f1, f2;
    private double eps = 1e-6;
}
