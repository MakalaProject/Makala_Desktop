package org.example.controllers.list.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.example.controllers.gift.subcontrollers.ManualStepController;
import org.example.customCells.ManualListViewCell;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.interfaces.ListToChangeTools;
import org.example.model.*;
import org.example.model.products.Action;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ManualController implements Initializable, IListController<ArrayList<Step>>, IControllerCreate<ArrayList<Step>> {
    @FXML FontAwesomeIconView updateButton;
    @FXML FontAwesomeIconView stepButton;
    @FXML Label giftName;
    @FXML ListView<Step> stepListView;
    Gift actualGift;
    int index = 0;

    protected final ObservableList<Step> stepObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stepListView.setOnMouseClicked(mouseEvent -> {
            loadDialog(Action.UPDATE);
        });

        stepButton.setOnMouseClicked(mouseEvent -> {
            loadDialog(Action.SAVE);
        });

        updateButton.setOnMouseClicked(mouseEvent -> {
            Node source = (Node)  mouseEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            ArrayList<Step> steps = new ArrayList<>(stepObservableList);
            new ListToChangeTools<Step,Integer>().setToDeleteItems(actualGift.getSteps(), steps);
            int idStep = 1;
            for (Step s : steps){
                s.setStepNumber(idStep);
                idStep++;
            }
            stage.setUserData(steps);
            stage.close();
        });
    }

    private void loadDialog(Action action){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/manual_step.fxml"));
        try {
            ManualStepController dialogController = new ManualStepController();
            fxmlLoader.setController(dialogController);
            Parent parent = fxmlLoader.load();
            dialogController.setObject(actualGift);
            if(action == Action.UPDATE) {
                Step step = stepListView.getSelectionModel().getSelectedItem();
                step.setPublic(true);
                dialogController.setStep(stepListView.getSelectionModel().getSelectedItem());
                index = stepObservableList.indexOf(stepListView.getSelectionModel().getSelectedItem());
            }
            else{
                Step step = new Step();
                step.setPublic(false);
                step.setAction(action);
                dialogController.setStep(step);
            }
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            Step object = (Step) stage.getUserData();
            if(object != null){
                if(object.getAction() == Action.UPDATE){
                    stepObservableList.set(index, object);
                }
                else if(object.getAction() == Action.DELETE){
                    stepObservableList.remove(index);
                    if(index>=1)
                        index--;
                    else{
                        if(actualGift.getPrivacy().equals("Publico") && stepObservableList.size()<2){
                           showAlertEmptyFields("No puedes eliminar todos los pasos de regalo ya publicado");
                           return;
                        }
                        index = 0;
                    }
                    sortNewIds();
                }
                else {

                    object.setStepNumber(stepObservableList.size() + 1);
                    object.setAction(Action.UPDATE);
                    stepObservableList.add(object);
                }
                showList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sortNewIds(){
        int newIndex = 1;
        for(Step step: stepObservableList){
            step.setStepNumber(newIndex);
            newIndex++;
        }
    }

    @Override
    public void setInfo(ArrayList<Step> object) {

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean existChanges() {
        return false;
    }

    @Override
    public void putFields() {
        giftName.setText(actualGift.getName());
        showList();
    }
    private void showList(){
        stepListView.getItems().setAll(FXCollections.observableList(stepObservableList.stream().filter(l -> !l.isToDelete()).collect(Collectors.toList())));
        stepListView.prefHeightProperty().bind(Bindings.size(FXCollections.observableList(stepListView.getItems())).multiply(23.7));
    }

    @Override
    public void updateView() {

    }

    @Override
    public void cleanForm() {

    }

    public void setObject(Gift gift, ArrayList<Step> steps) {
        stepObservableList.setAll(steps);
        stepObservableList.sort(Comparator.comparing(Step::getStepNumber));
        actualGift = gift;
        putFields();
    }
}
