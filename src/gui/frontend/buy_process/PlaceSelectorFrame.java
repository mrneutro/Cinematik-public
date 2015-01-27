package gui.frontend.buy_process;

import engine.Place;
import engine.Position;
import engine.Show;
import engine.accounts.User;
import gui.Icons;
import logger.InLogger;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Place selector frame
 */
public class PlaceSelectorFrame<T extends ActionCallable> extends JFrame {
    private Show show;
    private User user;
    protected T context;
    private String title;
    JPanel grid;

    public PlaceSelectorFrame(User user, T context, Show show, String title) throws HeadlessException {
        this.show = show;
        this.user = user;
        this.context = context;
        this.title = title;

        setIconImage(new ImageIcon(Icons.appIcon).getImage());
        setTitle("Seleziona il posto nella sala " + (show.getHall().getNumber() + 1) + " - Cinematik");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        initLayout();

        Utils.centreWindow(this);
    }

    public User getUser() {
        return user;
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel(this.title, SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setVerticalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);
        grid = new JPanel();
        InLogger.info("Create grid for h: " + show.getHall().getCols() + "w: " + show.getHall().getRows());
        grid.setLayout(new GridLayout(show.getHall().getCols() + 1, show.getHall().getRows() + 1, -2, -2));
        add(grid, BorderLayout.CENTER);
        JPanel pnLegend = new JPanel();
        pnLegend.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton freeBtn = new JButton("Disponibile");
        freeBtn.setBackground(new Color(144, 238, 144));
        JButton disabledBtn = new JButton("Non disponibile");
        disabledBtn.setBackground(new Color(170, 170, 170));
        JButton reservBtn = new JButton("Prenotato");
        reservBtn.setBackground(new Color(246, 255, 118));
        JButton busyBtn = new JButton("Occupato");
        busyBtn.setBackground(new Color(255, 113, 103));
        pnLegend.add(new JLabel("Legenda:"));
        pnLegend.add(freeBtn);
        pnLegend.add(reservBtn);
        pnLegend.add(disabledBtn);
        pnLegend.add(busyBtn);

        add(pnLegend, BorderLayout.SOUTH);
        createPositions();
    }

    /**
     * Creates position buttons and makes status
     */
    private void createPositions() {
        for (int i = -1; i < show.getHall().getCols(); i++) {
            for (int j = -1; j < show.getHall().getRows(); j++) {
                if (i == -1) {
                    if (j == -1) {
                        grid.add(new JLabel("-/-", SwingConstants.CENTER));
                    } else {
                        grid.add(new JLabel("Posto " + (j + 1), SwingConstants.CENTER));
                    }
                } else if (j == -1) {
                    grid.add(new JLabel("Fila: " + (i + 1), SwingConstants.CENTER));
                } else {
                    PlaceButton placeButton;
                    Position check = show.getHall().getPositions()[j][i]; //[W][H]
                    Place np = new Place(j, i);
                    if (check != null) {
                        switch (check.getStatus()) {
                            case BUSY:
                                placeButton = new PlaceButton(np, this, PlaceButton.States.BUSY);
                                break;
                            case DISABLED:
                                placeButton = new PlaceButton(np, this, PlaceButton.States.DISABLED);
                                break;
                            case RESERVED:
                                placeButton = new PlaceButton(np, this, PlaceButton.States.RESERV);
                                break;
                            default:
                                placeButton = new PlaceButton(np, this, PlaceButton.States.FREE);
                        }
                    } else {
                        placeButton = new PlaceButton(np, this, PlaceButton.States.FREE);
                    }
                    grid.add(placeButton);
                }
            }
        }

    }

    /**
     * Calls starting elaboration by context
     *
     * @param pos position of click
     */
    public void selectPosition(Place pos) {
        context.callStart(pos);
        closeWindow();
    }

    private void closeWindow() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}
