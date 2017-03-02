/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author gerenciatecnica
 */
public class Cl_Procedimientos {

    Cl_Conectar con = new Cl_Conectar();

    public Object[] insertar_afectos(int tienda, String empresa, String pedido, int tipo_documento, int id_cliente, String nom_cliente, String nro_documento, String vendedor, String estado, String periodo, double total, double efectivo, double tarjeta, double vale, String cajero) {
        System.out.println(tienda + " **/** " + empresa + " **/** " + pedido + " **/** " + tipo_documento + " **/** " + id_cliente + " **/** " + nom_cliente + " **/** " + nro_documento+ " **/** " + vendedor + " **/** " + periodo+ " **/** " + estado + " **/** " + total + " **/** " + efectivo + " **/** " + tarjeta + " **/** " + vale + " **/** " + cajero);
        Object retorno[] = new Object[3];
        try {
            // creamos la conexion
            Connection connMY = con.conx();
                // establecemos que no sea autocommit,
            // asi controlamos la transaccion de manera manual
            connMY.setAutoCommit(false);
            /* instanciamos el objeto callable statement
             * que usaremos para invocar el SP
             * La cantidad de "?" determina la cantidad
             * parametros que recibe el procedimiento
             */
            CallableStatement prcProcedimientoAlmacenado = connMY.prepareCall("{ CALL SP_INSERTAR_AFECTOS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            // cargar parametros al SP
            prcProcedimientoAlmacenado.setInt("_tienda", tienda);
            prcProcedimientoAlmacenado.setString("_empresa", empresa);
            prcProcedimientoAlmacenado.setInt("_pedido", Integer.parseInt(pedido));
            prcProcedimientoAlmacenado.setInt("_tipo_documento", tipo_documento);
            prcProcedimientoAlmacenado.setInt("_cliente", id_cliente);
            prcProcedimientoAlmacenado.setString("_nombre_cliente", nom_cliente);
            prcProcedimientoAlmacenado.setString("_documento_cliente", nro_documento);
            prcProcedimientoAlmacenado.setString("_vendedor", vendedor);
            prcProcedimientoAlmacenado.setString("_estado", estado);
            prcProcedimientoAlmacenado.setString("_periodo", periodo);
            prcProcedimientoAlmacenado.setDouble("_total", total);
            prcProcedimientoAlmacenado.setDouble("_efectivo", efectivo);
            prcProcedimientoAlmacenado.setDouble("_tarjeta", tarjeta);
            prcProcedimientoAlmacenado.setDouble("_vale", vale);
            prcProcedimientoAlmacenado.setString("_cajero", cajero);
            //prcProcedimientoAlmacenado.registerOutParameter(9, 1);
            prcProcedimientoAlmacenado.registerOutParameter(10, java.sql.Types.SMALLINT);
            prcProcedimientoAlmacenado.registerOutParameter(12, java.sql.Types.SMALLINT);
            prcProcedimientoAlmacenado.registerOutParameter(13, java.sql.Types.FLOAT);
            // ejecutar el SP
            prcProcedimientoAlmacenado.execute();

            // confirmar si se ejecuto sin errores
            connMY.commit();

            int id_venta_afecto = prcProcedimientoAlmacenado.getInt("_spventa");
            int contar_afecto = prcProcedimientoAlmacenado.getInt("_spcontar_afectos");
            double suma_afecto = prcProcedimientoAlmacenado.getFloat("_spsuma_afectos");
            System.out.println("IMPRIMIENDO TICKET PARA VENTA AFECTO - NRO: " + id_venta_afecto + "  POR EL TOTAL DE: " + suma_afecto);
            retorno[0] = id_venta_afecto;
            retorno[1] = contar_afecto;
            retorno[2] = suma_afecto;
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            System.out.println(e);
        }
        return retorno;
    }
    
    public Object[] insertar_inafectos(int tienda, String empresa, String pedido, int tipo_documento, int id_cliente, String nom_cliente, String nro_documento, String vendedor, String estado, String periodo, double total, double efectivo, double tarjeta, double vale, String cajero) {
        System.out.println(tienda + " **/** " + empresa + " **/** " + pedido + " **/** " + tipo_documento + " **/** " + id_cliente + " **/** " + nom_cliente + " **/** " + nro_documento+ " **/** " + vendedor + " **/** " + periodo+ " **/** " + estado + " **/** " + total + " **/** " + efectivo + " **/** " + tarjeta + " **/** " + vale + " **/** " + cajero);
        Object retorno[] = new Object[3];
        try {
            // creamos la conexion
            Connection connMY = con.conx();
                // establecemos que no sea autocommit,
            // asi controlamos la transaccion de manera manual
            connMY.setAutoCommit(false);
            /* instanciamos el objeto callable statement
             * que usaremos para invocar el SP
             * La cantidad de "?" determina la cantidad
             * parametros que recibe el procedimiento
             */
            CallableStatement prcProcedimientoAlmacenado = connMY.prepareCall("{ CALL SP_INSERTAR_INAFECTOS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            // cargar parametros al SP
            prcProcedimientoAlmacenado.setInt("_tienda", tienda);
            prcProcedimientoAlmacenado.setString("_empresa", empresa);
            prcProcedimientoAlmacenado.setInt("_pedido", Integer.parseInt(pedido));
            prcProcedimientoAlmacenado.setInt("_tipo_documento", tipo_documento);
            prcProcedimientoAlmacenado.setInt("_cliente", id_cliente);
            prcProcedimientoAlmacenado.setString("_nombre_cliente", nom_cliente);
            prcProcedimientoAlmacenado.setString("_documento_cliente", nro_documento);
            prcProcedimientoAlmacenado.setString("_vendedor", vendedor);
            prcProcedimientoAlmacenado.setString("_estado", estado);
            prcProcedimientoAlmacenado.setString("_periodo", periodo);
            prcProcedimientoAlmacenado.setDouble("_total", total);
            prcProcedimientoAlmacenado.setDouble("_efectivo", efectivo);
            prcProcedimientoAlmacenado.setDouble("_tarjeta", tarjeta);
            prcProcedimientoAlmacenado.setDouble("_vale", vale);
            prcProcedimientoAlmacenado.setString("_cajero", cajero);
            //prcProcedimientoAlmacenado.registerOutParameter(9, 1);
            prcProcedimientoAlmacenado.registerOutParameter(10, java.sql.Types.SMALLINT);
            prcProcedimientoAlmacenado.registerOutParameter(12, java.sql.Types.SMALLINT);
            prcProcedimientoAlmacenado.registerOutParameter(13, java.sql.Types.FLOAT);
            // ejecutar el SP
            prcProcedimientoAlmacenado.execute();

            // confirmar si se ejecuto sin errores
            connMY.commit();

            int id_venta_inafecto = prcProcedimientoAlmacenado.getInt("_spventa");
            int contar_inafecto = prcProcedimientoAlmacenado.getInt("_spcontar_inafectos");
            double suma_inafecto = prcProcedimientoAlmacenado.getFloat("_spsuma_inafectos");
            System.out.println("IMPRIMIENDO TICKET PARA VENTA INAFECTO - NRO: " + id_venta_inafecto + "  POR EL TOTAL DE: " + suma_inafecto);
            retorno[0] = id_venta_inafecto;
            retorno[1] = contar_inafecto;
            retorno[2] = suma_inafecto;
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            System.out.println(e);
        }
        return retorno;
    }
}
