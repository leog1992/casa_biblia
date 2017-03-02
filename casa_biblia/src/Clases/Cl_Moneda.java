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
public class Cl_Moneda {
    
    private int id;
    private String corto;

    public Cl_Moneda() {
    }
    
    private Cl_Conectar con = new Cl_Conectar();

    public Double cambio_venta_dolar(String fecha, Double valor) {
        //para cambiar de soles a dolares
        Double precio = 0.0;
        Double monto = 0.0;
        try {
            Statement st = con.conexion();
            String ver_tc = "select venta from tipo_cambio where fecha = '" + fecha + "'";
            ResultSet rs = con.consulta(st, ver_tc);
            if (rs.next()) {
                precio = rs.getDouble("venta");
            }
            monto = valor / precio;
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }

    public Double cambio_compra_dolar(String fecha, Double valor) {
        //para cambiar de soles a dolares
        Double precio = 0.0;
        Double monto = 0.0;
        try {
            Statement st = con.conexion();
            String ver_tc = "select compra from tipo_cambio where fecha = '" + fecha + "'";
            ResultSet rs = con.consulta(st, ver_tc);
            if (rs.next()) {
                precio = rs.getDouble("compra");
            }
            monto = valor / precio;
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }

    public Double cambio_venta_sol(String fecha, Double valor) {
        //para cambiar de dolares a soles
        Double precio = 1.0;
        Double monto = 1.0;
        try {
            Statement st = con.conexion();
            String ver_tc = "select venta from tipo_cambio where fecha = '" + fecha + "'";
            ResultSet rs = con.consulta(st, ver_tc);
            if (rs.next()) {
                precio = rs.getDouble("venta");
            }
            monto = valor * precio;
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }

    public Double cambio_compra_sol(String fecha, Double valor) {
        //para cambiar de dolares a soles
        Double precio = 0.0;
        Double monto = 0.0;
        try {
            Statement st = con.conexion();
            String ver_tc = "select compra from tipo_cambio where fecha = '" + fecha + "'";
            ResultSet rs = con.consulta(st, ver_tc);
            if (rs.next()) {
                precio = rs.getDouble("compra");
            }
            monto = valor * precio;
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }
    
    public Double tc_compra(String fecha) {
        double tc = 0.0;
        try {
            Statement st = con.conexion();
            String ver_tc = "select compra from tipo_cambio where fecha = '" + fecha + "'";
            System.out.println(ver_tc);
            ResultSet rs = con.consulta(st, ver_tc);
            if (rs.next()) {
                tc = rs.getDouble("compra");
            } else {
                tc = 1.0;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return tc;
    }

    public Cl_Conectar getCon() {
        return con;
    }

    public void setCon(Cl_Conectar con) {
        this.con = con;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorto() {
        return corto;
    }

    public void setCorto(String corto) {
        this.corto = corto;
    }
    
    public void ver_monedas (JComboBox cb_moneda) {
        cb_moneda.removeAllItems();
        try {
            Statement st = con.conexion();
            String c_tienda = "select * from moneda order by id_moneda asc";
            ResultSet rs = con.consulta(st, c_tienda);
            while (rs.next()) {
                cb_moneda.addItem(rs.getString("nombre"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
