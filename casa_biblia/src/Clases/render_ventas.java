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
public class render_ventas extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        // SI EN CADA FILA DE LA TABLA LA CELDA 5 ES IGUAL A ACTIVO COLOR AZUL
        switch (String.valueOf(table.getValueAt(row, 11)).trim()) {
            case "SEPARADO":
                setBackground(Color.green);
                setForeground(Color.black);
                break;
            case "ANULADO":
                setBackground(Color.black);
                setForeground(Color.white);
                break;
            case "POR RECOGER":
                setBackground(Color.yellow);
                setForeground(Color.black);
                break;
            case "ENTREGADO":
                setBackground(Color.pink);
                setForeground(Color.blue);
                break;
            case "POR COBRAR":
                setBackground(Color.red);
                setForeground(Color.white);
                break;
            default:
                setBackground(Color.white);
                setForeground(Color.black);
                break;
        }
        if (column == 0) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (column == 1) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (column == 2) {
            setHorizontalAlignment(SwingConstants.CENTER);
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
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (column == 7) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (column == 8) {
            setHorizontalAlignment(SwingConstants.LEFT);
        }
        if (column == 9) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        if (column == 10) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        if (column == 11) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        return this;
    }

}
