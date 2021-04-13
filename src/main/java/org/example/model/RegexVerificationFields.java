package org.example.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class RegexVerificationFields implements ChangeListener<String> {
    private TextField textField;
    private boolean isDecimal;
    private int maxRangeInteger;
    private int maxRangeDecimal;

    public RegexVerificationFields(TextField textField, boolean isDecimal, int maxRangeInteger){
        this.textField = textField;
        this.isDecimal = isDecimal;
        this.maxRangeInteger = maxRangeInteger;
    }
    public RegexVerificationFields(TextField textField, boolean isDecimal, int maxRangeInteger, int maxRangeDecimal){
        this.textField = textField;
        this.isDecimal = isDecimal;
        this.maxRangeInteger = maxRangeInteger;
        this.maxRangeDecimal = maxRangeDecimal;
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

        if (isDecimal){
            if (newValue.matches("^(\\d{0,"+maxRangeInteger+"}\\.)?$")) {
                textField.setText(newValue + "0");
            }
            if (newValue.matches("^(\\.\\d{0,"+maxRangeDecimal+"})?$")) {
                textField.setText("0" + newValue);
            }
            if (!newValue.matches("^\\d{0,"+maxRangeInteger+"}|(\\d{0,"+maxRangeInteger+"}\\.\\d{0,"+maxRangeDecimal+"})?$")) {
                textField.setText(oldValue);
            }
        }else {
            if (!newValue.matches("\\d{0,"+maxRangeInteger+"}?$")) {
                textField.setText(oldValue);
            }
            if (observableValue.getValue().isEmpty()){
                textField.setText("0");
            }
        }
    }
}
