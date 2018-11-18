package functions;

import functions.exceptions.FunctionPointIndexOutOfBoundsException;
import functions.exceptions.InappropriateFunctionPointException;

import java.io.*;

/**
 * Tabulated function as a linked list
 * @see FunctionPoint
 */

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        FunctionPoint[] points = new FunctionPoint[this.pointsCount];
        for (int i = 0; i < this.pointsCount; ++i){
            points[i] = this.getPoint(i);
        }
        out.writeObject(this.pointsCount);
        out.writeObject(points);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int pointsCount = (int) in.readObject();
        FunctionPoint[] points = (FunctionPoint[]) in.readObject();
        this.listInit(points[0], points[1]);
        for (int i = 2; i < pointsCount; ++i) {
            this.appendPoint(points[i]);
        }
        this.cacheNode = this.head.next;
    }

    /**
     * Represents a linked list node containing a FunctionPoint
     */
    private static class FunctionNode {

        FunctionPoint item;

        FunctionNode prev;
        FunctionNode next;

        FunctionNode() {
            this.item = null;
            this.prev = null;
            this.next = null;
        }

        FunctionNode(FunctionPoint item, FunctionNode prev, FunctionNode next) {
            this.item = new FunctionPoint(item);
            this.prev = prev;
            this.next = next;
        }

        FunctionNode(FunctionNode node) {
            this.item = new FunctionPoint(node.item);
            this.prev = node.prev;
            this.next = node.next;
        }

    }

    /** LL head, always empty, points on first and on last element of a list */
    private FunctionNode head;
    /** Number of points */
    private int pointsCount;

    /** Last accessed node, used for access optimization */
    private FunctionNode cacheNode;
    /** Cached node index */
    private int cacheIndex = 0;

    // ---------------------------------------CONSTRUCTORS---------------------------------------------------

    // for correct work of Externalizable
    public LinkedListTabulatedFunction() {}

    /**
     * Creates function by number of points with values equal 0
     *
     * @param leftX       - left domain border
     * @param rightX      - right domain border
     * @param pointsCount - number of points
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this(leftX, rightX, new double[pointsCount]);
    }

    /**
     * Creates function by values sequence
     *
     * @param leftX  - left domain border
     * @param rightX - right domain border
     * @param values - values sequence
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (values == null || values.length < 2 || leftX > rightX || Math.abs(leftX - rightX) < Utils.EPS) {
            throw new IllegalArgumentException();
        }
        double delta = (rightX - leftX) / (values.length - 1.0);

        this.listInit(new FunctionPoint(leftX, values[0]), new FunctionPoint(leftX + delta, values[1]));

        for (int i = 2; i < values.length; ++i) {
            //this.appendPoint(new FunctionPoint(leftX + delta * i, values[i]));
            this.addNodeToTail().item = new FunctionPoint(leftX + delta * i, values[i]);
        }
        this.cacheNode = this.head.next;
    }

    /**
     * Creates function by FunctionPoint sequence
     * @param points - FunctionPoint sequence
     */
    public LinkedListTabulatedFunction(FunctionPoint[] points){
        if (points == null || points.length < 2){
            throw new IllegalArgumentException("Number of points is less than 2");
        }
        if (points[0].getX() >= points[1].getX()){
            throw new IllegalArgumentException("Points are not sorted by X");
        }
        this.listInit(points[0], points[1]);
        for (int i = 2; i < points.length; ++i) {
            this.appendPoint(points[i]);
        }
        this.cacheNode = this.head.next;
    }

    // -----------------------------------------LIST_METHODS-----------------------------------------------------------

    /**
     * Gets a node by index
     * @param index - required index
     * @return - needed node
     */
    private FunctionNode getNodeByIndex(int index) {
        boundsCheck(index);
        if (Math.abs(index - this.cacheIndex) < index && Math.abs(index - this.cacheIndex) < this.pointsCount - index) {
            if (this.cacheIndex > index) {
                for (; index != this.cacheIndex; --this.cacheIndex) {
                    this.cacheNode = this.cacheNode.prev;
                }
            } else {
                for (; index != this.cacheIndex; ++this.cacheIndex) {
                    this.cacheNode = this.cacheNode.next;
                }
            }
        } else if (index < this.pointsCount - index) {
            for (this.cacheIndex = 0, this.cacheNode = head.next; this.cacheIndex != index; ++this.cacheIndex) {
                this.cacheNode = this.cacheNode.next;
            }
        } else {
            for (this.cacheIndex = this.pointsCount - 1, this.cacheNode = head.prev; this.cacheIndex != index; --this.cacheIndex) {
                this.cacheNode = this.cacheNode.prev;
            }
        }
        return this.cacheNode;
    }

    /**
     * Adds a default node to tail
     * @return - created node
     */
    private FunctionNode addNodeToTail() {
        ++pointsCount;
        FunctionNode newNode = new FunctionNode(new FunctionPoint(), head.prev, head.next);
        head.next.prev = newNode;
        head.prev.next = newNode;
        head.prev = newNode;
        return newNode;
    }

    /**
     * Adds a default node to head
     * @return - created node
     */
    private FunctionNode addNodeToHead() {
        ++pointsCount;
        FunctionNode newNode = new FunctionNode(new FunctionPoint(), head.prev, head.next);
        head.next.prev = newNode;
        head.prev.next = newNode;
        head.next = newNode;
        return newNode;
    }

    /**
     * Adds a default node by index, shifting all to right
     * @param index - index
     * @return - created node
     */
    private FunctionNode addNodeByIndex(int index) {
        if (index == 0)
            return addNodeToHead();
        if (index == this.pointsCount)
            return addNodeToTail();
        ++this.pointsCount;
        FunctionNode oldNode = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(new FunctionPoint(), oldNode.prev, oldNode);
        oldNode.prev.next = newNode;
        oldNode.prev = newNode;
        return newNode;
    }

    /**
     * Deletes a node by index
     * @param index - index
     * @return - deleted node
     */
    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode delNode = getNodeByIndex(index);
        --this.pointsCount;
        if (index == 0) {
            head.next = delNode.next;
        } else if (index == pointsCount - 1) {
            head.prev = delNode.prev;
        }
        delNode.next.prev = delNode.prev;
        delNode.prev.next = delNode.next;
        return delNode;
    }

    // --------------------------------------END_LIST_METHODS-----------------------------------------------------------

    // ---------------------------------------SERVICE_METHODS-----------------------------------------------------------

    /**
     * Initializes a list from 0 elements to 2 elements: adding first two points
     * @param first - first point
     * @param second - second point
     */
    private void listInit(FunctionPoint first, FunctionPoint second) {
        this.pointsCount = 2;
        head = new FunctionNode();
        FunctionNode firstNode = new FunctionNode(first, null, null);
        FunctionNode secondNode = new FunctionNode(second, null, null);
        head.next = firstNode;
        head.prev = secondNode;
        firstNode.next = secondNode;
        firstNode.prev = secondNode;
        secondNode.next = firstNode;
        secondNode.prev = firstNode;
    }

    /**
     * Appends a point to the end of the list, checking its X coordinate is bigger than last list item
     * @param fp - point to add
     * @throws IllegalArgumentException - if X coordinate of new point is equal or less than last list item's X
     */
    private void appendPoint(FunctionPoint fp) throws IllegalArgumentException {
        if (head.prev.item.getX() < fp.getX()) {
            this.addNodeToTail().item = new FunctionPoint(fp);
        } else
            throw new IllegalArgumentException("X coordinate of point being added (x = " + fp.getX() + ") must be " +
                    "greater than last item's X (x = " + head.prev.item.getX() + ")");
    }

    /**
     * Checks index whether it is out of bounds
     *
     * @param index - index to check
     * @throws FunctionPointIndexOutOfBoundsException - if is out of bounds
     */
    private void boundsCheck(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of range of [0; " + (this.pointsCount - 1) + "]");
        }
    }

    // -----------------------------------END_SERVICE_METHODS-----------------------------------------------------------

    // --------------------------------------LL_TF_METHODS--------------------------------------------------------------

    @Override
    public double getLeftDomainBorder() {
        return this.head.next.item.getX();
    }

    @Override
    public double getRightDomainBorder() {
        return this.head.prev.item.getX();
    }

    /**
     * Get function value at point x by linear interpolation
     *
     * @param x - argument
     * @return - f(x)
     */
    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;
        int i;

        this.cacheNode = this.head.next;
        this.cacheIndex = 0;
        for (i = 1; i < this.pointsCount && this.getPointX(i) < x; ++i) ;
        FunctionNode nodeI = getNodeByIndex(i);
        double x1 = nodeI.prev.item.getX(); // this.getPointX(i - 1);
        double y1 = nodeI.prev.item.getY(); // this.getPointY(i - 1);
        double x2 = nodeI.item.getX(); // this.getPointX(i);
        double y2 = nodeI.item.getY(); // this.getPointY(i);
        return ((x - x1) * (y2 - y1)) / (x2 - x1) + y1;
    }

    /**
     ** @return - general points count
     */
    @Override
    public int getPointsCount() {
        return this.pointsCount;
    }

    /**
     * Get a point by its number
     *
     * @param index - point number
     * @return - point with needed index
     */
    @Override
    public FunctionPoint getPoint(int index) {
        return getNodeByIndex(index).item;
    }

    /**
     * Replaces point with given index
     *
     * @param index - given index
     * @param p     - point to set
     */
    @Override
    public void setPoint(int index, FunctionPoint p) throws InappropriateFunctionPointException {
        if (index == 0) {
            if (p.getX() < head.next.next.item.getX()) {
                head.next.item = new FunctionPoint(p);
            } else {
                throw new InappropriateFunctionPointException("X parameter of the new point is not good with neighbours");
            }
        } else if (index == this.pointsCount - 1) {
            if (p.getX() >= head.prev.prev.item.getX()) {
                head.prev.item = new FunctionPoint(p);
            } else {
                throw new InappropriateFunctionPointException("X parameter of the new point is not good with neighbours");
            }
        } else {
            FunctionNode node = getNodeByIndex(index);
            if (p.getX() >= node.prev.item.getX() && p.getX() <= node.next.item.getX()) {
                node.item = new FunctionPoint(p);
            } else {
                throw new InappropriateFunctionPointException("X parameter of the new point is not good with neighbours");
            }
        }
    }

    @Override
    public double getPointX(int index) {
        return getNodeByIndex(index).item.getX();
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        setPoint(index, new FunctionPoint(x, getPointY(index)));
    }

    @Override
    public double getPointY(int index) {
        return getNodeByIndex(index).item.getY();
    }

    @Override
    public void setPointY(int index, double y) {
        this.getNodeByIndex(index).item.setY(y);
    }

    /**
     * Deletes point with given index, then shifts to left all after
     *
     * @param index - given index of a point to delete
     */
    @Override
    public void deletePoint(int index) {
        if (this.pointsCount < 3) {
            throw new IllegalStateException();
        }
        deleteNodeByIndex(index);
    }

    /**
     * Adds a point, automatically setting it in right place
     *
     * @param p - point to add
     */
    @Override
    public void addPoint(FunctionPoint p) throws InappropriateFunctionPointException {
        int index = 0;
        this.cacheNode = this.head.next;
        this.cacheIndex = 0;
        for (; index < this.pointsCount && p.getX() > getPointX(index); ++index) ;
        if (index != this.pointsCount && getPointX(index) == p.getX()) {
            throw new InappropriateFunctionPointException("Point with x = " + p.getX() + " is already defined!");
            //setPoint(index, p);
        } else {
            addNodeByIndex(index).item = new FunctionPoint(p);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        FunctionNode cur = head.next;
        for (int i = 0; i < this.pointsCount; i++) {
            builder.append(i).append(": ").append(cur.item.toString()).append('\n');
            cur = cur.next;
        }
        return builder.toString();
    }

}