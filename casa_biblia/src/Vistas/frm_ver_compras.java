/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Clases.Cl_Conectar;
import Clases.Cl_Moneda;
import Clases.Cl_Varios;
import Clases.render_compras;
import Forms.frm_reg_compra;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import casa_biblia.frm_menu;
import java.sql.Array;
import java.util.ArrayList;

/**
 *
 * @author CONTABILIDAD 02
 */
public class frm_ver_compras extends javax.swing.JInternalFrame {

    Cl_Varios ven = new Cl_Varios();
    Cl_Conectar con = new Cl_Conectar();
    Cl_Moneda mon = new Cl_Moneda();
    DefaultTableModel mostrar;
    DefaultTableModel mostrar_pagos;
    String periodo;
    int i;
    int fila_pagos = -1;
    int moneda_id = 0;
    Double sub_total = 0.0;
    Double por_pagar = 0.0;
    String id_compra;
    String periodo_compra;
    int tienda = frm_menu.alm.getCodigo();
    String empresa;
    String estado_fila;

    /**
     * Creates new form frm_ver_compra
     */
    public frm_ver_compras() {
        initComponents();
        Calendar fecha = Calendar.getInstance();
        int anio = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        periodo = String.format("%02d", mes) + "-" + anio;
        System.out.println(periodo);
        String ver_com = "select c.id_compra, c.periodo, c.empresa, c.fecha_compra, c.fecha_pago, c.afecto, c.igv, td.nombre as tipo_documento, c.serie, c.numero, c.ruc_proveedor, p.razon_social, m.nombre as moneda, "
                + "m.corto, c.sub_total, c.tc_compra, c.estado from compras as c inner join proveedores as p on c.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on "
                + "c.id_tido = td.id_tido inner join moneda as m on c.id_moneda = m.id_moneda where c.periodo = '" + periodo + "'";
        System.out.println(ver_com);
        ver_compras(ver_com);

        txt_tot.setText(ven.formato_totales(sumatoria(cbx_mon.getSelectedIndex())));

        String ver_tido = "select nombre from tipo_documento";
        ver_tipodoc(ver_tido);

        String ver_mon = "select nombre from moneda order by id_moneda asc";
        ver_moneda(ver_mon);
    }

    private void ver_compras(String ver_com) {
        mostrar = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        mostrar.addColumn("Id");
        mostrar.addColumn("Periodo");
        mostrar.addColumn("Empresa");
        mostrar.addColumn("Fec. Com.");
        mostrar.addColumn("Fec. Pago");
        mostrar.addColumn("Tipo Doc.");
        mostrar.addColumn("Ser.");
        mostrar.addColumn("Nro.");
        mostrar.addColumn("RUC");
        mostrar.addColumn("Razon Social");
        mostrar.addColumn("Moneda");
        mostrar.addColumn("Base");
        mostrar.addColumn("Base M.N");
        mostrar.addColumn("IGV");
        mostrar.addColumn("Total");
        mostrar.addColumn("Deuda");
        mostrar.addColumn("Estado");
        try {
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, ver_com);
            Object fila[] = new Object[17];
            double incrementa_suma_pagos = 0;
            while (rs.next()) {
                fila[0] = rs.getInt("id_compra");
                fila[1] = rs.getString("periodo");
                fila[2] = rs.getString("empresa");
                fila[3] = ven.fechaformateada(rs.getString("fecha_compra"));
                String fecha_pago = rs.getString("fecha_pago");
                if (fecha_pago.equals("7000-01-01")) {
                    fila[4] = "-";
                } else {
                    fila[4] = ven.fechaformateada(fecha_pago);
                }
                fila[5] = rs.getString("tipo_documento");
                fila[6] = rs.getString("serie");
                fila[7] = rs.getInt("numero");
                fila[8] = rs.getString("ruc_proveedor");
                fila[9] = rs.getString("razon_social");
                fila[10] = rs.getString("corto");
                fila[11] = ven.formato_numero(rs.getDouble("sub_total"));
                Double base;
                Double igv = rs.getDouble("igv");
                String afecto = rs.getString("afecto");
                double total = 0;
                double suma_pagos = 0;
                if (rs.getString("corto").equals("US$")) {
                    base = rs.getDouble("sub_total") * rs.getDouble("tc_compra");
                } else {
                    base = rs.getDouble("sub_total");
                }
                fila[12] = ven.formato_numero(base);
                if (afecto.equals("1")) {
                    fila[13] = ven.formato_numero(igv);
                    fila[14] = ven.formato_numero(base + igv);
                    total = base + igv;
                } else {
                    fila[13] = ven.formato_numero(0.0);
                    fila[14] = ven.formato_numero(base + igv);
                    total = base + igv;
                }

                if (rs.getString("estado").equals("0")) {
                    suma_pagos = pagado(rs.getString("empresa"), rs.getInt("id_compra"), rs.getString("periodo"));
                    fila[15] = ven.formato_numero(total - suma_pagos);
                    fila[16] = "PENDIENTE";
                } else {
                    fila[15] = "0.00";
                    fila[16] = "PAGADO";
                }
                incrementa_suma_pagos += (total - suma_pagos);
                mostrar.addRow(fila);
            }
            t_compras.setModel(mostrar);
            txt_pendiente.setText(ven.formato_totales(incrementa_suma_pagos));
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        t_compras.getColumnModel().getColumn(0).setPreferredWidth(40);
        t_compras.getColumnModel().getColumn(1).setPreferredWidth(70);
        t_compras.getColumnModel().getColumn(2).setPreferredWidth(100);
        t_compras.getColumnModel().getColumn(3).setPreferredWidth(80);
        t_compras.getColumnModel().getColumn(4).setPreferredWidth(80);
        t_compras.getColumnModel().getColumn(5).setPreferredWidth(100);
        t_compras.getColumnModel().getColumn(6).setPreferredWidth(70);
        t_compras.getColumnModel().getColumn(7).setPreferredWidth(90);
        t_compras.getColumnModel().getColumn(8).setPreferredWidth(100);
        t_compras.getColumnModel().getColumn(9).setPreferredWidth(300);
        t_compras.getColumnModel().getColumn(10).setPreferredWidth(40);
        t_compras.getColumnModel().getColumn(11).setPreferredWidth(80);
        t_compras.getColumnModel().getColumn(12).setPreferredWidth(80);
        t_compras.getColumnModel().getColumn(13).setPreferredWidth(80);
        t_compras.getColumnModel().getColumn(14).setPreferredWidth(80);
        t_compras.getColumnModel().getColumn(15).setPreferredWidth(80);
        t_compras.getColumnModel().getColumn(16).setPreferredWidth(80);
        t_compras.setDefaultRenderer(Object.class, new render_compras());
    }

