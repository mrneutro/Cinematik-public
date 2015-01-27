package gui.authentication;

import engine.accounts.AccountManager;
import engine.accounts.CreditCard;
import engine.accounts.EmployHolder;
import engine.accounts.UserExistException;
import gui.Icons;
import utils.PatternHolder;
import utils.PatternInputVerifier;
import utils.Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by Neutro on 14/12/2014.
 */
public class RegisterForm extends JFrame {
    JPanel centerPanel;
    AccountManager accountManager;
    LoginForm loginFormFrame;

    JTextField tfName = new JTextField();
    JTextField tfEmail = new JTextField();
    JPasswordField pfPassword = new JPasswordField();
    JComboBox<Integer> cmbAge = new JComboBox<>();
    JButton btnConfirm = new JButton("Conferma");
    JButton btnCancel = new JButton("Cancella");
    JComboBox<String> cmbProfession;
    JFormattedTextField tfCardNumber = new JFormattedTextField();
    JFormattedTextField tfCvv = new JFormattedTextField();
    JFormattedTextField tfValid = new JFormattedTextField();
    JLabel cardIcon;
    JLabel lbError = new JLabel("", JLabel.CENTER);


    public RegisterForm(LoginForm context, AccountManager accountManager) throws HeadlessException {
        this.accountManager = accountManager;
        loginFormFrame = context;
        context.setVisible(false);
        initializeLayout();
        initializeListeners();
        setSize(400, 300);
        Utils.centreWindow(this);
    }

    private void initializeListeners() {
        /* Login frame reopen on Register close */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginFormFrame.setVisible(true);
                setVisible(false);
            }
        });

        tfName.setInputVerifier(new PatternInputVerifier(PatternHolder.NAME, "Nome non e' valido", lbError));
        tfEmail.setInputVerifier(new PatternInputVerifier(PatternHolder.EMAIL, "Email non e' valido", lbError));
        pfPassword.setInputVerifier(new PatternInputVerifier(PatternHolder.PASS, "Password deve essere piu grande di 4 charatteri", lbError));
        tfCardNumber.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeIcon(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeIcon(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeIcon(e);
            }
        });

        btnConfirm.addActionListener(e -> {
            makeRegister();
        });
        btnCancel.addActionListener(e -> {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
    }

    /**
     * Starts registration process
     */
    private void makeRegister() {
        try {
            String name = tfName.getText();
            String email = tfEmail.getText();
            String pass = pfPassword.getText();
            int age = (Integer) cmbAge.getSelectedItem();
            int proff = cmbProfession.getSelectedIndex();
            String cardNo = tfCardNumber.getText();
            String cardDate = tfValid.getText();
            String cardCvv = tfCvv.getText();
            accountManager.register(name, email, pass, age, cardNo, cardDate, cardCvv, 0, proff);
            JOptionPane.showMessageDialog(this, "Registrazione completata");
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante il caricamento del DB", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante la registrazione", JOptionPane.ERROR_MESSAGE);
        } catch (UserExistException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante la registrazione", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Changes credit card icon
     *
     * @param e
     */
    private void changeIcon(DocumentEvent e) {
        try {
            Document source = e.getDocument();
            String inputText = source.getText(0, source.getLength());
            String cardName = CreditCard.verifyNumber(inputText);
            cardIcon.setIcon(CreditCard.getCardIcon(cardName));
        } catch (IllegalArgumentException ignore) {
            //NOP
        } catch (BadLocationException ignore) {
            //NOP
        }
    }

    private void initializeLayout() {
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon(Icons.appIcon).getImage());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setTitle("Cinematic - Registrazione");


        lbError.setForeground(Color.red);
        add(lbError, BorderLayout.NORTH);
        centerPanel = new JPanel(new GridBagLayout());

        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        //NAME
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Nome: "), cs);

        cs.gridx = 1;
        cs.gridwidth = 3;
        centerPanel.add(tfName, cs);

        //EMAIL
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("E-mail: "), cs);

        cs.gridx = 1;
        cs.gridwidth = 3;
        centerPanel.add(tfEmail, cs);

        //PASS
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Password: "), cs);

        cs.gridx = 2;
        cs.gridwidth = 2;
        centerPanel.add(pfPassword, cs);

        //AGE
        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Età: "), cs);

        cs.gridx = 2;
        cs.gridwidth = 2;
        for (int i = 1; i < 100; i++)
            cmbAge.addItem(i);
        cmbAge.setSelectedIndex(20);
        centerPanel.add(cmbAge, cs);

        //Employ
        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Impiego: "), cs);

        cmbProfession = new JComboBox<>(new DefaultComboBoxModel<>(EmployHolder.getValues()));
        cs.gridx = 2;
        cs.gridwidth = 2;
        centerPanel.add(cmbProfession, cs);

        //CARDS
        cs.gridx = 0;
        cs.gridy = 5;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Carta:"), cs);
        try {
            MaskFormatter ssnFormatter = new MaskFormatter("#### #### #### ####");
            ssnFormatter.setValueClass(String.class);
            ssnFormatter.setPlaceholderCharacter('_');
            DefaultFormatterFactory ssnFormatterFactory = new DefaultFormatterFactory(ssnFormatter);
            tfCardNumber.setFormatterFactory(ssnFormatterFactory);
        } catch (ParseException ignore) {
            //NOP
        }

        cs.gridx = 2;
        cs.gridwidth = 1;
        tfCardNumber.setSize(150, 15);
        centerPanel.add(tfCardNumber, cs);

        cs.gridx = 3;
        cs.gridwidth = 1;
        cardIcon = new JLabel("", JLabel.CENTER);
        centerPanel.add(cardIcon, cs);

        add(centerPanel, BorderLayout.CENTER);

        //CARDS
        cs.gridx = 0;
        cs.gridy = 6;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("CVV e scadenza:"), cs);

        cs.gridx = 2;
        cs.gridwidth = 1;

        try {
            MaskFormatter ssnFormatter = new MaskFormatter("###");
            ssnFormatter.setValueClass(NumberFormat.class);
            ssnFormatter.setPlaceholderCharacter('_');
            DefaultFormatterFactory ssnFormatterFactory = new DefaultFormatterFactory(ssnFormatter);
            tfCvv.setFormatterFactory(ssnFormatterFactory);
        } catch (ParseException ignore) {
            //NOP
        }

        centerPanel.add(tfCvv, cs);

        cs.gridx = 3;
        try {
            MaskFormatter ssnFormatter = new MaskFormatter("##/##");
            ssnFormatter.setValueClass(NumberFormat.class);
            ssnFormatter.setPlaceholderCharacter('_');
            DefaultFormatterFactory ssnFormatterFactory = new DefaultFormatterFactory(ssnFormatter);
            tfValid.setFormatterFactory(ssnFormatterFactory);
        } catch (ParseException ignore) {
            //NOP
        }
        centerPanel.add(tfValid, cs);

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnConfirm.setIcon(new ImageIcon(Icons.save));
        bp.add(btnConfirm);
        btnCancel.setIcon(new ImageIcon(Icons.cancel));
        bp.add(btnCancel);
        add(bp, BorderLayout.PAGE_END);
    }

}
