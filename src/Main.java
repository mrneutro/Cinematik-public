import gui.authentication.LoginForm;

import java.io.IOException;

import static utils.Utils.setupLAF;

public class Main {
    public static void main(String[] args) throws IOException {
        setupLAF();
        startLoginForm();
    }

    private static void startLoginForm() {
        new LoginForm().setVisible(true);
    }

}