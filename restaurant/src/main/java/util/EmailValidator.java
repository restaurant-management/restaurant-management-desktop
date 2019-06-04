package util;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty(value = "icon")
public class EmailValidator extends ValidatorBase {

    public EmailValidator(String message) {
        super(message);
    }

    public EmailValidator() {
    }

    @Override
    protected void eval() {
        if (srcControl.get() instanceof TextInputControl) {
            evalTextInputField();
        }
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        if (textField.getText() != null && !textField.getText().isEmpty()) {
            String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
            hasErrors.set(!textField.getText().matches(regex));
        } else {
            hasErrors.set(true);
        }
    }
}
