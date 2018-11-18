package functions;

import functions.exceptions.FunctionPointIndexOutOfBoundsException;
import functions.exceptions.InappropriateFunctionPointException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Tabulated function as a standard array
 * @see FunctionPoint
 */

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    /** Array of function points */
    private FunctionPoint[] points;
    /** Number of points */
    private int pointsCount;

    /**
     * Creates function by number of points with values equal 0
     * @param leftX - left domain border
     * @param rightX - right domain border
     * @param pointsCount - number of points
     */
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        this(leftX, rightX, new double[pointsCount]);
    }

    /**
     * Creates function by values sequence
     * @param leftX - left domain border
     * @param rightX - right domain border
     * @param values - values sequence
     */
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        if (values == null || values.length < 2 || leftX > rightX || Math.abs(leftX - rightX) < Utils.EPS) {
            throw new IllegalArgumentException();
        }
        this.pointsCount = values.length;
        this.points = new FunctionPoint[this.pointsCount + 15];
        double delta = (rightX - leftX) / (this.pointsCount - 1.0);

        for (int i = 0; i < this.pointsCount; ++i) {
            this.points[i] = new FunctionPoint(leftX + delta * i, values[i]);
        }
    }

    /**
     * Creates function by FunctionPoint sequence
     * @param points - FunctionPoint sequence
     */
    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Number of points is less than 2");
        }
        this.pointsCount = points.length;
        this.points = new FunctionPoint[this.pointsCount + 15];

        this.points[0] = points[0];
        for (int i = 1; i < this.pointsCount; ++i) {
            if (points[i].getX() > points[i-1].getX()) {
                this.points[i] = points[i];
            } else throw new IllegalArgumentException("Points are not sorted by X");
        }
    }

    @Override
    public double getLeftDomainBorder(){
        return points[0].getX();
    }

    @Override
    public double getRightDomainBorder(){
        return points[this.pointsCount - 1].getX();
    }

    /**
     * Get function value at point x by linear interpolation
     * @param x - argument
     * @return - f(x)
     */
    @Override
    public double getFunctionValue(double x){
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;
        int i;
        for (i = 1; i < this.pointsCount && this.points[i].getX() < x; ++i);
        double x1 = this.points[i - 1].getX();
        double y1 = this.points[i - 1].getY();
        double x2 = this.points[i].getX();
        double y2 = this.points[i].getY();
        return ((x - x1) * (y2 - y1))/(x2 - x1) + y1;
    }

    /**
     ** @return - general points count
     */
    @Override
    public int getPointsCount() {
        return pointsCount;
    }

    /**
     * Get a point by its number
     * @param index - point number
     * @return - point with needed index
     */
    @Override
    public FunctionPoint getPoint(int index) {
        boundsCheck(index);
        return points[index];
    }

    /**
     * Replaces point with given index
     * @param index - given index
     * @param p - point to set
     */
    @Override
    public void setPoint(int index, FunctionPoint p) throws InappropriateFunctionPointException {
        boundsCheck(index);
        if (index == 0) {
            if (p.getX() < this.points[index + 1].getX()) {
                this.points[index] = new FunctionPoint(p);
            } else {
                throw new InappropriateFunctionPointException("X parameter of the new point is not good with neighbours");
            }
        } else if (index == this.pointsCount - 1) {
            if (p.getX() >= this.points[index - 1].getX()) {
                this.points[index] = new FunctionPoint(p);
            } else {
                throw new InappropriateFunctionPointException("X parameter of the new point is not good with neighbours");
            }
        } else {
            if (p.getX() >= this.points[index - 1].getX() && p.getX() <= this.points[index + 1].getX()) {
                this.points[index] = new FunctionPoint(p);
            } else {
                throw new InappropriateFunctionPointException("X parameter of the new point is not good with neighbours");
            }
        }
    }

    @Override
    public double getPointX(int index){
        boundsCheck(index);
        return points[index].getX();
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        boundsCheck(index);
        setPoint(index, new FunctionPoint(x, this.points[index].getY()));
    }

    @Override
    public double getPointY(int index){
        boundsCheck(index);
        return points[index].getY();
    }

    @Override
    public void setPointY(int index, double y){
        boundsCheck(index);
        this.points[index].setY(y);
    }

    /**
     * Deletes point with given index, then shifts to left all after
     * @param index - given index of a point to delete
     */
    @Override
    public void deletePoint(int index) {
        if (this.pointsCount < 3) {
            throw new IllegalStateException();
        }
        boundsCheck(index);
        this.points[index] = null;
        System.arraycopy(this.points, index + 1, this.points, index, this.pointsCount - index);
        /*for (; index < this.pointsCount; ++index) {
            this.points[index] = this.points[index + 1];
        }*/
        --this.pointsCount;
    }

    /**
     * Adds a point, automatically setting it in right place
     * @param p - point to add
     */
    @Override
    public void addPoint(FunctionPoint p) throws InappropriateFunctionPointException {
        if (this.points.length - 1 == this.pointsCount) {
            addCapacity();
        }
        int index = 0;
        for (; index < this.pointsCount && p.getX() > this.points[index].getX(); ++index) ;
        if (index != this.pointsCount && this.points[index].getX() == p.getX()) {
            throw new InappropriateFunctionPointException("Point with x = " + p.getX() + " is already defined!");
            //setPoint(index, p);
        } else {
            System.arraycopy(this.points, index, this.points, index + 1, this.pointsCount - index);
            /*for (int i = this.pointsCount; i > index; --i) {
                this.points[i] = this.points[i - 1];
            }*/
            this.points[index] = p;
            ++this.pointsCount;
        }
    }

    /**
     * Checks index whether it is out of bounds
     * @param index - index to check
     * @throws FunctionPointIndexOutOfBoundsException - if is out of bounds
     */
    private void boundsCheck(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of range of [0; " + (this.pointsCount-1) + "]");
        }
    }

    /**
     * If capacity and pointsCount are equal, this method swaps current array
     * with a bigger array, copying all the stuff into it
     */
    private void addCapacity() {
        FunctionPoint[] newArray = new FunctionPoint[(int) (this.points.length * 1.7)];
        System.arraycopy(this.points, 0, newArray, 0, this.pointsCount);
        this.points = newArray;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        boolean first = true;
        for (int i = 0; i < this.pointsCount; i++) {
            if (first){
                first = false;
            } else{
                builder.append(", ");
            }
            builder.append(this.points[i].toString());
            //builder.append(i).append(": ").append(this.points[i].toString()).append('\n');
        }
        builder.append('}');
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TabulatedFunction)) return false;
        if (obj instanceof ArrayTabulatedFunction){
            ArrayTabulatedFunction atfObj = (ArrayTabulatedFunction) obj;
            if (atfObj.pointsCount != this.pointsCount) return false;
            for (int i = 0; i < this.pointsCount; ++i){
                if (!atfObj.points[i].equals(this.points[i])) return false;
            }
            return true;
        } else {
            TabulatedFunction tfObj = (TabulatedFunction) obj;
            if (tfObj.getPointsCount() != this.pointsCount) return false;
            for (int i = 0; i < this.pointsCount; ++i){
                if (!tfObj.getPoint(i).equals(this.points[i])) return false;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        return (31 * Integer.hashCode(this.pointsCount) + Arrays.hashCode(this.points));
    }

    @Override
    /*protected*/ public Object clone() throws CloneNotSupportedException {
        ArrayTabulatedFunction result = (ArrayTabulatedFunction) super.clone();
        result.points = (FunctionPoint[]) this.points.clone();
        return result;
        //return super.clone();
    }
}
