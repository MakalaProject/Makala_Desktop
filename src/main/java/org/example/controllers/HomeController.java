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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.controllers.elements.controllers.StatisticsProductInfo;
import org.example.interfaces.IListController;
import org.example.model.Employee;

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
    @FXML AnchorPane universalPane;
    @FXML FontAwesomeIconView menuButton;
    @FXML FontAwesomeIconView settings;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                administrator = (Employee) stage.getUserData();
                userName.setText(administrator.getFirstName());
            } catch (IOException e) {
                e.printStackTrace();
            };
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

        productosButton.setOnMouseClicked( new HomeLoader("/fxml/product.fxml", "product", "Productos"));

        proveedoresButton.setOnMouseClicked(new HomeLoader("/fxml/provider.fxml","provider", "Proveedores"));

        catalogosButton.setOnMouseClicked(new HomeLoader("/fxml/catalog.fxml","catalog", "Catalogos"));

        empleadosButton.setOnMouseClicked(new HomeLoader("/fxml/employee.fxml","employee", "Empleados"));

        regalosButton.setOnMouseClicked(new HomeLoader("/fxml/gift.fxml","storage", "Regalos"));

        clientesButton.setOnMouseClicked(new HomeLoader("/fxml/client.fxml","client", "Clientes"));

        comprasButton.setOnMouseClicked(new HomeLoader("/fxml/purchase.fxml","purchase", "Compras"));

        caducidadButton.setOnMouseClicked(new HomeLoader("/fxml/expiration_products.fxml","package", "Productos en Caducidad"));

        rebajasButton.setOnMouseClicked(new HomeLoader("/fxml/rebates.fxml","rebates", "Rebajas"));

        ventasButton.setOnMouseClicked(new HomeLoader("/fxml/order_list.fxml", "orders", "Ventas"));

        calendarioButton.setOnMouseClicked(new HomeLoader("/fxml/calendar.fxml", "calendar", "Calendario"));

        contabilidadButton.setOnMouseClicked(new HomeLoader("/fxml/contability.fxml", "accounting", "Contabilidad"));

        analisisButton.setOnMouseClicked(new HomeLoader("/fxml/statistics.fxml", "statistics", "Estadisticas"));

        produccionButton.setOnMouseClicked(new HomeLoader("/fxml/production_planifier.fxml", "production", "Produccion"));

    }

    private void loadView(String xmlResource){
        try {
            loader = new FXMLLoader(getClass().getResource(xmlResource));
            root = loader.load();
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

        public HomeLoader(String resource, String rootName, String title){
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
                rootResourceName = rootResourceNameCompare;
                titleLabel.setText(title);
            }
        }
    }
}