package org.example.customCells;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.example.interfaces.IConstructor;
import org.example.model.Formatter;
import org.example.model.Purchase;

import java.io.IOException;
import java.time.LocalDate;

public class PurchaseListViewCell extends ListCell<Purchase> implements IConstructor<PurchaseListViewCell> {
    @FXML
    Label purchaseDate;
    @FXML
    Label infLabel;
    @FXML
    FontAwesomeIconView checkIcon;

    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Purchase purchase, boolean empty) {
        super.updateItem(purchase, empty);

        if(empty || purchase == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/purchase_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            purchaseDate.setText(String.valueOf(purchase) + "- " +Formatter.FormatDate(purchase.getOrderDate()));
            if (purchase.getReceivedDate()!=null){
                infLabel.setText("Recibida");
                checkIcon.setIcon(FontAwesomeIcon.CHECK_CIRCLE);
            }else{
                checkIcon.setVisible(false);
                infLabel.setVisible(false);
            }
            setText(null);
            setGraphic(anchorPane);
        }

    }

    @Override
    public PurchaseListViewCell getControllerCell() {
        return new PurchaseListViewCell();
    }

}
