import functions.Functions;
import functions.basic.Exp;
import functions.basic.Log;
import threads.*;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {

        //System.out.println("∫[0, 1] e^x dx = " + Functions.integrate(new Exp(), 0, 1, .00001));
        // dx = .001 for 7th digit
        // dx = .0001 for 9th digit
        // dx = .00001 for 11th digit
        //System.out.println("e - 1 = " + (Math.E - 1));

        //nonThread();
        //simpleThreads();
        complicatedThreads();
    }

    private static void nonThread() {
        Task task = new Task(100);
        Random rnd = new Random();
        for (int i = 0; i < task.getCountOfTasks(); ++i) {
            double base = rnd.nextDouble() * 9 + 1;
            task.setFunction(new Log(base));
            double leftX = rnd.nextDouble() * 100;
            task.setLeftBorder(leftX);
            double rightX = rnd.nextDouble() * 100 + 100;
            task.setRightBorder(rightX);
            double step = rnd.nextDouble();
            task.setSamplingStep(step);
            System.out.printf("%d Source: {base = %.8f, left = %.8f, right = %.8f, step = %.8f}%n",
                    i, base, leftX, rightX, step);
            double result = Functions.integrate(task.getFunction(), task.getLeftBorder(),
                    task.getRightBorder(), task.getSamplingStep());
            System.out.printf("%d Result: {base = %.8f, left = %.8f, right = %.8f, step = %.8f, result = %.8f}%n",
                    i, base, leftX, rightX, step, result);

        }
    }

    private static void simpleThreads() {
        Task task = new Task(100);
        Thread generator = new Thread(new SimpleGenerator(task));
        Thread integrator = new Thread(new SimpleIntegrator(task));
        //generator.setPriority(1);
        //integrator.setPriority(10);
        generator.start();
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        integrator.start();
    }

    private static void complicatedThreads(){
        Task task = new Task(100);
        Semaphore genSem = new Semaphore(1);
        Semaphore intSem = new Semaphore(0);

        Generator generator = new Generator(task, genSem, intSem);
        Integrator integrator = new Integrator(task, genSem, intSem);

        //generator.setPriority(1);
        //integrator.setPriority(10);
        generator.start();
        integrator.start();

        try {
            Thread.sleep(50);
            System.err.println("Interrupt signal");
            generator.interrupt();
            integrator.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
