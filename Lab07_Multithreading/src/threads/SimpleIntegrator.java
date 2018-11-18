package threads;

import functions.Function;
import functions.Functions;
import functions.basic.Log;

public class SimpleIntegrator implements Runnable {

    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getCountOfTasks(); ++i) {
            Function f;
            double leftX;
            double rightX;
            double step;
            synchronized (task) {
                f = task.getFunction();
                leftX = task.getLeftBorder();
                rightX = task.getRightBorder();
                step = task.getSamplingStep();
            }
            double base = ((Log) f).getBase();

            double result = Functions.integrate(f, leftX, rightX, step);
            System.out.printf("%d Result: {base = %.8f, left = %.8f, right = %.8f, step = %.8f, result = %.8f}%n",
                    i, base, leftX, rightX, step, result);
        }
    }
}
