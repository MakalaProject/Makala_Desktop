package org.example.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ChangedVerificationFields extends RegexVerificationParent implements ChangeListener<String> {


    public ChangedVerificationFields(TextField textField, boolean isDecimal, int maxRangeInteger) {
        super(textField, isDecimal, maxRangeInteger);
    }

    public ChangedVerificationFields(TextField textField, boolean isDecimal, int maxRangeInteger, int maxRangeDecimal) {
        super(textField, isDecimal, maxRangeInteger, maxRangeDecimal);
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

        if (isDecimal){
            if (!newValue.matches("^\\d{0,"+maxRangeInteger+"}|(\\d{0,"+maxRangeInteger+"}\\.\\d{0,"+maxRangeDecimal+"})?$")) {
                textField.setText(oldValue);
            }
        }else {
            if (!newValue.matches("\\d{0,"+maxRangeInteger+"}?$")) {
                textField.setText(oldValue);
            }
        }
    }
}