    private Double sumatoria(int moneda) {
        Double suma = 0.0;
        int nro_filas = t_compras.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            String fecha = t_compras.getValueAt(j, 3).toString();
            String nom_mon = t_compras.getValueAt(j, 10).toString();
            Double valor = Double.parseDouble(t_compras.getValueAt(j, 14).toString());
            double convertido;
            if (!fecha.equals("-")) {
                if (moneda == 1) {
                    if (nom_mon.equals("US$")) {
                        convertido = mon.cambio_venta_sol(ven.fechabase(fecha), valor);
                        suma += convertido;
                    } else {
                        suma += valor;
                    }
                } else {
                    if (nom_mon.equals("S/")) {
                        convertido = mon.cambio_venta_dolar(ven.fechabase(fecha), valor);
                        suma += convertido;
                    } else {
                        suma += valor;
                    }
                }
            } else {
                suma += 0.0;
            }
        }
        return suma;
    }

    private double pagado(String empresa, int compra, String periodo) {
        double pagado = 0.0;
        try {
            Statement st = con.conexion();
            String c_pagos = "select sum(monto * tc_compra) as pagos from pago_compras where id_compra = '" + compra + "' and periodo = '" + periodo + "' and empresa = '" + empresa + "'";
            ResultSet rs = con.consulta(st, c_pagos);
            if (rs.next()) {
                pagado = rs.getDouble("pagos");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return pagado;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jd_pagos_compras = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        txt_proveedor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_jd_id = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_jd_periodo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbx_jd_moneda = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_jd_subtotal = new javax.swing.JTextField();
        txt_jd_igv = new javax.swing.JTextField();
        txt_jd_total = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbx_jd_tido = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_jd_serie = new javax.swing.JTextField();
        txt_jd_numero = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txt_jd_pendiente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_pagos_compra = new javax.swing.JTable();
        btn_jd_cerrar = new javax.swing.JButton();
        btn_jd_add = new javax.swing.JButton();
        jd_add_pago = new javax.swing.JDialog();
        jLabel14 = new javax.swing.JLabel();
        txt_jda_fec = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        cbx_jda_tipa = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cbx_jda_mon = new javax.swing.JComboBox();
        txt_jda_monto = new javax.swing.JTextField();
        btn_jda_reg = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txt_jda_tc = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txt_bus = new javax.swing.JTextField();
        btn_reg = new javax.swing.JButton();
        cbx_bus = new javax.swing.JComboBox();
        cbx_est = new javax.swing.JComboBox();
        btn_pag = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_compras = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_tot = new javax.swing.JTextField();
        cbx_mon = new javax.swing.JComboBox();
        btn_eli = new javax.swing.JButton();
        btn_modificar = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txt_pendiente = new javax.swing.JTextField();

        jd_pagos_compras.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_pagos_compras.setResizable(false);

        jLabel3.setText("Proveedor");

        txt_proveedor.setEnabled(false);

        jLabel4.setText("Id:");

        txt_jd_id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_id.setEnabled(false);

        jLabel5.setText("Periodo:");

        txt_jd_periodo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_periodo.setEnabled(false);

        jLabel6.setText("Moneda:");

        cbx_jd_moneda.setEnabled(false);

        jLabel7.setText("Sub Total");

        jLabel8.setText("IGV");

        jLabel9.setText("Total:");

        txt_jd_subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_subtotal.setEnabled(false);

        txt_jd_igv.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_igv.setEnabled(false);

        txt_jd_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_total.setEnabled(false);

        jLabel10.setText("Tipo Documento");

        cbx_jd_tido.setEnabled(false);

        jLabel11.setText("Serie:");

        jLabel12.setText("Numero:");

        txt_jd_serie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_serie.setEnabled(false);

        txt_jd_numero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_numero.setEnabled(false);

        jLabel13.setText("Por Pagar:");

        txt_jd_pendiente.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_pendiente.setEnabled(false);

        t_pagos_compra.setModel(new javax.swing.table.DefaultTableModel(
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
        t_pagos_compra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_pagos_compraMouseClicked(evt);
            }
        });
        t_pagos_compra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_pagos_compraKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(t_pagos_compra);

        btn_jd_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_jd_cerrar.setText("Cerrar");
        btn_jd_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_cerrarActionPerformed(evt);
            }
        });

        btn_jd_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/add.png"))); // NOI18N
        btn_jd_add.setText("Agregar Pago");
        btn_jd_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_pagos_comprasLayout = new javax.swing.GroupLayout(jd_pagos_compras.getContentPane());
        jd_pagos_compras.getContentPane().setLayout(jd_pagos_comprasLayout);
        jd_pagos_comprasLayout.setHorizontalGroup(
            jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_pagos_comprasLayout.createSequentialGroup()
                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jd_pagos_comprasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                            .addGroup(jd_pagos_comprasLayout.createSequentialGroup()
                                .addComponent(btn_jd_add)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_jd_cerrar))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jd_pagos_comprasLayout.createSequentialGroup()
                        .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jd_pagos_comprasLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7)))
                            .addGroup(jd_pagos_comprasLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4)))
                        .addGap(18, 18, 18)
                        .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_jd_subtotal, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addComponent(txt_jd_igv)
                                .addComponent(txt_jd_total))
                            .addComponent(txt_jd_id, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbx_jd_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbx_jd_tido, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jd_pagos_comprasLayout.createSequentialGroup()
                                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_jd_periodo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txt_jd_pendiente, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                        .addComponent(txt_jd_numero)
                                        .addComponent(txt_jd_serie)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jd_pagos_comprasLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_proveedor)))
                .addContainerGap())
        );
        jd_pagos_comprasLayout.setVerticalGroup(
            jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_pagos_comprasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_id, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_periodo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jd_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jd_tido, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_igv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_total, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_pendiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jd_pagos_comprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_jd_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_jd_add, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jd_add_pago.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_add_pago.setResizable(false);

        jLabel14.setText("Fecha");

        try {
            txt_jda_fec.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_jda_fec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jda_fec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jda_fecKeyPressed(evt);
            }
        });

        jLabel15.setText("Tipo Pago");

        cbx_jda_tipa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "EFECTIVO", "TARJETA", "VALE DESCUENTO" }));
        cbx_jda_tipa.setEnabled(false);
        cbx_jda_tipa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_jda_tipaKeyPressed(evt);
            }
        });

        jLabel16.setText("Monto:");

        jLabel17.setText("Moneda:");

        cbx_jda_mon.setEnabled(false);
        cbx_jda_mon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_jda_monKeyPressed(evt);
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

        btn_jda_reg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/accept.png"))); // NOI18N
        btn_jda_reg.setText("Registrar");
        btn_jda_reg.setEnabled(false);
        btn_jda_reg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jda_regActionPerformed(evt);
            }
        });

        jLabel18.setText("Tc.");

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

        javax.swing.GroupLayout jd_add_pagoLayout = new javax.swing.GroupLayout(jd_add_pago.getContentPane());
        jd_add_pago.getContentPane().setLayout(jd_add_pagoLayout);
        jd_add_pagoLayout.setHorizontalGroup(
            jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_add_pagoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addGroup(jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btn_jda_reg)
                        .addGroup(jd_add_pagoLayout.createSequentialGroup()
                            .addGroup(jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel14)
                                .addComponent(jLabel15))
                            .addGap(14, 14, 14)
                            .addGroup(jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cbx_jda_tipa, 0, 120, Short.MAX_VALUE)
                                .addComponent(cbx_jda_mon, 0, 120, Short.MAX_VALUE)
                                .addComponent(txt_jda_fec, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                .addComponent(txt_jda_monto, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                .addComponent(txt_jda_tc))))
                    .addComponent(jLabel18))
                .addContainerGap())
        );
        jd_add_pagoLayout.setVerticalGroup(
            jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_add_pagoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_fec, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jda_tipa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jda_mon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_tc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jda_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_jda_reg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setTitle("Listar Registro de Compras");

        jLabel1.setText("Buscar:");

        txt_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_busKeyPressed(evt);
            }
        });

        btn_reg.setText("Registrar");
        btn_reg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regActionPerformed(evt);
            }
        });

        cbx_bus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PROVEEDOR", "PERIODO", "NRO DOCUMENTO" }));
        cbx_bus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_busActionPerformed(evt);
            }
        });

        cbx_est.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PENDIENTES DE PAGO", "ACTUALES" }));
        cbx_est.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_estActionPerformed(evt);
            }
        });

        btn_pag.setText("Ver Pagos");
        btn_pag.setEnabled(false);
        btn_pag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pagActionPerformed(evt);
            }
        });

        t_compras.setModel(new javax.swing.table.DefaultTableModel(
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
        t_compras.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_compras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_comprasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_compras);

        jButton3.setText("Cerrar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setText("Total:");

        txt_tot.setEditable(false);
        txt_tot.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        cbx_mon.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DOLAR AMERICANO", "SOLES" }));
        cbx_mon.setSelectedIndex(1);
        cbx_mon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_monActionPerformed(evt);
            }
        });

        btn_eli.setText("Anular");
        btn_eli.setEnabled(false);
        btn_eli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliActionPerformed(evt);
            }
        });

        btn_modificar.setText("Modificar");
        btn_modificar.setEnabled(false);
        btn_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarActionPerformed(evt);
            }
        });

        jLabel19.setText("Monto Pendiente.");

        txt_pendiente.setEditable(false);
        txt_pendiente.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbx_est, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(btn_modificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_pag)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_reg))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_mon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_tot, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_eli)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_pendiente, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_reg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_est, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_pag, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_modificar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tot, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_mon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(txt_pendiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_regActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regActionPerformed
        frm_reg_compra compra = new frm_reg_compra();
        frm_reg_compra.accion = "registrar";
        ven.llamar_ventana(compra);
        this.dispose();
    }//GEN-LAST:event_btn_regActionPerformed

    private void btn_pagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pagActionPerformed
        jd_pagos_compras.setModal(true);
        jd_pagos_compras.setSize(560, 450);
        jd_pagos_compras.setLocationRelativeTo(null);

        if (estado_fila.equals("PENDIENTE")) {
            btn_jd_add.setEnabled(true);
        } else {
            btn_jd_add.setEnabled(false);
        }

        String ruc_proveedor = t_compras.getValueAt(i, 8).toString();
        String raz_proveedor = t_compras.getValueAt(i, 9).toString();
        id_compra = t_compras.getValueAt(i, 0).toString();
        periodo_compra = t_compras.getValueAt(i, 1).toString();
        empresa = t_compras.getValueAt(i, 2).toString();

        double igv = 0;

        int tido_id = 0;
        String serie = null;
        String numero = null;
        try {
            Statement st = con.conexion();
            String datos = "select id_tido, serie, numero, id_moneda, sub_total, igv from compras where id_compra = '" + id_compra + "' and periodo = '" + periodo_compra + "' and empresa = '" + empresa + "'";
            System.out.println(datos);
            ResultSet rs = con.consulta(st, datos);
            if (rs.next()) {
                tido_id = rs.getInt("id_tido");
                serie = rs.getString("serie");
                numero = rs.getString("numero");
                moneda_id = rs.getInt("id_moneda");
                sub_total = rs.getDouble("sub_total");
                igv = rs.getDouble("igv");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }

        txt_proveedor.setText(ruc_proveedor + " - " + raz_proveedor);
        txt_jd_id.setText(id_compra);
        txt_jd_serie.setText(serie);
        txt_jd_numero.setText(numero);
        cbx_jd_tido.setSelectedIndex(tido_id - 1);
        cbx_jd_moneda.setSelectedIndex(moneda_id - 1);
        txt_jd_subtotal.setText(ven.formato_totales(sub_total));
        txt_jd_igv.setText(ven.formato_totales(igv));
        txt_jd_total.setText(ven.formato_totales(sub_total + igv));

        txt_jd_periodo.setText(periodo_compra);

        modelo_pagos(periodo_compra, id_compra, empresa, moneda_id, sub_total + igv);

        jd_pagos_compras.setVisible(true);

        btn_pag.setEnabled(false);
    }//GEN-LAST:event_btn_pagActionPerformed

    private void modelo_pagos(String periodo, String id_compra, String empresa, int idmoneda, double total) {
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
            String ver_pagos = "select c.id_pago_compras, c.fecha, c.tc_compra, m.id_moneda as moneda, m.corto, c.monto, c.tipo_pago from pago_compras as c inner join"
                    + " moneda as m on c.id_moneda = m.id_moneda where c.id_compra = '" + id_compra + "' and c.periodo = '" + periodo + "' and c.empresa = '" + empresa + "'";
            ResultSet rs = con.consulta(st, ver_pagos);
            Object[] fila = new Object[7];
            Double suma_MN = 0.0;
            Double suma_ME = 0.0;
            while (rs.next()) {
                fila[0] = rs.getInt("id_pago_compras");
                fila[1] = rs.getString("tipo_pago");
                fila[2] = ven.fechaformateada(rs.getString("fecha"));
                fila[3] = rs.getString("corto");
                Double ME = 0.0;
                Double MN = 0.0;
                String id_mon = rs.getString("moneda");
                Double monto = rs.getDouble("monto");
                Double tc = rs.getDouble("tc_compra");
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
                fila[5] = ven.formato_tc(rs.getDouble("tc_compra"));
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
            txt_jd_pendiente.setText(ven.formato_totales(por_pagar));
            t_pagos_compra.setModel(mostrar_pagos);
            ven.centrar_celda(t_pagos_compra, 0);
            ven.centrar_celda(t_pagos_compra, 1);
            ven.centrar_celda(t_pagos_compra, 2);
            ven.centrar_celda(t_pagos_compra, 3);
            ven.derecha_celda(t_pagos_compra, 4);
            ven.derecha_celda(t_pagos_compra, 5);
            ven.derecha_celda(t_pagos_compra, 6);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }

    }

    private void ver_tipodoc(String query) {
        try {
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);

            while (rs.next()) {
                String fila;
                fila = rs.getString("nombre");
                cbx_jd_tido.addItem(fila);
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
                cbx_jda_mon.addItem(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
        } catch (SQLException e) {
            System.out.print(e);
        }
    }

    private void t_comprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_comprasMouseClicked
        i = t_compras.getSelectedRow();
        estado_fila = t_compras.getValueAt(i, 16).toString();
        btn_pag.setEnabled(true);
        btn_eli.setEnabled(true);
        btn_modificar.setEnabled(true);
    }//GEN-LAST:event_t_comprasMouseClicked

    private void txt_busKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_busKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String buscar = txt_bus.getText();
            if (cbx_bus.getSelectedIndex() == 0) {
                String ver_com = "select c.id_compra, c.periodo, c.empresa, c.fecha_compra, c.fecha_pago, c.afecto, c.igv, td.nombre as tipo_documento, c.serie, c.numero, c.ruc_proveedor, p.razon_social, m.nombre as moneda, "
                        + "m.corto, c.sub_total, c.tc_compra, c.estado from compras as c inner join proveedores as p on c.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on "
                        + "c.id_tido = td.id_tido inner join moneda as m on c.id_moneda = m.id_moneda where (c.ruc_proveedor like '%" + buscar + "%' or p.razon_social like '%" + buscar + "%')";
                ver_compras(ver_com);
            }
            if (cbx_bus.getSelectedIndex() == 1) {
                String ver_com = "select c.id_compra, c.periodo, c.empresa, c.fecha_compra, c.fecha_pago, c.afecto, c.igv, td.nombre as tipo_documento, c.serie, c.numero, c.ruc_proveedor, p.razon_social, m.nombre as moneda, "
                        + "m.corto, c.sub_total, c.tc_compra, c.estado from compras as c inner join proveedores as p on c.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on "
                        + "c.id_tido = td.id_tido inner join moneda as m on c.id_moneda = m.id_moneda where c.periodo = '" + buscar + "'";
                ver_compras(ver_com);
            }
            if (cbx_bus.getSelectedIndex() == 2) {
                String ver_com = "select c.id_compra, c.periodo, c.empresa, c.fecha_compra, c.fecha_pago, c.afecto, c.igv, td.nombre as tipo_documento, c.serie, c.numero, c.ruc_proveedor, p.razon_social, m.nombre as moneda, "
                        + "m.corto, c.sub_total, c.tc_compra, c.estado from compras as c inner join proveedores as p on c.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on "
                        + "c.id_tido = td.id_tido inner join moneda as m on c.id_moneda = m.id_moneda where c.numero like '%" + buscar + "%'";
                ver_compras(ver_com);
            }
            txt_tot.setText(ven.formato_totales(sumatoria(cbx_mon.getSelectedIndex())));
        }
    }//GEN-LAST:event_txt_busKeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cbx_estActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_estActionPerformed
        if (cbx_est.getSelectedIndex() == 0) {
            String ver_com = "select c.id_compra, c.periodo, c.empresa, c.fecha_compra, c.fecha_pago, c.afecto, c.igv, td.nombre as tipo_documento, c.serie, c.numero, c.ruc_proveedor, p.razon_social, m.nombre as moneda, "
                    + "m.corto, c.sub_total, c.tc_compra, c.estado from compras as c inner join proveedores as p on c.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on "
                    + "c.id_tido = td.id_tido inner join moneda as m on c.id_moneda = m.id_moneda where c.estado = '0'";
            ver_compras(ver_com);
        }
        if (cbx_est.getSelectedIndex() == 1) {
            String ver_com = "select c.id_compra, c.periodo, c.empresa, c.fecha_compra, c.fecha_pago, c.afecto, c.igv, td.nombre as tipo_documento, c.serie, c.numero, c.ruc_proveedor, p.razon_social, m.nombre as moneda, "
                    + "m.corto, c.sub_total, c.tc_compra, c.estado from compras as c inner join proveedores as p on c.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on "
                    + "c.id_tido = td.id_tido inner join moneda as m on c.id_moneda = m.id_moneda where c.periodo = '" + periodo + "'";
            ver_compras(ver_com);
        }
        txt_tot.setText(ven.formato_totales(sumatoria(cbx_mon.getSelectedIndex())));
    }//GEN-LAST:event_cbx_estActionPerformed

    private void btn_eliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliActionPerformed
        int confirmado = JOptionPane.showConfirmDialog(null, "¿Desea Eliminar la Compra Seleccionada?");

        if (JOptionPane.OK_OPTION == confirmado) {
            id_compra = t_compras.getValueAt(i, 0).toString();
            periodo_compra = t_compras.getValueAt(i, 1).toString();
            empresa = t_compras.getValueAt(i, 2).toString();
            //eliminar pagos
            try {
                Statement st = con.conexion();
                String del_cob = "delete from pago_compras where id_compra = '" + id_compra + "' and periodo = '" + periodo_compra + "' and empresa = '" + empresa + "'";
                System.out.println(del_cob);
                con.actualiza(st, del_cob);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }

            //eliminar compra
            try {
                Statement st = con.conexion();
                String anu_com = "delete from compras where id_compra = '" + id_compra + "' and periodo = '" + periodo_compra + "' and empresa = '" + empresa + "'";
                System.out.println(anu_com);
                con.actualiza(st, anu_com);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
            btn_eli.setEnabled(false);

            String ver_com = "select c.id_compra, c.periodo, c.empresa, c.fecha_compra, c.fecha_pago, c.afecto, c.igv, td.nombre as tipo_documento, c.serie, c.numero, c.ruc_proveedor, p.razon_social, m.nombre as moneda, "
                    + "m.corto, c.sub_total, c.tc_compra, c.estado from compras as c inner join proveedores as p on c.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on "
                    + "c.id_tido = td.id_tido inner join moneda as m on c.id_moneda = m.id_moneda where c.periodo = '" + periodo + "'";
            System.out.println(ver_com);
            ver_compras(ver_com);

            txt_tot.setText(ven.formato_totales(sumatoria(cbx_mon.getSelectedIndex())));
        }
    }//GEN-LAST:event_btn_eliActionPerformed

    private void cbx_monActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_monActionPerformed
        txt_tot.setText(ven.formato_totales(sumatoria(cbx_mon.getSelectedIndex())));
    }//GEN-LAST:event_cbx_monActionPerformed

    private void btn_jd_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_cerrarActionPerformed
        String ver_com = "select c.id_compra, c.periodo, c.empresa, c.fecha_compra, c.fecha_pago, c.afecto, c.igv, td.nombre as tipo_documento, c.serie, c.numero, c.ruc_proveedor, p.razon_social, m.nombre as moneda, "
                + "m.corto, c.sub_total, c.tc_compra, c.estado from compras as c inner join proveedores as p on c.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on "
                + "c.id_tido = td.id_tido inner join moneda as m on c.id_moneda = m.id_moneda where c.periodo = '" + periodo + "'";
        System.out.println(ver_com);
        ver_compras(ver_com);

        txt_tot.setText(ven.formato_totales(sumatoria(cbx_mon.getSelectedIndex())));
        jd_pagos_compras.dispose();
    }//GEN-LAST:event_btn_jd_cerrarActionPerformed

    private void btn_jd_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_addActionPerformed
        jd_add_pago.setModal(true);
        jd_add_pago.setSize(220, 270);
        jd_add_pago.setLocationRelativeTo(null);
        txt_jda_fec.setText(ven.fechaformateada(ven.getFechaActual()));
        jd_add_pago.setVisible(true);
    }//GEN-LAST:event_btn_jd_addActionPerformed

    private void txt_jda_fecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jda_fecKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_jda_fec.getText().length() == 10) {
                cbx_jda_tipa.setEnabled(true);
                cbx_jda_tipa.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_jda_fecKeyPressed

    private void cbx_jda_tipaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_jda_tipaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbx_jda_mon.setEnabled(true);
            cbx_jda_mon.requestFocus();
        }
    }//GEN-LAST:event_cbx_jda_tipaKeyPressed

    private void cbx_jda_monKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_jda_monKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cbx_jda_mon.getSelectedIndex() == 0) {
                txt_jda_tc.setText("1.000");
            }
            txt_jda_tc.setEnabled(true);
            txt_jda_tc.requestFocus();
        }
    }//GEN-LAST:event_cbx_jda_monKeyPressed

    private Double tc_compra(String fecha) {
        double tc = 0.0;
        try {
            Statement st = con.conexion();
            String ver_tc = "select compra from tipo_cambio where fecha = '" + fecha + "'";
            System.out.println(ver_tc);
            ResultSet rs = con.consulta(st, ver_tc);
            if (rs.next()) {
                tc = rs.getDouble("compra");
            } else {
                tc = 0.0;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return tc;
    }

    private void txt_jda_montoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jda_montoKeyTyped
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && car != '.') {
            evt.consume();
        }
    }//GEN-LAST:event_txt_jda_montoKeyTyped

    private void txt_jda_montoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jda_montoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_jda_monto.getText().length() > 1) {
                Double monto_recibido = Double.parseDouble(txt_jda_monto.getText());
                if (monto_recibido > por_pagar) {
                    txt_jda_monto.setText(ven.formato_numero(por_pagar));
                }
                btn_jda_reg.setEnabled(true);
                btn_jda_reg.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_jda_montoKeyPressed

    private void btn_jda_regActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jda_regActionPerformed
        String fecha_pago = ven.fechabase(txt_jda_fec.getText());
        String tipo_pago = cbx_jda_tipa.getSelectedItem().toString();
        int moneda = cbx_jda_mon.getSelectedIndex() + 1;
        Double tc_pago = Double.parseDouble(txt_jda_tc.getText());
        Double monto_pago = Double.parseDouble(txt_jda_monto.getText());

        int id_pago = 0;
        try {
            Statement st = con.conexion();
            String ver_cod = "select id_pago_compras from pago_compras where id_compra = '" + id_compra + "' and periodo = '" + periodo_compra + "' "
                    + " and empresa = '" + empresa + "' order by id_pago_compras desc limit 1";
            System.out.println(ver_cod);
            ResultSet rs = con.consulta(st, ver_cod);
            if (rs.next()) {
                id_pago = rs.getInt("id_pago_compras") + 1;
            } else {
                id_pago = 1;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            System.out.println(e);
        }

        try {
            Statement st = con.conexion();
            String ins_cuota = "insert into pago_compras values ('" + id_pago + "', '" + id_compra + "', '" + periodo_compra + "', '" + empresa + "', '" + fecha_pago + "', "
                    + "'" + moneda + "', '" + tc_pago + "', '" + monto_pago + "', '" + tipo_pago + "')";
            System.out.println(ins_cuota);
            con.actualiza(st, ins_cuota);
            con.cerrar(st);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            System.out.println(e);
        }

        if (monto_pago >= por_pagar) {
            try {
                Statement st = con.conexion();
                String upd_compra = "update compras set fecha_pago = '" + fecha_pago + "', estado = '1' where id_compra = '" + id_compra + "' and periodo = '" + periodo_compra + "'  and empresa = '" + empresa + "'";
                System.out.println(upd_compra);
                con.actualiza(st, upd_compra);
                con.cerrar(st);
                btn_jd_add.setEnabled(false);
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
        }

        modelo_pagos(periodo_compra, id_compra, empresa, moneda_id, sub_total);
        cbx_jda_mon.setEnabled(false);
        cbx_jda_tipa.setEnabled(false);
        txt_jda_monto.setText("");
        txt_jda_tc.setText("");
        txt_jda_monto.setEnabled(false);
        txt_jda_tc.setEnabled(false);
        btn_jda_reg.setEnabled(false);
        jd_add_pago.dispose();
    }//GEN-LAST:event_btn_jda_regActionPerformed

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

    private void t_pagos_compraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_pagos_compraMouseClicked
        if (evt.getClickCount() == 2) {
            fila_pagos = t_pagos_compra.getSelectedRow();
            JOptionPane.showMessageDialog(null, "Fila Seleccionada");
        }
    }//GEN-LAST:event_t_pagos_compraMouseClicked

    private void t_pagos_compraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_pagos_compraKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (fila_pagos > -1) {
                String id_pago = t_pagos_compra.getValueAt(fila_pagos, 0).toString();
                boolean elimina_pago = false;
                try {
                    Statement st = con.conexion();
                    String del_fila_pago = "delete from pago_compras where id_pago_compras = '" + id_pago + "' and id_compra = '" + id_compra + "' and periodo = '" + periodo_compra + "'  and empresa = '" + empresa + "'";
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
                            String upd_compra = "update compras set fecha_pago = '7000-01-01', estado = '0' where id_compra = '" + id_compra + "' and periodo = '" + periodo_compra + "'  and empresa = '" + empresa + "'";
                            System.out.println(upd_compra);
                            con.actualiza(st, upd_compra);
                            con.cerrar(st);
                            btn_jd_add.setEnabled(true);
                        } catch (Exception e) {
                            System.out.println(e);
                            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                        }
                    }
                }
                modelo_pagos(periodo_compra, id_compra, empresa, moneda_id, sub_total);
            } else {
                JOptionPane.showMessageDialog(null, "DOBLE CLIC PARA SELECCIONAR ESTA FILAs");
            }
        }
    }//GEN-LAST:event_t_pagos_compraKeyPressed

    private void cbx_busActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_busActionPerformed
        txt_bus.setText("");
        txt_bus.requestFocus();
    }//GEN-LAST:event_cbx_busActionPerformed

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        if (i > 0) {
            String cperiodo = t_compras.getValueAt(i, 1).toString().trim();
            String ccompra = t_compras.getValueAt(i, 0).toString().trim();
            String cempresa = t_compras.getValueAt(i, 2).toString().trim();

            frm_reg_compra compra = new frm_reg_compra();
            frm_reg_compra.accion = "modificar";
            try {
                Statement st = con.conexion();
                String c_compra = "select * from compras where periodo = '" + cperiodo + "' and id_compra = '" + ccompra + "' and empresa = '" + cempresa + "'";
                ResultSet rs = con.consulta(st, c_compra);
                if (rs.next()) {
                    frm_reg_compra.txt_per.setText(rs.getString("periodo"));
                    frm_reg_compra.cbx_empresas.setSelectedItem(rs.getString("empresa"));
                    frm_reg_compra.cbx_tido.setSelectedIndex(rs.getInt("id_tido") - 1);
                    frm_reg_compra.txt_ser.setText(rs.getString("serie").trim());
                    frm_reg_compra.txt_ndoc.setText(rs.getString("numero").trim());
                    frm_reg_compra.txt_glosa.setText(rs.getString("glosa").toUpperCase().trim());
                    frm_reg_compra.txt_fec_com.setText(ven.fechaformateada(rs.getString("fecha_compra")));
                    frm_reg_compra.txt_id.setText(rs.getString("id_compra"));
                    frm_reg_compra.txt_ruc.setText(rs.getString("ruc_proveedor"));
                    frm_reg_compra.cbx_mon.setSelectedIndex(rs.getInt("id_moneda") - 1);
                    frm_reg_compra.txt_tica.setText(ven.formato_tc(rs.getDouble("tc_compra")));
                    frm_reg_compra.txt_sub.setText(ven.formato_numero(rs.getDouble("sub_total")));
                    frm_reg_compra.txt_igv.setText(ven.formato_numero(rs.getDouble("igv")));
                    frm_reg_compra.txt_tot.setText(ven.formato_numero(rs.getDouble("igv") + rs.getDouble("sub_total")));
                    frm_reg_compra.txt_per.setEnabled(false);
                    frm_reg_compra.cbx_empresas.setEnabled(false);
                    frm_reg_compra.cbx_tido.setEnabled(false);
                    frm_reg_compra.txt_ser.setEnabled(false);
                    frm_reg_compra.txt_ndoc.setEnabled(false);
                    frm_reg_compra.txt_glosa.setEnabled(true);
                    frm_reg_compra.txt_fec_com.setEnabled(true);
                    frm_reg_compra.txt_ruc.setEnabled(false);
                    frm_reg_compra.cbx_mon.setEnabled(true);
                    frm_reg_compra.txt_tica.setEnabled(true);
                    frm_reg_compra.txt_sub.setEnabled(true);
                    frm_reg_compra.txt_igv.setEnabled(true);
                    frm_reg_compra.txt_tot.setEnabled(true);
                    frm_reg_compra.btn_reg.setEnabled(true);
                    frm_reg_compra.cbx_afecto.setEnabled(true);
                    int afecto = rs.getInt("afecto");
                    if (afecto == 0) {
                        frm_reg_compra.cbx_afecto.setSelected(true);
                    } else {
                        frm_reg_compra.cbx_afecto.setSelected(false);
                    }
                }
                con.cerrar(rs);
                con.cerrar(st);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
            ven.llamar_ventana(compra);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "NO SE HA SELECCIONADO UNA FILA");
        }
    }//GEN-LAST:event_btn_modificarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_eli;
    private javax.swing.JButton btn_jd_add;
    private javax.swing.JButton btn_jd_cerrar;
    private javax.swing.JButton btn_jda_reg;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JButton btn_pag;
    private javax.swing.JButton btn_reg;
    private javax.swing.JComboBox cbx_bus;
    private javax.swing.JComboBox cbx_est;
    private javax.swing.JComboBox cbx_jd_moneda;
    private javax.swing.JComboBox cbx_jd_tido;
    private javax.swing.JComboBox cbx_jda_mon;
    private javax.swing.JComboBox cbx_jda_tipa;
    private javax.swing.JComboBox cbx_mon;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JDialog jd_add_pago;
    private javax.swing.JDialog jd_pagos_compras;
    private javax.swing.JTable t_compras;
    private javax.swing.JTable t_pagos_compra;
    private javax.swing.JTextField txt_bus;
    private javax.swing.JTextField txt_jd_id;
    private javax.swing.JTextField txt_jd_igv;
    private javax.swing.JTextField txt_jd_numero;
    private javax.swing.JTextField txt_jd_pendiente;
    private javax.swing.JTextField txt_jd_periodo;
    private javax.swing.JTextField txt_jd_serie;
    private javax.swing.JTextField txt_jd_subtotal;
    private javax.swing.JTextField txt_jd_total;
    private javax.swing.JFormattedTextField txt_jda_fec;
    private javax.swing.JTextField txt_jda_monto;
    private javax.swing.JTextField txt_jda_tc;
    private javax.swing.JTextField txt_pendiente;
    private javax.swing.JTextField txt_proveedor;
    private javax.swing.JTextField txt_tot;
    // End of variables declaration//GEN-END:variables
}
