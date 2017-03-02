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
 * @author Dereck
 */
public class Cl_Almacen {

    Cl_Conectar con = new Cl_Conectar();
    private int codigo;
    private String empresa;
    private String nombre;
    private String direccion;
    private String estado;

    public Cl_Almacen() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int ultimo_codigo() {
        int id = 1;
        try {
            Statement st = con.conexion();
            String c_almacen = "select id_almacen from almacenes where empresa = '" + empresa + "' order by id_almacen desc limit 1";
            ResultSet rs = con.consulta(st, c_almacen);
            while (rs.next()) {
                id = rs.getInt("id_almacen") + 1;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return id;
    }

    public boolean crear_tienda() {
        boolean creado = false;
        int resultado = -1;
        Statement st = con.conexion();
        String ins_tienda = "insert into almacenes Values ('" + codigo + "', '" + empresa + "', '" + nombre + "', '" + direccion + "', '" + estado + "')";
        resultado = con.actualiza(st, ins_tienda);
        if (resultado > -1) {
            creado = true;
        }
        con.cerrar(st);
        return creado;
    }

    public Object[] obtener_datos() {
        Object[] datos = new Object[2];
        try {
            Statement st = con.conexion();
            String c_empresa = "select nombre, direccion from almacenes where id_almacen = '" + codigo + "' and empresa = '" + empresa + "'";
            ResultSet rs = con.consulta(st, c_empresa);
            if (rs.next()) {
                datos[0] = rs.getString("nombre");
                datos[1] = rs.getString("direccion");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        return datos;
    }

    public Object[] lista_tiendas() {
        Object[] datos = new Object[3];
        try {
            Statement st = con.conexion();
            String c_empresa = "select nombre, id_almacen, empresa from almacenes";
            ResultSet rs = con.consulta(st, c_empresa);
            if (rs.next()) {
                datos[0] = rs.getString("id_almacen");
                datos[1] = rs.getString("empresa");
                datos[2] = rs.getString("nombre");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        return datos;
    }

    public void ver_tiendas(JComboBox cb_tiendas) {
        cb_tiendas.removeAllItems();
        try {
            Statement st = con.conexion();
            String c_tienda = "select * from almacenes order by id_almacen asc";
            ResultSet rs = con.consulta(st, c_tienda);
            while (rs.next()) {
                cb_tiendas.addItem(rs.getString("nombre"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void ver_tiendas_empresa(JComboBox cb_tiendas, String empresa) {
        cb_tiendas.removeAllItems();
        try {
            Statement st = con.conexion();
            String c_tienda = "select * from almacenes where empresa = '" + empresa + "' order by id_almacen asc";
            ResultSet rs = con.consulta(st, c_tienda);
            while (rs.next()) {
                cb_tiendas.addItem(rs.getString("nombre"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void crear_tido() {
        int tido;
        try {
            Statement st = con.conexion();
            String c_documentos = "select id_tido from tipo_documento";
            ResultSet rs = con.consulta(st, c_documentos);
            while (rs.next()) {
                tido = rs.getInt("id_tido");
                Statement st1 = con.conexion();
                String i_documentos = "insert into documentos_almacenes values ('" + tido + "', '" + codigo + "', '" + empresa + "', '1', '1')";
                con.actualiza(st1, i_documentos);
                con.cerrar(st1);
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

}
