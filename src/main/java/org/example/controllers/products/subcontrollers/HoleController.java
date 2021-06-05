package org.example.controllers.products.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.interfaces.IControllerCreate;
import org.example.model.ChangedVerificationFields;
import org.example.model.FocusVerificationFields;
import org.example.model.products.Action;
import org.example.model.products.Hole;
import org.example.model.products.HoleToSend;
import org.example.model.products.Measure2Dimensions;

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

        altoField.textProperty().addListener(new ChangedVerificationFields(altoField, true, 3,2));
        altoField.focusedProperty().addListener(new FocusVerificationFields(altoField, true, 3,2));
        anchoField.textProperty().addListener(new ChangedVerificationFields(anchoField, true, 3,2));
        anchoField.focusedProperty().addListener(new FocusVerificationFields(anchoField, true, 3,2));

        updateButton.setOnMouseClicked(mouseEvent -> {
            checkFields();
            if (!anchoField.getText().isEmpty() && !altoField.getText().isEmpty()) {
                if (Float.parseFloat(anchoField.getText())>0 && Float.parseFloat(altoField.getText())>0) {
                    Node source = (Node)  mouseEvent.getSource();
                    Stage stage  = (Stage) source.getScene().getWindow();
                    Hole hole = new Hole();
                    setInfo(hole);
                    stage.setUserData(new HoleToSend(hole, Action.UPDATE));
                    stage.close();
                }else {
                    showAlertEmptyFields("Las medidas no pueden ser 0");
                }
            }else {
                showAlertEmptyFields("No puedes dejar los campos de las medidas vacios");
            }
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar división");
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
    public void setInfo(Hole hole) {
        hole.getHoleDimensions().setX(new BigDecimal(anchoField.getText()));
        hole.getHoleDimensions().setY(new BigDecimal(altoField.getText()));
    }

    public void setHole(Hole hole){
        this.actualHole =  new Hole(hole.getHoleNumber(), new Measure2Dimensions(hole.getHoleDimensions().getX(), hole.getHoleDimensions().getY()),false);
        anchoField.setText(hole.getHoleDimensions().getX().toString());
        altoField.setText(hole.getHoleDimensions().getX().toString());
    }
    private void checkFields(){
        if (altoField.getText().isEmpty() || Float.parseFloat(altoField.getText())==0){
            altoField.setStyle("-fx-background-color: #fea08c; -fx-border-color: white white black white; -fx-border-width: 2;");
        }else{
            altoField.setStyle("-fx-background-color: white; -fx-border-color: white white black white; -fx-border-width: 2;");
        }
        if (anchoField.getText().isEmpty() || Float.parseFloat(anchoField.getText())==0){
            anchoField.setStyle("-fx-background-color: #fea08c; -fx-border-color: white white black white; -fx-border-width: 2;");
        }else{
            anchoField.setStyle("-fx-background-color: white; -fx-border-color: white white black white; -fx-border-width: 2;");
        }
    }
}
