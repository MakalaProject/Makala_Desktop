package org.example.controllers.list.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.example.model.DataAnalysis;
import org.example.model.products.DataProduct;
import org.example.services.Request;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {
    @FXML private ComboBox<String> timeFilterCB;
    @FXML private ComboBox<String> estadisticaComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Button startButton;
    @FXML private BarChart<String, Integer> graph;

    private final String[] statisticsRoutes = new String[4];
    private final String[] timeFilterWords = new String[2];

    private ObservableList<String> statisticsItems = FXCollections.observableArrayList("Catálogos", "Regalos", "Productos", "Clientes");
    private ObservableList<String> timeFilterItems = FXCollections.observableArrayList("Mes", "Año");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        estadisticaComboBox.setItems(statisticsItems);
        timeFilterCB.setItems(timeFilterItems);

        estadisticaComboBox.getSelectionModel().select(0);
        timeFilterCB.getSelectionModel().select(0);

        statisticsRoutes[0] = "/data-analysis/catalogs";
        statisticsRoutes[1] = "/data-analysis/gifts";
        statisticsRoutes[3] = "/data-analysis/products";
        timeFilterWords[0] = "MONTH";
        timeFilterWords[1] = "YEAR";
        datePicker.setValue(LocalDate.now());
        startButton.setOnMouseClicked(mouseEvent -> {
            DataAnalysis sendData = new DataAnalysis(datePicker.getValue(), timeFilterWords[timeFilterCB.getSelectionModel().getSelectedIndex()]);
            List<DataProduct> products = (List<DataProduct>) Request.getJ(statisticsRoutes[estadisticaComboBox.getSelectionModel().getSelectedIndex()], DataProduct[].class);
            paintChart(products);
        });
    }

    private void paintChart(List<DataProduct> products){
        XYChart.Series xy = new XYChart.Series<>();
        for (DataProduct p: products) {
            xy.getData().add(new XYChart.Data(p.getProduct().getName(),p.getAmount()));
        }
        graph.getData().addAll(xy);
    }
}
