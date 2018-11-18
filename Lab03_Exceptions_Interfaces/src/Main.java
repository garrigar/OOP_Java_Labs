import functions.FunctionPoint;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.exceptions.InappropriateFunctionPointException;

public class Main {

    public static void main(String[] args){

        double[] d = {1, 2, 3};
        TabulatedFunction tf2 = new ArrayTabulatedFunction(0, 9, d);


        TabulatedFunction tf  = new LinkedListTabulatedFunction(0, 9, 5);

        FunctionPoint fp = tf.getPoint(2);
        System.out.println(fp.toString());

        System.out.println(tf.toString());

        try {
            tf.addPoint(new FunctionPoint(9, 2));
        } catch (InappropriateFunctionPointException e) {
            e.printStackTrace();
        }

        try {
            tf.setPoint(4, new FunctionPoint(0, 2));
        } catch (InappropriateFunctionPointException e) {
            e.printStackTrace();
            System.err.println();
        }

        System.out.println(tf.getPoint(1).toString());

        System.out.println(tf.toString());

        try {
            tf.deletePoint(2);
            tf.deletePoint(2);
            tf.deletePoint(2);
            tf.deletePoint(2);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.err.println();
        }
        System.out.println(tf.toString());



    }
}
