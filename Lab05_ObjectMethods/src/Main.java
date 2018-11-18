import functions.*;
import functions.basic.*;
import functions.exceptions.InappropriateFunctionPointException;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        TabulatedFunction atf = new ArrayTabulatedFunction(0, 10, 11);
        System.out.println("ArrayTF:\n" + atf.toString());
        System.out.println(atf.hashCode());
        TabulatedFunction lltf = new LinkedListTabulatedFunction(0, 10, 11);
        System.out.println("LinkedListTF:\n" + lltf.toString());
        System.out.println(lltf.hashCode());

        System.out.println("Are they equal? " + atf.equals(lltf) + "\nSure? " + lltf.equals(atf));
        System.out.println();

        System.out.println(atf.equals(atf));

        ArrayTabulatedFunction atf1 = new ArrayTabulatedFunction(0, 10, 10);
        System.out.println(atf.equals(atf1));
        System.out.println(atf.hashCode() + " vs. " + atf1.hashCode());
        System.out.println();

        System.out.println(atf.hashCode());
        try {
            atf.setPoint(2, new FunctionPoint(2, 0));
        } catch (InappropriateFunctionPointException e) {
            e.printStackTrace();
        }
        System.out.println(atf.hashCode());
        try {
            atf.setPoint(2, new FunctionPoint(2, 0.000000001));
        } catch (InappropriateFunctionPointException e) {
            e.printStackTrace();
        }
        System.out.println(atf.hashCode());
        System.out.println();

        try {
            System.out.println("Array");
            TabulatedFunction a1 = (TabulatedFunction) atf.clone();
            System.out.println(atf);
            System.out.println(a1);
            System.out.println();

            atf.addPoint(new FunctionPoint(2.5, 10 ));

            System.out.println(atf);
            System.out.println(a1);
            System.out.println();

            a1.deletePoint(0);

            System.out.println(atf);
            System.out.println(a1);
            System.out.println();

            System.out.println("LinkedList");
            TabulatedFunction l1 = (TabulatedFunction) lltf.clone();
            System.out.println(lltf);
            System.out.println(l1);
            System.out.println();

            lltf.addPoint(new FunctionPoint(2.5, 10 ));

            System.out.println(lltf);
            System.out.println(l1);
            System.out.println();

            l1.deletePoint(0);

            System.out.println(lltf);
            System.out.println(l1);
            System.out.println();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (InappropriateFunctionPointException e) {
            e.printStackTrace();
        }
    }

}
