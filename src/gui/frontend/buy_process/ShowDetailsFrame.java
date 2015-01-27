package gui.frontend.buy_process;

import engine.BusyPlaceException;
import engine.Place;
import engine.Show;
import engine.UnExistentPlaceException;
import engine.accounts.Client;
import engine.discount.DiscountFactory;
import engine.discount.AbstractDiscount;
import gui.Icons;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Show infos, dates ecc.
 */
public class ShowDetailsFrame extends JFrame implements ActionCallable {
    private Client client;
    private Show show;
    private AbstractDiscount discount;
    private boolean buy;
    JTextArea contentArea;
    JButton btnOrder;
    JButton btnBuy;


    public ShowDetailsFrame(Show show, Client client) throws HeadlessException {
        this.show = show;
        this.client = client;
        show.removeOutDatedReservations();
        setIconImage(new ImageIcon(Icons.appIcon).getImage());
        setTitle(show.getFilm().getTitle() + " - Cinematik");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initLayout();
        initListeners();

        setSize(350, 450);
        Utils.centreWindow(this);
    }

    private void initListeners() {
        btnOrder.addActionListener(e -> {
            buy = false;
            new PlaceSelectorFrame(client, this, show, "Clicca sul posto per prenotarlo").setVisible(true);
        });
        btnBuy.addActionListener(e -> {
            buy = true;
            new PlaceSelectorFrame(client, this, show, "Clicca sul posto per prenotarlo").setVisible(true);
        });
    }


    private void initLayout() {
        setLayout(new BorderLayout(5, 5));

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel filmPicture = new JLabel();
        filmPicture.setIcon(show.getFilm().getImg());
        imagePanel.add(filmPicture);

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        JLabel lbTitle = new JLabel(show.getFilm().getTitle());
        lbTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        dataPanel.add(lbTitle);

        dataPanel.add(new JLabel("Sala: " + (show.getHall().getNumber() + 1)));
        dataPanel.add(new JLabel("Durata: " + show.getFilm().getLength() + " min."));
        dataPanel.add(new JLabel("Inizio: " + (show.getDate().format(DateTimeFormatter.ofPattern("HH:mm")))));
        dataPanel.add(new JLabel("Prezzo regolare: " + show.getRegularCost()));
        if (show.getDiscount()) {
            discount = DiscountFactory.getDiscount(client, show);
            dataPanel.add(new JLabel("Scontato: " + discount.getPrice(show.getRegularCost())));
            dataPanel.add(new JLabel("Tipo sconto: " + discount.getName() + "(-" + discount.getPercentage() + "%)"));
        }

        contentArea = new JTextArea();
        contentArea.setText(show.getFilm().getDescription());
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        contentArea.setBorder(BorderFactory.createEmptyBorder());
        contentArea.setBackground(new Color(214, 217, 223));
        contentArea.setFont(new Font("Comic Sans", Font.ITALIC, 14));
        JScrollPane scroll = new JScrollPane(contentArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        dataPanel.add(scroll);

        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnBuy = new JButton("Acquista");
        btnOrder = new JButton("Prenota");
        btnBuy.setIcon(new ImageIcon(Icons.basketAdd));
        btnOrder.setIcon(new ImageIcon(Icons.basketEdit));

        LocalDateTime now = LocalDateTime.now();
        if (show.getDate().isBefore(now)) { //Show is outdated
            btnBuy.setEnabled(false);
            btnOrder.setEnabled(false);
        } else {
            if (show.isOutDated()) {
                btnOrder.setEnabled(false);
            }
        }

        orderPanel.add(btnBuy);
        orderPanel.add(btnOrder);
        add(orderPanel, BorderLayout.SOUTH);

        add(imagePanel, BorderLayout.NORTH);
        add(dataPanel, BorderLayout.CENTER);

    }

    /**
     * Part of calleble interface, makes ticket
     *
     * @param selectedPlace
     */
    public void callStart(Place selectedPlace) {
        try {
            int selectedOption = JOptionPane.showConfirmDialog(this,
                    "Sei sicuro di voler acquistare il biglietto \n(posto " + (selectedPlace.getCol() + 1) + "/" + (selectedPlace.getRow() + 1) + ") per '" + show.getFilm().getTitle() + "'?",
                    "Conferma l'acquisto", JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                client.makeTicket(show, selectedPlace, buy, discount);
                JOptionPane.showMessageDialog(this, "Ottimo! Lei ha ordinato il posto", "Auguri!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (UnExistentPlaceException ex) { //Impossible
            JOptionPane.showMessageDialog(this, "Siamo spiacenti, il posto non esiste", "Anulla", JOptionPane.ERROR_MESSAGE);
        } catch (BusyPlaceException ex) {
            JOptionPane.showMessageDialog(this, "Probabilmente qualcuno era più veloce di Lei", "Posto già occupato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
