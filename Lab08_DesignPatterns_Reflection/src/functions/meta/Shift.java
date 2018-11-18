package functions.meta;

import functions.Function;

/**
 * Shifts function by X and Y by given coefficients
 */
public class Shift implements Function {

    // function to shift
    private Function orig;
    private double shiftX = 0; // shift coefficient by X
    private double shiftY = 0; // shift coefficient by Y

    public Shift(Function orig, double shiftX, double shiftY) {
        this.orig = orig;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    @Override
    public double getLeftDomainBorder() {
        return this.orig.getLeftDomainBorder() + this.shiftX;
    }

    @Override
    public double getRightDomainBorder() {
        return this.orig.getRightDomainBorder() + this.shiftX;
    }

    /**
     * @param x - argument
     * @return f(x - shiftX) + shiftY
     */
    @Override
    public double getFunctionValue(double x) {
        return this.orig.getFunctionValue(x - this.shiftX) + this.shiftY;
    }
}
