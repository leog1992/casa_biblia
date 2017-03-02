/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Clases.Cl_Compra;
import Clases.Cl_Conectar;
import Clases.Cl_Movimiento;
import Clases.Cl_Productos;
import Clases.Cl_Proveedor;
import Clases.Cl_Tipo_Doc;
import Clases.Cl_Varios;
import Forms.frm_reg_ingreso;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import casa_biblia.frm_menu;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author CONTABILIDAD 02
 */
public class frm_ver_ingreso extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    Cl_Compra com = new Cl_Compra();
    Cl_Productos mat = new Cl_Productos();
    Cl_Movimiento mov = new Cl_Movimiento();
    Cl_Tipo_Doc tido = new Cl_Tipo_Doc();
    Cl_Proveedor prov = new Cl_Proveedor();

    int tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();
    public static DefaultTableModel mostrar;
    Integer i;

    /**
     * Creates new form frm_ver_compra
     */
    public frm_ver_ingreso() {
        initComponents();

        Calendar fecha = Calendar.getInstance();
        int year = fecha.get(Calendar.YEAR);
        int month = fecha.get(Calendar.MONTH) + 1;
        String query = "select i.id_ingreso, i.anio, i.ruc_proveedor, p.razon_social, td.nombre as tipo_documento, i.serie, i.numero, i.fecha, m.corto, i.sub_total from ingresos as i "
                + "inner join proveedores as p on i.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on i.id_tido=td.id_tido inner join moneda as m on i.id_moneda = m.id_moneda "
                + "where i.id_almacen = '" + tienda + "'and i.empresa = '" + empresa + "' and anio = '" + year + "' and month(i.fecha) = '" + month + "' order by i.fecha desc, i.id_ingreso desc";
        ver_compras(query);
    }

    private void ver_compras(String query) {
        try {
            mostrar = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            TableRowSorter sorter = new TableRowSorter(mostrar);
            mostrar.addColumn("Id");
            mostrar.addColumn("Año");
            mostrar.addColumn("Fec. Compra");
            mostrar.addColumn("Tipo Doc.");
            mostrar.addColumn("Serie");
            mostrar.addColumn("Nro.");
            mostrar.addColumn("RUC");
            mostrar.addColumn("Razon Social");
            mostrar.addColumn("Moneda");
            mostrar.addColumn("Total");

            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);
            while (rs.next()) {
                Object fila[] = new Object[10];
                fila[0] = rs.getString("id_ingreso");
                fila[1] = rs.getString("anio");
                fila[2] = ven.fechaformateada(rs.getString("fecha"));
                fila[3] = rs.getString("tipo_documento");
                fila[4] = rs.getString("serie");
                fila[5] = rs.getString("numero");
                fila[6] = rs.getString("ruc_proveedor");
                fila[7] = rs.getString("razon_social");
                fila[8] = rs.getString("corto");
                fila[9] = ven.formato_numero(rs.getDouble("sub_total") * 1.18);
                mostrar.addRow(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
            t_compras.setModel(mostrar);
            t_compras.getColumnModel().getColumn(0).setPreferredWidth(10);
            t_compras.getColumnModel().getColumn(1).setPreferredWidth(30);
            t_compras.getColumnModel().getColumn(2).setPreferredWidth(70);
            t_compras.getColumnModel().getColumn(3).setPreferredWidth(70);
            t_compras.getColumnModel().getColumn(4).setPreferredWidth(30);
            t_compras.getColumnModel().getColumn(5).setPreferredWidth(50);
            t_compras.getColumnModel().getColumn(6).setPreferredWidth(80);
            t_compras.getColumnModel().getColumn(7).setPreferredWidth(350);
            t_compras.getColumnModel().getColumn(8).setPreferredWidth(30);
            t_compras.getColumnModel().getColumn(9).setPreferredWidth(60);
            ven.centrar_celda(t_compras, 0);
            ven.centrar_celda(t_compras, 1);
            ven.centrar_celda(t_compras, 2);
            ven.centrar_celda(t_compras, 3);
            ven.centrar_celda(t_compras, 4);
            ven.centrar_celda(t_compras, 5);
            ven.centrar_celda(t_compras, 6);
            ven.derecha_celda(t_compras, 8);
            ven.derecha_celda(t_compras, 9);
            t_compras.setRowSorter(sorter);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        t_compras = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txt_bus = new javax.swing.JTextField();
        txt_reg = new javax.swing.JButton();
        txt_close = new javax.swing.JButton();
        btn_det = new javax.swing.JButton();
        btn_eli = new javax.swing.JButton();
        cbx_bus = new javax.swing.JComboBox();

        setTitle("Ingreso de Productos");

        t_compras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                t_comprasMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_compras);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/find.png"))); // NOI18N
        jLabel1.setText("Buscar:");

        txt_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_busKeyPressed(evt);
            }
        });

        txt_reg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/application_add.png"))); // NOI18N
        txt_reg.setText("Registrar");
        txt_reg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_regActionPerformed(evt);
            }
        });

        txt_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        txt_close.setText("Cerrar");
        txt_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_closeActionPerformed(evt);
            }
        });

        btn_det.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        btn_det.setText("Ver Detalle");
        btn_det.setEnabled(false);
        btn_det.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detActionPerformed(evt);
            }
        });

        btn_eli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cross.png"))); // NOI18N
        btn_eli.setText("Eliminar");
        btn_eli.setEnabled(false);
        btn_eli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliActionPerformed(evt);
            }
        });

        cbx_bus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PROVEEDOR", "NRO. DOCUMENTO" }));
        cbx_bus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_busActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_bus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_reg))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_det)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_eli)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_close)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_reg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_close, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_det, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_closeActionPerformed
        this.dispose();
    }//GEN-LAST:event_txt_closeActionPerformed

    private void txt_regActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_regActionPerformed
        frm_reg_ingreso compra = new frm_reg_ingreso();
        ven.llamar_ventana(compra);
        this.dispose();
    }//GEN-LAST:event_txt_regActionPerformed

    private void txt_busKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_busKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Calendar fecha = Calendar.getInstance();
            int year = fecha.get(Calendar.YEAR);
            int month = fecha.get(Calendar.MONTH) + 1;
            String busqueda = txt_bus.getText();
            if (cbx_bus.getSelectedIndex() == 0) {
                String query = "select i.id_ingreso, i.anio, i.ruc_proveedor, p.razon_social, td.nombre as tipo_documento, i.serie, i.numero, i.fecha, m.corto, i.sub_total from ingresos as i "
                        + "inner join proveedores as p on i.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on i.id_tido=td.id_tido inner join moneda as m on i.id_moneda = m.id_moneda "
                        + "where i.id_almacen = '" + tienda + "'and i.empresa = '" + empresa + "' and (i.ruc_proveedor = '" + busqueda + "' or p.razon_social like '%" + busqueda + "%') order by i.fecha desc, i.id_ingreso desc";
                ver_compras(query);
            }
            if (cbx_bus.getSelectedIndex() == 1) {
                busqueda = ven.fechabase(busqueda);
                String query = "select i.id_ingreso, i.anio, i.ruc_proveedor, p.razon_social, td.nombre as tipo_documento, i.serie, i.numero, i.fecha, m.corto, i.sub_total from ingresos as i "
                        + "inner join proveedores as p on i.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on i.id_tido=td.id_tido inner join moneda as m on i.id_moneda = m.id_moneda "
                        + "where i.id_almacen = '" + tienda + "'and i.empresa = '" + empresa + "' and (i.numero = '" + busqueda + "') order by i.fecha desc, i.id_ingreso desc";
                ver_compras(query);
            }
        }
    }//GEN-LAST:event_txt_busKeyPressed

    private void t_comprasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_comprasMousePressed
        i = t_compras.getSelectedRow();
        btn_det.setEnabled(true);
        btn_eli.setEnabled(true);
    }//GEN-LAST:event_t_comprasMousePressed

    private void btn_eliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliActionPerformed
        int confirmado = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el ingreso seleccionado?");
        if (JOptionPane.OK_OPTION == confirmado) {
            /*
             com.setId(Integer.parseInt(t_compras.getValueAt(i, 0).toString()));
             String anio = t_compras.getValueAt(i, 1).toString();
             prov.setRaz_soc(t_compras.getValueAt(i, 6).toString());
             //seleccionar almacen
             try {
             Statement st = con.conexion();
             String ver_datos = "select i.id_tido, i.serie, i.ruc_proveedor, i.numero, i.fecha, p.razon_social from ingresos as i inner join proveedores as p on i.ruc_proveedor = p.ruc_proveedor "
             + "where i.id_ingreso = '" + com.getId() + "' and i.anio = '" + anio + "' and i.id_almacen = '" + frm_menu.alm.getCodigo() + "'";
             System.out.println(ver_datos);
             ResultSet rs = con.consulta(st, ver_datos);
             if (rs.next()) {
             com.setFecha_compra(rs.getString("fecha"));
             prov.setRuc(rs.getString("ruc_proveedor"));
             prov.setRaz_soc(rs.getString("razon_social"));
             tido.setId(rs.getInt("id_tido"));
             tido.setSer(rs.getInt("serie"));
             tido.setNro(rs.getInt("numero"));
             }
             con.cerrar(rs);
             con.cerrar(st);
             } catch (SQLException ex) {
             System.out.print(ex);
             JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
             }

             //recorrer detalle de ingreso
             try {
                
             Statement st = con.conexion();
             String ver_detalle = "select id_producto, precio_compra, cantidad_compra, lote, cantidad_caja from detalle_ingreso where id_ingreso = '" + com.getId() + "' and anio = '" + anio + "' and "
             + "id_almacen = '" + frm_menu.alm.getCodigo() + "'";
             System.out.println(ver_detalle);
             ResultSet rs = con.consulta(st, ver_detalle);
             while (rs.next()) {
             mat.setId(rs.getInt("id_producto"));
             mat.setCan(rs.getDouble("cantidad_compra"));
             mat.setCantidad_caja(rs.getDouble("cantidad_caja"));
             mat.setCosto(rs.getDouble("precio_compra"));
             mat.setLote(rs.getString("lote"));
             mat.setCan(mat.getCan() * mat.getCantidad_caja());

             //capturar cantidad actual
             try {
             Statement st1 = con.conexion();
             String ver_cant_actual = "select cantidad_actual from productos_almacenes where id_producto = '" + mat.getId() + "' and id_almacen = '" + frm_menu.alm.getCodigo() + "'";
             System.out.println(ver_cant_actual);
             ResultSet rs1 = con.consulta(st1, ver_cant_actual);
             if (rs1.next()) {
             mat.setCant_act(rs1.getDouble("cantidad_actual"));
             }
             con.cerrar(rs1);
             con.cerrar(st1);
             mat.setCant_act(mat.getCant_act() - mat.getCan());
             } catch (SQLException ex1) {
             System.out.print(ex1);
             JOptionPane.showMessageDialog(null, ex1.getLocalizedMessage());
             }

             //actualizar cantidad actual
             try {
             Statement st1 = con.conexion();
             String upt_pro = "update productos_almacenes set cantidad_actual = '" + mat.getCant_act() + "' where id_producto = '" + mat.getId() + "' and "
             + "id_almacen = '" + frm_menu.alm.getCodigo() + "'";
             System.out.println(upt_pro);
             con.actualiza(st1, upt_pro);
             con.cerrar(st1);
             } catch (Exception ex1) {
             System.out.print(ex1);
             JOptionPane.showMessageDialog(null, ex1.getLocalizedMessage());
             }

             int id_kardex = 0;
             //capturar ultimo id de kardex
             try {
             Statement st1 = con.conexion();
             String bus_idk = "select id_kardex from kardex where id_producto = '" + mat.getId() + "' and id_almacen = '" + frm_menu.alm.getCodigo() + "' order by id_kardex desc limit 1";
             System.out.println(bus_idk);
             ResultSet rs1 = con.consulta(st1, bus_idk);
             if (rs1.next()) {
             id_kardex = rs1.getInt("id_kardex") + 1;
             } else {
             id_kardex = 1;
             }
             con.cerrar(rs1);
             con.cerrar(st1);
             } catch (SQLException ex1) {
             System.out.print(ex1);
             JOptionPane.showMessageDialog(null, ex1.getLocalizedMessage());
             }
                    
             //registrar devolucion en kardex
             try {
             Statement st1 = con.conexion();
             String ins_kardex = "insert into kardex Values ('" + id_kardex + "', '" + frm_menu.alm.getCodigo() + "', '" + mat.getId() + "', '" + mat.getLote() + "', '" + com.getFecha_compra() + "', '" + prov.getRuc() + "', "
             + "'" + prov.getRaz_soc() + "', '" + tido.getId() + "', '" + tido.getSer() + "', '" + tido.getNro() + "', '0.00', '0.00', '" + mat.getCan() + "', '" + mat.getCosto() + "', '6', NOW())";
             System.out.println(ins_kardex);
             con.actualiza(st1, ins_kardex);
             con.cerrar(st1);
             } catch (Exception ex1) {
             System.out.print(ex1);
             JOptionPane.showMessageDialog(null, ex1.getLocalizedMessage());
             }
                    
             //buscar si existe producto con su lote en lotes_productos, si existe actualzar cantidad, sino ingresar producto
             double lote_cantidad_actual = 0;
             double lote_cantidad_ingresada = 0;
             try {
             Statement st1 = con.conexion();
             String bus_pro = "select cantidad_actual, cantidad_ingresada from lotes_productos where id_producto = '" + mat.getId() + "' and id_almacen = '" + frm_menu.alm.getCodigo() + "' and lote = '" + mat.getLote() + "'";
             System.out.println(bus_pro);
             ResultSet rs1 = con.consulta(st1, bus_pro);
             if (rs.next()) {
             lote_cantidad_actual = rs1.getDouble("cantidad_actual");
             lote_cantidad_ingresada = rs1.getDouble("cantidad_ingresada");
             }
             con.cerrar(rs1);
             con.cerrar(st1);
             } catch (SQLException ex) {
             System.out.print(ex);
             JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
             }
                

             //ACTUALIZAR CANTIDAD ACTUAL
             lote_cantidad_actual = lote_cantidad_actual - mat.getCan();
             lote_cantidad_ingresada = lote_cantidad_ingresada - mat.getCan();
             try {
             Statement st1 = con.conexion();
             String act_pro = "update lotes_productos set cantidad_actual = '" + lote_cantidad_actual + "', cantidad_ingresada = '" + lote_cantidad_ingresada + "' where "
             + "id_producto = '" + mat.getId() + "' and id_almacen = '" + frm_menu.alm.getCodigo() + "' and lote = '" + mat.getLote() + "'";
             System.out.println(act_pro);
             con.actualiza(st1, act_pro);
             con.cerrar(st1);
             } catch (Exception ex) {
             System.out.print(ex);
             JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
             }
             }
             con.cerrar(rs);
             con.cerrar(st);
             } catch (SQLException ex) {
             System.out.print(ex);
             JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
             }

             try {
             Statement st = con.conexion();
             String del_detalle_ped = "delete from detalle_ingreso where id_ingreso = '" + com.getId() + "' and anio = '" + anio + "' and id_almacen = '" + frm_menu.alm.getCodigo() + "'";
             System.out.println(del_detalle_ped);
             con.actualiza(st, del_detalle_ped);
             con.cerrar(st);
             } catch (Exception ex) {
             System.out.print(ex);
             JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
             }

             try {
             Statement st = con.conexion();
             String del_ped = "delete from ingresos where id_ingreso = '" + com.getId() + "' and anio = '" + anio + "' and id_almacen = '" + frm_menu.alm.getCodigo() + "'";
             System.out.println(del_ped);
             con.actualiza(st, del_ped);
             con.cerrar(st);
             } catch (Exception ex) {
             System.out.print(ex);
             JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
             }

             Calendar fecha = Calendar.getInstance();
             int year = fecha.get(Calendar.YEAR);
             int month = fecha.get(Calendar.MONTH) + 1;
             String query = "select i.id_ingreso, i.anio, i.ruc_proveedor, p.razon_social, td.nombre as tipo_documento, i.serie, i.numero, i.fecha, m.corto, i.sub_total from ingresos as i "
             + "inner join proveedores as p on i.ruc_proveedor = p.ruc_proveedor inner join tipo_documento as td on i.id_tido=td.id_tido inner join moneda as m on i.id_moneda = m.id_moneda "
             + "where i.id_almacen = '" + frm_menu.alm.getCodigo() + "' and anio = '" + year + "' and month(i.fecha) = '" + month + "'";
             ver_compras(query);
             txt_bus.requestFocus();
             */
        }
    }//GEN-LAST:event_btn_eliActionPerformed

    private void btn_detActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detActionPerformed
        com.setId(Integer.parseInt(t_compras.getValueAt(i, 0).toString()));
        Calendar fecha = Calendar.getInstance();
        int year = fecha.get(Calendar.YEAR);
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("compra", com.getId());
        parametros.put("anio", year);
        parametros.put("tienda", tienda);
        parametros.put("empresa", empresa);
        ven.ver_reporte("rpt_ver_ingreso", parametros);
    }//GEN-LAST:event_btn_detActionPerformed

    private void cbx_busActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_busActionPerformed
        txt_bus.setText("");
        txt_bus.requestFocus();
    }//GEN-LAST:event_cbx_busActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_det;
    private javax.swing.JButton btn_eli;
    private javax.swing.JComboBox cbx_bus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable t_compras;
    private javax.swing.JTextField txt_bus;
    private javax.swing.JButton txt_close;
    private javax.swing.JButton txt_reg;
    // End of variables declaration//GEN-END:variables
}
