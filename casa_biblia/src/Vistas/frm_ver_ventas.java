/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Clases.Cl_Caja;
import Clases.Cl_Cliente;
import Clases.Cl_Conectar;
import Clases.Cl_Movimiento;
import Clases.Cl_Productos;
import Clases.Cl_Tipo_Doc;
import Clases.Cl_Varios;
import Clases.Cl_Venta;
import Clases.render_ventas;
import ImprimirClases.Print_Venta_Ticket;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import casa_biblia.frm_menu;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import nicon.notify.core.Notification;

/**
 *
 * @author CONTABILIDAD 02
 */
public class frm_ver_ventas extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    Cl_Venta ped = new Cl_Venta();
    Cl_Tipo_Doc tido = new Cl_Tipo_Doc();
    Cl_Productos mat = new Cl_Productos();
    Cl_Movimiento mov = new Cl_Movimiento();
    Cl_Cliente cli = new Cl_Cliente();

    DefaultTableModel mostrar;
    DefaultTableModel mostrar_pagos;
    DefaultTableModel mostrar_detalle;
    Integer i;
    int id_moneda;
    String fecha_hoy = ven.getFechaActual();
    int id_tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();
    String periodo;
    double por_pagar;
    double total;
    int fila_pagos;

    /**
     * Creates new form frm_ver_cobranzas
     */
    public frm_ver_ventas() {
        initComponents();
        String ver_ped = "select v.id_venta, v.periodo, td.nombre as tipo_documento, v.serie, v.numero, v.fecha_venta, v.fecha_registro, c.nro_documento, v.nombre_cliente, c.nombre,"
                + "v.total, v.estado from ventas as v inner join tipo_documento as td on v.id_tido = td.id_tido "
                + "inner join clientes as c on v.id_cliente = c.id_cliente where v.id_almacen = '" + id_tienda + "' and v.empresa = '" + empresa + "' and v.fecha_venta = current_date() order by v.id_venta desc";
        ver_pedidos(ver_ped);
        cbx_estado.setSelectedIndex(0);
        String ver_tido = "select nombre from tipo_documento";
        ver_tipodoc(ver_tido);

        String ver_mon = "select nombre from moneda order by id_moneda asc";
        ver_moneda(ver_mon);
        txt_bus.requestFocus();
    }

    private void ver_pedidos(String query) {
        mostrar = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        mostrar.addColumn("Id Vta.");
        mostrar.addColumn("Periodo");
        mostrar.addColumn("Fec. Vta.");
        mostrar.addColumn("Fec. Pago");
        mostrar.addColumn("Tipo Doc.");
        mostrar.addColumn("Serie");
        mostrar.addColumn("Nro.");
        mostrar.addColumn("Cliente");
        mostrar.addColumn("Nombre o Razon Social");
        mostrar.addColumn("Moneda");
        mostrar.addColumn("Monto");
        mostrar.addColumn("Estado");

        double total_tabla = 0;

        try {
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);
            Object[] fila = new Object[12];
            while (rs.next()) {
                fila[0] = rs.getString("id_venta");
                fila[1] = rs.getString("periodo");
                fila[2] = ven.fechaformateada(rs.getString("fecha_venta"));
                fila[3] = ven.mostrar_hora(rs.getString("fecha_registro"));
                fila[4] = rs.getString("tipo_documento");
                fila[5] = rs.getString("serie");
                fila[6] = rs.getString("numero");
                if (rs.getString("estado").equals("3")) {
                    fila[7] = "-";
                } else {
                    fila[7] = rs.getString("nro_documento");
                }
                if (rs.getString("estado").equals("3")) {
                    fila[8] = "-";
                } else {
                    fila[8] = rs.getString("nombre_cliente");
                }
                fila[9] = "S/ ";
                fila[10] = ven.formato_totales(rs.getDouble("total"));
                total_tabla = total_tabla + rs.getDouble("total");
                if (rs.getString("estado").equals("1")) {
                    fila[11] = "PAGADO";
                }
                if (rs.getString("estado").equals("2")) {
                    fila[11] = "SEPARADO";
                }
                if (rs.getString("estado").equals("3")) {
                    fila[11] = "ANULADO";
                }
                if (rs.getString("estado").equals("4")) {
                    fila[11] = "POR RECOGER";
                }
                if (rs.getString("estado").equals("5")) {
                    fila[11] = "ENTREGADO";
                }
                if (rs.getString("estado").equals("6")) {
                    fila[11] = "POR COBRAR";
                }
                mostrar.addRow(fila);
            }

            con.cerrar(st);
            con.cerrar(rs);
        } catch (SQLException e) {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        t_facturas.setModel(mostrar);
        txt_tot.setText(ven.formato_totales(total_tabla));
        t_facturas.getColumnModel().getColumn(0).setPreferredWidth(45);
        t_facturas.getColumnModel().getColumn(1).setPreferredWidth(45);
        t_facturas.getColumnModel().getColumn(2).setPreferredWidth(70);
        t_facturas.getColumnModel().getColumn(3).setPreferredWidth(70);
        t_facturas.getColumnModel().getColumn(4).setPreferredWidth(100);
        t_facturas.getColumnModel().getColumn(5).setPreferredWidth(35);
        t_facturas.getColumnModel().getColumn(6).setPreferredWidth(50);
        t_facturas.getColumnModel().getColumn(7).setPreferredWidth(70);
        t_facturas.getColumnModel().getColumn(8).setPreferredWidth(250);
        t_facturas.getColumnModel().getColumn(9).setPreferredWidth(40);
        t_facturas.getColumnModel().getColumn(10).setPreferredWidth(70);
        t_facturas.getColumnModel().getColumn(11).setPreferredWidth(70);
        t_facturas.setDefaultRenderer(Object.class, new render_ventas());

    }

    private void ver_tipodoc(String query) {
        try {
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);

            while (rs.next()) {
                String fila;
                fila = rs.getString("nombre");
                cbx_jd_tido.addItem(fila);
                cbx_jdb_tido.addItem(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
        } catch (SQLException e) {
            System.out.print(e);
        }
    }

    private void ver_moneda(String query) {
        try {
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);

            while (rs.next()) {
                String fila;
                fila = rs.getString("nombre");
                cbx_jd_moneda.addItem(fila);
                cbx_jda_moneda.addItem(fila);
                cbx_jdb_mon.addItem(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
        } catch (SQLException e) {
            System.out.print(e);
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

        jd_ver_pagos = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_jd_cliente = new javax.swing.JTextField();
        txt_jd_periodo = new javax.swing.JTextField();
        txt_jd_iventa = new javax.swing.JTextField();
        txt_jd_placa = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cbx_jd_tido = new javax.swing.JComboBox();
        txt_jd_serie = new javax.swing.JTextField();
        txt_jd_numero = new javax.swing.JTextField();
        txt_jd_porcobrar = new javax.swing.JTextField();
        cbx_jd_moneda = new javax.swing.JComboBox();
        txt_jd_subtotal = new javax.swing.JTextField();
        txt_jd_igv = new javax.swing.JTextField();
        txt_jd_total = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_jd_pagos = new javax.swing.JTable();
        btn_jd_cerrar = new javax.swing.JButton();
        btn_jd_registrar = new javax.swing.JButton();
        jd_ver_detalle_venta = new javax.swing.JDialog();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        t_jdb_detalle = new javax.swing.JTable();
        txt_jdb_serie = new javax.swing.JTextField();
        txt_jdb_nro = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txt_jdb_fecha = new javax.swing.JFormattedTextField();
        jLabel24 = new javax.swing.JLabel();
        cbx_jdb_mon = new javax.swing.JComboBox();
        cbx_jdb_tido = new javax.swing.JComboBox();
        btn_jdb_cerrar = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        txt_jdb_total = new javax.swing.JTextField();
        jd_reg_pago = new javax.swing.JDialog();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt_jda_fecha = new javax.swing.JFormattedTextField();
        cbx_jda_tipopago = new javax.swing.JComboBox();
        cbx_jda_moneda = new javax.swing.JComboBox();
        txt_jda_tc = new javax.swing.JTextField();
        txt_jda_monto = new javax.swing.JTextField();
        btn_jda_registrar = new javax.swing.JButton();
        jd_anular_venta = new javax.swing.JDialog();
        jLabel26 = new javax.swing.JLabel();
        cbx_motivo = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        btn_anular = new javax.swing.JButton();
        jd_detalle_anulado = new javax.swing.JDialog();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txt_jda_documento = new javax.swing.JTextField();
        txt_jda_nrodoc = new javax.swing.JTextField();
        txt_jda_motivo = new javax.swing.JTextField();
        txt_jda_fechadoc = new javax.swing.JTextField();
        txt_jda_montoanu = new javax.swing.JTextField();
        txt_jda_fechaanu = new javax.swing.JTextField();
        txt_jda_vale = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txt_bus = new javax.swing.JTextField();
        cbx_estado = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_facturas = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_tot = new javax.swing.JTextField();
        btn_anu = new javax.swing.JButton();
        cbx_est = new javax.swing.JComboBox();
        btn_detalle = new javax.swing.JButton();
        btn_cobro = new javax.swing.JButton();
        btn_motivo = new javax.swing.JButton();

        jd_ver_pagos.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_ver_pagos.setTitle("Detalle de Pagos");

        jLabel3.setText("Cliente:");

        jLabel4.setText("Placa:");

        jLabel5.setText("IdVenta");

        jLabel6.setText("Periodo:");

        txt_jd_cliente.setEnabled(false);

        txt_jd_periodo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_periodo.setEnabled(false);

        txt_jd_iventa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_iventa.setEnabled(false);

        txt_jd_placa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_placa.setEnabled(false);

        jLabel7.setText("Moneda:");

        jLabel8.setText("Tipo de Documento:");

        jLabel9.setText("Sub Total:");

        jLabel10.setText("IGV:");

        jLabel11.setText("Total:");

        jLabel12.setText("Serie:");

        jLabel13.setText("Numero");

        jLabel14.setText("Por Cobrar:");

        cbx_jd_tido.setEnabled(false);

        txt_jd_serie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_serie.setEnabled(false);

        txt_jd_numero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_numero.setEnabled(false);

        txt_jd_porcobrar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_porcobrar.setEnabled(false);

        cbx_jd_moneda.setEnabled(false);

        txt_jd_subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_subtotal.setEnabled(false);

        txt_jd_igv.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_igv.setEnabled(false);

        txt_jd_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_total.setEnabled(false);

        t_jd_pagos.setModel(new javax.swing.table.DefaultTableModel(
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
        t_jd_pagos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_jd_pagosMouseClicked(evt);
            }
        });
        t_jd_pagos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_jd_pagosKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(t_jd_pagos);

        btn_jd_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_jd_cerrar.setText("Cerrar");
        btn_jd_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_cerrarActionPerformed(evt);
            }
        });

        btn_jd_registrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/add.png"))); // NOI18N
        btn_jd_registrar.setText("Registrar");
        btn_jd_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_registrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_ver_pagosLayout = new javax.swing.GroupLayout(jd_ver_pagos.getContentPane());
        jd_ver_pagos.getContentPane().setLayout(jd_ver_pagosLayout);
        jd_ver_pagosLayout.setHorizontalGroup(
            jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                        .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel9)))
                            .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel7))
                            .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11))))
                        .addGap(18, 18, 18)
                        .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_jd_placa, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbx_jd_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_jd_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_jd_igv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(txt_jd_total, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                                        .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel12)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel14))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbx_jd_tido, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txt_jd_porcobrar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                                .addComponent(txt_jd_numero, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txt_jd_serie, javax.swing.GroupLayout.Alignment.LEADING)))
                                        .addGap(0, 50, Short.MAX_VALUE))
                                    .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_jd_iventa, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(116, 116, 116)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_jd_periodo))))
                            .addComponent(txt_jd_cliente)))
                    .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_ver_pagosLayout.createSequentialGroup()
                                .addComponent(btn_jd_registrar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_jd_cerrar)))))
                .addContainerGap())
        );
        jd_ver_pagosLayout.setVerticalGroup(
            jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_ver_pagosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_periodo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_iventa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_placa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jd_tido, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jd_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_igv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_porcobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_total, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_ver_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_jd_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_jd_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jd_ver_detalle_venta.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_ver_detalle_venta.setTitle("Detalle de Venta");

        jLabel20.setText("Tipo de Documento");

        jLabel21.setText("Serie:");

        jLabel22.setText("Nro:");

        t_jdb_detalle.setModel(new javax.swing.table.DefaultTableModel(
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
        t_jdb_detalle.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane3.setViewportView(t_jdb_detalle);

        txt_jdb_serie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jdb_serie.setEnabled(false);

        txt_jdb_nro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jdb_nro.setEnabled(false);

        jLabel23.setText("Fecha:");

        try {
            txt_jdb_fecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_jdb_fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jdb_fecha.setEnabled(false);

        jLabel24.setText("Moneda:");

        cbx_jdb_mon.setEnabled(false);

        cbx_jdb_tido.setEnabled(false);

        btn_jdb_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_jdb_cerrar.setText("Cerrar");
        btn_jdb_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jdb_cerrarActionPerformed(evt);
            }
        });

        jLabel25.setText("total:");

        txt_jdb_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jdb_total.setEnabled(false);

        javax.swing.GroupLayout jd_ver_detalle_ventaLayout = new javax.swing.GroupLayout(jd_ver_detalle_venta.getContentPane());
        jd_ver_detalle_venta.getContentPane().setLayout(jd_ver_detalle_ventaLayout);
        jd_ver_detalle_ventaLayout.setHorizontalGroup(
            jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_ver_detalle_ventaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jd_ver_detalle_ventaLayout.createSequentialGroup()
                        .addComponent(btn_jdb_cerrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jdb_total, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jd_ver_detalle_ventaLayout.createSequentialGroup()
                        .addGroup(jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_jdb_nro, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jdb_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbx_jdb_tido, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(98, 98, 98)
                        .addGroup(jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_jdb_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbx_jdb_mon, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)))
                .addContainerGap())
        );
        jd_ver_detalle_ventaLayout.setVerticalGroup(
            jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_ver_detalle_ventaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jdb_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jdb_tido, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jdb_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jdb_mon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jdb_nro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_ver_detalle_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_jdb_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jdb_total, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel15.setText("Fecha:");

        jLabel16.setText("Tipo de Pago:");

        jLabel17.setText("Moneda:");

        jLabel18.setText("Tc.");

        jLabel19.setText("Monto:");

        try {
            txt_jda_fecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_jda_fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jda_fecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jda_fechaKeyPressed(evt);
            }
        });

        cbx_jda_tipopago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "EFECTIVO", "TARJETA", "CHEQUE", "DEPOSITO" }));
        cbx_jda_tipopago.setEnabled(false);
        cbx_jda_tipopago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_jda_tipopagoKeyPressed(evt);
            }
        });

        cbx_jda_moneda.setEnabled(false);
        cbx_jda_moneda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_jda_monedaKeyPressed(evt);
            }
        });

        txt_jda_tc.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jda_tc.setEnabled(false);
        txt_jda_tc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jda_tcKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_jda_tcKeyTyped(evt);
            }
        });

        txt_jda_monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jda_monto.setEnabled(false);
        txt_jda_monto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jda_montoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_jda_montoKeyTyped(evt);
            }
        });

        btn_jda_registrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/accept.png"))); // NOI18N
        btn_jda_registrar.setText("Registrar");
        btn_jda_registrar.setEnabled(false);
        btn_jda_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jda_registrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_reg_pagoLayout = new javax.swing.GroupLayout(jd_reg_pago.getContentPane());
        jd_reg_pago.getContentPane().setLayout(jd_reg_pagoLayout);
        jd_reg_pagoLayout.setHorizontalGroup(
            jd_reg_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_reg_pagoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_reg_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addGap(28, 28, 28)
                .addGroup(jd_reg_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_jda_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_tc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jda_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jda_tipopago, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_reg_pagoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_jda_registrar)
                .addContainerGap())
        );
        jd_reg_pagoLayout.setVerticalGroup(
            jd_reg_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_reg_pagoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_reg_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_reg_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jda_tipopago, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_reg_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jda_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_reg_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_tc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_reg_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_jda_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jd_anular_venta.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel26.setText("Motivo:");

        cbx_motivo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CAMBIO DE PRODUCTOS", "DOCUMENTO MAL ELABORADO", "PRODUCTO DEFECTUOSO", "OTROS" }));
        cbx_motivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_motivoKeyPressed(evt);
            }
        });

        btn_anular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delete.png"))); // NOI18N
        btn_anular.setText("Anular Venta");
        btn_anular.setEnabled(false);
        btn_anular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_anularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_anular_ventaLayout = new javax.swing.GroupLayout(jd_anular_venta.getContentPane());
        jd_anular_venta.getContentPane().setLayout(jd_anular_ventaLayout);
        jd_anular_ventaLayout.setHorizontalGroup(
            jd_anular_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_anular_ventaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_anular_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jd_anular_ventaLayout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jd_anular_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1)
                            .addComponent(cbx_motivo, 0, 327, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_anular_ventaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_anular)))
                .addContainerGap())
        );
        jd_anular_ventaLayout.setVerticalGroup(
            jd_anular_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_anular_ventaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_anular_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_anular, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jd_detalle_anulado.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_detalle_anulado.setTitle("Detalle y Motivo de Anulacion");

        jLabel27.setText("Documento:");

        jLabel28.setText("Serie y Nro.:");

        jLabel29.setText("Fecha Doc.:");

        jLabel30.setText("Monto:");

        jLabel31.setText("Motivo:");

        jLabel32.setText("Vale:");

        jLabel33.setText("Fecha Anulac.:");

        txt_jda_documento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jda_documento.setText("SIN DATOS");
        txt_jda_documento.setFocusable(false);

        txt_jda_nrodoc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jda_nrodoc.setText("---");
        txt_jda_nrodoc.setFocusable(false);

        txt_jda_motivo.setText("SIN DATOS");
        txt_jda_motivo.setFocusable(false);

        txt_jda_fechadoc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jda_fechadoc.setText("00/00/0000");
        txt_jda_fechadoc.setFocusable(false);

        txt_jda_montoanu.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jda_montoanu.setText("0.00");
        txt_jda_montoanu.setFocusable(false);

        txt_jda_fechaanu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jda_fechaanu.setText("00/00/0000");
        txt_jda_fechaanu.setFocusable(false);

        txt_jda_vale.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jda_vale.setText("XXXXX");
        txt_jda_vale.setFocusable(false);

        javax.swing.GroupLayout jd_detalle_anuladoLayout = new javax.swing.GroupLayout(jd_detalle_anulado.getContentPane());
        jd_detalle_anulado.getContentPane().setLayout(jd_detalle_anuladoLayout);
        jd_detalle_anuladoLayout.setHorizontalGroup(
            jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_detalle_anuladoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jd_detalle_anuladoLayout.createSequentialGroup()
                        .addComponent(txt_jda_vale, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110)
                        .addGroup(jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jLabel33))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_jda_fechaanu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jda_fechadoc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jda_montoanu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txt_jda_nrodoc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_motivo)
                    .addComponent(txt_jda_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jd_detalle_anuladoLayout.setVerticalGroup(
            jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_detalle_anuladoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_fechadoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_nrodoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_montoanu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_detalle_anuladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_fechaanu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_vale, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setTitle("Registro de Ventas");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/find.png"))); // NOI18N
        jLabel1.setText("Buscar");

        txt_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_busKeyPressed(evt);
            }
        });

        cbx_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FECHA", "CLIENTE", "NRO. BOLETA / FACTURA" }));
        cbx_estado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_estadoActionPerformed(evt);
            }
        });

        t_facturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"2", "B/001-00025", "C0U944", "LUIS ENRIQUE OYANGUREN GIRON", "S/. 59.00", "Pendiente de Pago"},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id Ped.", "Glosa", "Nro Placa.", "Cliente", "Monto", "Estado"
            }
        ));
        t_facturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_facturasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_facturas);
        if (t_facturas.getColumnModel().getColumnCount() > 0) {
            t_facturas.getColumnModel().getColumn(0).setPreferredWidth(10);
            t_facturas.getColumnModel().getColumn(1).setPreferredWidth(50);
            t_facturas.getColumnModel().getColumn(2).setPreferredWidth(30);
            t_facturas.getColumnModel().getColumn(3).setPreferredWidth(250);
            t_facturas.getColumnModel().getColumn(4).setPreferredWidth(30);
            t_facturas.getColumnModel().getColumn(5).setPreferredWidth(60);
        }

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        jButton2.setText("Cerrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Total:");

        txt_tot.setEditable(false);
        txt_tot.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btn_anu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delete.png"))); // NOI18N
        btn_anu.setText("Anular");
        btn_anu.setEnabled(false);
        btn_anu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_anuActionPerformed(evt);
            }
        });

        cbx_est.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "POR COBRAR", "ANULADOS", "HOY" }));
        cbx_est.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_estActionPerformed(evt);
            }
        });

        btn_detalle.setText("Ver Detalle");
        btn_detalle.setEnabled(false);
        btn_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detalleActionPerformed(evt);
            }
        });

        btn_cobro.setText("Ver Pagos");
        btn_cobro.setEnabled(false);
        btn_cobro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cobroActionPerformed(evt);
            }
        });

        btn_motivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/comment.png"))); // NOI18N
        btn_motivo.setText("Motivo Anulacion");
        btn_motivo.setEnabled(false);
        btn_motivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_motivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1054, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbx_est, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cobro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_detalle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_tot, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_anu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_motivo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbx_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbx_est, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_cobro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tot, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_anu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private double total() {
        Double tot = 0.0;
        int nro_fila = t_facturas.getRowCount();
        if (nro_fila > 0) {
            for (int j = 0; j < nro_fila; j++) {
                tot = tot + Double.parseDouble(t_facturas.getValueAt(j, 9).toString());
            }
        }
        return tot;
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cbx_estadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_estadoActionPerformed
        if (cbx_estado.isDisplayable()) {
            txt_bus.setText("");
            txt_bus.requestFocus();
        }
    }//GEN-LAST:event_cbx_estadoActionPerformed

    private void txt_busKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_busKeyPressed
        String buscar = txt_bus.getText();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cbx_estado.getSelectedIndex() == 0) {
                buscar = ven.fechabase(txt_bus.getText());
                System.out.println(buscar);
                String ver_ped = "select v.id_venta, v.periodo, td.nombre as tipo_documento, v.serie, v.numero, v.fecha_venta, v.fecha_registro, c.nro_documento, v.nombre_cliente, c.nombre,"
                        + "v.nombre_cliente, v.total, v.estado from ventas as v inner join tipo_documento as td on v.id_tido = td.id_tido "
                        + "inner join clientes as c on v.id_cliente = c.id_cliente where v.id_almacen = '" + id_tienda + "' and v.empresa = '" + empresa + "' and v.fecha_venta = '" + buscar + "' order by v.id_venta desc";
                System.out.println(ver_ped);
                ver_pedidos(ver_ped);
            }

            if (cbx_estado.getSelectedIndex() == 1) {
                String ver_ped = "select v.id_venta, v.periodo, td.nombre as tipo_documento, v.serie, v.numero, v.fecha_venta, v.fecha_registro, c.nro_documento, c.nombre,"
                        + "v.nombre_cliente, v.total, v.estado from ventas as v inner join tipo_documento as td on v.id_tido = td.id_tido "
                        + "inner join clientes as c on v.id_cliente = c.id_cliente where v.id_almacen = '" + id_tienda + "' and v.empresa = '" + empresa + "' and (c.nro_documento like '%" + buscar + "%' or c.nombre like '%" + buscar + "%' or v.nombre_cliente like '%" + buscar + "%') order by v.id_venta desc";
                System.out.println(ver_ped);
                ver_pedidos(ver_ped);
            }

            if (cbx_estado.getSelectedIndex() == 2) {
                String ver_ped = "select v.id_venta, v.periodo, td.nombre as tipo_documento, v.serie, v.numero, v.fecha_venta, v.fecha_registro, c.nro_documento, c.nombre,"
                        + "v.nombre_cliente, v.total, v.estado from ventas as v inner join tipo_documento as td on v.id_tido = td.id_tido "
                        + "inner join clientes as c on v.id_cliente = c.id_cliente where v.id_almacen = '" + id_tienda + "' and v.empresa = '" + empresa + "' and v.numero = '" + buscar + "' order by v.id_venta desc";
                System.out.println(ver_ped);
                ver_pedidos(ver_ped);
            }
        }
    }//GEN-LAST:event_txt_busKeyPressed

    private void btn_anuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anuActionPerformed
        btn_anu.setEnabled(false);
        btn_detalle.setEnabled(false);
        btn_cobro.setEnabled(false);

        jd_anular_venta.setModal(true);
        jd_anular_venta.setSize(420, 160);
        jd_anular_venta.setLocationRelativeTo(null);
        jd_anular_venta.setVisible(true);
    }//GEN-LAST:event_btn_anuActionPerformed

    private void btn_cobroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cobroActionPerformed
        btn_cobro.setEnabled(false);
        btn_anu.setEnabled(false);
        btn_detalle.setEnabled(false);

        jd_ver_pagos.setModal(true);
        jd_ver_pagos.setSize(720, 460);
        jd_ver_pagos.setLocationRelativeTo(null);

        String dni = t_facturas.getValueAt(i, 7).toString();
        String nombre = t_facturas.getValueAt(i, 8).toString();
        txt_jd_cliente.setText(dni + " - " + nombre);
        txt_jd_iventa.setText(ped.getId() + "");
        txt_jd_periodo.setText(periodo);
        String placa = t_facturas.getValueAt(i, 7).toString();
        txt_jd_placa.setText(placa);
        total = 0;
        String afecto = null;
        try {
            Statement st = con.conexion();
            String datos_venta = "select id_tido, total, serie, numero, afecto from ventas where id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and "
                    + "id_almacen = '" + id_tienda + "' and empresa = '" + empresa + "'";
            ResultSet rs = con.consulta(st, datos_venta);
            if (rs.next()) {
                afecto = rs.getString("afecto");
                id_moneda = 1;
                cbx_jd_moneda.setSelectedIndex(id_moneda - 1);
                cbx_jd_tido.setSelectedIndex(rs.getInt("id_tido") - 1);
                txt_jd_serie.setText(rs.getString("serie"));
                txt_jd_numero.setText(rs.getString("numero"));
                total = rs.getDouble("total");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        if (afecto.equals("1") || afecto.equals("2")) {
            txt_jd_subtotal.setText(ven.formato_totales(total / 1.18));
            txt_jd_igv.setText(ven.formato_totales(total / 1.18 * 0.18));
            txt_jd_total.setText(ven.formato_totales(total));
        }
        if (afecto.equals("0")) {
            txt_jd_subtotal.setText(ven.formato_totales(total));
            txt_jd_igv.setText(ven.formato_totales(0.00));
            txt_jd_total.setText(ven.formato_totales(total));
        }

        modelo_pagos(periodo, id_tienda, ped.getId(), id_moneda, total);
        jd_ver_pagos.setVisible(true);
    }//GEN-LAST:event_btn_cobroActionPerformed

    private void modelo_pagos(String periodo, int id_tienda, int id_venta, int idmoneda, double total) {
        mostrar_pagos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        mostrar_pagos.addColumn("Id");
        mostrar_pagos.addColumn("Tipo Pago");
        mostrar_pagos.addColumn("Fecha");
        mostrar_pagos.addColumn("Moneda");
        mostrar_pagos.addColumn("Monto MN");
        mostrar_pagos.addColumn("T.Cambio");
        mostrar_pagos.addColumn("Monto ME");

        try {
            Statement st = con.conexion();
            String ver_pagos = "select pv.id_pago_ventas, pv.fecha, pv.tc_venta, m.id_moneda as moneda, m.corto, pv.monto, pv.tipo_pago from pago_ventas as pv inner join"
                    + " moneda as m on pv.id_moneda = m.id_moneda where pv.id_venta = '" + id_venta + "' and pv.periodo = '" + periodo + "' and pv.id_almacen = '" + id_tienda + "'";
            ResultSet rs = con.consulta(st, ver_pagos);
            Object[] fila = new Object[7];
            Double suma_MN = 0.0;
            Double suma_ME = 0.0;
            while (rs.next()) {
                fila[0] = rs.getInt("id_pago_ventas");
                fila[1] = rs.getString("tipo_pago");
                fila[2] = ven.fechaformateada(rs.getString("fecha"));
                fila[3] = rs.getString("corto");
                Double ME = 0.0;
                Double MN = 0.0;
                String id_mon = rs.getString("moneda");
                Double monto = rs.getDouble("monto");
                Double tc = rs.getDouble("tc_venta");
                if (id_mon.equals("1")) {
                    MN = monto;
                    ME = monto / tc;
                }
                if (id_mon.equals("2")) {
                    MN = monto * tc;
                    ME = monto;
                }
                suma_MN = suma_MN + MN;
                suma_ME = suma_ME + ME;
                fila[4] = ven.formato_totales(MN);
                fila[5] = ven.formato_tc(rs.getDouble("tc_venta"));
                fila[6] = ven.formato_totales(ME);
                mostrar_pagos.addRow(fila);
            }
            con.cerrar(rs);
            con.cerrar(st);
            if (idmoneda == 1) {
                por_pagar = total - suma_MN;
            }
            if (idmoneda == 2) {
                por_pagar = total - suma_ME;
            }
            if (por_pagar > 0) {
                btn_jd_registrar.setEnabled(true);
            } else {
                btn_jd_registrar.setEnabled(false);
            }
            txt_jd_porcobrar.setText(ven.formato_totales(por_pagar));
            t_jd_pagos.setModel(mostrar_pagos);
            ven.centrar_celda(t_jd_pagos, 0);
            ven.centrar_celda(t_jd_pagos, 1);
            ven.centrar_celda(t_jd_pagos, 2);
            ven.centrar_celda(t_jd_pagos, 3);
            ven.derecha_celda(t_jd_pagos, 4);
            ven.derecha_celda(t_jd_pagos, 5);
            ven.derecha_celda(t_jd_pagos, 6);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }

    }

    private void modelo_detalle(String periodo, int id_tienda, String empresa, int id_venta) {
        mostrar_detalle = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        mostrar_detalle.addColumn("Id");
        mostrar_detalle.addColumn("Descripcion");
        mostrar_detalle.addColumn("Cant.");
        mostrar_detalle.addColumn("Und Med");
        mostrar_detalle.addColumn("Pre. Unit.");
        mostrar_detalle.addColumn("Parcial");

        try {
            Statement st = con.conexion();
            String ver_detalle = "select dv.producto, p.descripcion, p.codigo_externo, dv.precio, dv.cantidad, um.corto from detalle_venta as dv inner join "
                    + "productos as p on dv.producto = p.id_producto inner join unidad_medida as um on p.id_und_med = um.id_und_med where dv.venta = '" + id_venta + "' and dv.periodo = '" + periodo + "' and "
                    + "dv.tienda = '" + id_tienda + "' and dv.empresa = '" + empresa + "'";
            System.out.println(ver_detalle);
            ResultSet rs = con.consulta(st, ver_detalle);
            Object[] fila = new Object[6];
            double total_detalle = 0.0;
            while (rs.next()) {
                double precio = rs.getDouble("precio");
                double cantidad = rs.getDouble("cantidad");
                double parcial = precio * cantidad;
                total_detalle += parcial;
                fila[0] = rs.getInt("producto");
                fila[1] = rs.getString("descripcion") + " - " + rs.getString("codigo_externo");
                fila[2] = ven.formato_numero(cantidad);
                fila[3] = rs.getString("corto");
                fila[4] = ven.formato_numero(precio);
                fila[5] = ven.formato_totales(parcial);
                mostrar_detalle.addRow(fila);
            }
            con.cerrar(rs);
            con.cerrar(st);
            txt_jdb_total.setText(ven.formato_totales(total_detalle));
            t_jdb_detalle.setModel(mostrar_detalle);
            t_jdb_detalle.getColumnModel().getColumn(0).setPreferredWidth(60);
            t_jdb_detalle.getColumnModel().getColumn(1).setPreferredWidth(400);
            t_jdb_detalle.getColumnModel().getColumn(2).setPreferredWidth(50);
            t_jdb_detalle.getColumnModel().getColumn(3).setPreferredWidth(50);
            t_jdb_detalle.getColumnModel().getColumn(4).setPreferredWidth(65);
            t_jdb_detalle.getColumnModel().getColumn(5).setPreferredWidth(65);
            ven.centrar_celda(t_jdb_detalle, 0);
            ven.derecha_celda(t_jdb_detalle, 2);
            ven.centrar_celda(t_jdb_detalle, 3);
            ven.derecha_celda(t_jdb_detalle, 4);
            ven.derecha_celda(t_jdb_detalle, 5);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }

    }


    private void t_facturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_facturasMouseClicked
        i = t_facturas.getSelectedRow();
        ped.setId(Integer.parseInt(t_facturas.getValueAt(i, 0).toString()));
        periodo = t_facturas.getValueAt(i, 1).toString();
        if (t_facturas.getValueAt(i, 11).equals("ANULADO")) {
            btn_motivo.setEnabled(true);
            btn_anu.setEnabled(false);
            btn_cobro.setEnabled(false);
            btn_detalle.setEnabled(true);
        } else {
            btn_motivo.setEnabled(false);
            btn_anu.setEnabled(true);
            btn_cobro.setEnabled(true);
            btn_detalle.setEnabled(true);
        }
        
        if (evt.getClickCount() ==2) {
            ped.setId(Integer.parseInt(t_facturas.getValueAt(i, 0).toString()));
            ped.setPeriodo(t_facturas.getValueAt(i, 1).toString());
            Print_Venta_Ticket ticket = new Print_Venta_Ticket();
            ticket.generar(ped.getId(), ped.getPeriodo(), 2, "10180909350");
        }
    }//GEN-LAST:event_t_facturasMouseClicked

    private void cbx_estActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_estActionPerformed
        if (cbx_est.getSelectedIndex() == 0) {
            String ver_ped = "select v.id_venta, v.periodo, td.nombre as tipo_documento, v.serie, v.numero, v.fecha_venta, v.fecha_pago, c.nro_documento, c.nombre,"
                    + "m.corto, v.total, v.estado from ventas as v inner join tipo_documento as td on v.id_tido = td.id_tido inner join moneda as m on v.id_moneda = m.id_moneda "
                    + "inner join clientes as c on v.id_cliente = c.id_cliente where id_almacen = '" + id_tienda + "' and empresa = '" + empresa + "' and v.estado = '0' order by v.id_venta desc";
            ver_pedidos(ver_ped);
        }
        if (cbx_est.getSelectedIndex() == 1) {
            String ver_ped = "select v.id_venta, v.periodo, td.nombre as tipo_documento, v.serie, v.numero, v.fecha_venta, v.fecha_pago, c.nro_documento, c.nombre,"
                    + "m.corto, v.total, v.estado from ventas as v inner join tipo_documento as td on v.id_tido = td.id_tido inner join moneda as m on v.id_moneda = m.id_moneda "
                    + "inner join clientes as c on v.id_cliente = c.id_cliente where id_almacen = '" + id_tienda + "' and empresa = '" + empresa + "' and v.estado = '2' order by v.id_venta desc";
            ver_pedidos(ver_ped);
        }
        if (cbx_est.getSelectedIndex() == 2) {
            String ver_ped = "select v.id_venta, v.periodo, td.nombre as tipo_documento, v.serie, v.numero, v.fecha_venta, v.fecha_pago, c.nro_documento, c.nombre,"
                    + "m.corto, v.total, v.estado from ventas as v inner join tipo_documento as td on v.id_tido = td.id_tido inner join moneda as m on v.id_moneda = m.id_moneda "
                    + "inner join clientes as c on v.id_cliente = c.id_cliente where id_almacen = '" + id_tienda + "' and empresa = '" + empresa + "' and v.fecha_venta = current_date() order by v.id_venta desc";
            ver_pedidos(ver_ped);
        }
    }//GEN-LAST:event_cbx_estActionPerformed

    private void btn_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detalleActionPerformed
        btn_cobro.setEnabled(false);
        btn_anu.setEnabled(false);
        btn_detalle.setEnabled(false);

        jd_ver_detalle_venta.setModal(true);
        jd_ver_detalle_venta.setSize(700, 370);
        jd_ver_detalle_venta.setLocationRelativeTo(null);

        //cargar datos de venta
        try {
            Statement st = con.conexion();
            String datos_venta = "select id_tido, serie, numero, fecha_venta from ventas where id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and "
                    + "id_almacen = '" + id_tienda + "' and empresa = '" + empresa + "'";
            ResultSet rs = con.consulta(st, datos_venta);
            if (rs.next()) {
                cbx_jdb_mon.setSelectedIndex(0);
                cbx_jdb_tido.setSelectedIndex(rs.getInt("id_tido") - 1);
                txt_jdb_serie.setText(rs.getInt("serie") + "");
                txt_jdb_nro.setText(rs.getInt("numero") + "");
                txt_jdb_fecha.setText(ven.fechaformateada(rs.getString("fecha_venta")));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            System.out.println(ex);
        }

        modelo_detalle(periodo, id_tienda, empresa, ped.getId());
        jd_ver_detalle_venta.setVisible(true);
    }//GEN-LAST:event_btn_detalleActionPerformed

    private void btn_jd_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_cerrarActionPerformed
        jd_ver_pagos.dispose();
    }//GEN-LAST:event_btn_jd_cerrarActionPerformed

    private void btn_jd_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_registrarActionPerformed
        jd_reg_pago.setModal(true);
        jd_reg_pago.setSize(250, 270);
        jd_reg_pago.setLocationRelativeTo(null);
        txt_jda_fecha.setText(ven.fechaformateada(ven.getFechaActual()));
        jd_reg_pago.setVisible(true);
    }//GEN-LAST:event_btn_jd_registrarActionPerformed

    private void txt_jda_fechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jda_fechaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_jda_fecha.getText().length() == 10) {
                cbx_jda_tipopago.setEnabled(true);
                cbx_jda_tipopago.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_jda_fechaKeyPressed

    private void cbx_jda_tipopagoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_jda_tipopagoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbx_jda_moneda.setEnabled(true);
            cbx_jda_moneda.requestFocus();
        }
    }//GEN-LAST:event_cbx_jda_tipopagoKeyPressed

    private void cbx_jda_monedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_jda_monedaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt_jda_tc.setText("1.000");
            txt_jda_tc.setEnabled(true);
            txt_jda_tc.requestFocus();
        }
    }//GEN-LAST:event_cbx_jda_monedaKeyPressed

    private void txt_jda_tcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jda_tcKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_jda_tc.getText().length() > 3) {
                txt_jda_monto.setEnabled(true);
                txt_jda_monto.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_jda_tcKeyPressed

    private void txt_jda_tcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jda_tcKeyTyped
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_jda_tcKeyTyped

    private void txt_jda_montoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jda_montoKeyTyped
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_jda_montoKeyTyped

    private void txt_jda_montoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jda_montoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_jda_monto.getText().length() > 0) {
                Double monto_recibido = Double.parseDouble(txt_jda_monto.getText());
                if (monto_recibido > por_pagar) {
                    txt_jda_monto.setText(ven.formato_numero(por_pagar));
                }
                btn_jda_registrar.setEnabled(true);
                btn_jda_registrar.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_jda_montoKeyPressed

    private void btn_jda_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jda_registrarActionPerformed
        String fecha_pago = ven.fechabase(txt_jda_fecha.getText());
        String tipo_pago = cbx_jda_tipopago.getSelectedItem().toString();
        int moneda = cbx_jda_moneda.getSelectedIndex() + 1;
        Double tc_pago = Double.parseDouble(txt_jda_tc.getText());
        Double monto_pago = Double.parseDouble(txt_jda_monto.getText());

        int id_pago = 0;
        try {
            Statement st = con.conexion();
            String ver_cod = "select id_pago_ventas from pago_ventas where id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and id_almacen = '" + id_tienda + "' "
                    + "and empresa = '" + empresa + "' order by id_pago_ventas desc limit 1";
            System.out.println(ver_cod);
            ResultSet rs = con.consulta(st, ver_cod);
            if (rs.next()) {
                id_pago = rs.getInt("id_pago_ventas") + 1;
            } else {
                id_pago = 1;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            System.out.println(e);
        }

        try {
            Statement st = con.conexion();
            String ins_cuota = "insert into pago_ventas values ('" + id_pago + "', '" + ped.getId() + "', '" + periodo + "', '" + id_tienda + "', '" + empresa + "', '" + fecha_pago + "', "
                    + "'" + moneda + "', '" + tc_pago + "', '" + monto_pago + "', '" + tipo_pago + "')";
            System.out.println(ins_cuota);
            con.actualiza(st, ins_cuota);
            con.cerrar(st);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            System.out.println(e);
        }

        //REGISTRAR MOVIMIENTO EN CAJA CHICA
        Cl_Caja caj = new Cl_Caja();
        String destino;
        if (tipo_pago.equals("EFECTIVO")) {
            destino = "E";
        } else {
            destino = "T";
        }
        tido.setDesc(cbx_jd_tido.getSelectedItem().toString());
        tido.setSer(Integer.parseInt(txt_jd_serie.getText()));
        tido.setNro(Integer.parseInt(txt_jd_numero.getText()));
        String concepto = "CUOTA DE PAGO DE VENTA - " + tido.getDesc() + "/  " + tido.getSer() + "-" + tido.getNro() + " - " + txt_jd_placa.getText();
        int nro_caja = caj.codigo_movimiento(fecha_pago);
        try {
            Statement st = con.conexion();
            String ins_caja = "insert into caja_chica value ('" + nro_caja + "', '" + id_tienda + "',  '" + empresa + "', '" + fecha_pago + "', '" + moneda + "', '" + concepto + "', 'I', 'C', '" + destino + "',"
                    + "'" + monto_pago + "', '" + frm_menu.usu.getNick() + "', NOW())";
            System.out.println(ins_caja);
            con.actualiza(st, ins_caja);
            con.cerrar(st);
        } catch (Exception ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }

        if (monto_pago >= por_pagar) {
            try {
                Statement st = con.conexion();
                String upd_compra = "update ventas set fecha_pago = '" + fecha_pago + "', estado = '1' where id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and id_almacen = '" + id_tienda + "' and empresa =  '" + empresa + "'";
                System.out.println(upd_compra);
                con.actualiza(st, upd_compra);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
        }

        modelo_pagos(periodo, id_tienda, ped.getId(), id_moneda, total);
        cbx_jda_moneda.setEnabled(false);
        cbx_jda_tipopago.setEnabled(false);
        txt_jda_monto.setText("");
        txt_jda_tc.setText("");
        txt_jda_monto.setEnabled(false);
        txt_jda_tc.setEnabled(false);
        btn_jda_registrar.setEnabled(false);
        jd_reg_pago.dispose();
    }//GEN-LAST:event_btn_jda_registrarActionPerformed

    private void t_jd_pagosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_jd_pagosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (fila_pagos > -1) {
                String id_pago = t_jd_pagos.getValueAt(fila_pagos, 0).toString();
                boolean elimina_pago = false;
                try {
                    Statement st = con.conexion();
                    String del_fila_pago = "delete from pago_ventas where id_pago_ventas = '" + id_pago + "' and id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and id_almacen = '" + id_tienda + "' and empresa   '" + empresa + "'";
                    System.out.println(del_fila_pago);
                    con.actualiza(st, del_fila_pago);
                    con.cerrar(st);
                    elimina_pago = true;
                } catch (Exception e) {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                }
                if (elimina_pago) {
                    if (por_pagar < 0.01) {
                        try {
                            Statement st = con.conexion();
                            String upd_venta = "update ventas set fecha_pago = '7000-01-01', estado = '0' where id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and id_almacen = '" + id_tienda + "' and empresa =  '" + empresa + "'";
                            System.out.println(upd_venta);
                            con.actualiza(st, upd_venta);
                            con.cerrar(st);
                        } catch (Exception e) {
                            System.out.println(e);
                            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                        }
                    }
                }
                modelo_pagos(periodo, id_tienda, ped.getId(), id_moneda, total);
            } else {
                JOptionPane.showMessageDialog(null, "DOBLE CLIC PARA SELECCIONAR ESTA FILAs");
            }
        }
    }//GEN-LAST:event_t_jd_pagosKeyPressed

    private void t_jd_pagosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_jd_pagosMouseClicked
        if (evt.getClickCount() == 2) {
            fila_pagos = t_jd_pagos.getSelectedRow();
            JOptionPane.showMessageDialog(null, "Fila Seleccionada");
        }
    }//GEN-LAST:event_t_jd_pagosMouseClicked

    private void btn_jdb_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jdb_cerrarActionPerformed
        jd_ver_detalle_venta.dispose();
    }//GEN-LAST:event_btn_jdb_cerrarActionPerformed

    private void btn_anularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anularActionPerformed
        int confirmado = JOptionPane.showConfirmDialog(null, "¿Confirma anular la venta?");
        if (JOptionPane.OK_OPTION == confirmado) {
            //cargar datos generales de venta
            try {
                Statement st = con.conexion();
                String ver_datos = "select v.id_tido, v.serie, v.numero, v.fecha_venta, c.nro_documento, c.nombre, v.id_cliente, v.nombre_cliente from ventas as v inner join clientes as c on v.id_cliente = c.id_cliente "
                        + "where v.id_venta = '" + ped.getId() + "' and v.periodo = '" + periodo + "' and v.id_almacen = '" + id_tienda + "' and v.empresa = '" + empresa + "'";
                System.out.println(ver_datos);
                ResultSet rs = con.consulta(st, ver_datos);
                if (rs.next()) {
                    cli.setNro_documento(rs.getString("nro_documento"));
                    if (rs.getString("v.nombre_cliente").equals("")) {
                        cli.setDatos(rs.getString("nombre"));
                    } else {
                        cli.setDatos(rs.getString("v.nombre_cliente"));
                    }
                    tido.setId(rs.getInt("id_tido"));
                    tido.setDesc(t_facturas.getValueAt(i, 4).toString());
                    tido.setSer(rs.getInt("serie"));
                    tido.setNro(rs.getInt("numero"));
                    ped.setFecha_venta(rs.getString("fecha_venta"));
                }
                con.cerrar(rs);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }

            //ver detalle de venta
            try {

                Statement st_dv = con.conexion();
                String ver_detalle = "select dv.producto, dv.cantidad, dv.precio, p.tipo_producto from detalle_venta as dv inner join productos as p on dv.producto = p.id_producto where dv.venta = '" + ped.getId() + "' and dv.periodo = '" + periodo + "' and "
                        + "dv.tienda = '" + id_tienda + "' and empresa = '" + empresa + "' and p.tipo_producto = 'BIEN'";
                System.out.println(ver_detalle);
                ResultSet rs_dv = con.consulta(st_dv, ver_detalle);
                while (rs_dv.next()) {
                    mat.setId(rs_dv.getInt("producto"));
                    mat.setCan(rs_dv.getDouble("cantidad"));
                    mat.setPrecio(rs_dv.getDouble("precio"));
                    //seleccionar cantidad actual
                    try {
                        Statement st = con.conexion();
                        String ver_cant = "select cantidad_actual from productos_almacenes where id_producto = '" + mat.getId() + "' and id_almacen = '" + id_tienda + "' and empresa = '" + empresa + "'";
                        System.out.println(ver_cant);
                        ResultSet rs = con.consulta(st, ver_cant);
                        if (rs.next()) {
                            mat.setCant_act(rs.getDouble("cantidad_actual"));
                        }
                        con.cerrar(st);
                        con.cerrar(rs);
                    } catch (SQLException e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                    }

                    //aumentar y modificar cantidad actual
                    mat.setCant_act(mat.getCant_act() + mat.getCan());
                    try {
                        Statement st = con.conexion();
                        String act_cantidad = "update productos_almacenes set cantidad_actual = '" + mat.getCant_act() + "' where id_producto = '" + mat.getId() + "' "
                                + "and id_almacen = '" + id_tienda + "' and empresa = '" + empresa + "'";
                        System.out.println(act_cantidad);
                        con.actualiza(st, act_cantidad);
                        con.cerrar(st);
                    } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                    }

                    int id_kardex = 0;
                    //seleccionar id de kardex
                    try {
                        Statement st = con.conexion();
                        String bus_idk = "select id_kardex from kardex where id_producto = '" + mat.getId() + "' and id_almacen = '" + id_tienda + "'  and empresa = '" + empresa + "' order by id_kardex desc limit 1";
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
                        Statement sti = con.conexion();
                        String ins_kardex = "insert into kardex Values ('" + id_kardex + "', '" + id_tienda + "', '" + empresa + "','" + mat.getId() + "', '" + fecha_hoy + "', '" + cli.getNro_documento() + "', "
                                + "'" + cli.getDatos() + "', '" + tido.getId() + "', '" + tido.getSer() + "', '" + tido.getNro() + "', '" + mat.getCan() + "', '" + mat.getPrecio() + "', '0.00', '0.00',  '5', NOW())";
                        System.out.println(ins_kardex);
                        con.actualiza(sti, ins_kardex);
                        con.cerrar(sti);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }
                }
                con.cerrar(rs_dv);
                con.cerrar(st_dv);

            } catch (SQLException e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());

            }

            //recorrer pagos y devolver dinero a caja
            Cl_Caja caj = new Cl_Caja();
            double total_monto = 0;
            try {
                Statement st_caja = con.conexion();
                String ver_pagos = "select * from pago_ventas where id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and "
                        + "id_almacen = '" + id_tienda + "' and empresa = '" + empresa + "'";
                ResultSet rs_caja = con.consulta(st_caja, ver_pagos);
                while (rs_caja.next()) {
                    double monto = rs_caja.getDouble("monto");
                    total_monto += monto;
                    String fecha = rs_caja.getString("fecha");
                    int moneda = rs_caja.getInt("id_moneda");
                    double tc = rs_caja.getInt("tc_venta");
                    String tipo = rs_caja.getString("tipo_pago");
                    String destino;
                    if (tipo.equals("EFECTIVO")) {
                        destino = "E";
                    } else {
                        destino = "T";
                    }
                    //REGISTRAR MOVIMIENTO EN CAJA CHICA
                    /*String concepto = "ANULACION DE VENTA - " + tido.getDesc() + "/  " + tido.getSer() + "-" + tido.getNro();
                     int nro_caja = caj.codigo_movimiento(fecha);
                     try {
                     Statement st = con.conexion();
                     String ins_caja = "insert into caja_chica value ('" + nro_caja + "', '" + id_tienda + "', '" + empresa + "', '" + fecha + "', '" + moneda + "', '" + concepto + "', 'E', 'V', '" + destino + "',"
                     + "'" + monto + "', '" + frm_menu.usu.getNick() + "', NOW())";
                     System.out.println(ins_caja);
                     con.actualiza(st, ins_caja);
                     con.cerrar(st);
                     } catch (Exception ex) {
                     System.out.print(ex);
                     JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                     }*/
                }
                con.cerrar(rs_caja);
                con.cerrar(st_caja);
            } catch (SQLException | HeadlessException e) {
                System.out.println(e.getLocalizedMessage());
            }

            //emitir vale por dinero en el aire
            //VA201701001  --- VR201701001
            //OBTENER ULTIMO VALE EMITIDO
            String ultimo_vale = "";
            String fechav = ven.getFechaActual();
            String anio_periodo = fechav.charAt(0) + "" + fechav.charAt(1) + fechav.charAt(2) + fechav.charAt(3);
            String mes_periodo = fechav.charAt(5) + "" + fechav.charAt(6);
            String periodovale = anio_periodo + "" + mes_periodo;
            try {
                Statement stv = con.conexion();
                String c_vale = "select id_cupon_pago from cupon_pago where id_cupon_pago like 'VA%' order by id_cupon_pago desc limit 1";
                ResultSet rsv = con.consulta(stv, c_vale);
                if (rsv.next()) {
                    ultimo_vale = rsv.getString("id_cupon_pago");
                } else {
                    ultimo_vale = "VA" + periodovale + "000";
                }
                con.cerrar(rsv);
                con.cerrar(stv);
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }

            //generar vale
            String codigovale = ultimo_vale.substring(8, 11);
            int codigocupon = Integer.parseInt(codigovale) + 1;
            String nuevovale = "VA" + periodovale + ven.ceros_izquierda(3, codigocupon + "");
            JOptionPane.showMessageDialog(null, "Codigo del Vale Generado por Anulacion de Venta\n" + nuevovale, "INFORMATION_MESSAGE", JOptionPane.WARNING_MESSAGE);
            try {
                Statement stv = con.conexion();
                String i_vale = "insert into cupon_pago values ('" + nuevovale + "', '" + fechav + "', '" + total_monto + "', '90','0')";
                con.actualiza(stv, i_vale);
                con.cerrar(stv);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }

            //REGISTRAR EN TABLA VENTAS_ELIMINADAS
            try {
                String motivo = cbx_motivo.getSelectedItem().toString();
                Statement st = con.conexion();
                String i_eliminada = "insert into venta_eliminada values ('" + ped.getId() + "', '" + periodo + "', '" + empresa + "', '" + id_tienda + "', '" + fechav + "', '" + nuevovale + "', '" + total_monto + "', '" + motivo + "', '" + frm_menu.usu.getNick() + "')";
                con.actualiza(st, i_eliminada);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }

            //eliminar pagos
            try {
                Statement st = con.conexion();
                String eli_pagos = "delete from pago_ventas where id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and "
                        + "id_almacen = '" + id_tienda + "' and empresa  = '" + empresa + "'";
                System.out.println(eli_pagos);
                con.actualiza(st, eli_pagos);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }

            //eliminar detalle de venta
            /*try {
             Statement st = con.conexion();
             String eli_detalle = "delete from detalle_venta where id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and "
             + "id_almacen = '" + id_tienda + "'";
             System.out.println(eli_detalle);
             con.actualiza(st, eli_detalle);
             con.cerrar(st);
             } catch (Exception e) {
             System.out.println(e.getLocalizedMessage());
             }*/
            //actualizar venta
            try {
                Statement st = con.conexion();
                String act_venta = "update ventas set fecha_pago = '7000-01-01', total = '0.0', estado = '3' where id_venta = '" + ped.getId() + "' and periodo = '" + periodo + "' and "
                        + "id_almacen = '" + id_tienda + "' and empresa = '" + empresa + "'";
                System.out.println(act_venta);
                con.actualiza(st, act_venta);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
            this.jd_anular_venta.dispose();

            Notification.show("Ventas", "Se Anulo el Documento " + tido.getDesc() + " de Serie: " + tido.getSer() + " - Numero: " + tido.getNro() + ", CORRECTAMENTE", Notification.NICON_LIGHT_THEME);

            //cargar tabla
            String ver_ped = "select v.id_venta, v.periodo, td.nombre as tipo_documento, v.serie, v.numero, v.fecha_venta, v.fecha_registro, c.nro_documento, v.nombre_cliente, c.nombre,"
                    + "v.total, v.estado from ventas as v inner join tipo_documento as td on v.id_tido = td.id_tido "
                    + "inner join clientes as c on v.id_cliente = c.id_cliente where v.id_almacen = '" + id_tienda + "' and v.empresa = '" + empresa + "' and v.fecha_venta = current_date() order by v.id_venta desc";
            ver_pedidos(ver_ped);

        }
    }//GEN-LAST:event_btn_anularActionPerformed

    private void cbx_motivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_motivoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn_anular.setEnabled(true);
            btn_anular.requestFocus();
        }
    }//GEN-LAST:event_cbx_motivoKeyPressed

    private void btn_motivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_motivoActionPerformed
        jd_detalle_anulado.setModal(true);
        jd_detalle_anulado.setSize(575, 186);
        jd_detalle_anulado.setLocationRelativeTo(null);
        String idventa = t_facturas.getValueAt(i, 0).toString();
        String periodov = t_facturas.getValueAt(i, 1).toString();
        String documento = t_facturas.getValueAt(i, 4).toString();
        String serie = t_facturas.getValueAt(i, 5).toString();
        String numero = t_facturas.getValueAt(i, 6).toString();
        String fecha_venta = t_facturas.getValueAt(i, 3).toString();
        try {
            Statement st = con.conexion();
            String c_eliminado = "select * from venta_eliminada where idventa = '" + idventa + "' and periodo = '" + periodov + "' and empresa = '" + empresa + "' and tienda= '" + id_tienda + "'";
            ResultSet rs = con.consulta(st, c_eliminado);
            if (rs.next()) {
                txt_jda_documento.setText(documento);
                txt_jda_nrodoc.setText(serie + " - " + numero);
                txt_jda_fechadoc.setText(fecha_venta);
                txt_jda_motivo.setText(rs.getString("motivo"));
                txt_jda_montoanu.setText(ven.formato_numero(rs.getDouble("monto")));
                txt_jda_fechaanu.setText(ven.fechaformateada(rs.getString("fecha")));
                txt_jda_vale.setText(rs.getString("vale"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        jd_detalle_anulado.setVisible(true);
    }//GEN-LAST:event_btn_motivoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_anu;
    private javax.swing.JButton btn_anular;
    private javax.swing.JButton btn_cobro;
    private javax.swing.JButton btn_detalle;
    private javax.swing.JButton btn_jd_cerrar;
    private javax.swing.JButton btn_jd_registrar;
    private javax.swing.JButton btn_jda_registrar;
    private javax.swing.JButton btn_jdb_cerrar;
    private javax.swing.JButton btn_motivo;
    private javax.swing.JComboBox cbx_est;
    private javax.swing.JComboBox cbx_estado;
    private javax.swing.JComboBox cbx_jd_moneda;
    private javax.swing.JComboBox cbx_jd_tido;
    private javax.swing.JComboBox cbx_jda_moneda;
    private javax.swing.JComboBox cbx_jda_tipopago;
    private javax.swing.JComboBox cbx_jdb_mon;
    private javax.swing.JComboBox cbx_jdb_tido;
    private javax.swing.JComboBox cbx_motivo;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JDialog jd_anular_venta;
    private javax.swing.JDialog jd_detalle_anulado;
    private javax.swing.JDialog jd_reg_pago;
    private javax.swing.JDialog jd_ver_detalle_venta;
    private javax.swing.JDialog jd_ver_pagos;
    private javax.swing.JTable t_facturas;
    private javax.swing.JTable t_jd_pagos;
    private javax.swing.JTable t_jdb_detalle;
    private javax.swing.JTextField txt_bus;
    private javax.swing.JTextField txt_jd_cliente;
    private javax.swing.JTextField txt_jd_igv;
    private javax.swing.JTextField txt_jd_iventa;
    private javax.swing.JTextField txt_jd_numero;
    private javax.swing.JTextField txt_jd_periodo;
    private javax.swing.JTextField txt_jd_placa;
    private javax.swing.JTextField txt_jd_porcobrar;
    private javax.swing.JTextField txt_jd_serie;
    private javax.swing.JTextField txt_jd_subtotal;
    private javax.swing.JTextField txt_jd_total;
    private javax.swing.JTextField txt_jda_documento;
    private javax.swing.JFormattedTextField txt_jda_fecha;
    private javax.swing.JTextField txt_jda_fechaanu;
    private javax.swing.JTextField txt_jda_fechadoc;
    private javax.swing.JTextField txt_jda_monto;
    private javax.swing.JTextField txt_jda_montoanu;
    private javax.swing.JTextField txt_jda_motivo;
    private javax.swing.JTextField txt_jda_nrodoc;
    private javax.swing.JTextField txt_jda_tc;
    private javax.swing.JTextField txt_jda_vale;
    private javax.swing.JFormattedTextField txt_jdb_fecha;
    private javax.swing.JTextField txt_jdb_nro;
    private javax.swing.JTextField txt_jdb_serie;
    private javax.swing.JTextField txt_jdb_total;
    private javax.swing.JTextField txt_tot;
    // End of variables declaration//GEN-END:variables
}
