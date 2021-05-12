package org.example.customCells;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.example.interfaces.IConstructor;
import org.example.model.*;

import java.io.IOException;

public class RebateListViewCell extends ListCell<Rebate> implements IConstructor<RebateListViewCell> {
    @FXML
    Label objectName;
    @FXML
    FontAwesomeIconView icon;

    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Rebate rebate, boolean empty) {
        super.updateItem(rebate, empty);
        if(empty || rebate == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/rebate_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (rebate.getClass() == ProductRebate.class){
                objectName.setText(((ProductRebate) rebate).getProduct().getName());
                icon.setIcon(FontAwesomeIcon.PRODUCT_HUNT);

            }else {
                objectName.setText(((GiftRebate) rebate).getGift().getName());
                icon.setIcon(FontAwesomeIcon.GIFT);
            }
            setText(null);
            setGraphic(anchorPane);
        }

    }

    @Override
    public RebateListViewCell getControllerCell() {
        return new RebateListViewCell();
    }
}
