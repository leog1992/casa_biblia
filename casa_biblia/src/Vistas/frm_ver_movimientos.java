/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Generales.frm_rpt_fecha_unica;
import Clases.Cl_Caja;
import Clases.Cl_Conectar;
import Clases.Cl_Movimiento;
import Clases.Cl_Varios;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import casa_biblia.frm_menu;

/**
 *
 * @author pc
 */
public class frm_ver_movimientos extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    Cl_Movimiento mov = new Cl_Movimiento();
    Cl_Caja caj = new Cl_Caja();
    DefaultTableModel modelo_sol;
    DefaultTableModel modelo_dolar;
    Double suma_ing_sol;
    Double suma_sal_sol;
    Double suma_ing_dol;
    Double suma_sal_dol;
    Double suma_ing_sol_tar;
    Double suma_sal_sol_tar;
    Double suma_ing_dol_tar;
    Double suma_sal_dol_tar;
    public static String ventana = "movimientos";
    String fecha;
    int id_moneda;
    String origen, tipo, destino;
    int tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();

    /**
     * Creates new form frm_movimientos
     */
    public frm_ver_movimientos() {
        initComponents();
        fecha = ven.getFechaActual();
        mostrar_movimientos();
    }

    private void mostrar_movimientos() {
        String soles = "select * from caja_chica where fecha = '" + fecha + "' and id_moneda = '1' and empresa = '"+empresa+"' and id_almacen = '"+tienda+"' order by id_movimiento asc";
        System.out.println(soles);
        mov_soles(soles);
        sumar_ing();
        sumar_sal();
        Double total_sol;
        total_sol = suma_ing_sol - suma_sal_sol;
        txt_tot_sol.setText(ven.formato_totales(total_sol));
    }

    private void mov_soles(String query) {
        try {
            modelo_sol = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };

            modelo_sol.addColumn("Usuario");
            modelo_sol.addColumn("Destino");
            modelo_sol.addColumn("Descripcion");
            modelo_sol.addColumn("Fecha");
            modelo_sol.addColumn("Ingreso");
            modelo_sol.addColumn("Salida");

            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);

            //Creando las filas para el JTable
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getObject("usuario");
                String destino = rs.getString("tipo");
                if (destino.equals("E")) {
                    fila[1] = "EFECTIVO";
                } else {
                    fila[1] = "TARJETA";
                }
                fila[2] = rs.getObject("concepto");
                fila[3] = ven.fechaformateada(rs.getString("fecha"));
                String origen = rs.getString("origen");
                if (origen.equals("I")) {
                    fila[4] = ven.formato_numero(rs.getDouble("monto"));
                    fila[5] = "0.00";
                } else {
                    fila[4] = "0.00";
                    fila[5] = ven.formato_numero(rs.getDouble("monto"));
                }

                modelo_sol.addRow(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
            t_soles.setModel(modelo_sol);
            t_soles.getColumnModel().getColumn(0).setPreferredWidth(80);
            t_soles.getColumnModel().getColumn(1).setPreferredWidth(80);
            t_soles.getColumnModel().getColumn(2).setPreferredWidth(400);
            t_soles.getColumnModel().getColumn(3).setPreferredWidth(80);
            t_soles.getColumnModel().getColumn(4).setPreferredWidth(70);
            t_soles.getColumnModel().getColumn(5).setPreferredWidth(70);
            ven.centrar_celda(t_soles, 0);
            ven.centrar_celda(t_soles, 1);
            ven.centrar_celda(t_soles, 3);
            ven.derecha_celda(t_soles, 4);
            ven.derecha_celda(t_soles, 5);
            modelo_sol.fireTableDataChanged();
        } catch (SQLException e) {
            System.out.print(e);
        }

    }

    private void sumar_ing() {
        int tot_filas_soles = t_soles.getRowCount();
        suma_ing_sol = 0.00;
        suma_ing_sol_tar = 0.00;
        for (int x = 0; x < tot_filas_soles; x++) {
            String destino = t_soles.getValueAt(x, 1).toString();
            if (destino.equals("EFECTIVO")) {
                suma_ing_sol += Double.parseDouble(t_soles.getValueAt(x, 4).toString());
            } else {
                suma_ing_sol += Double.parseDouble(t_soles.getValueAt(x, 4).toString());
            }
        }
        txt_ing_sol.setText(ven.formato_numero(suma_ing_sol));
    }

    private void sumar_sal() {
        int tot_filas_soles = t_soles.getRowCount();
        suma_sal_sol = 0.00;
        suma_sal_sol_tar = 0.00;
        for (int x = 0; x < tot_filas_soles; x++) {
            String destino = t_soles.getValueAt(x, 1).toString();
            if (destino.equals("EFECTIVO")) {
                suma_sal_sol += Double.parseDouble(t_soles.getValueAt(x, 5).toString());
            } else {
                suma_sal_sol += Double.parseDouble(t_soles.getValueAt(x, 5).toString());
            }
        }
        txt_sal_sol.setText(ven.formato_numero(suma_sal_sol));
   }

    private void cargar_empleados() {
        try {
            TextAutoCompleter autocompletar_empleado = new TextAutoCompleter(txt_nom);
            autocompletar_empleado.setMode(0);
            Statement st = con.conexion();
            String sql = "select dni, nombres, ape_pat, ape_mat from empleados order by ape_pat asc, ape_mat asc, nombres asc";
            ResultSet rs = con.consulta(st, sql);
            while (rs.next()) {
                autocompletar_empleado.addItem(rs.getString("dni") + " - " + rs.getString("ape_pat") + " " + rs.getString("ape_mat") + " " + rs.getString("nombres"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error" + e.getLocalizedMessage());
            System.out.println(e);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jd_add_movimiento = new javax.swing.JDialog();
        jLabel12 = new javax.swing.JLabel();
        txt_nom = new javax.swing.JTextField();
        cbx_mon = new javax.swing.JComboBox();
        txt_dni = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_reg = new javax.swing.JButton();
        txt_monto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        rbt_sal = new javax.swing.JRadioButton();
        rbt_ing = new javax.swing.JRadioButton();
        txt_fecha = new javax.swing.JFormattedTextField();
        txt_mot = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btn_clo = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jsoles = new javax.swing.JScrollPane();
        t_soles = new javax.swing.JTable();
        txt_tot_sol = new javax.swing.JTextField();
        txt_sal_sol = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_ing_sol = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        jd_add_movimiento.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_add_movimiento.setTitle("AGREGAR MOVIMIENTOS A CAJA CHICA");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Moneda");

        txt_nom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nomKeyPressed(evt);
            }
        });

        cbx_mon.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NUEVO SOL", "DOLAR AMERICANO" }));
        cbx_mon.setEnabled(false);
        cbx_mon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_monKeyPressed(evt);
            }
        });

        txt_dni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_dni.setEnabled(false);
        txt_dni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_dniKeyPressed(evt);
            }
        });

        jLabel1.setText("Seleccionar Empleado");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Fecha");

        btn_reg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/add.png"))); // NOI18N
        btn_reg.setText("Aceptar");
        btn_reg.setEnabled(false);
        btn_reg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regActionPerformed(evt);
            }
        });
        btn_reg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_regKeyPressed(evt);
            }
        });

        txt_monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_monto.setEnabled(false);
        txt_monto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_montoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_montoKeyTyped(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Monto:");

        jLabel7.setText("Motivo:");

        buttonGroup1.add(rbt_sal);
        rbt_sal.setText("Registrar Salida de Dinero");
        rbt_sal.setEnabled(false);
        rbt_sal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbt_salActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbt_ing);
        rbt_ing.setText("Registrar Ingreso de Dinero");
        rbt_ing.setEnabled(false);
        rbt_ing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbt_ingActionPerformed(evt);
            }
        });

        try {
            txt_fecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_fecha.setEnabled(false);
        txt_fecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_fechaKeyPressed(evt);
            }
        });

        txt_mot.setEnabled(false);
        txt_mot.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_motKeyPressed(evt);
            }
        });

        jLabel2.setText("Seleccionar Operacion:");

        javax.swing.GroupLayout jd_add_movimientoLayout = new javax.swing.GroupLayout(jd_add_movimiento.getContentPane());
        jd_add_movimiento.getContentPane().setLayout(jd_add_movimientoLayout);
        jd_add_movimientoLayout.setHorizontalGroup(
            jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_add_movimientoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jd_add_movimientoLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(txt_mot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_reg))
                    .addGroup(jd_add_movimientoLayout.createSequentialGroup()
                        .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jd_add_movimientoLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbt_ing)
                                    .addComponent(rbt_sal))))
                        .addGap(84, 84, 84)
                        .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_fecha)
                            .addComponent(txt_monto)
                            .addComponent(cbx_mon, 0, 129, Short.MAX_VALUE)))
                    .addGroup(jd_add_movimientoLayout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(txt_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nom)))
                .addContainerGap())
        );
        jd_add_movimientoLayout.setVerticalGroup(
            jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_add_movimientoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txt_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(rbt_ing, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbt_sal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbx_mon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_movimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_mot, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_reg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setTitle("Detalle de Movimientos de Dinero");

        btn_clo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_clo.setText("Cerrar");
        btn_clo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cloActionPerformed(evt);
            }
        });

        t_soles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "LUIS OYANGUREN GIRON", "COMPRA DE FILTROS", "2014-11-09", "0.00", "135.00"},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id.", "Usuario", "Descripcion", "Fecha", "Ingreso", "Salida"
            }
        ));
        jsoles.setViewportView(t_soles);
        if (t_soles.getColumnModel().getColumnCount() > 0) {
            t_soles.getColumnModel().getColumn(0).setPreferredWidth(10);
            t_soles.getColumnModel().getColumn(1).setPreferredWidth(40);
            t_soles.getColumnModel().getColumn(2).setPreferredWidth(180);
            t_soles.getColumnModel().getColumn(3).setPreferredWidth(40);
            t_soles.getColumnModel().getColumn(4).setPreferredWidth(30);
            t_soles.getColumnModel().getColumn(5).setPreferredWidth(40);
        }

        txt_tot_sol.setEditable(false);
        txt_tot_sol.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txt_sal_sol.setEditable(false);
        txt_sal_sol.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel8.setText("Total");

        txt_ing_sol.setEditable(false);
        txt_ing_sol.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel6.setText("Total Salidas:");

        jLabel5.setText("Total Ingresos");

        jLabel16.setText("Efectivo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsoles)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txt_ing_sol, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_sal_sol, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_tot_sol, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel16)
                        .addGap(0, 28, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jsoles, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ing_sol, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_sal_sol, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tot_sol, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Caja chica Soles (PEN)", jPanel1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/add.png"))); // NOI18N
        jButton3.setText("Añadir Movimiento");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_clo))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_clo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbt_ingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbt_ingActionPerformed
        txt_monto.setEnabled(false);
        txt_mot.setText("");
        txt_mot.setEnabled(true);
        txt_mot.requestFocus();

    }//GEN-LAST:event_rbt_ingActionPerformed

    private void rbt_salActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbt_salActionPerformed
        txt_monto.setEnabled(false);
        txt_mot.setText("");
        txt_mot.setEnabled(true);
        txt_mot.requestFocus();
    }//GEN-LAST:event_rbt_salActionPerformed

    private void btn_cloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cloActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_cloActionPerformed

    private void txt_montoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_montoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt_fecha.setEnabled(true);
            txt_fecha.setText(ven.fechaformateada(ven.getFechaActual()));
            txt_fecha.requestFocus();
        }
    }//GEN-LAST:event_txt_montoKeyPressed

    private void btn_regKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_regKeyPressed

    }//GEN-LAST:event_btn_regKeyPressed

    private void txt_montoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_montoKeyTyped
        if (txt_monto.getText().length() == 8) {
            evt.consume();
        }
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && car != '.') {
            evt.consume();
        }
    }//GEN-LAST:event_txt_montoKeyTyped

    private void llenar() {
        mov.setGlosa(txt_mot.getText().toUpperCase() + " - " + txt_nom.getText());
        if (rbt_ing.isSelected()) {
            origen = "I";
            destino = "OI";
            tipo = "E";
        }
        if (rbt_sal.isSelected()) {
            origen = "E";
            destino = "OG";
            tipo = "E";
        }
        mov.setMonto(Double.parseDouble(txt_monto.getText()));
        mov.setFec(ven.fechabase(txt_fecha.getText()));
        id_moneda = cbx_mon.getSelectedIndex() + 1;
        tienda = frm_menu.alm.getCodigo();
    }

    private void limpiar() {
        txt_dni.setText("");
        txt_dni.setEnabled(false);
        cbx_mon.setEnabled(false);
        txt_nom.setText("");
        rbt_ing.setEnabled(false);
        rbt_sal.setEnabled(false);
        txt_mot.setText("");
        txt_mot.setEnabled(false);
        txt_monto.setEnabled(false);
        txt_monto.setText("");
        txt_fecha.setText("");
        txt_fecha.setEnabled(false);
        btn_reg.setEnabled(false);
        rbt_ing.setSelected(false);
        rbt_sal.setSelected(false);

    }
    private void btn_regActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regActionPerformed
        llenar();
        //REGISTRAR MOVIMIENTO EN CAJA CHICA
        int nro_caja = caj.codigo_movimiento(fecha);
        try {
            Statement st = con.conexion();
            String ins_caja = "insert into caja_chica value ('" + nro_caja + "', '" + tienda + "', '" + empresa + "', '" + mov.getFec() + "', '" + id_moneda + "', '" + mov.getGlosa() + "', '" + origen + "', '" + destino + "', '" + tipo + "',"
                    + "'" + mov.getMonto() + "', '" + frm_menu.usu.getNick() + "', NOW())";
            System.out.println(ins_caja);
            con.actualiza(st, ins_caja);
            con.cerrar(st);
        } catch (Exception ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }

        mostrar_movimientos();
        limpiar();
    }//GEN-LAST:event_btn_regActionPerformed

    private void txt_motKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_motKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_mot.getText().length() > 5) {
                txt_monto.setEnabled(true);
                txt_monto.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_motKeyPressed

    private void txt_fechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fechaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_fecha.getText().length() == 10) {
                cbx_mon.setEnabled(true);
                cbx_mon.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_fechaKeyPressed

    private void cbx_monKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_monKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn_reg.setEnabled(true);
            btn_reg.requestFocus();
        }
    }//GEN-LAST:event_cbx_monKeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jd_add_movimiento.setModal(true);
        jd_add_movimiento.setSize(654, 236);
        jd_add_movimiento.setLocationRelativeTo(null);
        cargar_empleados();
        jd_add_movimiento.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txt_nomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nomKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String cadena = txt_nom.getText();
            if (cadena.length() > 0) {
                boolean contiene_guion = false;
                for (int j = 0; j < cadena.length(); j++) {
                    if (cadena.charAt(j) == '-') {
                        contiene_guion = true;
                    }
                }
                if (contiene_guion == true) {
                    contiene_guion = false;
                    String[] array_empleado = cadena.split("-");
                    String dni = array_empleado[0];
                    String nombre = array_empleado[1];
                    txt_nom.setText("");
                    txt_dni.setText("");
                    txt_dni.setText(dni.trim());
                    txt_dni.setEnabled(true);
                    txt_dni.requestFocus();
                } else {
                    txt_nom.setText("");
                    txt_nom.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txt_nomKeyPressed

    private void txt_dniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dniKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_dni.getText().length() == 8) {
                try {
                    String dni = txt_dni.getText();
                    Statement st = con.conexion();
                    String ver_empleado = "select nombres, ape_pat, ape_mat from empleados where dni = '" + dni + "'";
                    ResultSet rs = con.consulta(st, ver_empleado);
                    if (rs.next()) {
                        txt_nom.setText(rs.getString("nombres") + " " + rs.getString("ape_pat") + " " + rs.getString("ape_mat"));
                        rbt_ing.setEnabled(true);
                        rbt_sal.setEnabled(true);
                        rbt_ing.requestFocus();
                    }
                    con.cerrar(rs);
                    con.cerrar(st);
                } catch (SQLException e) {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_txt_dniKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_clo;
    private javax.swing.JButton btn_reg;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbx_mon;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JDialog jd_add_movimiento;
    private javax.swing.JScrollPane jsoles;
    public static javax.swing.JRadioButton rbt_ing;
    public static javax.swing.JRadioButton rbt_sal;
    private javax.swing.JTable t_soles;
    public static javax.swing.JTextField txt_dni;
    private javax.swing.JFormattedTextField txt_fecha;
    private javax.swing.JTextField txt_ing_sol;
    private javax.swing.JTextField txt_monto;
    public static javax.swing.JTextField txt_mot;
    public static javax.swing.JTextField txt_nom;
    private javax.swing.JTextField txt_sal_sol;
    private javax.swing.JTextField txt_tot_sol;
    // End of variables declaration//GEN-END:variables
}
