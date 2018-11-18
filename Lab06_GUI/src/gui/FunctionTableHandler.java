package gui;

import functions.FunctionPoint;
import functions.TabulatedFunction;
import functions.exceptions.InappropriateFunctionPointException;
import gui.utils.ErrorAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class FunctionTableHandler {

    protected static class FunctionPointItem {

        private String x, y;

        private FunctionPointItem(FunctionPoint fp){
            this.x = String.valueOf(fp.getX());
            this.y = String.valueOf(fp.getY());
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public void setX(double x) {
            this.x = String.valueOf(x);
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public void setY(double y) {
            this.y = String.valueOf(y);
        }

    }

    private TabulatedFunction function;

    private TableView<FunctionPointItem> table;

    private ObservableList<FunctionPointItem> pointItems;

    @SuppressWarnings("unchecked")
    public FunctionTableHandler(TabulatedFunction function, TableView table) {
        this.function = function;
        this.table = (TableView<FunctionPointItem>) table;

        TableColumn<FunctionPointItem, String> colX = (TableColumn<FunctionPointItem, String>) this.table.getColumns().get(0);
        TableColumn<FunctionPointItem, String> colY = (TableColumn<FunctionPointItem, String>) this.table.getColumns().get(1);

        colX.setCellValueFactory(new PropertyValueFactory<>("x"));
        colY.setCellValueFactory(new PropertyValueFactory<>("y"));
        colX.setCellFactory(TextFieldTableCell.forTableColumn());
        colY.setCellFactory(TextFieldTableCell.forTableColumn());

        this.pointItems = FXCollections.observableArrayList();
        this.table.setItems(this.pointItems); // will be updated automatically

        colX.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            int index = event.getTablePosition().getRow();
            try {
                double x = Double.parseDouble(newValue);
                this.function.setPointX(index, x);
                this.pointItems.get(index).setX(newValue);
            }
            catch (NumberFormatException e){
                new ErrorAlert("Number format exception",
                        e.getMessage(),
                        "Input only double values").showAndWait();
                this.table.refresh();
            } catch (InappropriateFunctionPointException e) {
                new ErrorAlert("Inappropriate function point exception",
                        "Incorrect point X",
                        e.getMessage()).showAndWait();
                this.table.refresh();
            }
        });

        colY.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            int index = event.getTablePosition().getRow();
            try {
                double y = Double.parseDouble(newValue);
                this.function.setPointY(index, y);
                this.pointItems.get(index).setY(newValue);
            }
            catch (NumberFormatException e){
                new ErrorAlert("Number format exception",
                        e.getMessage(),
                        "Input only double values").showAndWait();
                this.table.refresh();
            }
        });

        //this.reloadPointItems();
    }

    public void reloadPointItems(){
        this.pointItems.clear();
        for (int i = 0; i < this.function.getPointsCount(); ++i){
            this.pointItems.add(new FunctionPointItem(this.function.getPoint(i)));
        }
    }

    public void addItem(String newX, String newY) throws NumberFormatException, InappropriateFunctionPointException {
        double x = Double.parseDouble(newX);
        double y = Double.parseDouble(newY);
        this.function.addPoint(new FunctionPoint(x, y));
        this.reloadPointItems();
    }

    public void deleteSelectedItem() throws IllegalStateException {
        int index = this.table.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            this.table.getSelectionModel().clearSelection();
            this.function.deletePoint(index);
            this.pointItems.remove(index);
        }
    }

    // ------------------------------------------USELESS METHODS--------------------------------------------------------

    @Deprecated
    public int getRowCount() {
        return this.function.getPointsCount();
    }

    @Deprecated
    public int getColumnCount() {
        return 2;
    }

    @Deprecated
    public String getColumnName(int column) {
        try {
            switch (column){
                case 0: return "X";
                case 1: return "Y";
                default: throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            new ErrorAlert("Index out of bounds exception", "Index out of bounds", "").showAndWait();
            return null;
        }
    }

    @Deprecated
    public Class<?> getColumnClass(int columnIndex) {
        return Double.class;
    }

    @Deprecated
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Deprecated
    public Object getValueAt(int row, int column) {
        try {
            if (row < 0 || row >= this.getRowCount()) throw new IndexOutOfBoundsException();
            switch (column){
                case 0: return this.function.getPointX(row);
                case 1: return this.function.getPointY(row);
                default: throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            new ErrorAlert("Index out of bounds exception", "Index out of bounds", "").showAndWait();
            return null;
        }
    }

    @Deprecated
    public void setValueAt(Object newValue, int row, int column) {
        try {
            if (!(newValue instanceof Double)) throw new IllegalArgumentException("New value isn't a double");
            if (row < 0 || row >= this.getRowCount()) throw new IndexOutOfBoundsException();
            if (column == 0) {
                double x = (Double) newValue;
                this.function.setPointX(row, x);
                this.pointItems.get(row).setX(x);
                this.table.refresh();
                return;
            } else if (column == 1){
                double y = (Double) newValue;
                this.function.setPointY(row, y);
                this.pointItems.get(row).setY(y);
                this.table.refresh();
                return;
            }
            throw new IndexOutOfBoundsException();
        } catch (IllegalArgumentException e) {
            new ErrorAlert("Illegal argument exception", "Not a double", e.getMessage()).showAndWait();
        } catch (IndexOutOfBoundsException e) {
            new ErrorAlert("Index out of bounds exception", "Index out of bounds", "").showAndWait();
        } catch (InappropriateFunctionPointException e) {
            new ErrorAlert("Inappropriate function point exception",
                    "Incorrect point X",
                    e.getMessage()).showAndWait();
        }
    }

}
