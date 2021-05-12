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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.example.interfaces.IListController;

public class HomeController implements Initializable {
    @FXML JFXButton productosButton;
    @FXML JFXButton empleadosButton;
    @FXML JFXButton proveedoresButton;
    @FXML JFXButton catalogosButton;
    @FXML JFXButton regalosButton;
    @FXML JFXButton clientesButton;
    @FXML JFXButton comprasButton;
    @FXML JFXButton rebajasButton;
    @FXML AnchorPane universalPane;
    @FXML FontAwesomeIconView menuButton;
    @FXML BorderPane principalPane;
    @FXML AnchorPane menuPane;
    @FXML AnchorPane topPane;

    Parent root;
    FXMLLoader loader;
    String rootResourceName = "";
    boolean menuIsOpen = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

        productosButton.setOnMouseClicked( new HomeLoader("/fxml/product.fxml", "product"));

        proveedoresButton.setOnMouseClicked(new HomeLoader("/fxml/provider.fxml","provider"));

        catalogosButton.setOnMouseClicked(new HomeLoader("/fxml/catalog.fxml","catalog"));

        empleadosButton.setOnMouseClicked(new HomeLoader("/fxml/employee.fxml","employee"));

        regalosButton.setOnMouseClicked(new HomeLoader("/fxml/gift.fxml","storage"));

        clientesButton.setOnMouseClicked(new HomeLoader("/fxml/client.fxml","client"));

        comprasButton.setOnMouseClicked(new HomeLoader("/fxml/purchase.fxml","purchase"));

        rebajasButton.setOnMouseClicked(new HomeLoader("/fxml/rebates.fxml","rebates"));
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

        public HomeLoader(String resource, String rootName){
            this.resource = resource;
            this.rootResourceNameCompare = rootName;
        }

        @Override
        public void handle(Event event) {
            if(!rootResourceName.equals(rootResourceNameCompare)) {
                if (saveCanges())
                    return;
                loadView(resource);
                rootResourceName = rootResourceNameCompare;
            }
        }
    }
}