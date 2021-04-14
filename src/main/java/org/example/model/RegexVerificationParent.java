package org.example.model;
import javafx.scene.control.TextField;

public abstract class RegexVerificationParent {
    protected TextField textField;
    protected boolean isDecimal;
    protected int maxRangeInteger;
    protected int maxRangeDecimal;

    public RegexVerificationParent(TextField textField, boolean isDecimal, int maxRangeInteger){
        this.textField = textField;
        this.isDecimal = isDecimal;
        this.maxRangeInteger = maxRangeInteger;
    }
    public RegexVerificationParent(TextField textField, boolean isDecimal, int maxRangeInteger, int maxRangeDecimal){
        this.textField = textField;
        this.isDecimal = isDecimal;
        this.maxRangeInteger = maxRangeInteger;
        this.maxRangeDecimal = maxRangeDecimal;
    }
}
