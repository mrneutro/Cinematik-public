package gui.managment.halls;

import engine.Hall;

import javax.swing.*;
import java.awt.*;

/**
 * Hall item button
 */
public class HallItem extends JButton {
    private Hall hall;
    private int hallId;

    public HallItem(int hallId, Hall hall) {
        this.hall = hall;
        this.hallId = hallId;
        setText("<html><center><b>SALA N." + (hallId + 1) + "</b><br/><font size=-1>Posti per fila: " + hall.getRows() + "<br>File: " + hall.getCols() + "</font></center></html>");
        setFont(new Font("Tahoma", Font.PLAIN, 30));
    }

    public Hall getHall() {
        return hall;
    }

    public int getHallId() {
        return hallId;
    }
}
