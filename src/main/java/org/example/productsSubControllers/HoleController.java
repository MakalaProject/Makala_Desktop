package org.example.productsSubControllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.products.Hole;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class HoleController implements Initializable, IControllerCreate<Hole> {
    @FXML public TextField altoField;
    @FXML public TextField anchoField;
    @FXML public FontAwesomeIconView updateButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.setOnMouseClicked(mouseEvent -> {
            if(!altoField.getText().isEmpty() || !anchoField.getText().isEmpty() ){
                Hole hole = setInfo(new Hole());
                Node source = (Node)  mouseEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                stage.close();
                stage.setUserData(hole);
            }else{
                showAlertEmptyFields();
            }
        });
    }

    @Override
    public Hole setInfo(Hole hole) {
        hole.getHoleDimensions().setX(new BigDecimal(anchoField.getText()));
        hole.getHoleDimensions().setY(new BigDecimal(altoField.getText()));
        return hole;
    }
}
