package gui.managment.halls;

import db.ContentProvider;
import db.DefaultManager;
import engine.Hall;
import gui.Icons;
import logger.InLogger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Main hall tab
 */
public class HallTab extends JPanel {
    public static HallTab context;
    private DefaultManager<Hall> hallManager = new DefaultManager<>();
    private ContentProvider cp;
    JButton addButton;
    JPanel centerPanel;

    public HallTab() {
        try {
            context = this;
            InLogger.info("hall tab created");
            cp = new ContentProvider();
            hallManager.setSource(cp.getHalls());
            setLayout(new BorderLayout());
            initLayout();
            initListeners();
            refreshList();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante il caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void initListeners() {
        addButton.addActionListener(e -> {
            HallEditForm hef = new HallEditForm(this, hallManager);
            hef.setVisible(true);
        });
    }

    private void initLayout() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Aggiungi nuova sala");
        addButton.setIcon(new ImageIcon(Icons.addIcon));
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);
        centerPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Loads list of halls on UI
     */
    public void refreshList() {
        centerPanel.removeAll();
        for (int i = 0; i < hallManager.size(); i++) {
            HallItem btn = new HallItem(i, hallManager.get(i));
            btn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Modifiche applicate alle dimensioni delle sale avranno l'effetto solo per nuovi eventi!", "Attenzione!", JOptionPane.INFORMATION_MESSAGE);
                hallClick((HallItem) e.getSource());
            });
            centerPanel.add(btn);
        }
        revalidate();
        repaint();
    }

    private void hallClick(HallItem clickHall) {
        HallEditForm hef = new HallEditForm(this, hallManager, clickHall.getHallId());
        hef.setVisible(true);
    }
}
