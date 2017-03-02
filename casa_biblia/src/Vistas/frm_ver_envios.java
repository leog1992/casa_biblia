/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Clases.Cl_Conectar;
import Clases.Cl_Envio;
import Clases.Cl_Productos;
import Clases.Cl_Varios;
import Forms.frm_reg_envio;
import ImprimirClases.Print_Envio;
import casa_biblia.frm_menu;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dereck
 */
public class frm_ver_envios extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    Cl_Envio env = new Cl_Envio();
    Cl_Productos mat = new Cl_Productos();
    DefaultTableModel mostrar;
    int anio;
    int fila_seleccionada;

    int tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();

    /**
     * Creates new form frm_ver_envios
     */
    public frm_ver_envios() {
        initComponents();
        Calendar c = Calendar.getInstance();
        anio = c.get(Calendar.YEAR);
        String query = "select e.id_envio, e.anio, e.fecha, e.motivo, e.nro_documento, e.denominacion, e.estado, td.nombre as tipo_documento, e.serie, e.numero, tm.nombre as movimiento "
                + "from envios as e inner join tipo_documento as td on e.id_tido = td.id_tido inner join tipo_movimiento as tm on e.id_timo = tm.id_timo where e.anio = '" + anio + "' "
                + "and e.id_almacen = '" + tienda + "' and e.empresa = '" + empresa + "' order by e.id_envio asc";
        ver_envios(query);
    }

    private void ver_envios(String query) {
        try {
            mostrar = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            mostrar.addColumn("Id");
            mostrar.addColumn("Año");
            mostrar.addColumn("Motivo");
            mostrar.addColumn("Movimiento");
            mostrar.addColumn("Fecha");
            mostrar.addColumn("Tipo Doc.");
            mostrar.addColumn("Serie");
            mostrar.addColumn("Nro.");
            mostrar.addColumn("RUC / DNI");
            mostrar.addColumn("Razon Social");
            mostrar.addColumn("Estado");

            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);
            while (rs.next()) {
                Object fila[] = new Object[11];
                fila[0] = rs.getString("id_envio");
                fila[1] = rs.getString("anio");
                fila[2] = rs.getString("motivo");
                fila[3] = rs.getString("movimiento");
                fila[4] = ven.fechaformateada(rs.getString("fecha"));
                fila[5] = rs.getString("tipo_documento");
                fila[6] = rs.getString("serie");
                fila[7] = rs.getString("numero");
                fila[8] = rs.getString("nro_documento");
                fila[9] = rs.getString("denominacion");
                String estado = rs.getString("estado");
                if (estado.equals("1")) {
                    fila[10] = "ACTIVO";
                } else {
                    fila[10] = "ANULADO";
                }
                mostrar.addRow(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
            t_envios.setModel(mostrar);
            t_envios.getColumnModel().getColumn(0).setPreferredWidth(30);
            t_envios.getColumnModel().getColumn(1).setPreferredWidth(35);
            t_envios.getColumnModel().getColumn(2).setPreferredWidth(120);
            t_envios.getColumnModel().getColumn(3).setPreferredWidth(100);
            t_envios.getColumnModel().getColumn(4).setPreferredWidth(70);
            t_envios.getColumnModel().getColumn(5).setPreferredWidth(100);
            t_envios.getColumnModel().getColumn(6).setPreferredWidth(50);
            t_envios.getColumnModel().getColumn(7).setPreferredWidth(80);
            t_envios.getColumnModel().getColumn(8).setPreferredWidth(80);
            t_envios.getColumnModel().getColumn(9).setPreferredWidth(400);
            t_envios.getColumnModel().getColumn(10).setPreferredWidth(60);
            ven.centrar_celda(t_envios, 0);
            ven.centrar_celda(t_envios, 1);
            ven.centrar_celda(t_envios, 2);
            ven.centrar_celda(t_envios, 3);
            ven.centrar_celda(t_envios, 4);
            ven.centrar_celda(t_envios, 5);
            ven.centrar_celda(t_envios, 6);
            ven.centrar_celda(t_envios, 7);
            ven.centrar_celda(t_envios, 8);
            ven.centrar_celda(t_envios, 10);
        } catch (SQLException ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txt_buscar = new javax.swing.JTextField();
        cbx_busqueda = new javax.swing.JComboBox();
        btn_cerrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_envios = new javax.swing.JTable();
        btn_registrar = new javax.swing.JButton();
        btn_anular = new javax.swing.JButton();
        btn_detalle = new javax.swing.JButton();
        btn_imprimir = new javax.swing.JButton();

        setTitle("Ver Envios");

        jLabel1.setText("Buscar:");

        txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_buscarKeyPressed(evt);
            }
        });

        cbx_busqueda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "RAZON SOCIAL", "FECHA" }));

        btn_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_cerrar.setText("Cerrar");
        btn_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerrarActionPerformed(evt);
            }
        });

        t_envios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        t_envios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_envios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_enviosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_envios);

        btn_registrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/accept.png"))); // NOI18N
        btn_registrar.setText("Registrar Envio");
        btn_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrarActionPerformed(evt);
            }
        });

        btn_anular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delete.png"))); // NOI18N
        btn_anular.setText("Anular");
        btn_anular.setEnabled(false);
        btn_anular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_anularActionPerformed(evt);
            }
        });

        btn_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        btn_detalle.setText("Ver Detalle");
        btn_detalle.setEnabled(false);
        btn_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detalleActionPerformed(evt);
            }
        });

        btn_imprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        btn_imprimir.setText("Imprimir");
        btn_imprimir.setEnabled(false);
        btn_imprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_imprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_detalle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_imprimir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(btn_anular)
                        .addGap(92, 92, 92)
                        .addComponent(btn_registrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cerrar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_anular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_imprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
        frm_reg_envio envio = new frm_reg_envio();
        ven.llamar_ventana(envio);
        this.dispose();
    }//GEN-LAST:event_btn_registrarActionPerformed

    private void btn_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_cerrarActionPerformed

    private void btn_anularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anularActionPerformed
        btn_anular.setEnabled(false);
        //capturar codigo de envio
        env.setCodigo(Integer.parseInt(t_envios.getValueAt(fila_seleccionada, 0).toString()));
        env.setAnio(Integer.parseInt(t_envios.getValueAt(fila_seleccionada, 1).toString()));

        int tido = 0;
        int serie = 0;
        int nro = 0;
        //cargar datos del envio
        try {
            Statement st = con.conexion();
            String c_envio = "select fecha, id_tido, serie, numero, nro_documento, denominacion from envios where id_envio = '" + env.getCodigo() + "' and anio = '" + env.getAnio() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            ResultSet rs = con.consulta(st, c_envio);
            if (rs.next()) {
                env.setFecha(rs.getString("fecha"));
                env.setNro_documento(rs.getString("nro_documento"));
                env.setDenominacion(rs.getString("denominacion"));
                env.setTimo(5);
                tido = rs.getInt("id_tido");
                serie = rs.getInt("serie");
                nro = rs.getInt("numero");
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        //recorrer detalle de envio
        try {
            Statement std = con.conexion();
            String c_detalle = "select * from detalle_envio where id_envio = '" + env.getCodigo() + "' and anio = '" + env.getAnio() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            ResultSet rsd = con.consulta(std, c_detalle);
            while (rsd.next()) {
                mat.setId(rsd.getInt("id_producto"));
                mat.setCan(rsd.getDouble("cantidad"));

                //capturar el costo del producto
                try {
                    Statement st = con.conexion();
                    String bus_pro = "select costo from productos where id_producto = '" + mat.getId() + "'";
                    System.out.println(bus_pro);
                    ResultSet rs = con.consulta(st, bus_pro);
                    if (rs.next()) {
                        mat.setCosto(rs.getDouble("costo"));
                    }
                    con.cerrar(rs);
                    con.cerrar(st);
                } catch (SQLException ex) {
                    System.out.print(ex);
                    JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                }

                //capturar cantidad actual en la tienda
                try {
                    Statement st = con.conexion();
                    String bus_pro = "select cantidad_actual from productos_almacenes where id_producto = '" + mat.getId() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
                    System.out.println(bus_pro);
                    ResultSet rs = con.consulta(st, bus_pro);
                    if (rs.next()) {
                        mat.setCant_act(rs.getDouble("cantidad_actual"));
                    }
                    con.cerrar(rs);
                    con.cerrar(st);
                } catch (SQLException ex) {
                    System.out.print(ex);
                    JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                }

                //ACTUALIZAR CANTIDAD ACTUAL
                mat.setCant_act(mat.getCant_act() + mat.getCan());
                try {
                    Statement st = con.conexion();
                    String act_pro = "update productos_almacenes set cantidad_actual = '" + mat.getCant_act() + "' where id_producto = '" + mat.getId() + "' "
                            + "and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
                    System.out.println(act_pro);
                    con.actualiza(st, act_pro);
                    con.cerrar(st);
                } catch (Exception ex) {
                    System.out.print(ex);
                    JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                }

                int id_kardex = 0;
                //capturar ultimo id de kardex
                try {
                    Statement st = con.conexion();
                    String bus_idk = "select id_kardex from kardex where id_producto = '" + mat.getId() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "' order by id_kardex desc limit 1";
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
                    String ins_kardex = "insert into kardex Values ('" + id_kardex + "', '" + tienda + "', '" + empresa + "','" + mat.getId() + "', '" + env.getFecha() + "', '" + env.getNro_documento() + "', "
                            + "'" + env.getDenominacion() + "', '" + tido + "', '" + serie + "', '" + nro + "', '" + mat.getCan() + "', '" + mat.getCosto() + "', '0.00', '0.00', '" + env.getTimo() + "', NOW())";
                    System.out.println(ins_kardex);
                    con.actualiza(st, ins_kardex);
                    con.cerrar(st);
                } catch (Exception ex) {
                    System.out.print(ex);
                    JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                }
            }
            con.cerrar(rsd);
            con.cerrar(std);
        } catch (SQLException | HeadlessException e) {
            System.out.println(e.getLocalizedMessage());
        }

        //eliminar detalle de envio.
        try {
            Statement std = con.conexion();
            String c_detalle = "delete from detalle_envio where id_envio = '" + env.getCodigo() + "' and anio = '" + env.getAnio() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            System.out.println(c_detalle);
            con.actualiza(std, c_detalle);
            con.cerrar(std);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        //eliminar envio.
        try {
            Statement std = con.conexion();
            String c_detalle = "delete from envios where id_envio = '" + env.getCodigo() + "' and anio = '" + env.getAnio() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            System.out.println(c_detalle);
            con.actualiza(std, c_detalle);
            con.cerrar(std);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        String query = "select e.id_envio, e.anio, e.fecha, e.motivo, e.nro_documento, e.denominacion, e.estado, td.nombre as tipo_documento, e.serie, e.numero, tm.nombre as movimiento "
                + "from envios as e inner join tipo_documento as td on e.id_tido = td.id_tido inner join tipo_movimiento as tm on e.id_timo = tm.id_timo where e.anio = '" + anio + "' "
                + "and e.id_almacen = '" + tienda + "' and e.empresa = '" + empresa + "' order by e.id_envio asc";
        ver_envios(query);

    }//GEN-LAST:event_btn_anularActionPerformed

    private void t_enviosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_enviosMouseClicked
        if (evt.getClickCount() == 2) {
            fila_seleccionada = t_envios.getSelectedRow();
            env.setCodigo(Integer.parseInt(t_envios.getValueAt(fila_seleccionada, 0).toString()));
            env.setAnio(Integer.parseInt(t_envios.getValueAt(fila_seleccionada, 1).toString()));
            btn_anular.setEnabled(true);
            btn_detalle.setEnabled(true);
            btn_imprimir.setEnabled(true);
        }
    }//GEN-LAST:event_t_enviosMouseClicked

    private void btn_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detalleActionPerformed
        if (fila_seleccionada > -1) {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("envio", env.getCodigo());
            parametros.put("anio", env.getAnio());
            parametros.put("empresa", empresa);
            parametros.put("tienda", tienda);
            btn_detalle.setEnabled(false);
            ven.ver_reporte("rpt_ver_envio", parametros);
        }
    }//GEN-LAST:event_btn_detalleActionPerformed

    private void txt_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String busqueda = txt_buscar.getText().trim();
            if (cbx_busqueda.getSelectedIndex() == 0) {
                String query = "select e.id_envio, e.anio, e.fecha, e.motivo, e.nro_documento, e.denominacion, e.estado, td.nombre as tipo_documento, e.serie, e.numero, tm.nombre as movimiento "
                        + "from envios as e inner join tipo_documento as td on e.id_tido = td.id_tido inner join tipo_movimiento as tm on e.id_timo = tm.id_timo where e.anio = '" + anio + "' "
                        + "and e.id_almacen = '" + tienda + "' and e.empresa = '" + empresa + "' and (e.nro_documento = '" + busqueda + "' or e.denominacion like '%" + busqueda + "%') order by e.id_envio asc";
                ver_envios(query);
            }
            if (cbx_busqueda.getSelectedIndex() == 1) {
                busqueda = ven.fechabase(busqueda);
                String query = "select e.id_envio, e.anio, e.fecha, e.motivo, e.nro_documento, e.denominacion, e.estado, td.nombre as tipo_documento, e.serie, e.numero, tm.nombre as movimiento "
                        + "from envios as e inner join tipo_documento as td on e.id_tido = td.id_tido inner join tipo_movimiento as tm on e.id_timo = tm.id_timo where e.anio = '" + anio + "' "
                        + "and e.id_almacen = '" + tienda + "' and e.empresa = '" + empresa + "' and (e.fecha = '" + busqueda + "') order by e.id_envio asc";
                ver_envios(query);
            }
        }
    }//GEN-LAST:event_txt_buscarKeyPressed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        if (fila_seleccionada > -1) {
            Print_Envio imprimir = new Print_Envio();
            imprimir.generar(env.getCodigo(), env.getAnio(), empresa, tienda);
            btn_imprimir.setEnabled(false);
        }

    }//GEN-LAST:event_btn_imprimirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_anular;
    private javax.swing.JButton btn_cerrar;
    private javax.swing.JButton btn_detalle;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JButton btn_registrar;
    private javax.swing.JComboBox cbx_busqueda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable t_envios;
    private javax.swing.JTextField txt_buscar;
    // End of variables declaration//GEN-END:variables
}
