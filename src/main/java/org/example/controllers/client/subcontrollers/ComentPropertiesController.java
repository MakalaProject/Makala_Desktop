package org.example.controllers.client.subcontrollers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.example.model.Comment;
import org.example.model.GiftEditable;

import java.net.URL;
import java.util.ResourceBundle;


public class ComentPropertiesController implements Initializable {
    Comment actualComment;
    @FXML
    TextArea commentText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setObject(Comment selectedItem) {
        actualComment = selectedItem;
        commentText.setText(actualComment.getComment());
    }
}
