package gui.managment.films;

import db.DefaultManager;
import engine.Film;
import gui.Icons;
import logger.InLogger;
import utils.PatternHolder;
import utils.PatternInputVerifier;
import utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Film creation/Editing frame
 */
public class FilmEditForm extends JFrame {
    JPanel centerPanel;
    JTextField tfTitle = new JTextField(20);
    JTextField tfYear = new JTextField(20);
    JTextField tfLength = new JTextField(20);
    JLabel lbError = new JLabel("", JLabel.CENTER);
    JTextArea tfDescr = new JTextArea();
    JButton btnChooseImage = new JButton("Seleziona...");
    JLabel lbImage = new JLabel();
    BufferedImage bufferedImage = null;
    JButton btnConfirm = new JButton("Salva");
    JButton btnCancel = new JButton("Anulla");
    DefaultManager<Film> films;
    private Film editFilm;
    private boolean reloadImage = true;
    private boolean creation = false;
    private int filmId;

    public FilmEditForm(DefaultManager<Film> films) throws HeadlessException {
        InLogger.info(getClass().getName() + " created. Action: creating");
        InLogger.info("Films passed: " + films);
        creation = true;
        this.films = films;
        startForm("Aggiunta del nuovo film - Cinematik");
    }

    /**
     * Start frame
     *
     * @param title title of frame
     */
    private void startForm(String title) {
        initLayout();
        initListeners();
        Utils.centreWindow(this);
        Utils.setUIFont(new javax.swing.plaf.FontUIResource("Tahoma", Font.PLAIN, 13));
        setTitle(title);
    }

    public FilmEditForm(Film editFilm, DefaultManager<Film> films) {
        InLogger.info(getClass().getName() + " created. Action: editing");
        InLogger.info("Films passed: " + films);
        InLogger.info("Editing film id: " + editFilm);
        this.films = films;
        this.editFilm = editFilm;
        filmId = films.indexOf(editFilm);
        InLogger.info("Film id: " + filmId);
        startForm(editFilm.getTitle() + " - Modifica - Cinematik");

        tfTitle.setText(editFilm.getTitle());
        tfDescr.setText(editFilm.getDescription());
        tfYear.setText(editFilm.getYear());
        lbImage.setIcon(editFilm.getImg());
        tfLength.setText("" + editFilm.getLength());
        reloadImage = false;
    }

    private void initLayout() {
        setIconImage(new ImageIcon(Icons.appIcon).getImage());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        lbError.setForeground(Color.red);
        add(lbError, BorderLayout.NORTH);
        tfDescr.setBorder(new EtchedBorder());
        tfDescr.setRows(5);
        tfDescr.setLineWrap(true);
        centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Titolo: "), cs);

        cs.gridx = 1;
        cs.gridwidth = 2;
        centerPanel.add(tfTitle, cs);

        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 3;
        centerPanel.add(new JPanel(), cs);

        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Anno: "), cs);

        cs.gridx = 1;
        cs.gridwidth = 2;
        centerPanel.add(tfYear, cs);

        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 3;
        centerPanel.add(new JPanel(), cs);

        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Descrizione: "), cs);

        cs.gridx = 1;
        cs.gridwidth = 2;
        centerPanel.add(tfDescr, cs);

        cs.gridx = 0;
        cs.gridy = 5;
        cs.gridwidth = 3;
        centerPanel.add(new JPanel(), cs);

        cs.gridx = 0;
        cs.gridy = 6;
        cs.gridwidth = 1;

        centerPanel.add(new JLabel("Lunghezza (min): "), cs);

        cs.gridx = 1;
        cs.gridwidth = 2;

        centerPanel.add(tfLength, cs);

        cs.gridx = 0;
        cs.gridy = 7;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Immagine: "), cs);

        cs.gridx = 1;
        cs.gridwidth = 1;
        centerPanel.add(btnChooseImage, cs);
        cs.gridx = 2;
        cs.gridwidth = 1;
        centerPanel.add(lbImage, cs);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(btnConfirm);
        btnConfirm.setIcon(new ImageIcon(Icons.save));
        bp.add(btnCancel);
        btnCancel.setIcon(new ImageIcon(Icons.cancel));
        add(bp, BorderLayout.PAGE_END);
        setSize(500, 400);
    }

    private void initListeners() {
        tfTitle.setInputVerifier(new PatternInputVerifier(PatternHolder.NAME, "Titolo deve essere corretto", lbError));
        tfYear.setInputVerifier(new PatternInputVerifier(PatternHolder.YEAR, "Anno non valido, es. 2011", lbError));
        tfLength.setInputVerifier(new PatternInputVerifier(PatternHolder.FLENGTH, "Lunghezza film non valida", lbError));
        btnChooseImage.addActionListener(e -> {
            chooseImage();
        });
        btnConfirm.addActionListener(e -> {
            if (creation) {
                confirmAdding();
            } else {
                confirmSaving();
            }
        });
        btnCancel.addActionListener(e -> {
            setVisible(false);
        });
    }

    /**
     * Confirms editing
     */
    private void confirmSaving() {
        try {
            Film film = new Film(tfTitle.getText(), tfDescr.getText(), tfLength.getText(), (reloadImage == true ? new ImageIcon(Utils.getScaledImage(bufferedImage, 90, 90)) : editFilm.getImg()), tfYear.getText());
            InLogger.info("updating film: " + film);
            films.set(filmId, film);
            FilmTab.context.refreshFilmList();
            setVisible(false);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante l'inserimento del film", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore input/output. Impossibile proseguire.", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Adds new film to manager
     */
    private void confirmAdding() {
        try {
            Film film = new Film(tfTitle.getText(), tfDescr.getText(), tfLength.getText(), (reloadImage == true ? new ImageIcon(Utils.getScaledImage(bufferedImage, 90, 90)) : editFilm.getImg()), tfYear.getText());
            films.add(film);
            FilmTab.context.refreshFilmList();
            setVisible(false);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante l'inserimento del film", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore input/output. Impossibile proseguire.", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Image loader
     */
    private void chooseImage() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.addChoosableFileFilter(new FileNameExtensionFilter("File di immagini", "jpg", "png", "tif"));
            fc.setAcceptAllFileFilterUsed(false);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                bufferedImage = ImageIO.read(f);
                reloadImage = true;
                lbImage.setIcon(new ImageIcon(Utils.getScaledImage(bufferedImage, 90, 90)));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error nel caricamento del file", "Scegli un'altro file", JOptionPane.ERROR_MESSAGE);
        }

    }

}
