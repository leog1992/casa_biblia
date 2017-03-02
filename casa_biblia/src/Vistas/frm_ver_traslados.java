/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Clases.Cl_Almacen;
import Clases.Cl_Conectar;
import Clases.Cl_Empresa;
import Clases.Cl_Varios;
import Forms.frm_reg_traslado;
import ImprimirClases.Print_Traslado;
import casa_biblia.frm_menu;
import java.io.File;
import java.io.IOException;
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
public class frm_ver_traslados extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    Cl_Empresa emp = new Cl_Empresa();
    Cl_Almacen alm = new Cl_Almacen();
    DefaultTableModel mostrar;
    String periodo;
    String nombre_tienda;
    int codigo;
    int fila_seleccionada;

    int tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();

    /**
     * Creates new form frm_ver_envios
     */
    public frm_ver_traslados() {
        initComponents();
        Calendar fecha = Calendar.getInstance();
        int anio = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        periodo = anio + String.format("%02d", mes);
        String query = "select t.id_traslado, t.periodo, ao.nombre as origen, ad.nombre as destino, t.fecha_envio, t.fecha_confirmacion, td.nombre as tipo_documento, t.serie, t.numero, t.estado, "
                + "t.empresa_origen, t.empresa_destino from traslados_almacen as t inner join tipo_documento as td on t.id_tido = td.id_tido inner join almacenes as ao on ao.id_almacen = t.id_almacen_origen and "
                + "ao.empresa = t.empresa_origen inner join almacenes as ad on ad.id_almacen = t.id_almacen_destino and ad.empresa = t.empresa_destino where t.estado = '0' and ((t.empresa_origen = '" + empresa + "' and t.id_almacen_origen = '" + tienda + "') "
                + "or (t.empresa_destino = '" + empresa + "' and t.id_almacen_destino = '" + tienda + "')) order by t.fecha_envio desc, t.id_traslado desc";
        ver_traslados(query);
    }

    private void ver_traslados(String query) {
        try {
            mostrar = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            mostrar.addColumn("Id");
            mostrar.addColumn("Periodo");
            mostrar.addColumn("T. Origen");
            mostrar.addColumn("Empresa");
            mostrar.addColumn("T. Destino");
            mostrar.addColumn("Empresa");
            mostrar.addColumn("Enviado");
            mostrar.addColumn("Recibido");
            mostrar.addColumn("Tipo Doc.");
            mostrar.addColumn("Serie");
            mostrar.addColumn("Nro.");
            mostrar.addColumn("Estado");

            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);
            while (rs.next()) {
                Object fila[] = new Object[12];
                fila[0] = rs.getString("id_traslado");
                fila[1] = rs.getString("periodo");
                fila[2] = rs.getString("origen");
                fila[3] = rs.getString("empresa_origen");
                fila[4] = rs.getString("destino");
                fila[5] = rs.getString("empresa_destino");
                fila[6] = ven.fechaformateada(rs.getString("fecha_envio"));
                String fecha_confirmacion = rs.getString("fecha_confirmacion");
                if (!fecha_confirmacion.equals("7000-01-01")) {
                    fila[7] = ven.fechaformateada(rs.getString("fecha_confirmacion"));
                } else {
                    fila[7] = "-";
                }
                fila[8] = rs.getString("tipo_documento");
                fila[9] = rs.getString("serie");
                fila[10] = rs.getString("numero");
                String estado = rs.getString("estado");
                if (estado.equals("1")) {
                    fila[11] = "ACEPTADO";
                }
                if (estado.equals("0")) {
                    fila[11] = "PENDIENTE";
                }
                if (estado.equals("2")) {
                    fila[11] = "ANULADO";
                }
                mostrar.addRow(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
            t_envios.setModel(mostrar);
            t_envios.getColumnModel().getColumn(0).setPreferredWidth(40);
            t_envios.getColumnModel().getColumn(1).setPreferredWidth(60);
            t_envios.getColumnModel().getColumn(2).setPreferredWidth(100);
            t_envios.getColumnModel().getColumn(3).setPreferredWidth(100);
            t_envios.getColumnModel().getColumn(4).setPreferredWidth(100);
            t_envios.getColumnModel().getColumn(5).setPreferredWidth(100);
            t_envios.getColumnModel().getColumn(6).setPreferredWidth(100);
            t_envios.getColumnModel().getColumn(7).setPreferredWidth(100);
            t_envios.getColumnModel().getColumn(8).setPreferredWidth(150);
            t_envios.getColumnModel().getColumn(9).setPreferredWidth(40);
            t_envios.getColumnModel().getColumn(10).setPreferredWidth(60);
            t_envios.getColumnModel().getColumn(11).setPreferredWidth(100);
            ven.centrar_celda(t_envios, 0);
            ven.centrar_celda(t_envios, 1);
            ven.centrar_celda(t_envios, 2);
            ven.centrar_celda(t_envios, 3);
            ven.centrar_celda(t_envios, 4);
            ven.centrar_celda(t_envios, 5);
            ven.centrar_celda(t_envios, 6);
            ven.centrar_celda(t_envios, 7);
            ven.centrar_celda(t_envios, 8);
            ven.centrar_celda(t_envios, 9);
            ven.centrar_celda(t_envios, 10);
            ven.centrar_celda(t_envios, 11);
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
        btn_aceptar = new javax.swing.JButton();
        btn_ver_detalle = new javax.swing.JButton();
        cbx_estado = new javax.swing.JComboBox();
        btn_imprimir = new javax.swing.JButton();

        setTitle("Ver Traslado entre Tiendas");

        jLabel1.setText("Buscar:");

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

        btn_aceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/sockets.png"))); // NOI18N
        btn_aceptar.setText("Aceptar");
        btn_aceptar.setEnabled(false);
        btn_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_aceptarActionPerformed(evt);
            }
        });

        btn_ver_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        btn_ver_detalle.setText("Ver Detalle");
        btn_ver_detalle.setEnabled(false);
        btn_ver_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ver_detalleActionPerformed(evt);
            }
        });

        cbx_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PENDIENTE", "ACEPTADO" }));
        cbx_estado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_estadoActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 238, Short.MAX_VALUE)
                        .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_aceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_registrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cerrar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_anular)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_ver_detalle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_imprimir)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                    .addComponent(btn_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_anular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ver_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_imprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
        frm_reg_traslado envio = new frm_reg_traslado();
        frm_reg_traslado.accion = "registrar";
        ven.llamar_ventana(envio);
        this.dispose();
    }//GEN-LAST:event_btn_registrarActionPerformed

    private void btn_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_cerrarActionPerformed

    private void t_enviosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_enviosMouseClicked
        if (evt.getClickCount() == 2) {
            fila_seleccionada = t_envios.getSelectedRow();
            String tienda_origen = t_envios.getValueAt(fila_seleccionada, 2).toString().trim();
            switch (t_envios.getValueAt(fila_seleccionada, 11).toString().trim()) {
                case "ACEPTADO":
                    btn_anular.setEnabled(false);
                    btn_aceptar.setEnabled(false);
                    btn_ver_detalle.setEnabled(true);
                    btn_imprimir.setEnabled(true);
                    break;
                case "ANULADO":
                    btn_anular.setEnabled(false);
                    btn_aceptar.setEnabled(false);
                    btn_ver_detalle.setEnabled(false);
                    btn_imprimir.setEnabled(false);
                    break;
                default:
                    if (!tienda_origen.equals(frm_menu.alm.getNombre())) {
                        codigo = Integer.parseInt(t_envios.getValueAt(fila_seleccionada, 0).toString().trim());
                        periodo = t_envios.getValueAt(fila_seleccionada, 1).toString().trim();
                        nombre_tienda = t_envios.getValueAt(fila_seleccionada, 2).toString().trim();
                        btn_aceptar.setEnabled(true);
                    } else {
                        btn_aceptar.setEnabled(false);
                    }
                    btn_ver_detalle.setEnabled(true);
                    btn_imprimir.setEnabled(true);
                    btn_anular.setEnabled(true);
                    break;
            }
        }

    }//GEN-LAST:event_t_enviosMouseClicked

    private void btn_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_aceptarActionPerformed
        frm_reg_traslado traslado = new frm_reg_traslado();
        frm_reg_traslado.accion = "verificar";
        //cargar datos del traslado 
        try {
            Statement st = con.conexion();
            String c_traslado = "select ta.id_almacen_origen, ta.empresa_origen, ta.fecha_envio, ta.id_tido, ta.serie, ta.numero from traslados_almacen as ta inner join almacenes as a on "
                    + "a.id_almacen = ta.id_almacen_origen and a.empresa = ta.empresa_origen where empresa_destino = '" + empresa + "' and id_almacen_destino = '" + tienda + "' and "
                    + "id_traslado = '" + codigo + "' and periodo = '" + periodo + "' and ta.estado = '0' and a.nombre = '" + nombre_tienda + "'";
            System.out.println(c_traslado);
            ResultSet rs = con.consulta(st, c_traslado);
            if (rs.next()) {
                traslado.setTitle("ACEPTACION DE TRASLADO");

                emp.setRuc(rs.getString("empresa_origen"));
                alm.setCodigo(rs.getInt("id_almacen_origen"));
                alm.setEmpresa(emp.getRuc());
                alm.ver_tiendas_empresa(frm_reg_traslado.cbx_tiendas, emp.getRuc());
                Object datos[] = alm.obtener_datos();
                Object datos_tienda[] = frm_menu.alm.obtener_datos();

                llenar_traslado(codigo, periodo);

                frm_reg_traslado.vcodigo = codigo;
                frm_reg_traslado.vperiodo = periodo;
                frm_reg_traslado.vempresa = emp.getRuc();
                frm_reg_traslado.vtienda = alm.getCodigo();

                frm_reg_traslado.cbx_tido.setEnabled(false);
                frm_reg_traslado.cbx_tido.setSelectedIndex(rs.getInt("id_tido") - 1);
                frm_reg_traslado.txt_serie.setText(rs.getInt("serie") + "");
                frm_reg_traslado.txt_numero.setText(rs.getInt("numero") + "");
                frm_reg_traslado.txt_fecha.setText(ven.fechaformateada(rs.getString("fecha_envio")));
                frm_reg_traslado.lbl_empresa.setText("Empresa Origen");
                frm_reg_traslado.lbl_tienda.setText("Tienda Origen");
                frm_reg_traslado.cbx_empresa.setSelectedItem(rs.getString("empresa_origen"));
                frm_reg_traslado.cbx_tiendas.setSelectedIndex(rs.getInt("id_almacen_origen") - 1);
                frm_reg_traslado.txt_razon_social.setText(emp.obtener_razon());
                frm_reg_traslado.txt_partida.setText(datos[1].toString());
                frm_reg_traslado.txt_llegada.setText(datos_tienda[1].toString());
                frm_reg_traslado.btn_verificar.setEnabled(true);

                this.dispose();
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        ven.llamar_ventana(traslado);
    }//GEN-LAST:event_btn_aceptarActionPerformed

    private void btn_ver_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ver_detalleActionPerformed
        Map<String, Object> parametros = new HashMap<>();
        String rpt_codigo = t_envios.getValueAt(fila_seleccionada, 0).toString();
        String rpt_periodo = t_envios.getValueAt(fila_seleccionada, 1).toString();
        String rpt_empresa = t_envios.getValueAt(fila_seleccionada, 3).toString();
        String rpt_tienda = t_envios.getValueAt(fila_seleccionada, 2).toString();
        parametros.put("empresa", rpt_empresa);
        parametros.put("tienda", rpt_tienda);
        parametros.put("envio", rpt_codigo);
        parametros.put("periodo", rpt_periodo);
        ven.ver_reporte("rpt_ver_traslado", parametros);
    }//GEN-LAST:event_btn_ver_detalleActionPerformed

    private void cbx_estadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_estadoActionPerformed
        if (cbx_estado.getSelectedIndex() == 0) {
            Calendar fecha = Calendar.getInstance();
            int anio = fecha.get(Calendar.YEAR);
            int mes = fecha.get(Calendar.MONTH) + 1;
            periodo = anio + String.format("%02d", mes);
            String query = "select t.id_traslado, t.periodo, ao.nombre as origen, ad.nombre as destino, t.fecha_envio, t.fecha_confirmacion, td.nombre as tipo_documento, t.serie, t.numero, t.estado, "
                    + "t.empresa_origen, t.empresa_destino from traslados_almacen as t inner join tipo_documento as td on t.id_tido = td.id_tido inner join almacenes as ao on ao.id_almacen = t.id_almacen_origen and "
                    + "ao.empresa = t.empresa_origen inner join almacenes as ad on ad.id_almacen = t.id_almacen_destino and ad.empresa = t.empresa_destino where t.estado = '0' and ((t.empresa_origen = '" + empresa + "' and t.id_almacen_origen = '" + tienda + "') "
                    + "or (t.empresa_destino = '" + empresa + "' and t.id_almacen_destino = '" + tienda + "')) order by t.fecha_envio desc, t.id_traslado desc";
            ver_traslados(query);
        }

        if (cbx_estado.getSelectedIndex() == 1) {
            Calendar fecha = Calendar.getInstance();
            int anio = fecha.get(Calendar.YEAR);
            int mes = fecha.get(Calendar.MONTH) + 1;
            periodo = anio + String.format("%02d", mes);
            String query = "select t.id_traslado, t.periodo, ao.nombre as origen, ad.nombre as destino, t.fecha_envio, t.fecha_confirmacion, td.nombre as tipo_documento, t.serie, t.numero, t.estado, "
                    + "t.empresa_origen, t.empresa_destino from traslados_almacen as t inner join tipo_documento as td on t.id_tido = td.id_tido inner join almacenes as ao on ao.id_almacen = t.id_almacen_origen and "
                    + "ao.empresa = t.empresa_origen inner join almacenes as ad on ad.id_almacen = t.id_almacen_destino and ad.empresa = t.empresa_destino where t.estado = '1' and ((t.empresa_origen = '" + empresa + "' and t.id_almacen_origen = '" + tienda + "') "
                    + "or (t.empresa_destino = '" + empresa + "' and t.id_almacen_destino = '" + tienda + "')) order by t.fecha_envio desc, t.id_traslado desc";
            ver_traslados(query);
        }

    }//GEN-LAST:event_cbx_estadoActionPerformed

    private void btn_anularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anularActionPerformed
        //ASASASAS
    }//GEN-LAST:event_btn_anularActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        if (fila_seleccionada > -1) {
            String rpt_codigo = t_envios.getValueAt(fila_seleccionada, 0).toString();
            String rpt_periodo = t_envios.getValueAt(fila_seleccionada, 1).toString();
            String rpt_empresa = t_envios.getValueAt(fila_seleccionada, 3).toString();
            String rpt_tienda = t_envios.getValueAt(fila_seleccionada, 2).toString();
            Print_Traslado imprimir = new Print_Traslado();
            imprimir.generar(rpt_codigo, rpt_periodo, rpt_empresa, rpt_tienda);
            btn_imprimir.setEnabled(false);
        }
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void llenar_traslado(int codigo, String periodo) {
        frm_reg_traslado.modelo_detalle.addColumn("Cant. Rec.");
        try {
            Statement st = con.conexion();
            String c_productos = "select p.descripcion, p.codigo_externo, p.id_producto, p.precio, dt.cantidad_enviada, um.corto from detalle_traslado as dt "
                    + "inner join traslados_almacen as ta on dt.id_traslado = ta.id_traslado and dt.periodo = ta.periodo and dt.empresa = ta.empresa_origen and "
                    + "dt.tienda = ta.id_almacen_origen inner join productos as p on dt.id_producto = p.id_producto inner join unidad_medida as um on "
                    + "p.id_und_med = um.id_und_med inner join almacenes as a on a.id_almacen = ta.id_almacen_origen and a.empresa = ta.empresa_origen where "
                    + "ta.id_traslado = '" + codigo + "' and ta.periodo = '" + periodo + "' and ta.empresa_destino = '" + empresa + "' and ta.id_almacen_destino "
                    + "= '" + tienda + "' and ta.estado = '0' and a.nombre = '" + nombre_tienda + "'";
            System.out.println(c_productos);
            ResultSet rs = con.consulta(st, c_productos);
            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getInt("id_producto");
                fila[1] = rs.getString("descripcion") + " - " + rs.getString("codigo_externo");;
                fila[2] = "--";
                fila[3] = rs.getString("corto");
                fila[4] = ven.formato_numero(rs.getDouble("cantidad_enviada"));
                fila[5] = ven.formato_numero(rs.getDouble("precio"));
                fila[6] = "0.00";
                frm_reg_traslado.modelo_detalle.addRow(fila);
            }
            frm_reg_traslado.t_detalle.setModel(frm_reg_traslado.modelo_detalle);
            frm_reg_traslado.t_detalle.getColumnModel().getColumn(0).setPreferredWidth(50);
            frm_reg_traslado.t_detalle.getColumnModel().getColumn(1).setPreferredWidth(400);
            frm_reg_traslado.t_detalle.getColumnModel().getColumn(2).setPreferredWidth(62);
            frm_reg_traslado.t_detalle.getColumnModel().getColumn(3).setPreferredWidth(60);
            frm_reg_traslado.t_detalle.getColumnModel().getColumn(4).setPreferredWidth(62);
            frm_reg_traslado.t_detalle.getColumnModel().getColumn(5).setPreferredWidth(60);
            frm_reg_traslado.t_detalle.getColumnModel().getColumn(6).setPreferredWidth(65);
            frm_reg_traslado.modelo_detalle.fireTableDataChanged();
            ven.centrar_celda(frm_reg_traslado.t_detalle, 0);
            ven.derecha_celda(frm_reg_traslado.t_detalle, 2);
            ven.centrar_celda(frm_reg_traslado.t_detalle, 3);
            ven.derecha_celda(frm_reg_traslado.t_detalle, 4);
            ven.derecha_celda(frm_reg_traslado.t_detalle, 5);
            ven.derecha_celda(frm_reg_traslado.t_detalle, 6);
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_aceptar;
    private javax.swing.JButton btn_anular;
    private javax.swing.JButton btn_cerrar;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JButton btn_registrar;
    private javax.swing.JButton btn_ver_detalle;
    private javax.swing.JComboBox cbx_busqueda;
    private javax.swing.JComboBox cbx_estado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable t_envios;
    private javax.swing.JTextField txt_buscar;
    // End of variables declaration//GEN-END:variables
}
