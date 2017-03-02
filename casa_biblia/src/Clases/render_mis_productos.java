/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author lubricante
 */
public class render_mis_productos extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        // SI EN CADA FILA DE LA TABLA LA CELDA 5 ES IGUAL A ACTIVO COLOR AZUL
        if (String.valueOf(table.getValueAt(row, 9)).equals("POR TERMINAR")) {
            setBackground(Color.green);
            setForeground(Color.black);
        }
        if (String.valueOf(table.getValueAt(row, 9)).equals("NO DISPONIBLE")) {
            setBackground(Color.red);
            setForeground(Color.white);
        }
        if (String.valueOf(table.getValueAt(row, 9)).equals("NORMAL")) {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        if (column == 0) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        if (column == 1) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (column == 2) {
            setHorizontalAlignment(SwingConstants.LEFT);
        }
        if (column == 3) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (column == 4) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (column == 5) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (column == 6) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        if (column == 7) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (column == 8) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        if (column == 9) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        return this;
    }

}
