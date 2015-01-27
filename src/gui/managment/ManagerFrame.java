package gui.managment;

import db.ContentProvider;
import engine.accounts.Admin;
import gui.Icons;
import gui.managment.analytics.AnalyticTab;
import gui.managment.films.FilmTab;
import gui.managment.halls.HallTab;
import gui.managment.programm.ProgrammTab;
import logger.InLogger;
import utils.Utils;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main manager Frame
 */
public class ManagerFrame extends JFrame {
    private Admin user;
    private ContentProvider cp;
    JPanel btmPanel;
    JLabel timeLabel = new JLabel();
    private Timer moveBallTimer;

    public ManagerFrame(Admin admin) throws HeadlessException {
        try {
            cp = new ContentProvider();
            user = admin;
            Utils.setUIFont(new javax.swing.plaf.FontUIResource("Tahoma", Font.PLAIN, 13));
            setSize(788, 500);
            initLayout();
            initListeners();
            Utils.centreWindow(this);
            moveBallTimer.start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore lettura dal file", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Admin getUser() {
        return user;
    }

    private void initListeners() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    InLogger.info("Saving to file all data");
                    cp.flush();
                } catch (IOException ex) {
                    InLogger.exception(ex.toString());
                }
            }
        });

        moveBallTimer = new Timer(1000, e -> {
            refreshTimer();
        });
    }

    private void initLayout() {
        setIconImage(new ImageIcon(Icons.appIcon).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manager Panel - Cinematik - " + user.getName());
        setLayout(new BorderLayout(2, 2));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Sale ", new ImageIcon(Icons.halls), new HallTab());
        tabbedPane.addTab("Film ", new ImageIcon(Icons.films), new FilmTab());
        tabbedPane.addTab("Programmazione ", new ImageIcon(Icons.programm), new ProgrammTab(user));
        tabbedPane.addTab("Analisi ", new ImageIcon(Icons.chart), new AnalyticTab());

        add(tabbedPane, BorderLayout.CENTER);

        btmPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btmPanel.setBorder(new BorderUIResource.EtchedBorderUIResource());

        btmPanel.add(timeLabel);

        add(btmPanel, BorderLayout.SOUTH);

        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
    }

    /**
     * Bottom timer refresh
     */
    private void refreshTimer() {
        timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/YYY")));
    }
}
