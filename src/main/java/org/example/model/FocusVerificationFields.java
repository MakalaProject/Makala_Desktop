package org.example.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class FocusVerificationFields extends RegexVerificationParent implements ChangeListener<Boolean> {

    public FocusVerificationFields(TextField textField, boolean isDecimal, int maxRangeInteger) {
        super(textField, isDecimal, maxRangeInteger);
    }

    public FocusVerificationFields(TextField textField, boolean isDecimal, int maxRangeInteger, int maxRangeDecimal) {
        super(textField, isDecimal, maxRangeInteger, maxRangeDecimal);
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {

        if (isDecimal){
            if (textField.getText().matches("^(\\d{0,"+maxRangeInteger+"}\\.)?$")) {
                textField.setText(textField.getText() + "0");
            }
            if (textField.getText().matches("^(\\.\\d{0,"+maxRangeDecimal+"})?$")) {
                textField.setText("0" + textField.getText());
            }
        }else {
            if (textField.getText().isEmpty()){
                textField.setText("0");
            }
        }
    }
}
