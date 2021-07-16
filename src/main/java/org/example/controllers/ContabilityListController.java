package org.example.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.customCells.MonthListViewCell;
import org.example.model.Accounting;
import org.example.model.Formatter;

import java.net.URL;
import java.util.ResourceBundle;

public class ContabilityListController implements Initializable {
    @FXML
    ListView<Accounting> monthsListView;
    Accounting actualAccounting;
    @FXML
    TextField vendidoField;
    @FXML
    Label titleLabel;
    @FXML TextField gastosField;
    @FXML TextField gananciaField;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthsListView.setOnMouseClicked(mouseEvent -> {
            actualAccounting = monthsListView.getSelectionModel().getSelectedItem();
            titleLabel.setText("Detalles de: " + Formatter.FormatMonth(actualAccounting.getMonth()));
            vendidoField.setText(actualAccounting.getSold().toString());
            gananciaField.setText(actualAccounting.getBought().toString());
            gastosField.setText(actualAccounting.getBalance().toString());
        });
    }

    public void setData(ObservableList<Accounting> accountingObservableList){
        monthsListView.setItems(accountingObservableList);
        monthsListView.setCellFactory(cellList -> new MonthListViewCell());
        monthsListView.prefHeightProperty().bind(Bindings.size(accountingObservableList).multiply(35));
    }
}
