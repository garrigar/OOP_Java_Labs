package threads;

import functions.Function;

public class Task {
    private int countOfTasks;

    private Function function;
    private double leftBorder;
    private double rightBorder;
    private double samplingStep;

    public Task(int countOfTasks) {
        this.countOfTasks = countOfTasks;
    }

    public int getCountOfTasks() {
        return countOfTasks;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(double leftBorder) {
        this.leftBorder = leftBorder;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(double rightBorder) {
        this.rightBorder = rightBorder;
    }

    public double getSamplingStep() {
        return samplingStep;
    }

    public void setSamplingStep(double samplingStep) {
        this.samplingStep = samplingStep;
    }
}
