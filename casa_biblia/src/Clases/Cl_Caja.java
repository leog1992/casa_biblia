/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import casa_biblia.frm_menu;

/**
 *
 * @author LUBRICANTE
 */
public class Cl_Caja {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();

    private String fecha;
    private Double monto;
    private String concepto;
    private String origen;
    private String destino;
    private String tipo;

    public Cl_Caja() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double saldo_anterior(int moneda) {
        Double saldo = 0.0;
        try {
            Statement st = con.conexion();
            String query = "select monto from cierre_caja where id_moneda = '" + moneda + "' and id_almacen = '" + frm_menu.alm.getCodigo() + "' order by fecha desc limit 1";
            ResultSet rs = con.consulta(st, query);
            while (rs.next()) {
                saldo = rs.getDouble("monto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return saldo;
    }

    public double suma_caja(String fecha, String origen) {
        Double suma = 0.0;
        try {
            Statement st = con.conexion();
            String query = "select sum(monto) as monto from caja_chica where fecha = '" + fecha + "' and id_almacen = '" + frm_menu.alm.getCodigo() + "' and origen = '" + origen + "' order by fecha desc limit 1";
            ResultSet rs = con.consulta(st, query);
            while (rs.next()) {
                suma += rs.getDouble("monto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return suma;
    }

    public int codigo_movimiento(String fecha_caja) {
        int codigo = 0;
        try {
            Statement st = con.conexion();
            String ver_cod = "select MAX(id_movimiento) as id_movimiento from caja_chica where id_almacen = '" + frm_menu.alm.getCodigo() + "' and fecha = '" + fecha_caja + "' and empresa = '" + frm_menu.emp.getRuc() + "'";
            System.out.println(ver_cod);
            ResultSet rs = con.consulta(st, ver_cod);
            if (rs.next()) {
                codigo = rs.getInt("id_movimiento") + 1;
            } else {
                codigo = 1;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        return codigo;
    }

}
