package gui.managment.halls;

import db.DefaultManager;
import engine.Hall;
import gui.Icons;
import utils.PatternHolder;
import utils.PatternInputVerifier;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Hall edit/create frame
 */
public class HallEditForm extends JFrame {
    private DefaultManager<Hall> halls;
    private int hallId;
    private Hall editHall = null;
    private HallTab context;
    private boolean creation = false;
    JPanel centerPanel;
    JButton btnSave;
    JButton btnExit;
    JTextField tfHeight;
    JTextField tfWidth;
    JLabel lbError = new JLabel("", JLabel.CENTER);


    public HallEditForm(HallTab context, DefaultManager<Hall> halls, int hallId) {
        this.context = context;
        this.halls = halls;
        this.hallId = hallId;
        editHall = halls.get(hallId);
        setTitle((editHall.getNumber() + 1) + " - modifica sala - Cinematic");
        initLayout();
        initListeners();
        tfHeight.setText("" + editHall.getCols());
        tfWidth.setText("" + editHall.getRows());
    }

    public HallEditForm(HallTab context, DefaultManager<Hall> halls) throws HeadlessException {
        this.context = context;
        this.hallId = halls.size();
        this.halls = halls;
        creation = true;
        setTitle("Creazione sala - Cinematic");
        initLayout();
        initListeners();
    }

    private void initListeners() {
        tfWidth.setInputVerifier(new PatternInputVerifier(PatternHolder.NUMBER, "Quantita file errato", lbError));
        tfHeight.setInputVerifier(new PatternInputVerifier(PatternHolder.NUMBER, "Quantita posti per fila errato", lbError));
        btnSave.addActionListener(e -> {
            onClickSave();
        });
        btnExit.addActionListener(e -> {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

    }

    /**
     * On click save, if hall exists - updates data, otherwise creates new ones
     */
    private void onClickSave() {
        try {
            int hallCols = Integer.parseInt(tfWidth.getText());
            int hallRows = Integer.parseInt(tfHeight.getText());
            Hall newHall = new Hall(hallId, hallCols, hallRows);
            if (creation) {
                halls.add(newHall);
            } else {
                halls.set(hallId, newHall);
            }
            context.refreshList();
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore nella creazione", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore input/output. Impossibile proseguire.", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void initLayout() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        add(lbError, BorderLayout.NORTH);

        centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel picLabel = new JLabel(new ImageIcon(Icons.halls_xl));
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 3;
        centerPanel.add(picLabel, cs);

        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 2;
        JLabel lbHallNo = new JLabel("Sala numero " + (hallId + 1));
        lbHallNo.setFont(new Font("Tahoma", Font.BOLD, 15));
        centerPanel.add(lbHallNo, cs);

        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        JPanel dv = new JPanel();
        dv.setSize(10, 10);
        centerPanel.add(dv, cs);

        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Numero file: "), cs);

        tfHeight = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 3;
        cs.gridwidth = 2;
        centerPanel.add(tfHeight, cs);

        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 1;
        centerPanel.add(new JLabel("Posti per fila: "), cs);

        tfWidth = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 4;
        cs.gridwidth = 2;
        centerPanel.add(tfWidth, cs);

        centerPanel.setBorder(new LineBorder(Color.GRAY));

        btnSave = new JButton("Salva");
        btnSave.setIcon(new ImageIcon(Icons.save));
        btnExit = new JButton("Esci");
        btnExit.setIcon(new ImageIcon(Icons.cancel));

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(btnSave);
        bp.add(btnExit);


        add(centerPanel, BorderLayout.CENTER);
        add(bp, BorderLayout.PAGE_END);
        setSize(400, 300);
        Utils.centreWindow(this);
    }

}
