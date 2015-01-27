package gui.frontend.prog;

import db.DefaultManager;
import engine.Hall;
import engine.ObjectFilter;
import engine.Show;
import engine.accounts.Client;
import engine.custom_managers.ShowManager;
import engine.sorting.Sorter;
import gui.frontend.UserFrame;
import javafx.util.Pair;
import logger.InLogger;
import utils.ComboItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;

/**
 * ProgTab orginizes tickets data
 */
public class ProgTab extends JPanel {
    UserFrame context;
    JPanel listHolder;
    JPanel topPanel;
    JComboBox<ComboItem<String>> cbOrder = new JComboBox<>();
    JComboBox<ComboItem<Pair>> cbWeek = new JComboBox<>();
    JCheckBox cbActive;
    ButtonGroup hallSelectionGrop;
    OnFilteringChange filteringChange = new OnFilteringChange();
    private ShowManager showManager;
    private DefaultManager<Hall> halls;
    private Client myClient;

    /**
     * @param context Context of parent for interconnectivity
     */
    public ProgTab(UserFrame context, DefaultManager<Hall> halls, ShowManager showManager) {
        this.context = context;
        myClient = context.getUser();
        this.halls = halls;
        this.showManager = showManager;
        InLogger.info("tab created");
        initLayout();
        initListeners();
        refreshShowList();
    }

    /**
     * Initializing listeners
     */
    private void initListeners() {
        cbWeek.addActionListener(filteringChange);
        cbActive.addActionListener(filteringChange);
        cbOrder.addActionListener(filteringChange);
    }

    /**
     * Loads events from ContentManager in corrispondence of selected week
     */
    private void refreshShowList() {
        InLogger.info("Refreshing showList");
        Pair<LocalDate, LocalDate> interval = ((ComboItem<Pair>) cbWeek.getSelectedItem()).getValue();
        InLogger.info("Selected interval: " + interval);

        ObjectFilter filter = getShowFilter();
        ArrayList<Show> filteredShows = showManager.getInInterval(interval.getKey(), interval.getValue(), filter);

        listHolder.removeAll();
        ArrayList<ArrayList<Show>> byDayList = separateDays(filteredShows);


        //GUI day by day painting
        JPanel ipan;
        for (ArrayList<Show> subList : byDayList) {
            ipan = new JPanel();
            ipan.setBorder(BorderFactory.createTitledBorder(subList.get(0).getDate().format(DateTimeFormatter.ofPattern("dd/MM/YYYY"))));
            ipan.setLayout(new GridLayout(0, 4, 2, 2));
            listHolder.add(ipan);
            Sorter<Show> sort = new Sorter<>();
            for (Show finalShow : subList) {
                sort.add(finalShow);
            }
            sort.sort(Sorter.ASC);
            for (Show sorted : sort) {
                ipan.add(new SingleShowView(this, sorted, showManager));
            }
        }

        if (byDayList.size() == 0) { //Nothing in GUI
            JLabel lbNoResults = new JLabel("Nessun risultato per questi criteri");
            lbNoResults.setVerticalAlignment(SwingConstants.CENTER);
            JPanel pnNoResult = new JPanel();
            pnNoResult.add(lbNoResults);
            listHolder.add(pnNoResult);
        }
        revalidate();
        repaint();
    }

    /**
     * Separates array list in groups day-by-day
     *
     * @param filteredShows initial list
     * @return date sorted two-layered arrayList
     */
    private ArrayList<ArrayList<Show>> separateDays(ArrayList<Show> filteredShows) {
        ArrayList<ArrayList<Show>> byDayList = new ArrayList<>(); //ArrayList contains only days
        ArrayList<Show> day = new ArrayList<>();
        LocalDate prevDate = null;
        String sortCriteria = ((ComboItem<String>) cbOrder.getSelectedItem()).getValue();
        Collections.sort(filteredShows); //Pre sorting by DATE
        for (Show s : filteredShows) {
            if (prevDate == null || !prevDate.equals(s.getDate().toLocalDate())) {
                day = new ArrayList<>();
                byDayList.add(day);
            }
            prevDate = s.getDate().toLocalDate();
            s.setSortElement(sortCriteria);
            day.add(s);
        }
        return byDayList;
    }

