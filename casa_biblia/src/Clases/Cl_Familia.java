/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author luis-d
 */
public class Cl_Familia {

    Cl_Conectar con = new Cl_Conectar();
    private int id;
    private String descripcion;

    public Cl_Familia() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void ver_familia(JComboBox cb_familia) {
        cb_familia.removeAllItems();
        try {
            Statement st = con.conexion();
            String c_familia = "select * from familia_productos order by id asc";
            ResultSet rs = con.consulta(st, c_familia);
            while (rs.next()) {
                cb_familia.addItem(rs.getString("descripcion"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public boolean crear_familia() {
        boolean creado = false;
        int resultado;
        Statement st = con.conexion();
        String i_familia = "insert into familia_productos values (null, '" + descripcion + "')";
        resultado = con.actualiza(st, i_familia);
        if (resultado > -1) {
            creado = true;
        } else {
            JOptionPane.showMessageDialog(null, "ERROR AL INSERTAR");
        }
        con.cerrar(st);
        return creado;
    }

    public boolean modificar_familia() {
        boolean creado = false;
        int resultado;
        Statement st = con.conexion();
        String a_familia = "update familia_productos set descricion = '" + descripcion + "' where id = '" + id + "'";
        resultado = con.actualiza(st, a_familia);
        if (resultado > -1) {
            creado = true;
        } else {
            JOptionPane.showMessageDialog(null, "ERROR AL MODIFICAR");
        }
        con.cerrar(st);
        return creado;
    }
    
    public boolean eliminar_familia() {
        boolean creado = false;
        int resultado;
        Statement st = con.conexion();
        String e_familia = "delete from familia_productos where id = '" + id + "'";
        resultado = con.actualiza(st, e_familia);
        if (resultado > -1) {
            creado = true;
        } else {
            JOptionPane.showMessageDialog(null, "ERROR AL ELIMINAR");
        }
        con.cerrar(st);
        return creado;
    }

}
