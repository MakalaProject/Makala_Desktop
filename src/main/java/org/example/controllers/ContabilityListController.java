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

import java.net.URL;
import java.util.ResourceBundle;

public class ContabilityListController implements Initializable {
    @FXML
    ListView<Accounting> contabilityListView;
    Accounting actualAccounting;
    @FXML
    TextField vendidoField;
    @FXML
    Label titleLabel;
    @FXML TextField gastosField;
    @FXML TextField gananciaField;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contabilityListView.setOnMouseClicked(mouseEvent -> {
            actualAccounting = contabilityListView.getSelectionModel().getSelectedItem();
            titleLabel.setText("Detalles de: " + actualAccounting.getMonth().toString());
            vendidoField.setText(actualAccounting.getSold().toString());
            gastosField.setText(actualAccounting.getBought().toString());
            gananciaField.setText(actualAccounting.getBalance().toString());
        });
    }

    public void setData(ObservableList<Accounting> accountingObservableList){

        contabilityListView.setItems(accountingObservableList);
        contabilityListView.setCellFactory(cellList -> new MonthListViewCell());
        contabilityListView.prefHeightProperty().bind(Bindings.size(accountingObservableList).multiply(35));
    }
}
