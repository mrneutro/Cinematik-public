package gui.frontend;

import db.ContentProvider;
import db.DefaultManager;
import engine.Hall;
import engine.accounts.AccountManager;
import engine.accounts.Client;
import engine.custom_managers.ShowManager;
import gui.Icons;
import gui.frontend.prog.ProgTab;
import gui.frontend.tickets.TicketsTab;
import logger.InLogger;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Main user frame, inititalize managers and makes tabs
 */
public class UserFrame extends JFrame {
    private Client client;
    private AccountManager accountManager;
    private ContentProvider cp;
    private DefaultManager<Hall> hallManager = new DefaultManager<>();
    private ShowManager showManager;

    JButton btnLogout;
    ProgTab pt;
    TicketsTab tt;


    public UserFrame(AccountManager accountManager, Client currentClient) throws HeadlessException {
        try {
            this.accountManager = accountManager;
            cp = new ContentProvider();
            client = currentClient;
            hallManager.setSource(cp.getHalls());
            showManager = new ShowManager();
            initLayout();
            initListeners();
            setSize(850, 500);
            Utils.centreWindow(this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore durante il caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * OnClose listener for flushing all data to file
     */
    private void initListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                InLogger.info("Saving all data to file");
                try {
                    cp.flush();
                } catch (IOException ex) {
                    InLogger.exception(ex.toString());
                }
            }
        });
    }

    private void initLayout() {
        setIconImage(new ImageIcon(Icons.appIcon).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cinematic - Area utente - " + client.getEmail());

        setLayout(new BorderLayout());
        //TOP USER PANEL
        JPanel topUserPanel = new JPanel(new BorderLayout());
        topUserPanel.setBorder(new EtchedBorder());
        JLabel picLabel = new JLabel(new ImageIcon(Icons.user)); //LEFT ICON
        topUserPanel.add(picLabel, BorderLayout.WEST);

        JLabel lbUserName = new JLabel("Benvenuti, " + client.getName());
        {
            lbUserName.setFont(new Font("Tahoma", Font.BOLD, 25));
            lbUserName.setForeground(new Color(37, 73, 128));
        }
        JPanel topNameEmail = new JPanel();
        {
            topNameEmail.setLayout(new BoxLayout(topNameEmail, BoxLayout.Y_AXIS));
            topNameEmail.add(lbUserName);
        }
        JLabel lbEmail = new JLabel(client.getEmail());
        {
            lbEmail.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0)); //It was a little bit askew XD
            lbEmail.setFont(new Font("Tahoma", Font.BOLD, 17));
            lbEmail.setForeground(new Color(84, 84, 84));
        }
        topNameEmail.add(lbEmail);


        topUserPanel.add(topNameEmail, BorderLayout.CENTER);
        add(topUserPanel, BorderLayout.NORTH);

        //MAIN USERSPACE
        JTabbedPane tabbedPane = new JTabbedPane();

        pt = new ProgTab(this, hallManager, showManager);
        tt = new TicketsTab(this, hallManager, showManager);

        tabbedPane.addTab("Programma generale ", new ImageIcon(Icons.calendar), pt);
        tabbedPane.addTab("Biglietti e prenotazioni ", new ImageIcon(Icons.basket), tt);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                tt.refreshList();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    public Client getUser() {
        return client;
    }
}
