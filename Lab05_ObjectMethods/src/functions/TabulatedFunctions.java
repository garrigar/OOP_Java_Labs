package functions;

import java.io.*;

public final class TabulatedFunctions {

    private TabulatedFunctions() {
        throw new UnsupportedOperationException("Cannot create instance of " + TabulatedFunctions.class.getName() + " class");
    }

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
        return new ArrayTabulatedFunction(points);
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
            return new ArrayTabulatedFunction(points);
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
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; (i < pointsCount) & (st.nextToken() != StreamTokenizer.TT_EOF); ++i){
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(points);
    }

}
