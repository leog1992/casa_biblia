/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import casa_biblia.frm_menu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author luis-d
 */
public class Cl_Ingreso {

    Cl_Conectar con = new Cl_Conectar();

    private int id;
    private int anio;
    private String proveedor;
    private int tido;
    private int serie;
    private int numero;
    private String fecha_ingreso;
    private int moneda;
    private double tc;
    private double subtotal;
    private String fecha_registro;

    public Cl_Ingreso() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public int getTido() {
        return tido;
    }

    public void setTido(int tido) {
        this.tido = tido;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    public double getTc() {
        return tc;
    }

    public void setTc(double tc) {
        this.tc = tc;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public int ultimo_codigo(int tienda, String empresa) {
        int codigo = 1;
        try {
            Statement st = con.conexion();
            String ver_id = "select id_ingreso from ingresos where anio = '" + anio + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "' order by id_ingreso desc limit 1";
            System.out.println(ver_id);
            ResultSet rs = con.consulta(st, ver_id);
            if (rs.next()) {
                codigo = rs.getInt("id_ingreso") + 1;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return codigo;
    }

    public boolean crear_ingreso(int tienda, String empresa) {
        boolean creado = false;
        int resultado;
        Statement st = con.conexion();
        String i_ingreso = "insert into ingresos values ('" + id + "', '" + anio + "', '" + tienda + "', '"+empresa+"', '" + proveedor + "', '" + tido + "', '" + serie + "', "
                + "'" + numero + "', '" + fecha_ingreso + "', '" + moneda + "', '" + tc + "', '" + subtotal + "', NOW())";
        resultado = con.actualiza(st, i_ingreso);
        if (resultado > -1) {
            creado = true;
        } else {
            JOptionPane.showMessageDialog(null, "ERROR AL INSERTAR");
        }
        con.cerrar(st);
        return creado;
    }
}
