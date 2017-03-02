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
public class Cl_Empresa {

    Cl_Conectar con = new Cl_Conectar();
    private String ruc;
    private String razon_social;
    private String direccion;
    private String contacto;
    private String c_telefono;
    private String c_email;
    private String fecha_registro;
    private String fecha_vencimiento;
    private String estado;

    public Cl_Empresa() {
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getC_telefono() {
        return c_telefono;
    }

    public void setC_telefono(String c_telefono) {
        this.c_telefono = c_telefono;
    }

    public String getC_email() {
        return c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String obtener_razon() {
        String nombre_empresa = "";
        try {
            Statement st = con.conexion();
            String c_empresa = "select razon_social from empresas where ruc_empresa = '" + ruc + "'";
            ResultSet rs = con.consulta(st, c_empresa);
            if (rs.next()) {
                nombre_empresa = rs.getString("razon_social").toUpperCase();
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            nombre_empresa = "NO ENCONTRADO";
        }
        return nombre_empresa;
    }
    
    public void ver_empresas (JComboBox cb_empresas) {
        cb_empresas.removeAllItems();
        try {
            Statement st = con.conexion();
            String c_tienda = "select ruc_empresa from empresas order by ruc_empresa asc";
            ResultSet rs = con.consulta(st, c_tienda);
            while (rs.next()) {
                cb_empresas.addItem(rs.getString("ruc_empresa"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    
    public boolean crear_empresa() {
        boolean creado = false;
        int resultado = -1;
        Statement st = con.conexion();
        String ins_empresa = "insert into empresas Values ('" + ruc + "', '" + razon_social + "', '" + direccion + "', '1')";
        resultado = con.actualiza(st, ins_empresa);
        if (resultado > -1) {
            creado = true;
        }
        con.cerrar(st);
        return creado;
    }
}
