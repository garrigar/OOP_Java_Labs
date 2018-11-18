package functions.meta;

import functions.Function;

/**
 * Function to some power
 */
public class Power implements Function {

    // base function
    private Function base;
    // degree
    private double deg;

    // left domain border
    private double lDB = Double.NEGATIVE_INFINITY;
    // right domain border
    private double rDB = Double.POSITIVE_INFINITY;

    public Power(Function base, double deg) {
        this.base = base;
        this.deg = deg;
        this.lDB = base.getLeftDomainBorder();
        this.rDB = base.getRightDomainBorder();
        // if deg is not an integer, then domain must be only positive
        if (this.deg % 1 != 0) {
            if (this.rDB < 0) {
                this.lDB = this.rDB = Double.NaN;
            } else if (this.lDB < 0) {
                this.lDB = 0;
            }
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
     * @return (base^deg)(x) = base(x) ^ deg
     */
    @Override
    public double getFunctionValue(double x) {
        return Math.pow(this.base.getFunctionValue(x), this.deg);
    }
}
