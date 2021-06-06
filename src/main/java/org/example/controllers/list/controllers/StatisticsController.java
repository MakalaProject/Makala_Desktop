package org.example.controllers.list.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.client.subcontrollers.FrequentClientController;
import org.example.controllers.elements.controllers.StatisticsProductInfo;
import org.example.interfaces.IControllerCreate;
import org.example.interfaces.IListController;
import org.example.model.*;
import org.example.model.products.*;
import org.example.services.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class StatisticsController implements Initializable, IListController<DataAnalysisReceived> {
    @FXML private ComboBox<String> estadisticaComboBox;
    @FXML private DatePicker inicioDatePicker;
    @FXML private DatePicker finalDatePicker;
    @FXML private Button startButton;
    @FXML private BarChart<String, Integer> graph;
    @FXML private Button productTypeButton;
    @FXML private ComboBox<String> giftType;
    @FXML private Label tituloLabel;

    private String actualGraph = "";

    private final String[] statisticsRoutes = new String[3];
    private final Class[] dataTypes = new Class[3];
    private boolean validation = true;
    List<DataAnalysisReceived> statistics;
    private ObservableList<String> statisticsItems = FXCollections.observableArrayList("Catálogos", "Regalos", "Productos", "Clientes frecuentes");
    private ObservableList<String> types = FXCollections.observableArrayList("Por Cantidad", "Por Pedido");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        estadisticaComboBox.setItems(statisticsItems);
        estadisticaComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.equals("Catálogos")){
                    productTypeButton.setVisible(false);
                    giftType.setVisible(false);
                }
                else if(t1.equals("Regalos")){
                    productTypeButton.setVisible(false);
                    giftType.setVisible(true);
                }
                else if(t1.equals("Productos")){
                    productTypeButton.setVisible(true);
                    giftType.setVisible(false);
                }
                else if(t1.equals("Clientes frecuentes")){
                    productTypeButton.setVisible(false);
                    giftType.setVisible(false);
                }
            }
        });
        estadisticaComboBox.getSelectionModel().select(0);
        giftType.setItems(types);
        giftType.setValue("Por Cantidad");
        statisticsRoutes[0] = "demands/catalogs";
        statisticsRoutes[1] = "demands/gifts";
        statisticsRoutes[2] = "demands/products";
        dataTypes[0] = DataCatalog[].class;
        dataTypes[1] = DataGift[].class;
        dataTypes[2] = DataProduct[].class;
        inicioDatePicker.setValue(LocalDate.now().plusMonths(-1));
        finalDatePicker.setValue(LocalDate.now());
        finalDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) > 0 );
            }
        });
        inicioDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                today = today.plusMonths(-1);
                setDisable(empty || date.compareTo(today) > 0 );
            }
        });
        startButton.setOnMouseClicked(mouseEvent -> {
            actualGraph = estadisticaComboBox.getValue();
            //DataAnalysis sendData = new DataAnalysis(inicioDatePicker.getValue(), timeFilterWords[timeFilterCB.getSelectionModel().getSelectedIndex()]);
            String route;
            if(estadisticaComboBox.getValue().equals("Productos")) {
                productTypeButton.setVisible(true);
            }else{
                productTypeButton.setVisible(false);
            }
            if(estadisticaComboBox.getValue().equals("Regalos")){
                if(giftType.getValue().equals("Por Cantidad")){
                    route = statisticsRoutes[estadisticaComboBox.getSelectionModel().getSelectedIndex()]+"?amount=true&"+ "endDate=" + finalDatePicker.getValue().toString() + "&startDate=" + inicioDatePicker.getValue().toString();
                }else{
                    route = statisticsRoutes[estadisticaComboBox.getSelectionModel().getSelectedIndex()]+ "?amount=false&" + "endDate=" + finalDatePicker.getValue().toString() + "&startDate=" + inicioDatePicker.getValue().toString();
                }
            }
            else if(estadisticaComboBox.getValue().equals("Clientes frecuentes")){
                ArrayList<Client> clients = new ArrayList<>(Request.getJ("clients", Client[].class, true));
                ArrayList<Client> frequent = new ArrayList<>();
                for(Client c : clients){
                    if(c.isFrequentClient()){
                        frequent.add(c);
                    }
                }
                paintClientChart(frequent);
                return;
            }
            else{
                route = statisticsRoutes[estadisticaComboBox.getSelectionModel().getSelectedIndex()] + "?endDate=" + finalDatePicker.getValue().toString() + "&startDate=" + inicioDatePicker.getValue().toString();
            }
            statistics = Request.getJ(route, dataTypes[estadisticaComboBox.getSelectionModel().getSelectedIndex()], false);
            String type = statisticsRoutes[estadisticaComboBox.getSelectionModel().getSelectedIndex()];
            if(type.equals("demands/products")){
                productTypeButton.setVisible(true);
            }else{
                productTypeButton.setVisible(false);
                validation = true;
            }
            paintChart(statistics);

        });

        productTypeButton.setOnMouseClicked(mouseEvent -> {
            validation = !validation;
            paintChart(statistics);
        });

    }

    private void paintClientChart(ArrayList<Client> clients){
        graph.getData().clear();
        XYChart.Series<String, Integer> xyNormal = new XYChart.Series<String, Integer>();

        XYChart.Series<String, Integer> xyBulk = new XYChart.Series<String, Integer>();
        for (Client p : clients) {
            String name;
            if (p.getFirstName().length() > 10) {
                name = p.getFirstName().substring(0, 8);
                name += "...";
            } else {
                name = p.getFirstName();
            }
            xyNormal.getData().add(new XYChart.Data<String, Integer>(name, p.getGifts()));
        }
        graph.setAnimated(false);
        graph.getData().addAll(xyNormal);
        graph.getYAxis().setLabel("Regalos adquiridos");
        graph.getXAxis().setLabel("Nombre cliente");
        tituloLabel.setText("Estadistica de Clientes frecuentes");
        for (XYChart.Series<String,Integer> serie: graph.getData()){
            for (int i = 0; i < serie.getData().size(); i++) {
                int finalI = i;
                serie.getData().get(i).getNode().setOnMouseClicked(mouseEvent -> {
                    Client client = clients.get(finalI);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/frequent_client.fxml"));
                    try {
                        FrequentClientController dialogController = new FrequentClientController();
                        fxmlLoader.setController(dialogController);
                        Parent parent = fxmlLoader.load();
                        dialogController.setClient(client);
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


    private void paintChart(List<DataAnalysisReceived> products) {
        graph.getData().clear();
        XYChart.Series<String, Integer> xyNormal = new XYChart.Series<String, Integer>();
        XYChart.Series<String, Integer> xyBulk = new XYChart.Series<String, Integer>();
        tituloLabel.setText(estadisticaComboBox.getValue());
        ArrayList<DataAnalysisReceived> newProducts = new ArrayList<>();
        ArrayList<DataAnalysisReceived> bulkProducts = new ArrayList<>();
        for (DataAnalysisReceived p : products) {
            String name;
            String type = statisticsRoutes[estadisticaComboBox.getSelectionModel().getSelectedIndex()];
            if (p.toString().length() > 10) {
                name = p.toString().substring(0, 8);
                name += "...";
            } else {
                name = p.toString();
            }
            if (type.equals("demands/products")) {
                Product product = (Product) p.getObject();
                if (product.getProductClassDto().getProductType().equals("Creados")) {
                    CraftedProduct craftedProduct = (CraftedProduct) Request.find("products/crafted", product.getId(), CraftedProduct.class);
                    BulkProduct bulk = new BulkProduct();
                    for(InsideProduct bulkProduct : craftedProduct.getProductsInside()){
                        bulk = (BulkProduct) Request.find("products/bulks", bulkProduct.getId(), BulkProduct.class);
                        p.setAmount(bulkProduct.getAmount().multiply(new BigDecimal(p.getAmount().toString())).intValue());
                        xyBulk.getData().add(new XYChart.Data<String, Integer>(bulkProduct.getName(), bulkProduct.getAmount().multiply(new BigDecimal(p.getAmount().toString())).intValue()));
                    }
                    p.setObject(bulk);
                    bulkProducts.add(p);
                    continue;
                }
                if(product.getProductClassDto().getProductType().equals("Granel")){
                    xyBulk.getData().add(new XYChart.Data<String, Integer>(product.getName(), p.getAmount()));
                    bulkProducts.add(p);
                    continue;
                }

            }
            newProducts.add(p);
            xyNormal.getData().add(new XYChart.Data<String, Integer>(name, p.getAmount()));
        }
        graph.setAnimated(false);
        if(validation && statisticsRoutes[estadisticaComboBox.getSelectionModel().getSelectedIndex()].equals("demands/products")) {
            graph.getYAxis().setLabel("Cantidad de productos vendidos");
            tituloLabel.setText(estadisticaComboBox.getValue() + " (Fijos)");
            productTypeButton.setText("Granel");
            graph.getData().clear();
            graph.getData().addAll(xyNormal);
        }else if(!validation && statisticsRoutes[estadisticaComboBox.getSelectionModel().getSelectedIndex()].equals("demands/products")){
            tituloLabel.setText(estadisticaComboBox.getValue() + " (Granel)");
            graph.getYAxis().setLabel("Cantidad de gr vendidos");
            productTypeButton.setText("Fijos");
            newProducts.clear();
            newProducts.addAll(bulkProducts);
            graph.getData().clear();
            graph.getData().addAll(xyBulk);
        }else{
            if(estadisticaComboBox.getValue().equals("Regalos") && giftType.getValue().equals("Por Cantidad")){
                graph.getYAxis().setLabel("Cantidad de regalos vendidos");
            }else if(estadisticaComboBox.getValue().equals("Regalos") && giftType.getValue().equals("Por Pedido")){
                graph.getYAxis().setLabel("Cantidad de pedidos vendidos");
            }else{
                graph.getYAxis().setLabel("Catalogos vendidos");
            }
            graph.getData().clear();
            graph.getData().addAll(xyNormal);
            tituloLabel.setText(estadisticaComboBox.getValue());
        }
        for (XYChart.Series<String,Integer> serie: graph.getData()){
            for (int i = 0; i < serie.getData().size(); i++) {
                int finalI = i;
                serie.getData().get(i).getNode().setOnMouseClicked(mouseEvent -> {
                    DataAnalysisReceived product = newProducts.get(finalI);
                    if (!product.isReceived()) {
                        product.setReceived(true);
                        product.setObject(Request.find(product.getRoute(), product.getId(), product.getObject().getClass()));
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/statistics_product_info.fxml"));
                    try {
                        Parent parent = fxmlLoader.load();
                        StatisticsProductInfo dialogController = fxmlLoader.getController();
                        dialogController.setObject(newProducts.get(finalI));
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
