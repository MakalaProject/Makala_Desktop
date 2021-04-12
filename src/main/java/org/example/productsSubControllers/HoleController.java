package org.example.productsSubControllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.products.Action;
import org.example.model.products.Hole;
import org.example.model.products.HoleToSend;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HoleController implements Initializable, IControllerCreate<Hole> {
    @FXML public TextField altoField;
    @FXML public TextField anchoField;
    @FXML public FontAwesomeIconView updateButton;
    @FXML public FontAwesomeIconView deleteButton;
    private Hole actualHole = new Hole();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.setOnMouseClicked(mouseEvent -> {
            if(!altoField.getText().isEmpty() || !anchoField.getText().isEmpty() ){
                Node source = (Node)  mouseEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                stage.close();
                stage.setUserData(new HoleToSend(setInfo(actualHole), Action.UPDATE));
            }else{
                showAlertEmptyFields();
            }
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar orificio");
            alert.setContentText("¿Seguro quieres eliminarlo?");
            Optional<ButtonType> result = alert.showAndWait();
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            if (result.get() == ButtonType.OK){
                stage.setUserData(new HoleToSend(actualHole, Action.DELETE));
            }
            stage.close();
        });
    }

    @Override
    public Hole setInfo(Hole hole) {
        hole.getHoleDimensions().setX(new BigDecimal(anchoField.getText()));
        hole.getHoleDimensions().setY(new BigDecimal(altoField.getText()));
        return hole;
    }

    public void setHole(Hole hole){
        this.actualHole = hole;
        anchoField.setText(hole.getHoleDimensions().getX().toString());
        altoField.setText(hole.getHoleDimensions().getX().toString());
    }
}
