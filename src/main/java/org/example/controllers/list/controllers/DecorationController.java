package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.parent.controllers.DecorationsParentController;
import org.example.customCells.CatalogListViewCell;
import org.example.customCells.DecorationListViewCell;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.Decoration;
import org.example.model.Gift;
import org.example.services.ImageService;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DecorationController extends DecorationsParentController implements Initializable, IListController<Decoration>, IControllerCreate<Decoration> {
    @FXML FontAwesomeIconView deleteButton;
    @FXML FontAwesomeIconView addButton;
    @FXML ListView<Decoration> listView;
    @FXML TextField searchField;
    @FXML ToggleSwitch editSwitch;
    @FXML protected AnchorPane fieldsAnchorPane;
    @FXML AnchorPane disableImageAnchorPane;
    @FXML Label requireLabel;
    Decoration actualDecoration = new Decoration();
    private ObservableList<Decoration> decorationObservableList = FXCollections.observableArrayList();
    private final ObservableList<Decoration> actualList = FXCollections.observableArrayList();
    FilteredList<Decoration> filteredDecorations;
    private int index;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        decorationObservableList.setAll(Request.getJ("decorations/basics", Decoration[].class, false));
        actualList.setAll(decorationObservableList);
        showList(decorationObservableList, listView, DecorationListViewCell.class);
        filteredDecorations = new FilteredList<>(decorationObservableList, p ->true);
        //Check if the list is empty to update the view and show its values at the beggining
        if(!decorationObservableList.isEmpty()){
            listView.getSelectionModel().select(0);
            updateView();
        }
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredDecorations.setPredicate(decoration -> {
                if (newValue.isEmpty()){
                    return true;
                }
                String lowerCaseText = newValue.toLowerCase();
                if (decoration.getName().toLowerCase().contains(lowerCaseText)){
                    return true;
                }else{
                    return false;
                }
            });
            showList(FXCollections.observableArrayList(filteredDecorations), listView, CatalogListViewCell.class);
        } );

        listView.setOnMouseClicked(mouseEvent -> {
            if (listView.getSelectionModel().getSelectedItem() != null && !existChanges()) {
                updateView();
            }
        });

        editSwitch.setOnMouseClicked(mouseEvent -> {
            editSwitch.requestFocus();
            editView(fieldsAnchorPane, disableImageAnchorPane,requireLabel, editSwitch, updateButton);
        });


        //------------------------------------------------CRUD BUTTONS--------------------------------------------------------------------

        updateButton.setOnMouseClicked(mouseEvent -> {
            updateButton.requestFocus();
            if (actualDecoration != null)
                update();
        });


        addButton.setOnMouseClicked(mouseEvent -> {
            addButton.requestFocus();
            if (!existChanges()){
                add();
            }
        });

        deleteButton.setOnMouseClicked(mouseEvent -> {
            deleteButton.requestFocus();
            deleteAlert("Decoración");
        });

    }

    public void add() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/decoration_create.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Decoration object = (Decoration) stage.getUserData();
            if(object != null){
                actualDecoration = object;
                decorationObservableList.add(object);
                actualList.add(object);
                showList(decorationObservableList, listView, DecorationListViewCell.class);
                listView.getSelectionModel().select(object);
                listView.scrollTo(object);
                updateView();
            }
            if(actualDecoration != null) {
                updateView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            Request.deleteJ( "decorations", actualDecoration.getId());

        } catch (Exception e) {
            errorAlert(e.getMessage());
            return;
        }

        if (listView.getItems().size() > 1) {
            decorationObservableList.remove(index);
            listView.getSelectionModel().select(0);
            updateView();
            showList(FXCollections.observableList(decorationObservableList), listView, DecorationListViewCell.class);
        }else {
            actualDecoration = null;
            decorationObservableList.remove(index);
            showList(FXCollections.observableList(decorationObservableList), listView, DecorationListViewCell.class);
            cleanForm();
        }
    }


    @Override
    public void update() {
        if(!priceField.getText().isEmpty() && !descriptionTextArea.getText().isEmpty() && !priceField.getText().isEmpty()){
            if(new BigDecimal(priceField.getText()).compareTo(new BigDecimal(0)) > 0) {
                int listIndex = listView.getSelectionModel().getSelectedIndex();
                Decoration decoration = new Decoration();
                setInfo(decoration);
                Decoration returnedDecoration = null;
                try {
                    returnedDecoration = (Decoration) Request.putJ(decoration.getRoute(), decoration);
                    if (imageFile != null && !imageFile.equals("") && (!imageFile.contains("http://res.cloudinary.com") && !imageFile.contains("/images/bow.png"))) {
                        ArrayList<String> pictures = new ArrayList<>();
                        pictures.add(decoration.getPath());
                        List<String> images = ImageService.uploadImages(pictures);
                        decoration.setPath(images.get(0));
                        imageFile = decoration.getPath();
                        returnedDecoration = (Decoration) Request.putJ(decoration.getRoute(), decoration);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                actualDecoration = returnedDecoration;
                Decoration g = listView.getItems().get(listIndex);
                actualList.set(actualList.indexOf(g), actualDecoration);
                if(listView.getItems().get(listIndex) != actualDecoration) {
                    listView.getItems().set(listIndex, actualDecoration);
                }
                decorationObservableList.set(index, actualDecoration);
                listView.getSelectionModel().select(actualDecoration);
                listView.scrollTo(decoration);
                editSwitch.setSelected(false);
                editView(fieldsAnchorPane, disableImageAnchorPane, requireLabel, editSwitch, updateButton);
            }else{
                checkFields();
                showAlertEmptyFields("El valor del precio no puede ser 0");
            }
        }else {
            checkFields();
            showAlertEmptyFields("Tienes un campo indispensable vacio");
        }
        checkFields();
    }


    @Override
    public boolean existChanges() {
        if(actualDecoration == null){
            return false;
        }
        Decoration decoration = new Decoration();
        setInfo(decoration);
        if (!actualDecoration.equals(decoration)){
            if(!showAlertUnsavedElement(decoration.getName(), decoration.getIdentifier())) {
                listView.getSelectionModel().select(decoration);
            }else{
                updateView();
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void putFields() {
        nameField.setText(actualDecoration.getName());
        imageFile = actualDecoration.getPath();
        if(actualDecoration.getPath() != null && !actualDecoration.getPath().equals("")){
            decorationImage.setImage(new Image(actualDecoration.getPath()));
        }else{
            decorationImage.setImage(new Image(getClass().getResource("/images/catalog.png").toString()));
        }
        descriptionTextArea.setText(actualDecoration.getDescription());
        priceField.setText(actualDecoration.getPrice().toString());
    }

    @Override
    public void updateView(){
        actualDecoration = listView.getSelectionModel().getSelectedItem();
        index = decorationObservableList.indexOf(actualDecoration);
        if(actualDecoration.getPrice() == null) {
            actualDecoration = (Decoration) Request.find(actualDecoration.getRoute(), actualDecoration.getIdDeco(), Decoration.class);
            int listIndex = listView.getSelectionModel().getSelectedIndex();
            actualList.set(actualList.indexOf(listView.getSelectionModel().getSelectedItem()), actualDecoration);
            if(listView.getItems().get(listIndex) != actualDecoration) {
                listView.getItems().set(listIndex, actualDecoration);
            }
            decorationObservableList.set(index, actualDecoration);
        }
        editSwitch.setSelected(false);
        editView(fieldsAnchorPane, disableImageAnchorPane,requireLabel, editSwitch, updateButton);
        putFields();
    }

    @Override
    public void cleanForm() {
        nameField.setText("");
        priceField.setText("");
        descriptionTextArea.setText("");
    }

    @Override
    public void setInfo(Decoration decoration) {
        decoration.setName(nameField.getText());
        decoration.setPath(imageFile);
        decoration.setId(actualDecoration.getId());
        decoration.setDescription(descriptionTextArea.getText());
        decoration.setPrice(new BigDecimal(priceField.getText()));

    }
}
