package org.example.controllers.gift.subcontrollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.text.StringSubstitutor;
import org.example.model.*;
import org.example.model.products.Action;
import org.example.model.products.Product;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ManualStepController implements Initializable {
    @FXML FontAwesomeIconView updateButton;
    @FXML FontAwesomeIconView deleteButton;
    @FXML TextArea instructionsTextArea;
    @FXML protected ListView<Product> containerListView;
    @FXML protected TextField tiempoField;
    @FXML protected ListView<GiftProductsToSend> internalProductsListView;
    @FXML protected ListView<PaperProductToSend> internalPapersListView;
    @FXML protected ListView<RibbonProductToSend> internalRibbonsListView;
    @FXML protected FontAwesomeIconView imageButton;
    @FXML protected FontAwesomeIconView deletePicture;
    @FXML protected ImageView stepImage;

    protected final ObservableList<PaperProductToSend> papersObservableList = FXCollections.observableArrayList();
    protected final ObservableList<RibbonProductToSend> ribbonsObservableList = FXCollections.observableArrayList();
    protected final ObservableList<GiftProductsToSend> productsObservableList = FXCollections.observableArrayList();
    protected final ObservableList<Product> containerObservableList = FXCollections.observableArrayList();
    Gift actualGift;
    Step actualStep;
    String templatedString = "";
    ArrayList<InstructionElement> instructionElements = new ArrayList<>();
    String imageFile;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tiempoField.focusedProperty().addListener(new FocusVerificationFields(tiempoField, false, 3));
        tiempoField.textProperty().addListener(new ChangedVerificationFields(tiempoField, false, 3));
        deleteButton.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Image image = new Image(getClass().getResource("/Images/delete.png").toString(), 50, 70, false, false);
            ImageView imageView = new ImageView(image);
            alert.setGraphic(imageView);
            alert.setTitle("Eliminar paso");
            alert.setHeaderText("Estas a punto de eliminar un paso");
            alert.setContentText("¿Seguro quieres eliminarlo?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Node source = (Node)  mouseEvent.getSource();
                Stage stage  = (Stage) source.getScene().getWindow();
                actualStep.setAction(Action.DELETE);
                setInfo(actualStep);
                stage.setUserData(actualStep);
                stage.close();
            }
        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            if(actualStep.getAction() == Action.UPDATE){
                setInfo(actualStep);
                stage.setUserData(actualStep);
            }else{
                setInfo(actualStep);
                stage.setUserData(actualStep);
            }
            stage.close();
        });

        containerListView.setOnMouseClicked(mouseEvent -> {
            InstructionElement instructionElement = new InstructionElement("{c}");
            instructionElements.add(instructionElement);
            templatedString += instructionsTextArea.getText() + instructionElement.formatedElement();
            instructionsTextArea.setText(instructionsTextArea.getText() + instructionElement.containerFormat());
            instructionsTextArea.requestFocus();
            instructionsTextArea.positionCaret(instructionsTextArea.getText().length());
        });
        internalProductsListView.setOnMouseClicked(mouseEvent -> {
            GiftProductsToSend product = internalProductsListView.getSelectionModel().getSelectedItem();
            setElementText(product.getProduct().getIdProduct(), product, "{p:");
        });

        internalPapersListView.setOnMouseClicked(mouseEvent -> {
            PaperProductToSend product = internalPapersListView.getSelectionModel().getSelectedItem();
            setElementText(product.getProduct().getIdProduct(), product, "{f:");
        });

        internalRibbonsListView.setOnMouseClicked(mouseEvent -> {
            RibbonProductToSend product = internalRibbonsListView.getSelectionModel().getSelectedItem();
            setElementText(product.getProduct().getIdProduct(), product, "{r:");
        });

        imageButton.setOnMouseClicked(mouseEvent -> {
            imageButton.requestFocus();
            Stage s = (Stage)((Node)(mouseEvent.getSource())).getScene().getWindow();
            uploadImage(s);
        });

        deletePicture.setOnMouseClicked(mouseEvent -> {
            deletePicture.requestFocus();
            removePicutre();
        });


    }

    protected void removePicutre() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar imagen");
        alert.setContentText("¿Seguro quieres eliminarla?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            stepImage.setImage(new Image(getClass().getResource("/images/instruction.png").toString()));
            imageFile = null;
        }
    }



    protected void uploadImage(Stage s){
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(s);
        if (file != null){
            Image img = new Image(file.toURI().toString());
            imageFile = file.getPath();
            stepImage.setImage(img);
        }
    }

    private <D extends GiftProductsParent> void setElementText(int idProduct,D product, String format){
        InstructionElement instructionElement = new InstructionElement(idProduct, product.getId(), format);
        instructionElements.add(instructionElement);
        instructionsTextArea.setText(instructionsTextArea.getText() + instructionElement.formatedElement());
        instructionsTextArea.requestFocus();
        instructionsTextArea.positionCaret(instructionsTextArea.getText().length());
    }

    public void setObject(Gift gift){
        actualGift = gift;
        papersObservableList.setAll(actualGift.getPapers());
        ribbonsObservableList.setAll(actualGift.getRibbons());
        productsObservableList.setAll(actualGift.getStaticProducts());
        containerObservableList.setAll(actualGift.getContainer());
        showList();
    }

    private void showList() {
        internalPapersListView.getItems().setAll(papersObservableList);
        internalPapersListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(papersObservableList) ).multiply(23.7));
        internalRibbonsListView.getItems().setAll(ribbonsObservableList);
        internalRibbonsListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(ribbonsObservableList) ).multiply(23.7));
        internalProductsListView.getItems().setAll(productsObservableList);
        internalProductsListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(productsObservableList)).multiply(23.7));
        containerListView.getItems().setAll(containerObservableList);
    }

    public void setStep(Step step) {
        actualStep = step;
        if(step.getAction() == Action.UPDATE){
            putStepFields();
            deleteButton.setVisible(true);
        }else{
            deleteButton.setVisible(false);
        }
    }

    private void putStepFields() {
        instructionsTextArea.setText(actualStep.getInstruction());
        tiempoField.setText(actualStep.getTime().toString());
    }

    private Step setInfo(Step step){
        step.setInstruction(instructionsTextArea.getText());
        step.setTime(15);
        step.setPath(imageFile);
        step.setTime(Integer.parseInt(tiempoField.getText()));
        return step;
    }





}
@Data
@AllArgsConstructor
class InstructionElement{
    private int productId;
    private int listId;
    private String format;

    public String formatedElement(){
        return format + productId + ":" + listId+"}";
    }
    public InstructionElement(String format){
        this.format = format;
    }

    public String containerFormat(){
        return format;
    }
}