    /**
     * Creates filter in function of GUI settings like Hall Sorting and others
     *
     * @return ShowFilter filtering lambda function
     */
    private ObjectFilter getShowFilter() {
        int selectedHall = Integer.parseInt(hallSelectionGrop.getSelection().getActionCommand());
        InLogger.info("Sort by hall (-1 = all): " + selectedHall);
        InLogger.info("Only book: " + cbActive.isSelected());
        return (show) -> {
            Show obj = (Show) show;
            if ((selectedHall == -1) || (obj.getHall().getNumber() == selectedHall)) {
                if (cbActive.isSelected() == false || obj.getDate().compareTo(LocalDateTime.now()) > 0) {
                    return true;
                }
            }
            return false;
        };
    }


    /**
     * Layout Creator
     */
    private void initLayout() {
        setLayout(new BorderLayout());
        topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        //Week selector
        topPanel.add(new JLabel("Sett: "));
        topPanel.add(cbWeek);
        loadWeekInterval();

        //Halls selector
        loadHallsSelector();

        //Ordering filter
        cbOrder.addItem(new ComboItem<String>("orario", "date"));
        cbOrder.addItem(new ComboItem<String>("sala crescente", "hall"));
        cbOrder.addItem(new ComboItem<String>("titolo", "title"));
        topPanel.add(new JLabel("Ordina per: "));
        topPanel.add(cbOrder);

        //Active or not
        cbActive = new JCheckBox("Ancora fruibili");
        topPanel.add(cbActive);

        //General layout parts
        add(topPanel, BorderLayout.NORTH);
        listHolder = new JPanel();
        listHolder.setLayout(new BoxLayout(listHolder, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listHolder, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Loads layout for hallSelector
     */
    private void loadHallsSelector() {
        //Hall selector
        hallSelectionGrop = new ButtonGroup();
        JPanel jplRadio = new JPanel();
        //jplRadio.setBackground(new Color(170, 170, 170));
        jplRadio.setLayout(new GridLayout(1, 0));
        jplRadio.add(new JLabel("Sale:"));
        JRadioButton jrbAll = new JRadioButton("Tutte");
        jrbAll.setActionCommand("-1");
        jrbAll.setSelected(true);
        jrbAll.addActionListener(filteringChange);
        hallSelectionGrop.add(jrbAll);
        jplRadio.add(jrbAll);
        for (Hall h : halls) {
            JRadioButton jrbItem = new JRadioButton("#" + (h.getNumber() + 1));
            jrbItem.setActionCommand("" + h.getNumber());
            jrbItem.addActionListener(filteringChange);
            hallSelectionGrop.add(jrbItem);
            jplRadio.add(jrbItem);
        }
        topPanel.add(jplRadio);
    }

    /**
     * Loads week based JCombobox
     */
    private void loadWeekInterval() {
        LocalDate ldNow = LocalDate.now();
        if (!LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            ldNow = ldNow.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        }

        for (int i = 0; i < 4; i++) {
            LocalDate ldNext = ldNow.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            Pair<LocalDate, LocalDate> period = new Pair<>(ldNow, ldNext);
            String caption = period.getKey().format(DateTimeFormatter.ofPattern("dd/MM"));
            caption += " - " + period.getValue().minusDays(1).format(DateTimeFormatter.ofPattern("dd/MM"));
            cbWeek.addItem(new ComboItem<>(caption, period));
            ldNow = ldNext;
        }
    }

    public Client getClient() {
        return myClient;
    }

    /**
     * On filtering option change listener
     */
    class OnFilteringChange implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshShowList();
        }
    }
}
