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

/**
 *
 * @author CONTABILIDAD 02
 */
public class Cl_Tipo_Doc {

    Cl_Conectar con = new Cl_Conectar();
    private Integer id;
    private String desc;
    private Integer ser = 0;
    private Integer nro = 0;

    public Cl_Tipo_Doc() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSer() {
        return ser;
    }

    public void setSer(Integer ser) {
        this.ser = ser;
    }

    public Integer getNro() {
        return nro;
    }

    public void setNro(Integer nro) {
        this.nro = nro;
    }

    public void ver_tipodoc(JComboBox cb_tipodoc) {
        cb_tipodoc.removeAllItems();
        try {
            Statement st = con.conexion();
            String c_tipodoc = "select * from tipo_documento order by id_tido asc";
            ResultSet rs = con.consulta(st, c_tipodoc);
            while (rs.next()) {
                cb_tipodoc.addItem(rs.getString("nombre"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public Integer ver_serie(int tienda, String empresa) {
        int serie = 0;
        try {
            Statement st = con.conexion();
            String ver_ser = "select serie from documentos_almacenes where id_tido = '" + id + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            System.out.println(ver_ser);
            ResultSet rs = con.consulta(st, ver_ser);
            if (rs.next()) {
                serie = rs.getInt("serie");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return serie;
    }

    public Integer ver_numero(int tienda, String empresa) {
        int num = 0;
        try {
            Statement st = con.conexion();
            String ver_num = "select numero from documentos_almacenes where id_tido = '" + id + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            System.out.println(ver_num);
            ResultSet rs = con.consulta(st, ver_num);
            if (rs.next()) {
                num = rs.getInt("numero");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return num;
    }

    public void act_numero(int idtido, int numero, int tienda, String empresa) {
        try {
            int num = numero + 1;
            Statement st = con.conexion();
            String act_num = "update documentos_almacenes set numero = '" + num + "' where id_tido = '" + idtido + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            System.out.println(act_num);
            con.actualiza(st, act_num);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
