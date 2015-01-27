package gui.frontend.tickets;

import db.DefaultManager;
import engine.Hall;
import engine.NotChangedException;
import engine.Reservation;
import engine.accounts.Client;
import engine.custom_managers.ShowManager;
import gui.frontend.UserFrame;
import gui.managment.programm.TableButton;
import logger.InLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Tickets tab, takes table in it
 */
public class TicketsTab extends JPanel {
    private UserFrame context;
    private Client myClient;
    private ShowManager showManager;
    private DefaultManager<Hall> halls;
    private ArrayList<Reservation> reservations;
    JTable table;

    public TicketsTab(UserFrame context, DefaultManager<Hall> halls, ShowManager showManager) {
        this.context = context;
        myClient = context.getUser();
        this.halls = halls;
        this.showManager = showManager;
        InLogger.info("tab created");
        initLayout();
        refreshList();
    }

    /**
     * Refreshs tickets after changes
     */
    public void refreshList() {
        tableRefresh();

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        tableModel.setRowCount(0);
        reservations = myClient.getMyTickets();
        Collections.sort(reservations);
        LocalDateTime ldTime = LocalDateTime.now();
        for (Reservation tickets : reservations) {
            if (ldTime.isBefore(tickets.getShow().getDate())) {
                String time = tickets.getShow().getDate().format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm"));
                int hallNumber = tickets.getShow().getHall().getNumber() + 1;
                String place = "Fila:" + (tickets.getPosition().getCol() + 1) + " Posto:" + (tickets.getPosition().getRow() + 1);
                tableModel.addRow(new Object[]{time, hallNumber, place, tickets.getShow().getFilm().getTitle(), (tickets.isPayed() ? "Confermato" : "Conferma")});
            }
        }
        revalidate();
    }

    /**
     * Refreshing table model
     */
    private void tableRefresh() {
        table.setModel(getCustomTableModel());
        TableColumn buttonColumnModify = table.getColumnModel().getColumn(4);

        TableButton buttonEdit = new TableButton();
        buttonEdit.addHandler((row, column) -> {
            tableEditClick(row, column);
        });

        buttonColumnModify.setCellRenderer(buttonEdit);
        buttonColumnModify.setCellEditor(buttonEdit);
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        table = new JTable();
        JScrollPane jScrollTable = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        add(jScrollTable, BorderLayout.CENTER);
        revalidate();
    }

    /**
     * Returns a table model for JTable
     *
     * @return
     */
    private DefaultTableModel getCustomTableModel() {
        return new DefaultTableModel(new String[]{"Orario", "Sala", "Posto", "Film", "Modifica"}, 0) {
            Class[] types = new Class[]{String.class, Integer.class, String.class, String.class, String.class};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            boolean[] canEdit = new boolean[]{false, false, false, false, true};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }

    /**
     * On Click in "Modifica" coloumn
     *
     * @param row    table row
     * @param column table coloum
     */
    private void tableEditClick(int row, int column) {
        try {
            if (!reservations.get(row).isPayed()) {
                Object[] options = {"Confermare", "Cancellare", "Niente per il momento"};
                int selected = JOptionPane.showOptionDialog(null, "Cosa vuole fare con la prenotazione?", "Modifica del biglietto",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);
                if (selected == 0) {
                    myClient.confirmReservation(reservations.get(row));
                } else if (selected == 1) {
                    myClient.removeReservation(reservations.get(row));
                }
                refreshList();
            }
        } catch (NotChangedException ex) {
            InLogger.exception(ex.toString());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore di elaborazione", JOptionPane.ERROR_MESSAGE);
        }
    }
}
