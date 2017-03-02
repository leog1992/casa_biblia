/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import casa_biblia.frm_menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.Timer;
import nicon.notify.core.Notification;

/**
 *
 * @author luis-d
 */
public class Cl_Notificaciones extends Thread{

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    int pedidos_actual = 0;
    int pedidos_nuevo = 0;
    
    String empresa = frm_menu.emp.getRuc();
    int tienda = frm_menu.alm.getCodigo();

    @Override
    public void run() {
        cargar_notificaciones();
    }

    private void cargar_notificaciones() {
        pedidos_actual = cantidad_guias();
        System.out.println("pedidos cantidad actual = " + pedidos_actual);
        try {
            Timer timer;
            timer = new Timer(300000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    notificar();
                }
            });
            timer.start();
            timer.setRepeats(true);
        } catch (Exception e) {
            System.out.println("Error grave " + e.getLocalizedMessage());
        }
    }

    public void notificar() {
        notificar_pedido();
    }

    private int cantidad_guias() {
        int cantidad = 0;
        try {
            Statement st = con.conexion();
            String ver_ped = "select count(usuario) as nro from traslados_almacen where empresa_destino = '"+empresa+"' and id_almacen_destino = '"+tienda+"' and estado = '0'";
            System.out.println(ver_ped);
            ResultSet rs = con.consulta(st, ver_ped);
            if (rs.next()) {
                cantidad = rs.getInt("nro");
                frm_menu.btn_traslados.setText("Traslados (" + cantidad + ")");
            } else {
                cantidad = 0;
                frm_menu.btn_traslados.setText("Traslados");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return cantidad;
    }

    private void notificar_pedido() {
        pedidos_nuevo = cantidad_guias();
        int diferencia = 0;
        if (pedidos_nuevo > pedidos_actual) {
            System.out.println("comparando cantidades");
            System.out.println("cantidad actual " + pedidos_actual);
            System.out.println("cantidad nueva consulta " + pedidos_nuevo);
            diferencia = pedidos_nuevo - pedidos_actual;

            try {
                Statement st = con.conexion();
                String ver_ped = "select t.fecha_envio, a.nombre from traslados_almacen as t inner join almacenes as a on t.id_almacen_origen = a.id_almacen and t.empresa_origen = a.empresa "
                        + "where t.empresa_destino = '"+empresa+"' and t.id_almacen_destino = '"+tienda+"' and t.estado = '0' order by t.fecha_envio desc limit " + diferencia + "";
                ResultSet rs = con.consulta(st, ver_ped);
                while (rs.next()) {
                    String fecha = ven.fechaformateada(rs.getString("fecha_envio"));
                    String tienda_origen = rs.getString("nombre").toUpperCase().trim();
                    Notification.show("TRASLADO ENTRE ALMACENES", "TRASLADO ENTRANTE DE TIENDA: " + tienda_origen + ", FECHA: " + fecha, Notification.WARNING_MESSAGE, Notification.NICON_LIGHT_THEME);

                }
                con.cerrar(rs);
                con.cerrar(st);
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        pedidos_actual = cantidad_guias();
    }

}
