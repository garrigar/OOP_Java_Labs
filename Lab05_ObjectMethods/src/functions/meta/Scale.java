package functions.meta;

import functions.Function;

/**
 * Scales function by X and Y by given coefficients
 */
public class Scale implements Function {

    // function to scale
    private Function orig;
    private double scaleX = 1; // scale coefficient by X
    private double scaleY = 1; // scale coefficient by Y

    // left domain border
    private double lDB = Double.NEGATIVE_INFINITY;
    // right domain border
    private double rDB = Double.POSITIVE_INFINITY;

    public Scale(Function orig, double scaleX, double scaleY) {
        if (scaleX == 0) throw new IllegalArgumentException("Scale coefficient by X must not be equal 0");
        this.orig = orig;
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        // scaling domain
        this.lDB = orig.getLeftDomainBorder() * scaleX;
        this.rDB = orig.getRightDomainBorder() * scaleX;
        // if scaleX < 0, must swap left and right
        if (scaleX < 0){
            double temp = this.lDB;
            this.lDB = this.rDB;
            this.rDB = temp;
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
     * @return f(x / scaleX) * scaleY
     */
    @Override
    public double getFunctionValue(double x) {
        return this.orig.getFunctionValue(x / this.scaleX) * this.scaleY;
    }
}
