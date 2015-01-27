package gui.authentication;

import engine.accounts.*;
import gui.Icons;
import gui.frontend.UserFrame;
import gui.managment.ManagerFrame;
import utils.PatternHolder;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

/**
 * Login form
 */
public class LoginForm extends JFrame {
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel centerPanel;
    JLabel userCount = new JLabel("Utenti registrati: 0");
    JButton btnLogin;
    JButton btnCancel;
    JButton btnRegister;
    JTextField tfUsername;
    JPasswordField pfPassword;
    JLabel lbUsername;
    JLabel lbPassword;
    User user = null;
    AccountManager accountManager;

    public LoginForm() throws HeadlessException {
        try {
            accountManager = new AccountManager();
            initLayout();
            refreshCounter();
            initListeners();
            setSize(400, 300);
            Utils.centreWindow(this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante il caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refresh user counter
     */
    private void refreshCounter() {
        userCount.setText("Utenti registrati: " + accountManager.size());
    }

    private void loginClick(ActionEvent e) {
        try {
            String email = tfUsername.getText();
            String pass = new String(pfPassword.getPassword());
            user = accountManager.login(email, pass);
            if (user.isAdmin()) {
                new ManagerFrame((Admin) user).setVisible(true);
            } else {
                new UserFrame(accountManager, (Client) user).setVisible(true);
            }
            this.setVisible(false);
        } catch (UserNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void initListeners() {

        btnLogin.addActionListener(e -> {
            loginClick(e);
        });

        btnRegister.addActionListener(e -> {
            new RegisterForm(this, accountManager).setVisible(true);
        });

        btnCancel.addActionListener(e -> {
            System.exit(0);
        });

        tfUsername.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkEmail();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkEmail();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkEmail();
            }
        });

        pfPassword.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkPassword();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkPassword();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkPassword();
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                refreshCounter();
            }
        });
    }

    /**
     * Checks email for patterns and enambles button login
     */
    private void checkEmail() {
        if (tfUsername.getForeground().equals(Color.black) && pfPassword.getForeground().equals(Color.black)) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
        if (tfUsername.getText().matches(PatternHolder.EMAIL)) {
            tfUsername.setForeground(Color.black);
        } else {
            tfUsername.setForeground(Color.red);
        }
    }

    /**
     * Checks pass for patterns and enambles button login
     */
    private void checkPassword() {
        if (tfUsername.getForeground().equals(Color.black) && pfPassword.getForeground().equals(Color.black)) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
        if (pfPassword.getPassword().length < 5) {
            pfPassword.setForeground(Color.RED);
        } else {
            pfPassword.setForeground(Color.BLACK);
        }
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        topPanel.add(userCount);
        add(topPanel, BorderLayout.NORTH);

        setIconImage(new ImageIcon(Icons.appIcon).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cinematic - Login");
        setResizable(false);

        centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel picLabel = new JLabel(new ImageIcon(Icons.appIcon));
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 3;
        centerPanel.add(picLabel, cs);

        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        centerPanel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        centerPanel.add(tfUsername, cs);

        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        JPanel dv = new JPanel();
        dv.setSize(10, 10);
        centerPanel.add(dv, cs);

        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        centerPanel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        pfPassword.setSize(50, 15);
        cs.gridx = 1;
        cs.gridy = 3;
        cs.gridwidth = 2;
        centerPanel.add(pfPassword, cs);
        centerPanel.setBorder(new LineBorder(Color.GRAY));

        btnLogin = new JButton("Login");
        btnRegister = new JButton("Registrazione");
        btnCancel = new JButton("Cancel");
        btnLogin.setEnabled(false);

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLogin.setIcon(new ImageIcon(Icons.door_in));
        bp.add(btnLogin);
        btnRegister.setIcon(new ImageIcon(Icons.register));
        bp.add(btnRegister);
        btnCancel.setIcon(new ImageIcon(Icons.cancel));
        bp.add(btnCancel);


        add(centerPanel, BorderLayout.CENTER);
        add(bp, BorderLayout.PAGE_END);
    }
}