package gui.managment.programm;

import engine.accounts.User;
import engine.custom_managers.ShowManager;
import gui.Icons;
import logger.InLogger;
import org.freixas.jcalendar.JCalendarCombo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.ZoneId;
import java.util.GregorianCalendar;

/**
 * Programming tab
 */
public class ProgrammTab extends JPanel {
    ProgrammTable progTable;
    JButton addButton;
    JCalendarCombo jCalendarCombo;
    JComboBox<String> orderBy = new JComboBox<>();
    private ShowManager showManager;
    private User user;

    public ProgrammTab(User user) {
        try {
            this.user = user;
            InLogger.info("created");
            showManager = new ShowManager();
            setLayout(new BorderLayout());
            initLayout();
            initListeners();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante il caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    public User getUser() {
        return user;
    }

    private void initListeners() {
        jCalendarCombo.addDateListener(dateEvent -> {
            progTable.refreshList(dateEvent.getSelectedDate());
        });
        addButton.addActionListener(e -> {
            new CreateNewShow(this, jCalendarCombo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), showManager);
        });
        orderBy.addActionListener(e -> {
            switch (orderBy.getSelectedIndex()) {
                case 0:
                    progTable.setSorting("date");
                    break;
                case 1:
                    progTable.setSorting("postsfree");
                    break;
                case 2:
                    progTable.setSorting("postsbusy");
                    break;
            }
        });
    }

    private void initLayout() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(new JLabel("Ordina:"));

        orderBy.addItem("orario");
        orderBy.addItem("posti disp. cresenti");
        orderBy.addItem("posti occupati. cresenti");

        topPanel.add(orderBy);
        topPanel.add(new JLabel("Programma per data:"));
        jCalendarCombo = new JCalendarCombo();
        topPanel.add(jCalendarCombo);
        addButton = new JButton("Aggiungi lo spettacolo");
        addButton.setIcon(new ImageIcon(Icons.addIcon));
        topPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);
        progTable = new ProgrammTable(this, showManager);
        add(progTable, BorderLayout.CENTER);

        progTable.refreshList(new GregorianCalendar());
    }

}
