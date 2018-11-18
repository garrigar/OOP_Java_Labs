import functions.*;
import functions.basic.Cos;
import functions.basic.Sin;

public class Main {

    public static void main(String[] args) {
        iterators();
        factories();
        reflection();
    }

    private static void reflection() {
        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                }
        );
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);

    }

    private static void factories() {
        Function f = new Cos();
        TabulatedFunction tf;

        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

    }

    private static void iterators() {
        TabulatedFunction f = new LinkedListTabulatedFunction(0, 2, 3);
        for (FunctionPoint fp : f) {
            System.out.println(fp);
        }
        f = new ArrayTabulatedFunction(5, 7, 3);
        for (FunctionPoint fp : f) {
            System.out.println(fp);
        }
        for (FunctionPoint fp :
                new LinkedListTabulatedFunction(new FunctionPoint[]{
                        new FunctionPoint(.5, 3),
                        new FunctionPoint(1, 1)
                })
        ) {
            System.out.println(fp);
        }
    }

}
