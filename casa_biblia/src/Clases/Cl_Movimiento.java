/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author pc
 */
public class Cl_Movimiento {

    Cl_Conectar con = new Cl_Conectar();
    private Integer id;
    private String glosa;
    private String fec;
    private Double monto;
    String empresa;
    int tienda;
    String fecha;

    public Cl_Movimiento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
    }

    public String getFec() {
        return fec;
    }

    public void setFec(String fec) {
        this.fec = fec;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getTienda() {
        return tienda;
    }

    public void setTienda(int tienda) {
        this.tienda = tienda;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double saldo_inicial() {
        double saldo = 0;
        try {
            Statement st = con.conexion();
            String c_saldo = "select monto_apertura from cierre_caja where id_almacen = '" + tienda + "' and empresa = '" + empresa + "' and fecha = '" + fecha + "'";
            ResultSet rs = con.consulta(st, c_saldo);
            if (rs.next()) {
                saldo = rs.getDouble("monto_apertura");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return saldo;
    }

    public double venta_efectivo() {
        double monto = 0;
        try {
            Statement st = con.conexion();
            String c_saldo = "select sum(monto) as smonto from caja_chica where id_almacen = '" + tienda + "' and empresa = '" + empresa + "' and fecha = '" + fecha + "' "
                    + "and origen = 'I' and destino = 'V' and tipo = 'E'";
            ResultSet rs = con.consulta(st, c_saldo);
            if (rs.next()) {
                monto = rs.getDouble("smonto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }
    
    public double venta_tarjeta() {
        double monto = 0;
        try {
            Statement st = con.conexion();
            String c_saldo = "select sum(monto) as smonto from caja_chica where id_almacen = '" + tienda + "' and empresa = '" + empresa + "' and fecha = '" + fecha + "' "
                    + "and origen = 'I' and destino = 'V' and tipo = 'T'";
            ResultSet rs = con.consulta(st, c_saldo);
            if (rs.next()) {
                monto = rs.getDouble("smonto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }
    
    public double otros_ingresos() {
        double monto = 0;
        try {
            Statement st = con.conexion();
            String c_saldo = "select sum(monto) as smonto from caja_chica where id_almacen = '" + tienda + "' and empresa = '" + empresa + "' and fecha = '" + fecha + "' "
                    + "and origen = 'I' and destino = 'OI' and tipo = 'E'";
            ResultSet rs = con.consulta(st, c_saldo);
            if (rs.next()) {
                monto = rs.getDouble("smonto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }
    
    public double venta_vales() {
        double monto = 0;
        try {
            Statement st = con.conexion();
            String c_saldo = "select sum(monto) as smonto from pago_ventas where id_almacen = '" + tienda + "' and empresa = '" + empresa + "' and fecha = '" + fecha + "' "
                    + "and tipo_pago = 'CUPON'";
            ResultSet rs = con.consulta(st, c_saldo);
            if (rs.next()) {
                monto = rs.getDouble("smonto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }
    
    public double cobros() {
        double monto = 0;
        try {
            Statement st = con.conexion();
            String c_saldo = "select sum(monto) as smonto from caja_chica where id_almacen = '" + tienda + "' and empresa = '" + empresa + "' and fecha = '" + fecha + "' "
                    + "and origen = 'I' and destino = 'C' and tipo = 'E'";
            ResultSet rs = con.consulta(st, c_saldo);
            if (rs.next()) {
                monto = rs.getDouble("smonto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }
    
    public double otros_gastos() {
        double monto = 0;
        try {
            Statement st = con.conexion();
            String c_saldo = "select sum(monto) as smonto from caja_chica where id_almacen = '" + tienda + "' and empresa = '" + empresa + "' and fecha = '" + fecha + "' "
                    + "and origen = 'E' and destino = 'OG' and tipo = 'E'";
            ResultSet rs = con.consulta(st, c_saldo);
            if (rs.next()) {
                monto = rs.getDouble("smonto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }
    
    public double vales_emitidos() {
        double monto = 0;
        try {
            Statement st = con.conexion();
            String c_saldo = "select sum(monto) as smonto from venta_eliminada where tienda = '" + tienda + "' and empresa = '" + empresa + "' and fecha = '" + fecha + "'";
            ResultSet rs = con.consulta(st, c_saldo);
            if (rs.next()) {
                monto = rs.getDouble("smonto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return monto;
    }

}
