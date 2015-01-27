package gui.frontend.buy_process;

import engine.Place;

import javax.swing.*;
import java.awt.*;

/**
 * This is a position holder on steroids,
 * takes positions
 */
public class PlaceButton extends JButton {
    private Place position;
    private PlaceSelectorFrame context;

    private States state;

    public static enum States {
        FREE, BUSY, RESERV, DISABLED
    }

    public PlaceButton(Place position, PlaceSelectorFrame context, States state) {
        this.position = position;
        this.context = context;
        this.state = state;
        setFont(new Font("Arial", Font.BOLD, 11));
        setText("" + (position.getRow() + 1) + "/" + (position.getCol() + 1));
        if (state == States.BUSY) {
            setBackground(new Color(255, 113, 103));
        } else if (state == States.RESERV) {
            setBackground(new Color(246, 255, 118));
        } else if (state == States.DISABLED) {
            setBackground(new Color(170, 170, 170));
        } else {
            setBackground(new Color(144, 238, 144));
        }
        this.addActionListener(e -> {
            switch (state) {
                case FREE:
                    context.selectPosition(position);
                    break;
                case BUSY:
                    JOptionPane.showMessageDialog(this, "Questo posto è già acquistato!", "Errore", JOptionPane.ERROR_MESSAGE);
                    break;
                case DISABLED:
                    if (context.getUser().isAdmin() == false) {
                        JOptionPane.showMessageDialog(this, "Questo posto è indisponibile!", "Errore", JOptionPane.ERROR_MESSAGE);
                    } else {
                        context.selectPosition(position);
                    }
                    break;
                case RESERV:
                    JOptionPane.showMessageDialog(this, "Questo posto è prenotato, se il cliente non conferma l'acquisto 12 ore priva - posto diventerà libero", "Errore", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    throw new IllegalArgumentException("Err switch state");

            }

        });
    }

}
