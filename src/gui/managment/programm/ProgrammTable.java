package gui.managment.programm;

import engine.NotChangedException;
import engine.Place;
import engine.Position;
import engine.Show;
import engine.custom_managers.ShowManager;
import engine.sorting.Sorter;
import gui.frontend.buy_process.ActionCallable;
import gui.frontend.buy_process.PlaceSelectorFrame;
import logger.InLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;

/**
 * Programm table shows and modifies showss
 */
public class ProgrammTable extends JPanel implements ActionCallable {
    private ShowManager showManager;
    private JTable table;
    private ArrayList<Show> tableShow;
    private ProgrammTab context;
    private String sortBy = "date";
    private Show editingShow;


    public ProgrammTable(ProgrammTab context, ShowManager showManager) {
        this.context = context;
        setLayout(new BorderLayout());
        this.showManager = showManager;
        table = new JTable();
        JScrollPane jScrollTable = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        table.setModel(getCustomTableModel());
        TableColumn checkColoumn = table.getColumnModel().getColumn(7);
        checkColoumn.setCellRenderer(new TableCheckBox());
        TableColumn buttonColumn = table.getColumnModel().getColumn(8);

        TableButton buttons = new TableButton();
        buttons.addHandler((row, column) -> {
            tableButtonClick(row, column);
        });

        buttonColumn.setCellRenderer(buttons);
        buttonColumn.setCellEditor(buttons);

        add(jScrollTable, BorderLayout.CENTER);
        revalidate();
    }

    public void setSorting(String sortBy) {
        this.sortBy = sortBy;
        refreshList(null);
    }

    /**
     * Refresh table data
     *
     * @param date show date or null
     */
    public void refreshList(Calendar date) {
        if (showManager.size() == 0) return;

        InLogger.info("Start sorting by: " + sortBy);
        if (date == null) {
            date = context.jCalendarCombo.getCalendar();
        }
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        LocalDate selectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        InLogger.info("Selected date for refreshing: " + selectedDate);

        tableShow = new ArrayList<>();
        tableShow.clear();
        tableModel.setRowCount(0);

        for (Show s : showManager) {
            if (selectedDate.equals(s.getDate().toLocalDate())) {
                s.setSortElement(sortBy);
                tableShow.add(s);
            }
        }
        Sorter<Show> sorter = tableShow.stream().collect(Collectors.toCollection(() -> new Sorter<>()));
        sorter.sort(Sorter.ASC);

        tableShow = new ArrayList<>();
        tableShow.addAll(sorter.stream().collect(Collectors.toList()));

        for (Show sorted : tableShow) {
            tableModel.addRow(new Object[]{sorted.getDate().format(DateTimeFormatter.ofPattern("H:mm")), "" + (sorted.getHall().getNumber() + 1), sorted.getFilm().getTitle(), sorted.getHall().getFree(), sorted.getHall().getReserved(), sorted.getHall().getBusy(), sorted.getRegularCost(), sorted.getDiscount(), "Modifica"});
        }
        revalidate();
    }

    /**
     * Table model getter
     *
     * @return DefaultTableModel
     */
    private DefaultTableModel getCustomTableModel() {
        return new DefaultTableModel(new String[]{"Inizio", "Sala", "Film", "Liberi", "Prenotati", "Acquistati", "Prezzo regolare", "Sconti", "Modifica"}, 0) {
            Class[] types = new Class[]{String.class, String.class, String.class, Integer.class, Integer.class, Integer.class, Double.class, Boolean.class, String.class};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            boolean[] canEdit = new boolean[]{false, false, false, false, false, false, false, false, true};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }

    private void tableButtonClick(int row, int column) {
        editingShow = tableShow.get(row);
        new PlaceSelectorFrame<>(context.getUser(), this, editingShow, "Clicca sul posto per abilitare/disabilitare").setVisible(true);
    }

    @Override
    public void callStart(Place pos) {
        try {
            if (editingShow.getHall().getState(pos) == Position.Status.DISABLED) {
                editingShow.getHall().setEnabled(pos);
            } else {
                editingShow.getHall().setDisabled(pos);
            }
        } catch (NotChangedException ex) {
            InLogger.exception(ex.toString());
        }
    }
}
