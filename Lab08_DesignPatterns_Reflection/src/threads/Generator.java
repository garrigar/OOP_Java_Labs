package threads;

import functions.basic.Log;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {

    private Task task;
    private Semaphore genSem;
    private Semaphore intSem;

    public Generator(Task task, Semaphore genSem, Semaphore intSem) {
        this.task = task;
        this.genSem = genSem;
        this.intSem = intSem;
    }

    @Override
    public void run() {
        Random rnd = new Random();
        for (int i = 0; i < task.getCountOfTasks(); ++i) {
            try {
                genSem.acquire();

                double base = rnd.nextDouble() * 9 + 1;
                double leftX = rnd.nextDouble() * 100;
                double rightX = rnd.nextDouble() * 100 + 100;
                double step = rnd.nextDouble();

                task.setFunction(new Log(base));
                task.setLeftBorder(leftX);
                task.setRightBorder(rightX);
                task.setSamplingStep(step);

                System.out.printf("%d Source: {base = %.8f, left = %.8f, right = %.8f, step = %.8f}%n",
                        i, base, leftX, rightX, step);

                intSem.release();
            } catch (InterruptedException e) {
                System.err.println("Generator was interrupted");
                break;
            }
        }
    }
}
