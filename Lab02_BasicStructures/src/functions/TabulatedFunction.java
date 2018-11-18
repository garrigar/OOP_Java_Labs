package functions;

/**
 * Tabulated function as an standard array
 * @see FunctionPoint
 */

public class TabulatedFunction {
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
    public TabulatedFunction(double leftX, double rightX, int pointsCount){
        this(leftX, rightX, new double[pointsCount]);
    }

    /**
     * Creates function by values sequence
     * @param leftX - left domain border
     * @param rightX - right domain border
     * @param values - values sequence
     */
    public TabulatedFunction(double leftX, double rightX, double[] values){
        if (values.length < 2 || leftX > rightX || Math.abs(leftX - rightX) < Utils.EPS) {
            throw new IllegalArgumentException();
        }
        this.pointsCount = values.length;
        this.points = new FunctionPoint[this.pointsCount + 15];
        double delta = (rightX - leftX) / (this.pointsCount - 1.0);

        for (int i = 0; i < this.pointsCount; ++i) {
            this.points[i] = new FunctionPoint(leftX + delta * i, values[i]);
        }
    }

    public double getLeftDomainBorder(){
        return points[0].getX();
    }

    public double getRightDomainBorder(){
        return points[this.pointsCount - 1].getX();
    }

    /**
     * Get function value at point x by interpolation
     * @param x - argument
     * @return - f(x)
     */
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
    public int getPointsCount() {
        return pointsCount;
    }

    /**
     * Get a point by its number
     * @param index - point number
     * @return - point with needed index
     */
    public FunctionPoint getPoint(int index) {
        return points[index];
    }

    /**
     * Replaces point with given index
     * @param index - given index
     * @param p - point to set
     */
    public void setPoint(int index, FunctionPoint p) {
        boundsCheck(index);
        if (index == 0) {
            if (p.getX() < this.points[index + 1].getX()) {
                this.points[index] = new FunctionPoint(p);
            } else {
                throw new IllegalArgumentException();
            }
        } else if (index == this.pointsCount - 1) {
            if (p.getX() >= this.points[index - 1].getX()) {
                this.points[index] = new FunctionPoint(p);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            if (p.getX() >= this.points[index - 1].getX() && p.getX() <= this.points[index + 1].getX()) {
                this.points[index] = new FunctionPoint(p);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public double getPointX(int index){
        boundsCheck(index);
        return points[index].getX();
    }

    public void setPointX(int index, double x){
        boundsCheck(index);
        setPoint(index, new FunctionPoint(x, this.points[index].getY()));
    }

    public double getPointY(int index){
        boundsCheck(index);
        return points[index].getY();
    }

    public void setPointY(int index, double y){
        boundsCheck(index);
        this.points[index].setY(y);
    }

    /**
     * Deletes point with given index, then shifts to left all after
     * @param index - given index of a point to delete
     */
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
    public void addPoint(FunctionPoint p) {
        if (this.points.length - 1 == this.pointsCount) {
            addCapacity();
        }
        int index = 0;
        for (; index < this.pointsCount && p.getX() > this.points[index].getX(); ++index) ;
        if (index != this.pointsCount && this.points[index].getX() == p.getX()) {
            setPoint(index, p);
        } else {
            System.arraycopy(this.points, index, this.points, index + 1, this.pointsCount - index);
            /*for (int i = this.pointsCount; i > index; --i) {
                this.points[i] = this.points[i - 1];
            }*/
            this.points[index] = p;
            ++this.pointsCount;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.pointsCount; i++) {
            builder.append(i).append(": ").append(this.points[i].toString()).append('\n');
        }
        return builder.toString();
    }

    /**
     * Checks index whether it is out of bounds
     * @param index - index to check
     * @throws IndexOutOfBoundsException - if is out of bounds
     */
    private void boundsCheck(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.pointsCount) {
            throw new IndexOutOfBoundsException();
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

}
