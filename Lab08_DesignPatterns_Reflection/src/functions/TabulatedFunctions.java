package functions;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public final class TabulatedFunctions {

    private static TabulatedFunctionFactory tfFactory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {
        throw new UnsupportedOperationException("Cannot create instance of " + TabulatedFunctions.class.getName() + " class");
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory tabulatedFunctionFactory) {
        tfFactory = tabulatedFunctionFactory;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return tfFactory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return tfFactory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return tfFactory.createTabulatedFunction(points);
    }


    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz,
                                                            double leftX, double rightX, int pointsCount)
                                                                                        throws IllegalArgumentException {
        try {
            return clazz.getConstructor(double.class, double.class, int.class).newInstance(leftX, rightX, pointsCount);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz,
                                                            double leftX, double rightX, double[] values)
                                                                                        throws IllegalArgumentException {
        try {
            return clazz.getConstructor(double.class, double.class, double[].class).newInstance(leftX, rightX, values);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz,
                                                            FunctionPoint[] points) throws IllegalArgumentException {
        try {
            return clazz.getConstructor(FunctionPoint[].class).newInstance((Object) points);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a tabulated function on base of analytic function
     * @param function - function to tabulate
     * @param leftX - left tabulation border
     * @param rightX - right tabulation border
     * @param pointsCount - number of points
     * @return - tabulated function
     */
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if (leftX >= rightX)
            throw new IllegalArgumentException("Invalid bounds");
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder())
            throw new IllegalArgumentException("Tabulation bounds are out of function's domain");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Points number is less than 2");

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double delta = (rightX - leftX) / (pointsCount - 1.0);

        for (int i = 0; i < pointsCount; ++i){
            double x = leftX + delta * i;
            points[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        return createTabulatedFunction(points);
    }

    /**
     * Creates a tabulated function on base of analytic function
     * @param clazz - class to create instance of
     * @param function - function to tabulate
     * @param leftX - left tabulation border
     * @param rightX - right tabulation border
     * @param pointsCount - number of points
     * @return - tabulated function
     */
    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> clazz,
                                             Function function, double leftX, double rightX, int pointsCount){
        if (leftX >= rightX)
            throw new IllegalArgumentException("Invalid bounds");
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder())
            throw new IllegalArgumentException("Tabulation bounds are out of function's domain");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Points number is less than 2");

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double delta = (rightX - leftX) / (pointsCount - 1.0);

        for (int i = 0; i < pointsCount; ++i){
            double x = leftX + delta * i;
            points[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        return createTabulatedFunction(clazz, points);
    }

    /**
     * Output a tabulated function to given OutputStream
     * @param function - function to output
     * @param out - OutputStream to output to
     * @throws IOException - if some troubles with IO
     */
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        try (DataOutputStream outputStream = new DataOutputStream(out)) {
            int pointsCount = function.getPointsCount();
            outputStream.writeInt(pointsCount);
            for (int i = 0; i < pointsCount; ++i) {
                outputStream.writeDouble(function.getPointX(i));
                outputStream.writeDouble(function.getPointY(i));
            }
        }
    }

    /**
     * Input a tabulated function from given InputStream
     * @param in - InputStream to input from
     * @return - resulting tabulated function
     * @throws IOException - if some troubles with IO
     */
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        try (DataInputStream inputStream = new DataInputStream(in)){
            int pointsCount = inputStream.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; ++i){
                points[i] = new FunctionPoint(inputStream.readDouble(), inputStream.readDouble());
            }
            return createTabulatedFunction(points);
        }
    }

    /**
     * Input a tabulated function from given InputStream
     * @param clazz - class to create instance of
     * @param in - InputStream to input from
     * @return - resulting tabulated function
     * @throws IOException - if some troubles with IO
     */
    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> clazz,
                                                           InputStream in) throws IOException {
        try (DataInputStream inputStream = new DataInputStream(in)){
            int pointsCount = inputStream.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; ++i){
                points[i] = new FunctionPoint(inputStream.readDouble(), inputStream.readDouble());
            }
            return createTabulatedFunction(clazz, points);
        }
    }

    /**
     * Write a tabulated function to given Writer
     * @param function - function to write
     * @param out - Writer to write to
     * @throws IOException - if some troubles with IO
     */
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(out)) {
            int pointsCount = function.getPointsCount();

            StringBuilder sb = new StringBuilder();
            sb.append(pointsCount).append(' ');
            //writer.write(pointsCount + " ");
            for (int i = 0; i < pointsCount; ++i) {
                sb.append(function.getPointX(i)).append(' ').append(function.getPointY(i)).append(' ');
                //writer.write(function.getPointX(i) + " " + function.getPointY(i) + " ");
            }
            writer.write(sb.toString());
        }
    }

    /**
     * Read a tabulated function from given Reader
     * @param in - Reader to read from
     * @return - resulting tabulated function
     * @throws IOException - if some troubles with IO
     */
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        try (BufferedReader reader = new BufferedReader(in)) {
            StreamTokenizer st = new StreamTokenizer(reader);
            st.nextToken();
            int pointsCount = (int) st.nval;
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; (i < pointsCount) & (st.nextToken() != StreamTokenizer.TT_EOF); ++i){
                double x = st.nval;
                st.nextToken();
                double y = st.nval;
                points[i] = new FunctionPoint(x, y);
            }
            return createTabulatedFunction(points);
        }
    }

    /**
     * Read a tabulated function from given Reader
     * @param clazz - class to create instance of
     * @param in - Reader to read from
     * @return - resulting tabulated function
     * @throws IOException - if some troubles with IO
     */
    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> clazz,
                                                          Reader in) throws IOException {
        try (BufferedReader reader = new BufferedReader(in)) {
            StreamTokenizer st = new StreamTokenizer(reader);
            st.nextToken();
            int pointsCount = (int) st.nval;
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; (i < pointsCount) & (st.nextToken() != StreamTokenizer.TT_EOF); ++i){
                double x = st.nval;
                st.nextToken();
                double y = st.nval;
                points[i] = new FunctionPoint(x, y);
            }
            return createTabulatedFunction(clazz, points);
        }
    }

}
