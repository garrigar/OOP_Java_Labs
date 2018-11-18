import functions.FunctionPoint;
import functions.TabulatedFunction;

public class Main {

    public static void main(String[] args){

        double[] d = {1, 2, 3};
        TabulatedFunction tf2 = new TabulatedFunction(0, 9, d);


        TabulatedFunction tf = new TabulatedFunction(0, 9, 5);

        FunctionPoint fp = tf.getPoint(2);
        System.out.println(fp.toString());

        System.out.println(tf.toString());

        tf.addPoint(new FunctionPoint(1, 2));

        System.out.println(tf.getPoint(1).toString());

        System.out.println(tf.toString());

        tf.deletePoint(2);
        System.out.println(tf.toString());

        /*int[] a = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.arraycopy(a, 5, a, 4, 6);
        for (int i: a) {
            System.out.print(i + " ");
        }
        System.out.println();
        int[] b = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.arraycopy(b, 5, b, 6, 5);
        for (int i: b) {
            System.out.print(i + " ");
        }*/

    }
}
