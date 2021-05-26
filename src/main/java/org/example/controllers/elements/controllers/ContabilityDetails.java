package org.example.controllers.elements.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.example.model.Accounting;

import java.lang.module.FindException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContabilityDetails implements Initializable {
    Accounting actualAccounting;
    @FXML TextField vendidoField;
    @FXML TextField gastosField;
    @FXML TextField gananciaField;
    @FXML TextField fechaField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setObject(Accounting selectedItem) {
        this.actualAccounting = selectedItem;
        putFields();
    }

    private void putFields() {
        vendidoField.setText(actualAccounting.getSold().toString());
        gastosField.setText(actualAccounting.getBought().toString());
        gananciaField.setText(actualAccounting.getBalance().toString());
        fechaField.setText(actualAccounting.getMonth().toString());
    }
}
