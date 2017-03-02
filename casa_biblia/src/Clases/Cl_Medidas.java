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
 * @author pc
 */
public class Cl_Medidas {
    Cl_Conectar con = new Cl_Conectar();
    private Integer id;
    private String descripcion;

    public Cl_Medidas() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public void ver_medidas(JComboBox cb_medidas) {
        cb_medidas.removeAllItems();
        try {
            Statement st = con.conexion();
            String c_familia = "select * from unidad_medida order by id_und_med asc";
            ResultSet rs = con.consulta(st, c_familia);
            while (rs.next()) {
                cb_medidas.addItem(rs.getString("nombre"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public boolean crear_medida() {
        boolean creado = false;
        int resultado;
        Statement st = con.conexion();
        String i_medida = "insert into unidad_medida values (null, '" + descripcion + "')";
        resultado = con.actualiza(st, i_medida);
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
