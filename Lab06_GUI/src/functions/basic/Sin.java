package functions.basic;

/**
 * Sine function sin(x)
 */
public class Sin extends TrigonometricFunction {
    @Override
    public double getFunctionValue(double x) {
        return Math.sin(x);
    }
}
