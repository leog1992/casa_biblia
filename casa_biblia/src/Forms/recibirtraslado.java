/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Cl_Conectar;
import Clases.Cl_Envio;
import Clases.Cl_Productos;
import Clases.Cl_Varios;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import nicon.notify.core.Notification;

/**
 *
 * @author luis-d
 */
public class recibirtraslado extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();

    /**
     * Creates new form recibirtraslado
     */
    public recibirtraslado() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(jButton1)
                .addContainerGap(163, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(jButton1)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String fecha = ven.getFechaActual();
        Cl_Productos mat = new Cl_Productos();
        Cl_Envio env = new Cl_Envio();
        //consultar traslados pendientes 
        try {
            Statement stp = con.conexion();
            String c_traslados = "select ta.id_traslado, ta.periodo, ta.id_almacen_origen, ta.empresa_origen, ta.id_almacen_destino, ta.empresa_destino, ta.id_tido, ta.serie, "
                    + "ta.numero, a.nombre from traslados_almacen as ta inner join almacenes as a on ta.id_almacen_origen = a.id_almacen and ta.empresa_origen = a.empresa";
            /*String c_traslados = "select ta.id_traslado, ta.periodo, ta.id_almacen_origen, ta.empresa_origen, ta.id_almacen_destino, ta.empresa_destino, ta.id_tido, ta.serie, "
             + "ta.numero, a.nombre from traslados_almacen as ta inner join almacenes as a on ta.id_almacen_origen = a.id_almacen and ta.empresa_origen = a.empresa where "
             + "id_traslado = '3' and periodo = '012017' and id_almacen_origen = '1' and empresa_origen = '10180909350'";*/
            ResultSet rsp = con.consulta(stp, c_traslados);
            while (rsp.next()) {
                String vcodigo = rsp.getString("id_traslado");
                String vperiodo = rsp.getString("periodo");
                String vtienda = rsp.getString("id_almacen_origen");
                String vempresa = rsp.getString("empresa_origen");
                String dtienda = rsp.getString("id_almacen_destino");
                String dempresa = rsp.getString("empresa_destino");
                String nombre_tienda = rsp.getString("nombre");
                int tido = rsp.getInt("id_tido");
                int serie = rsp.getInt("serie");
                int numero = rsp.getInt("numero");
                String denominacion = "DESDE TIENDA: " + nombre_tienda;

                //modificar de traslado a aceptado
                int registro = -1;
                try {
                    Statement st = con.conexion();
                    String u_traslado = "update traslados_almacen set fecha_confirmacion = '" + fecha + "', estado = '1' where id_traslado = '" + vcodigo + "' and periodo = '" + vperiodo + "' "
                            + "and id_almacen_origen = '" + vtienda + "' and empresa_origen = '" + vempresa + "'";
                    registro = con.actualiza(st, u_traslado);
                    System.out.println(u_traslado);
                    con.cerrar(st);
                } catch (Exception e) {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                }

                Notification.show("Aceptacion de Mercaderia", "Registro Exitoso", Notification.CONFIRM_MESSAGE, Notification.NICON_LIGHT_THEME);

                try {
                    Statement std = con.conexion();
                    String c_detalle = "select dt.id_producto, dt.cantidad_enviada, p.precio from detalle_traslado as dt inner join productos as p on p.id_producto = dt.id_producto "
                            + "where dt.id_traslado = '" + vcodigo + "' and dt.periodo = '" + vperiodo + "' and dt.tienda = '" + vtienda + "' and dt.empresa = '" + vempresa + "'";
                    ResultSet rsd = con.consulta(std, c_detalle);
                    while (rsd.next()) {
                        mat.setId(rsd.getInt("id_producto"));
                        mat.setCan(rsd.getDouble("cantidad_enviada"));
                        mat.setPrecio(rsd.getDouble("precio"));

                        //capturar cantidad actual en la tienda
                        boolean existe_producto = true;
                        try {
                            Statement st = con.conexion();
                            String bus_pro = "select cantidad_actual from productos_almacenes where id_producto = '" + mat.getId() + "' and id_almacen = '" + dtienda + "' "
                                    + "and empresa = '" + dempresa + "'";
                            System.out.println(bus_pro);
                            ResultSet rs = con.consulta(st, bus_pro);
                            if (rs.next()) {
                                mat.setCant_act(rs.getDouble("cantidad_actual"));
                                existe_producto = true;
                            } else {
                                mat.setCant_act(0.0);
                                existe_producto = false;
                            }
                            con.cerrar(rs);
                            con.cerrar(st);
                        } catch (SQLException ex) {
                            System.out.print(ex);
                            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                        }

                        //ACTUALIZAR CANTIDAD ACTUAL
                        mat.setCant_act(mat.getCant_act() + mat.getCan());
                        if (existe_producto == false) {
                            try {
                                Statement st = con.conexion();
                                String i_pro = "insert into productos_almacenes values ('" + dtienda + "', '" + dempresa + "', '" + mat.getId() + "', '-', '3', '10',"
                                        + " '" + mat.getCant_act() + "', CURRENT_DATE(), '2070-01-01')";
                                System.out.println(i_pro);
                                con.actualiza(st, i_pro);
                                con.cerrar(st);
                            } catch (Exception ex) {
                                System.out.print(ex);
                                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                            }

                            int id_kardex = 0;
                            //capturar ultimo id de kardex
                            try {
                                Statement st = con.conexion();
                                String bus_idk = "select id_kardex from kardex where id_producto = '" + mat.getId() + "' and id_almacen = '" + dtienda + "' and empresa = '" + dempresa + "' order by id_kardex desc limit 1";
                                System.out.println(bus_idk);
                                ResultSet rs = con.consulta(st, bus_idk);
                                if (rs.next()) {
                                    id_kardex = rs.getInt("id_kardex") + 1;
                                } else {
                                    id_kardex = 1;
                                }
                                con.cerrar(rs);
                                con.cerrar(st);
                            } catch (SQLException ex) {
                                System.out.print(ex);
                                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                            }

                            //ingresar detalle a kardex
                            try {
                                Statement st = con.conexion();
                                String ins_kardex = "insert into kardex Values ('" + id_kardex + "', '" + dtienda + "', '" + dempresa + "','" + mat.getId() + "', '" + fecha + "', '" + dempresa + "', "
                                        + "'" + denominacion + "', '" + tido + "', '" + serie + "', '" + numero + "', '" + mat.getCan() + "', '" + mat.getCosto() + "', '0.00', '0.00', '11', NOW())";
                                System.out.println(ins_kardex);
                                con.actualiza(st, ins_kardex);
                                con.cerrar(st);
                            } catch (Exception ex) {
                                System.out.print(ex);
                                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                            }

                            //guardar detalle de envio
                            try {
                                Statement st = con.conexion();
                                String u_detalle = "update detalle_traslado set cantidad_recibida = '" + mat.getCan() + "' where id_traslado = '" + vcodigo + "' and periodo = '" + vperiodo + "' "
                                        + "and tienda = '" + vtienda + "' and empresa = '" + vempresa + "'";
                                con.actualiza(st, u_detalle);
                                System.out.println(u_detalle);
                            } catch (Exception e) {
                                System.out.println(e);
                                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                            }
                        }
                    }
                    con.cerrar(rsd);
                    con.cerrar(std);
                } catch (SQLException e) {
                    System.out.println(e.getLocalizedMessage());
                }

            }
            con.cerrar(rsp);
            con.cerrar(stp);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
}