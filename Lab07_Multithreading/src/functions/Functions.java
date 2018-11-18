package functions;

import functions.meta.*;

public final class Functions {

    private Functions() {
        throw new UnsupportedOperationException("Cannot create instance of " + Functions.class.getName() + " class");
    }

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    public static double integrate(Function f, double leftX, double rightX, double dx) {
        if (leftX > rightX) {
            if (rightX < f.getLeftDomainBorder() || leftX > f.getRightDomainBorder())
                throw new IllegalArgumentException("Integration borders are out of function domain borders");
        } else {
            if (leftX < f.getLeftDomainBorder() || rightX > f.getRightDomainBorder())
                throw new IllegalArgumentException("Integration borders are out of function domain borders");
        }
        if (leftX == rightX)
            return 0;

        int invert = 1;
        if (leftX > rightX) {
            double t = leftX;
            leftX = rightX;
            rightX = t;
            invert = -1;
        }

        int steps = (int) Math.ceil((rightX - leftX) / dx);
        double ans = 0;

        double x = leftX;
        for (int i = 0; i < steps - 1; ++i, x += dx) {
            ans += dx * ((f.getFunctionValue(x) + f.getFunctionValue(x + dx)) / 2);
        }
        ans += (rightX - x) * ((f.getFunctionValue(x) + f.getFunctionValue(rightX)) / 2);

        return ans * invert;
    }
}
