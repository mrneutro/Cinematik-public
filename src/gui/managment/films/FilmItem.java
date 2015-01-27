package gui.managment.films;

import engine.Film;
import engine.custom_managers.FilmManager;
import engine.sorting.Sorting;
import gui.Icons;
import logger.InLogger;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Film item panel
 */
public class FilmItem extends JPanel implements Sorting {
    private FilmManager films;
    private Film film;
    JPanel centerPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JLabel lblYear = new JLabel();
    JLabel titleLabel = new JLabel();
    JButton btnEdit = new JButton("Modifica");
    JButton btnDelete = new JButton("Cancella");
    JTextArea contentArea = new JTextArea();
    JLabel imgLabel = new JLabel();
    Comparable sortingElement;

    public FilmItem(FilmManager films, int inx) {
        this.film = films.get(inx);
        this.films = films;
        setLayout(new BorderLayout());
        initLayout();
        initListeners();
        InLogger.info("created with: " + film);
    }

    private void initListeners() {
        btnEdit.addActionListener(e -> {
            FilmEditForm filmCreateEditForm = new FilmEditForm(film, films);
            filmCreateEditForm.setVisible(true);
        });
        btnDelete.addActionListener(e -> {
            try {
                films.remove(film);
                setVisible(false);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore cancellazione", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Loads details in UI
     */
    private void readFilmDetails() {
        titleLabel.setText(film.getTitle());
        contentArea.setText(film.getDescription());
        imgLabel.setIcon(film.getImg());
        lblYear.setText("" + film.getYear());
        InLogger.info("reading film " + film);
    }

    private void initLayout() {
        setMaximumSize(new Dimension(9000, 90)); //Crazy code

        setBorder(new LineBorder(Color.gray, 1));
        //IMAGE LEFT
        add(imgLabel, BorderLayout.WEST);

        //CONTENT CENTER
        centerPanel.setLayout(new BorderLayout());
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
        contentArea.setBorder(BorderFactory.createEmptyBorder());
        contentArea.setLineWrap(true);
        contentArea.setBackground(new Color(214, 217, 223));
        JScrollPane scroll = new JScrollPane(contentArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(contentArea, BorderLayout.CENTER);
        contentArea.add(scroll, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnEdit.setSize(150, 10);
        btnEdit.setIcon(new ImageIcon(Icons.edit));
        btnDelete.setIcon(new ImageIcon(Icons.cancel));
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        // centerPanel.add(actionPanel, BorderLayout.SOUTH);

        //LEFT YEAR
        lblYear.setFont(new Font("Tahoma", Font.BOLD, 27));
        lblYear.setForeground(new Color(39, 73, 98));
        lblYear.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.add(lblYear);
        leftPanel.add(btnDelete);
        leftPanel.add(btnEdit);

        add(centerPanel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.EAST);

        readFilmDetails();
    }

    public LocalDateTime getDate() {
        return film.getDate();
    }

    public String getTitle() {
        return film.getTitle();
    }

    @Override
    public Comparable getSortElement() {
        return sortingElement;
    }

    /**
     * Sets sorting by value
     *
     * @param sortingby "title" "date"
     */
    @Override
    public void setSortElement(String sortingby) {
        switch (sortingby) {
            case "title":
                sortingElement = getTitle();
                break;
            case "date":
            default:
                sortingElement = getDate();
                break;
        }
    }
}
