package gui.managment.analytics;

import db.ContentProvider;
import db.DefaultManager;
import engine.Analytics;
import engine.Film;
import engine.ObjectFilter;
import engine.custom_managers.ShowManager;
import javafx.util.Pair;
import logger.InLogger;
import utils.ComboItem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * Visualizes data from shows, gains, tickets ecc
 */
public class AnalyticTab extends JPanel {
    private ShowManager showManager;
    private DefaultManager<Film> filmsManager = new DefaultManager<>();
    private ContentProvider cp;
    JComboBox<ComboItem<Film>> cbFilm = new JComboBox<>();
    JComboBox<ComboItem<Pair>> cbWeek = new JComboBox<>();
    JLabel lbProfit = new JLabel("0");
    JLabel lbBuyed = new JLabel("0");
    JLabel lbReserv = new JLabel("0");
    JLabel lbWithDiscount = new JLabel("0");

    public AnalyticTab() {
        try {
            InLogger.info("created");
            cp = new ContentProvider();
            filmsManager.setSource(cp.getFilms());
            showManager = new ShowManager();
            setLayout(new BorderLayout());
            initLayout();
            initListeners();
            refreshData();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante il caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initListeners() {
        cbFilm.addActionListener(e -> {
            refreshData();
        });
        cbWeek.addActionListener(e -> {
            refreshData();
        });
    }

    /**
     * Refreshs data if filter is changed
     */
    private void refreshData() {
        Pair<LocalDate, LocalDate> interval = ((ComboItem<Pair<LocalDate, LocalDate>>) cbWeek.getSelectedItem()).getValue();
        Film selFilm = ((ComboItem<Film>) cbFilm.getSelectedItem()).getValue();
        ObjectFilter<Film> filter = obj -> {
            if (cbFilm.getSelectedIndex() == 0 || obj.equals(selFilm)) return true;
            else return false;
        };
        Analytics analytics = new Analytics(cp, interval, filter);
        lbBuyed.setText("" + analytics.getTotalTickets());
        lbProfit.setText("€" + analytics.getTotalProfit());
        lbReserv.setText("" + analytics.getTotalReservations());
        lbWithDiscount.setText("" + analytics.getTicketsWithDiscount());
    }

    private void initLayout() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(new JLabel("Settimana:"));
        topPanel.add(cbWeek);
        topPanel.add(new JLabel("Film:"));
        topPanel.add(cbFilm);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 3, 2, 2));
        Font lbFont = new Font("Arial", Font.BOLD, 42);
        //Profit panel
        JPanel profitPanel = new JPanel();
        profitPanel.setBorder(new TitledBorder("Profitto"));
        profitPanel.add(lbProfit);
        lbProfit.setFont(lbFont);
        centerPanel.add(profitPanel);

        //Total buyed
        JPanel totalTickets = new JPanel();
        totalTickets.setBorder(new TitledBorder("Biglietti acquistati"));
        totalTickets.add(lbBuyed);
        lbBuyed.setFont(lbFont);
        centerPanel.add(totalTickets);

        //Total reserved
        JPanel totalReserv = new JPanel();
        totalReserv.setBorder(new TitledBorder("Biglietti prenotati"));
        totalReserv.add(lbReserv);
        lbReserv.setFont(lbFont);
        centerPanel.add(totalReserv);

        //Total with discount
        JPanel totalWDiscount = new JPanel();
        totalWDiscount.setBorder(new TitledBorder("Biglietti con lo sconto"));
        totalWDiscount.add(lbWithDiscount);
        lbWithDiscount.setFont(lbFont);
        centerPanel.add(totalWDiscount);

        loadWeekInterval();
        loadFilms();

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadFilms() {
        cbFilm.addItem(new ComboItem<>("Tutti", null));
        for (Film film : filmsManager) {
            cbFilm.addItem(new ComboItem<>(film.getTitle(), film));
        }
    }

    /**
     * Loads week based JCombobox
     */
    private void loadWeekInterval() {
        LocalDate ldNow = LocalDate.now().minusWeeks(2);
        if (!LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            ldNow = ldNow.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        }

        for (int i = 0; i < 8; i++) {
            LocalDate ldNext = ldNow.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            Pair<LocalDate, LocalDate> period = new Pair<>(ldNow, ldNext);
            String caption = period.getKey().format(DateTimeFormatter.ofPattern("dd/MM"));
            caption += " - " + period.getValue().minusDays(1).format(DateTimeFormatter.ofPattern("dd/MM"));
            cbWeek.addItem(new ComboItem<>(caption, period));
            ldNow = ldNext;
        }
        cbWeek.setSelectedIndex(2);//This week
    }
}
