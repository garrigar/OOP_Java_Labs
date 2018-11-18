package functions;

/**
 * Represents a single-variable function
 *
 * Contains declarations of common methods of a single-variable function
 */
public interface Function {

    /**
     * @return - left domain border
     */
    double getLeftDomainBorder();

    /**
     * @return - right domain border
     */
    double getRightDomainBorder();

    /**
     * Get function value at point x
     * @param x - argument
     * @return - f(x)
     */
    double getFunctionValue(double x);
}
