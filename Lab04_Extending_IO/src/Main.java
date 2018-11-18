import functions.*;
import functions.basic.*;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        testTrig();
        testExpLog();
        testSerial(false); // Serializable
        testSerial(true); // Externalizable
    }

    private static void testTrig() {
        // -------------------------------------------------------------------------------
        Sin sin = new Sin();
        TabulatedFunction tab_sin = TabulatedFunctions.tabulate(sin, 0, Math.PI * 2, 10);

        System.out.println("sin(x), x in [0; 2pi], step = 0.1");
        System.out.println("x\t\tsin(x)\t\ttab_sin(x)(with 10 points)");

        for (double x = 0; x <= Math.PI * 2; x += 0.1) {
            System.out.println(String.format("%.1f", x) + "\t\t" + sin.getFunctionValue(x) + "\t\t" + tab_sin.getFunctionValue(x));
        }
        System.out.println();
        // --------------------------------------------------------------------------------
        Cos cos = new Cos();
        TabulatedFunction tab_cos = TabulatedFunctions.tabulate(cos, 0, Math.PI * 2, 10);

        System.out.println("cos(x), x in [0; 2pi], step = 0.1");
        System.out.println("x\t\tcos(x)\t\ttab_cos(x)(with 10 points)");

        for (double x = 0; x <= Math.PI * 2; x += 0.1) {
            System.out.println(String.format("%.1f", x) + "\t\t" + cos.getFunctionValue(x) + "\t\t" + tab_cos.getFunctionValue(x));
        }
        System.out.println();
        // --------------------------------------------------------------------------------
        Function pythagoreanTrigId = Functions.sum(Functions.power(tab_sin, 2), Functions.power(tab_cos, 2));

        System.out.println("sin^2(x)+cos^2(x), x in [0; 2pi], step = 0.1");
        System.out.println("x\t\tsin^2(x)+cos^2(x)");

        for (double x = 0; x <= Math.PI * 2; x += 0.1) {
            System.out.println(String.format("%.1f", x) + "\t\t" + pythagoreanTrigId.getFunctionValue(x));
        }
        System.out.println();
        // ----------------------------------------------------------------------------------
    }

    private static void testExpLog() {
        try {
            TabulatedFunction tab_exp = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);
            BufferedWriter bw = new BufferedWriter(new FileWriter("textfile.txt"));
            TabulatedFunctions.writeTabulatedFunction(tab_exp, bw);
            bw.close();

            BufferedReader br = new BufferedReader(new FileReader("textfile.txt"));
            TabulatedFunction tab_exp1 = TabulatedFunctions.readTabulatedFunction(br);
            br.close();

            System.out.println("tab_exp(x), x in [0; 10], step = 1 (tabulated with 11 points)");
            System.out.println("x\t\ttab_exp(x)\t\ttab_exp1(x)");

            for (int x = 0; x <= 10; ++x) {
                System.out.println(x + "\t\t" + tab_exp.getFunctionValue(x) + "\t\t" + tab_exp1.getFunctionValue(x));
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TabulatedFunction tab_ln = TabulatedFunctions.tabulate(new Log(Math.E), 0, 10, 11);
            FileOutputStream out =  new FileOutputStream("textfile.bin");
            TabulatedFunctions.outputTabulatedFunction(tab_ln, out);
            out.close();

            FileInputStream in =  new FileInputStream("textfile.bin");
            TabulatedFunction tab_ln1 = TabulatedFunctions.inputTabulatedFunction(in);
            in.close();

            System.out.println("tab_ln(x), x in [0; 10], step = 1 (tabulated with 11 points)");
            System.out.println("x\t\ttab_ln(x)\t\ttab_ln1(x)");

            for (int x = 0; x <= 10; ++x) {
                System.out.println(x + "\t\t" + tab_ln.getFunctionValue(x) + "\t\t" + tab_ln1.getFunctionValue(x));
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void testSerial(boolean externalizable){
        try {
            TabulatedFunction tab_log_exp = ( externalizable ?
                    new LinkedListTabulatedFunction(0, 10, new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) :
                    TabulatedFunctions.tabulate(Functions.composition(new Log(Math.E), new Exp()), 0, 10, 11)
            );
            String filename = ( externalizable ? "serial_ext.txt" : "serial.txt" );

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(tab_log_exp);
            out.close();

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            TabulatedFunction tab_log_exp1 = (TabulatedFunction) in.readObject();
            in.close();

            System.out.println("tab_ln_exp(x), x in [0; 10], step = 1 (tabulated with 11 points)");
            System.out.println("x\t\ttab_ln_exp(x)\t\ttab_ln_exp1(x)");

            for (int x = 0; x <= 10; ++x) {
                System.out.println(x + "\t\t" + tab_log_exp.getFunctionValue(x) + "\t\t" + tab_log_exp1.getFunctionValue(x));
            }
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Some error occurred!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Wrong object type");
        }

    }

}
