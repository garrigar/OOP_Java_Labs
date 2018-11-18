package gui;

import functions.*;
import functions.exceptions.InappropriateFunctionPointException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class FunctionDocument implements TabulatedFunction {

    private TabulatedFunction function;
    private String filename;
    private boolean modified = false;
    private boolean filenameAssigned = false;

    public void newFunction(double leftX, double rightX, int pointsCount) {
        this.function = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        this.modified = true;
    }

    public void saveFunction() throws IllegalStateException, IOException {
        if (this.modified) {
            if (!this.filenameAssigned)
                throw new IllegalStateException("Filename is not assigned yet");
            TabulatedFunctions.writeTabulatedFunction(this.function, new FileWriter(this.filename));
            this.modified = false;
        }
    }

    public void saveFunctionAs(String fileName) throws IllegalArgumentException, IOException {
        if (fileName == null || fileName.isEmpty())
            throw new IllegalArgumentException("Invalid filename");
        this.filename = fileName;
        this.filenameAssigned = true;
        TabulatedFunctions.writeTabulatedFunction(this.function, new FileWriter(this.filename));
        this.modified = false;
    }

    public void loadFunction(String fileName) throws IllegalArgumentException, IOException {
        if (fileName == null || fileName.isEmpty())
            throw new IllegalArgumentException("Invalid filename");
        this.filename = fileName;
        this.filenameAssigned = true;
        this.function = TabulatedFunctions.readTabulatedFunction(new FileReader(this.filename));
        this.modified = false;
    }

    public void tabulateFunction(Function function, double leftX, double rightX, int pointsCount) {
        this.function = TabulatedFunctions.tabulate(function, leftX, rightX, pointsCount);
        this.modified = true;
    }

    public boolean isModified() {
        return this.modified;
    }

    public boolean isFilenameAssigned() {
        return this.filenameAssigned;
    }

    @Override
    public int getPointsCount() {
        return this.function.getPointsCount();
    }

    @Override
    public FunctionPoint getPoint(int index) {
        return this.function.getPoint(index);
    }

    @Override
    public void setPoint(int index, FunctionPoint p) throws InappropriateFunctionPointException {
        this.function.setPoint(index, p);
        this.modified = true;
    }

    @Override
    public double getPointX(int index) {
        return this.function.getPointX(index);
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        this.function.setPointX(index, x);
        this.modified = true;
    }

    @Override
    public double getPointY(int index) {
        return this.function.getPointY(index);
    }

    @Override
    public void setPointY(int index, double y) {
        this.function.setPointY(index, y);
        this.modified = true;
    }

    @Override
    public void deletePoint(int index) {
        this.function.deletePoint(index);
        this.modified = true;
    }

    @Override
    public void addPoint(FunctionPoint p) throws InappropriateFunctionPointException {
        this.function.addPoint(p);
        this.modified = true;
    }

    @Override
    public double getLeftDomainBorder() {
        return this.function.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return this.function.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return this.function.getFunctionValue(x);
    }

    @Override
    public String toString() {
        return this.function.toString();
    }

    @Override
    public int hashCode() {
        return this.function.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionDocument)) return false;
        FunctionDocument temp = (FunctionDocument) obj;

        if (!this.filenameAssigned){
            if (temp.filenameAssigned) return false;

            return this.function.equals(temp.function);
        }
        return (temp.filenameAssigned && this.filename.equals(temp.filename) && this.function.equals(temp.function));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionDocument result = (FunctionDocument) super.clone();
        result.function = (TabulatedFunction) this.function.clone();
        result.filename = new String(this.filename);
        return result;
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return this.function.iterator();
    }
}
