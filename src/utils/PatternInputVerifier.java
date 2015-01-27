package utils;

import javax.swing.*;
import java.awt.*;

/**
 * Pattern input verifier
 */
public class PatternInputVerifier extends InputVerifier {
    private String pattern = null;
    private String errorMessage;
    private JLabel errComponent;

    public PatternInputVerifier(String pattern, String errorMessage, JLabel errComponent) {
        super();
        this.errorMessage = errorMessage;
        this.errComponent = errComponent;
        this.pattern = pattern;
    }

    @Override
    public boolean verify(JComponent input) {
        JTextField in = (JTextField) input;
        if (in.getText().matches(pattern)) {
            in.setForeground(Color.black);
            errComponent.setVisible(false);
            return true;
        } else {
            in.setForeground(Color.red);
            errComponent.setText(errorMessage);
            errComponent.setVisible(true);
            return false;
        }
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        return super.shouldYieldFocus(input);
    }
}
