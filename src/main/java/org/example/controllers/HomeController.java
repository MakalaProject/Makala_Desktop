package org.example.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.controllers.elements.controllers.NotificationController;
import org.example.controllers.elements.controllers.StatisticsProductInfo;
import org.example.controllers.list.controllers.PurchaseController;
import org.example.controllers.parent.controllers.PurchaseParentController;
import org.example.interfaces.IListController;
import org.example.model.Employee;
import org.example.model.Notification;
import org.example.model.Purchase;
import org.example.services.Request;

public class HomeController implements Initializable {
    @FXML JFXButton productosButton;
    @FXML JFXButton empleadosButton;
    @FXML JFXButton proveedoresButton;
    @FXML JFXButton catalogosButton;
    @FXML JFXButton regalosButton;
    @FXML JFXButton clientesButton;
    @FXML JFXButton comprasButton;
    @FXML JFXButton caducidadButton;
    @FXML JFXButton rebajasButton;
    @FXML JFXButton ventasButton;
    @FXML JFXButton calendarioButton;
    @FXML JFXButton contabilidadButton;
    @FXML JFXButton analisisButton;
    @FXML JFXButton produccionButton;
    @FXML JFXButton tiempoStockButton;
    @FXML AnchorPane universalPane;
    @FXML FontAwesomeIconView menuButton;
    @FXML FontAwesomeIconView settings;
    @FXML FontAwesomeIconView notifications;
    @FXML FontAwesomeIconView exitButton;
    @FXML Label userName;
    @FXML Label titleLabel;
    @FXML BorderPane principalPane;
    @FXML AnchorPane menuPane;
    @FXML AnchorPane topPane;

