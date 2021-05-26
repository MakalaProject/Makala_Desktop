package org.example.controllers.list.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.elements.controllers.StatisticsProductInfo;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.*;
import org.example.services.Request;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable, IListController<DataAnalysisReceived> {
    @FXML private ComboBox<String> timeFilterCB;
    @FXML private ComboBox<String> estadisticaComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Button startButton;
    @FXML private BarChart<String, Integer> graph;

    private String actualGraph = "";

    private final String[] statisticsRoutes = new String[3];
    private final String[] timeFilterWords = new String[2];
    private final Class[] dataTypes = new Class[3];

    private ObservableList<String> statisticsItems = FXCollections.observableArrayList("Catálogos", "Regalos", "Productos");
    private ObservableList<String> timeFilterItems = FXCollections.observableArrayList("Mes", "Año");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        estadisticaComboBox.setItems(statisticsItems);
        timeFilterCB.setItems(timeFilterItems);

        estadisticaComboBox.getSelectionModel().select(0);
        timeFilterCB.getSelectionModel().select(0);

        statisticsRoutes[0] = "data-analysis/catalogs";
        statisticsRoutes[1] = "data-analysis/gifts";
        statisticsRoutes[2] = "data-analysis/products";
        timeFilterWords[0] = "MONTH";
        timeFilterWords[1] = "YEAR";
        dataTypes[0] = DataCatalog[].class;
        dataTypes[1] = DataGift[].class;
        dataTypes[2] = DataProduct[].class;
        datePicker.setValue(LocalDate.now());

        startButton.setOnMouseClicked(mouseEvent -> {
            if(actualGraph != estadisticaComboBox.getValue()) {
                actualGraph = estadisticaComboBox.getValue();
                DataAnalysis sendData = new DataAnalysis(datePicker.getValue(), timeFilterWords[timeFilterCB.getSelectionModel().getSelectedIndex()]);
                String route = statisticsRoutes[estadisticaComboBox.getSelectionModel().getSelectedIndex()] + "?timeFilter=" + sendData.getTimeFilter() + "&date=" + sendData.getDate();
                List<DataAnalysisReceived> statistics = Request.getJ(route, dataTypes[estadisticaComboBox.getSelectionModel().getSelectedIndex()], false);
                paintChart(statistics);
            }
        });
    }

    private void paintChart(List<DataAnalysisReceived> products){
        graph.getData().clear();
        XYChart.Series<String,Integer> xy = new XYChart.Series<String, Integer>();
        for (DataAnalysisReceived p: products) {
            xy.getData().add(new XYChart.Data<String,Integer>(p.toString(),p.getAmount()));
        }
        graph.setAnimated(false);
        graph.getData().addAll(xy);

        for (XYChart.Series<String,Integer> serie: graph.getData()){
            for (int i = 0; i < serie.getData().size(); i++) {
                int finalI = i;
                serie.getData().get(i).getNode().setOnMouseClicked(mouseEvent -> {
                    DataAnalysisReceived product = products.get(finalI);
                    if (!product.isReceived()) {
                        product.setReceived(true);
                        product.setObject(Request.find(product.getRoute(), product.getId(), product.getObject().getClass()));
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/statistics_product_info.fxml"));
                    try {
                        Parent parent = fxmlLoader.load();
                        StatisticsProductInfo dialogController = fxmlLoader.getController();
                        dialogController.setObject(products.get(finalI));
                        Scene scene = new Scene(parent);
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(scene);
                        stage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    };

                });
            }
        }
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
    }

    @Override
    public void updateView() {
    }

    @Override
    public void cleanForm() {
    }
}
