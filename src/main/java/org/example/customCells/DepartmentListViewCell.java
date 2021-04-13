package org.example.customCells;

import org.example.model.Department;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DepartmentListViewCell extends ListCell<Department> {

    @FXML
    Label fullName;
    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Department department, boolean empty) {
        super.updateItem(department, empty);

        if(empty || department == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/user_list_view.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            fullName.setText(department.getDepartName());
            setText(null);
            setGraphic(anchorPane);
        }

    }
}