    Parent root;
    FXMLLoader loader;
    String rootResourceName = "";
    boolean menuIsOpen = false;
    private Employee administrator;
    JFXButton actualButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Notification notification = (Notification) Request.getJ("notifications", Notification.class);
        if(notification.isNextMonthBusy() || notification.isWeek3Busy()){
            notifications.setStyle("-fx-fill: RED;");
        }
        settings.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/admin_info.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                AdminInfo dialogController = fxmlLoader.getController();
                dialogController.setEmployee(administrator);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
                Employee employee = (Employee) stage.getUserData();
                if (employee != null){
                    administrator = employee;
                    userName.setText(administrator.getFirstName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            };
        });
        notifications.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/notification.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                NotificationController dialogController = fxmlLoader.getController();
                dialogController.setObject(notification);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            };
        });
        exitButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();
                ((Node)(mouseEvent.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuPane.setTranslateX(-200);
        menuButton.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.2));
            slide.setNode(menuPane);
            if (!menuIsOpen){
                slide.setToX(0);
                slide.play();
                menuPane.setTranslateX(-200);
                menuIsOpen = true;
                principalPane.setLeft(menuPane);

            }else {
                slide.setToX(-200);
                slide.play();
                menuPane.setTranslateX(0);
                menuIsOpen = false;
                principalPane.setLeft(null);
            }
        });

        productosButton.setOnMouseClicked( new HomeLoader("/fxml/product.fxml", "product", "Productos",produccionButton));

        proveedoresButton.setOnMouseClicked(new HomeLoader("/fxml/provider.fxml","provider", "Proveedores", proveedoresButton));

        catalogosButton.setOnMouseClicked(new HomeLoader("/fxml/catalog.fxml","catalog", "Catalogos", catalogosButton));

        empleadosButton.setOnMouseClicked(new HomeLoader("/fxml/employee.fxml","employee", "Empleados", empleadosButton));

        regalosButton.setOnMouseClicked(new HomeLoader("/fxml/gift.fxml","storage", "Regalos", regalosButton));

        clientesButton.setOnMouseClicked(new HomeLoader("/fxml/client.fxml","client", "Clientes",clientesButton));

        comprasButton.setOnMouseClicked(new HomeLoader("/fxml/purchase.fxml","purchase", "Compras",comprasButton));

        caducidadButton.setOnMouseClicked(new HomeLoader("/fxml/expiration_products.fxml","package", "Productos en Caducidad", caducidadButton));

        rebajasButton.setOnMouseClicked(new HomeLoader("/fxml/rebates.fxml","rebates", "Rebajas", rebajasButton));

        ventasButton.setOnMouseClicked(new HomeLoader("/fxml/order_list.fxml", "orders", "Ventas", ventasButton));

        calendarioButton.setOnMouseClicked(new HomeLoader("/fxml/calendar.fxml", "calendar", "Calendario", calendarioButton));

        contabilidadButton.setOnMouseClicked(new HomeLoader("/fxml/contability.fxml", "accounting", "Contabilidad", contabilidadButton));

        analisisButton.setOnMouseClicked(new HomeLoader("/fxml/statistics.fxml", "statistics", "Estadisticas",analisisButton));

        produccionButton.setOnMouseClicked(new HomeLoader("/fxml/production_planifier.fxml", "production", "Produccion", produccionButton));

        tiempoStockButton.setOnMouseClicked(new HomeLoader("/fxml/product_stock_time.fxml", "stock", "Promedio tiempo Stock", tiempoStockButton));
        try {
            loadInitial();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadInitial() throws IOException {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.2));
        slide.setNode(menuPane);
        if (!menuIsOpen){
            slide.setToX(0);
            slide.play();
            menuPane.setTranslateX(-200);
            menuIsOpen = true;
            principalPane.setLeft(menuPane);

        }else {
            slide.setToX(-200);
            slide.play();
            menuPane.setTranslateX(0);
            menuIsOpen = false;
            principalPane.setLeft(null);
        }
        loader = new FXMLLoader(getClass().getResource("/fxml/catalog.fxml"));
        root = loader.load();
        if (rootResourceName.equals("purchase")){
            PurchaseParentController controller = loader.getController();
            controller.setActualEmployee(administrator);
        }
        AnchorPane.setTopAnchor(root, 0D);
        AnchorPane.setBottomAnchor(root, 0D);
        AnchorPane.setRightAnchor(root, 0D);
        AnchorPane.setLeftAnchor(root, 0D);
        universalPane.getChildren().setAll(root);
        if (actualButton!=null){
            actualButton.setStyle(" -fx-background-color: black;");
        }
        catalogosButton.setStyle(" -fx-background-color: #a99f9f;");
        actualButton = catalogosButton;
        rootResourceName = "catalog";
        titleLabel.setText("Catalogos");
    }

    private void loadView(String xmlResource){
        try {
            loader = new FXMLLoader(getClass().getResource(xmlResource));
            root = loader.load();
            if (rootResourceName.equals("purchase")){
                PurchaseParentController controller = loader.getController();
                controller.setActualEmployee(administrator);
            }
            AnchorPane.setTopAnchor(root, 0D);
            AnchorPane.setBottomAnchor(root, 0D);
            AnchorPane.setRightAnchor(root, 0D);
            AnchorPane.setLeftAnchor(root, 0D);
            universalPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setAdministrator(Employee employee){
        administrator = employee;
        userName.setText(administrator.getFirstName());
    }

    private boolean saveCanges(){
        if (loader != null ) {
            IListController controller = loader.getController();
            return controller.existChanges();
        }
        return false;
    }

    public class HomeLoader implements EventHandler{
        private final String rootResourceNameCompare;
        private final String resource;
        private final String title;
        private final JFXButton button;

        public HomeLoader(String resource, String rootName, String title, JFXButton button){
            this.button = button;
            this.resource = resource;
            this.rootResourceNameCompare = rootName;
            this.title = title;
        }

        @Override
        public void handle(Event event) {
            if(!rootResourceName.equals(rootResourceNameCompare)) {
                if (saveCanges())
                    return;
                loadView(resource);
                if (actualButton!=null){
                    actualButton.setStyle(" -fx-background-color: black;");
                }
                button.setStyle(" -fx-background-color: #a99f9f;");
                actualButton = button;
                rootResourceName = rootResourceNameCompare;
                titleLabel.setText(title);
            }
        }
    }
}