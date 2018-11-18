package threads;

import functions.basic.Log;

import java.util.Random;

public class SimpleGenerator implements Runnable{

    private final Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        Random rnd = new Random();
        for (int i = 0; i < task.getCountOfTasks(); ++i) {
            double base = rnd.nextDouble() * 9 + 1;
            double leftX = rnd.nextDouble() * 100;
            double rightX = rnd.nextDouble() * 100 + 100;
            double step = rnd.nextDouble();
            synchronized (task) {
                task.setFunction(new Log(base));
                task.setLeftBorder(leftX);
                task.setRightBorder(rightX);
                task.setSamplingStep(step);
            }
            System.out.printf("%d Source: {base = %.8f, left = %.8f, right = %.8f, step = %.8f}%n",
                    i, base, leftX, rightX, step);
        }
    }
}
