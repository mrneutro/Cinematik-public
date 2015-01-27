package gui.managment.programm;

import db.ContentProvider;
import db.DefaultManager;
import engine.Film;
import engine.Hall;
import engine.Show;
import engine.custom_managers.ShowManager;
import gui.Icons;
import logger.InLogger;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Show creating Frame
 */
public class CreateNewShow extends JFrame {
    private ShowManager showManager;
    private Show editShow;
    private DefaultManager<Film> filmManager = new DefaultManager<>();
    private DefaultManager<Hall> hallManager = new DefaultManager<>();
    private ContentProvider cp;
    private LocalDateTime date;
    private int index;
    private ProgrammTab context;
    private boolean creation = false;

    JLabel lbError = new JLabel();
    JLabel lbFilmLength;
    JComboBox<ComboItem> cbFilm;
    JComboBox<ComboItem> cbSala;
    JComboBox<String> cbHour;
    JComboBox<String> cbMinute;
    JTextField tfPrice;
    JCheckBox cbDiscount;
    JButton btnConfirm = new JButton("Conferma");
    JButton btnCancel = new JButton("Anulla");

    public CreateNewShow(ProgrammTab context, LocalDateTime date, ShowManager showManager) throws HeadlessException {
        try {
            this.context = context;
            this.showManager = showManager;
            this.date = date;

            cp = new ContentProvider();
            filmManager.setSource(cp.getFilms());
            hallManager.setSource(cp.getHalls());


            creation = true;
            setTitle("Creazione evento per la data: " + date.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
            initLayout();
            initListeners();
            setResizable(false);
            setSize(400, 300);
            Utils.centreWindow(this);
            setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore del caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initListeners() {
        tfPrice.setInputVerifier(new PatternInputVerifier(PatternHolder.PRICE, "Prezzo inserito non valido", lbError));
        cbFilm.addActionListener(e -> {
            recalcFilmLength();
        });
        btnConfirm.addActionListener(e -> {
            saveAction();
        });
        btnCancel.addActionListener(e -> {
            closeWindow();
        });
    }

    private void closeWindow() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * On save Action, loads show in manager
     */
    private void saveAction() {
        try {
            Film selectedFilm = ((ComboItem<Film>) cbFilm.getSelectedItem()).getValue();
            Hall selectedHall = ((ComboItem<Hall>) cbSala.getSelectedItem()).getValue();
            double price = Double.parseDouble(tfPrice.getText());
            Time selTime = new Time(Integer.parseInt((String) cbHour.getSelectedItem()), Integer.parseInt((String) cbMinute.getSelectedItem()));

            LocalDateTime showDate = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), selTime.getHour(), selTime.getMinute());

            boolean discountActive = cbDiscount.isSelected();
            {
                InLogger.info("Selected film: " + selectedFilm);
                InLogger.info("Selected hall: " + selectedHall);
                InLogger.info("Price: " + price);
                InLogger.info("Selected time: " + selTime);
                InLogger.info("Discount: " + discountActive);
            }
            Show newShow = new Show(selectedFilm, showDate, price, discountActive, selectedHall);
            InLogger.info("Show created: " + newShow);
            showManager.add(newShow);
            context.progTable.refreshList(null);
            closeWindow();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prezzo errato", "Errore nell'inserimento", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Impossibile salvare l'evento", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initLayout() {
        setIconImage(new ImageIcon(Icons.appIcon).getImage());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        lbError.setForeground(Color.red);
        add(lbError, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Film: "), cs);

        cbFilm = new JComboBox<>();

        for (int i = 0; i < filmManager.size(); i++)
            cbFilm.addItem(new ComboItem<Film>(filmManager.get(i).getTitle(), filmManager.get(i)));

        cs.gridx = 1;
        cs.gridwidth = 2;
        centerPanel.add(cbFilm, cs);

        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Lunghezza: "), cs);

        lbFilmLength = new JLabel();
        cs.gridx = 1;
        cs.gridwidth = 1;
        centerPanel.add(lbFilmLength, cs);
        recalcFilmLength();

        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Sala: "), cs);

        cbSala = new JComboBox<>();
        for (int i = 0; i < hallManager.size(); i++)
            cbSala.addItem(new ComboItem("" + (hallManager.get(i).getNumber() + 1), hallManager.get(i)));


        cs.gridx = 1;
        cs.gridwidth = 2;
        centerPanel.add(cbSala, cs);

        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Orario: "), cs);

        cbHour = new JComboBox<>(Time.getHours());
        cbMinute = new JComboBox<>(Time.getMinutes());

        cs.gridx = 1;
        cs.gridwidth = 2;
        JPanel hourPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hourPanel.add(cbHour);
        hourPanel.add(new JLabel(":"));
        hourPanel.add(cbMinute);
        centerPanel.add(hourPanel, cs);

        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Prezzo senza sconto: "), cs);

        tfPrice = new JTextField(10);
        cs.gridx = 1;
        cs.gridwidth = 2;
        centerPanel.add(tfPrice, cs);

        cs.gridx = 0;
        cs.gridy = 5;
        cs.gridwidth = 1;

        cs.gridx = 1;
        cs.gridwidth = 2;
        cbDiscount = new JCheckBox("Sconti attivi");
        centerPanel.add(cbDiscount, cs);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(btnConfirm);
        btnConfirm.setIcon(new ImageIcon(Icons.save));
        bp.add(btnCancel);
        btnCancel.setIcon(new ImageIcon(Icons.cancel));
        add(bp, BorderLayout.PAGE_END);
    }

    private void recalcFilmLength() {
        ComboItem<Film> selectedFilm = (ComboItem) cbFilm.getSelectedItem();
        lbFilmLength.setText(" " + selectedFilm.getValue().getLength() + " min");
    }
}
