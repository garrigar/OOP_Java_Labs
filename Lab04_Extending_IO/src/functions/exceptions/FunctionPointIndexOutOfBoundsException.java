package functions.exceptions;

/**
 * Index out of bounds exception, if trying to access by unreal indexes of function points
 */
public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public FunctionPointIndexOutOfBoundsException(String s) {
        super(s);
    }
}
