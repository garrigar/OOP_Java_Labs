package threads;

import functions.Function;
import functions.Functions;
import functions.basic.Log;

import java.util.concurrent.Semaphore;

public class Integrator extends Thread {

    private Task task;
    private Semaphore genSem;
    private Semaphore intSem;

    public Integrator(Task task, Semaphore genSem, Semaphore intSem) {
        this.task = task;
        this.genSem = genSem;
        this.intSem = intSem;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getCountOfTasks(); ++i) {
            try {
                intSem.acquire();

                Function f = task.getFunction();
                double leftX = task.getLeftBorder();
                double rightX = task.getRightBorder();
                double step = task.getSamplingStep();

                double base = ((Log) f).getBase();

                double result = Functions.integrate(f, leftX, rightX, step);
                System.out.printf("%d Result: {base = %.8f, left = %.8f, right = %.8f, step = %.8f, result = %.8f}%n",
                        i, base, leftX, rightX, step, result);

                genSem.release();
            } catch (InterruptedException e) {
                System.err.println("Integrator was interrupted");
                break;
            }
        }
    }
}
