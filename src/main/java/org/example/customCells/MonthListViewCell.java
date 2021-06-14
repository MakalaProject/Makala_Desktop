package org.example.customCells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.example.interfaces.IConstructor;
import org.example.model.Accounting;
import org.example.model.Formatter;

import java.io.IOException;

public class MonthListViewCell extends ListCell<Accounting> implements IConstructor<MonthListViewCell> {
    @FXML
    Label monthName;

    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Accounting month, boolean empty) {
        super.updateItem(month, empty);

        if(empty || month == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/month_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            monthName.setText(Formatter.FormatMonth(month.getDate()));
            setText(null);
            setGraphic(anchorPane);

        }

    }

    @Override
    public MonthListViewCell getControllerCell() {
        return new MonthListViewCell();
    }

}
