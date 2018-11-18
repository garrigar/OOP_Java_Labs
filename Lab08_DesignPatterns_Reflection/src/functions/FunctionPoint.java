package functions;

import java.io.Serializable;
import java.util.Objects;

/** Function point in rectangular coordinates */

public class FunctionPoint implements Serializable, Cloneable {
    private double x, y;

    /** Default constructor, creates point (0, 0) */
    public FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }

    /**
     * Creates point (x, y)
     * @param x - OX coordinate
     * @param y - OY coordinate
     */
    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a copy of a point
     * @param fp - point to copy
     */
    public FunctionPoint(FunctionPoint fp){
        this.x = fp.getX();
        this.y = fp.getY();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ')';
        //return "x = " + x + "; y = " + y + ";";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionPoint)) return false;
        FunctionPoint fp = (FunctionPoint) obj;
        return ((this.getX() == fp.getX()) && (this.getY() == fp.getY()));
        //return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
