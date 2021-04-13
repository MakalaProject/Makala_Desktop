package org.example.customCells;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import org.example.model.Employee;
import org.example.model.User;

import java.io.IOException;

public class EmployeeListViewCell extends ListCell<Employee> {
    @FXML Label fullName;
    @FXML private AnchorPane anchorPane;
    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Employee user, boolean empty) {
        super.updateItem(user, empty);

        if(empty || user == null) {
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
            fullName.setText(String.valueOf(user.getFirstName()) +" "+ String.valueOf(user.getLastName()));
            setText(null);
            setGraphic(anchorPane);
        }

    }


}