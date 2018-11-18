package functions;

import functions.exceptions.InappropriateFunctionPointException;

/**
 * Represents a tabulated function
 * Extends a single-variable function
 *
 * Contains declarations of methods of a tabulated function from mathematical point of view
 */
public interface TabulatedFunction extends Function, Cloneable, Iterable<FunctionPoint> {

    /**
     ** @return - general points count
     */
    int getPointsCount();

    /**
     * Get a point by its number
     * @param index - point number
     * @return - point with needed index
     */
    FunctionPoint getPoint(int index);

    /**
     * Replaces point with given index
     * @param index - given index
     * @param p - point to set
     */
    void setPoint(int index, FunctionPoint p) throws InappropriateFunctionPointException;

    double getPointX(int index);

    void setPointX(int index, double x) throws InappropriateFunctionPointException;

    double getPointY(int index);

    void setPointY(int index, double y);

    /**
     * Deletes point with given index, then shifts to left all after
     * @param index - given index of a point to delete
     */
    void deletePoint(int index);

    /**
     * Adds a point, automatically setting it in right place
     * @param p - point to add
     */
    void addPoint(FunctionPoint p) throws InappropriateFunctionPointException;

    Object clone() throws CloneNotSupportedException;

}
