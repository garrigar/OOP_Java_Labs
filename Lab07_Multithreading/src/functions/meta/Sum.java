package functions.meta;

import functions.Function;

/**
 * Sum of two functions
 */
public class Sum implements Function {

    // functions to sum
    private Function f1, f2;
    // left domain border
    private double lDB = Double.NEGATIVE_INFINITY;
    // right domain border
    private double rDB = Double.POSITIVE_INFINITY;

    public Sum(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
        this.lDB = (f1.getLeftDomainBorder() < f2.getLeftDomainBorder() ? f2.getLeftDomainBorder() : f1.getLeftDomainBorder());
        this.rDB = (f1.getRightDomainBorder() > f2.getRightDomainBorder() ? f2.getRightDomainBorder() : f1.getRightDomainBorder());
        // if domains are not crossing, resulting domain does not exist
        if (this.lDB > this.rDB){
            this.lDB = this.rDB = Double.NaN;
        }
    }

    @Override
    public double getLeftDomainBorder() {
        return this.lDB;
    }

    @Override
    public double getRightDomainBorder() {
        return this.rDB;
    }

    /**
     * @param x - argument
     * @return (f1+f2)(x) = f1(x) + f2(x)
     */
    @Override
    public double getFunctionValue(double x) {
        return this.f1.getFunctionValue(x) + this.f2.getFunctionValue(x);
    }
}
