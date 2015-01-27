package gui.managment.films;

import db.ContentProvider;
import engine.custom_managers.FilmManager;
import engine.sorting.Sorter;
import gui.Icons;
import logger.InLogger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Main film tab
 */
public class FilmTab extends JPanel {
    private FilmManager films = new FilmManager();
    private ContentProvider cp;
    JButton addButton;
    JPanel listHolder;
    JScrollPane scrollPane;
    JComboBox<String> orderBy = new JComboBox<>();
    public static FilmTab context;
    private int filmSortDirection = Sorter.ASC;
    private String filmSortCriteria = "data";

    public FilmTab() {
        try {
            cp = new ContentProvider();
            films.setSource(cp.getFilms());

            context = this;
            InLogger.info("film tab created");

            initLayout();
            initListeners();
            refreshFilmList();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante il caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refresh list after sorting settings change
     */
    public void refreshFilmList() {
        InLogger.info("Refreshing film getList");
        listHolder.removeAll();
        if (films.size() > 0) {
            Sorter<FilmItem> sorter = new Sorter<>();
            for (int i = 0; i < films.size(); i++) {
                FilmItem f = new FilmItem(films, i);
                f.setSortElement(filmSortCriteria);
                sorter.add(f);
            }
            sorter.sort(filmSortDirection);
            sorter.forEach(listHolder::add);
            revalidate();
        }
    }

    private void initListeners() {
        addButton.addActionListener(e -> {
            InLogger.info("Film add button clicked");
            FilmEditForm fap = new FilmEditForm(films);
            fap.setVisible(true);
        });
        orderBy.addActionListener(e -> {
            switch (orderBy.getSelectedIndex()) {
                case 0: {
                    filmSortCriteria = "data";
                    filmSortDirection = Sorter.ASC;
                }
                break;
                case 1: {
                    filmSortCriteria = "data";
                    filmSortDirection = Sorter.DESC;
                }
                break;
                case 2: {
                    filmSortCriteria = "title";
                    filmSortDirection = Sorter.ASC;
                }
                break;
                case 3: {
                    filmSortCriteria = "title";
                    filmSortDirection = Sorter.DESC;
                }
                break;
            }
            refreshFilmList();
        });
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(new JLabel("Ordina per: "));

        orderBy.addItem("Data creazione disc.");
        orderBy.addItem("Data creazione cres.");
        orderBy.addItem("Nome cres.");
        orderBy.addItem("Nome disc.");

        topPanel.add(orderBy);
        addButton = new JButton("Aggiungi nuovo film");
        addButton.setIcon(new ImageIcon(Icons.addIcon));
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);

        //Add filmList
        listHolder = new JPanel();
        scrollPane = new JScrollPane(listHolder, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listHolder.setLayout(new BoxLayout(listHolder, BoxLayout.Y_AXIS));
        add(scrollPane, BorderLayout.CENTER);
    }
}
