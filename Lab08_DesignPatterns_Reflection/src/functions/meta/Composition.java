package functions.meta;

import functions.Function;

/**
 * Composition of two functions
 */
public class Composition implements Function {

    private Function outer, inner;

    public Composition(Function outer, Function inner) {
        this.outer = outer;
        this.inner = inner;
    }

    /**
     * @return - LDB of inner function
     */
    @Override
    public double getLeftDomainBorder() {
        return this.inner.getLeftDomainBorder();
    }

    /**
     * @return - RDB of inner function
     */
    @Override
    public double getRightDomainBorder() {
        return this.inner.getRightDomainBorder();
    }

    /**
     * @param x - argument
     * @return -  outer(inner(x))
     */
    @Override
    public double getFunctionValue(double x) {
        return this.outer.getFunctionValue(this.inner.getFunctionValue(x));
    }
}
