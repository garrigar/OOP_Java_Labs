package functions.basic;

import functions.Function;

/**
 * Log function log<base>(x)
 */
public class Log implements Function {

    private double base = Math.E;

    public Log(){}

    public Log(double base) {
        this.base = base;
    }

    @Override
    public double getLeftDomainBorder() {
        return 0;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.log(x)/Math.log(base);
    }

    public double getBase() {
        return base;
    }
}
