package org.example.customDialogs;

import javafx.fxml.Initializable;
import org.example.interfaces.IControllerCreate;
import org.example.model.Catalog;

import java.net.URL;
import java.util.ResourceBundle;

public class CatalogCreateController implements Initializable, IControllerCreate<Catalog> {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public Catalog setInfo(Catalog object) {
        return null;
    }
}
