package gui.managment.programm;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class TableButton extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    public interface TableButtonPressedHandler {
        /**
         * Called when the button is pressed.
         *
         * @param row    The row in which the button is in the table.
         * @param column The column the button is in in the table.
         */
        void onButtonPress(int row, int column);
    }

    private List<TableButtonPressedHandler> handlers;
    private Hashtable<Integer, JButton> buttons;

    public TableButton() {
        handlers = new ArrayList<TableButtonPressedHandler>();
        buttons = new Hashtable<Integer, JButton>();
    }

    /**
     * Add a slide callback handler
     *
     * @param handler
     */
    public void addHandler(TableButtonPressedHandler handler) {
        if (handlers != null) {
            handlers.add(handler);
        }
    }

    /**
     * Remove a slide callback handler
     *
     * @param handler
     */
    public void removeHandler(TableButtonPressedHandler handler) {
        if (handlers != null) {
            handlers.remove(handler);
        }
    }


    /**
     * Removes the component at that row index
     *
     * @param row The row index which was just removed
     */
    public void removeRow(int row) {
        if (buttons.containsKey(row)) {
            buttons.remove(row);
        }
    }

    /**
     * Moves the component at oldRow index to newRow index
     *
     * @param oldRow The old row index
     * @param newRow THe new row index
     */
    public void moveRow(int oldRow, int newRow) {
        if (buttons.containsKey(oldRow)) {
            JButton button = buttons.remove(oldRow);
            buttons.put(newRow, button);
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, final int row, final int column) {
        JButton button = null;
        if (buttons.containsKey(row)) {
            button = buttons.get(row);
        } else {
            button = new JButton();
            if (value != null && value instanceof String) {
                button.setText((String) value);
            }
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (handlers != null) {
                        for (TableButtonPressedHandler handler : handlers) {
                            handler.onButtonPress(row, column);
                        }
                    }
                }
            });
            buttons.put(row, button);
        }

        return button;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int column) {
        JButton button = null;
        if (buttons.containsKey(row)) {
            button = buttons.get(row);
        } else {
            button = new JButton();
            if (value != null && value instanceof String) {
                button.setText((String) value);
            }

            buttons.put(row, button);
        }

        return button;
    }

    /**
     * Setts button text
     *
     * @param row  row
     * @param text String text
     */
    public void setButtonText(int row, String text) {
        JButton button = null;
        if (buttons.containsKey(row)) {
            button = buttons.get(row);
            button.setText(text);
        }
    }

    public Object getCellEditorValue() {
        return null;
    }

    public void dispose() {
        if (handlers != null) {
            handlers.clear();
        }
    }
}

