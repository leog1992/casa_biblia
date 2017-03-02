/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Clases.Cl_Categoria;
import Clases.Cl_Conectar;
import Clases.Cl_Medidas;
import Clases.Cl_Productos;
import Clases.Cl_Varios;
import Forms.frm_reg_productos;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import casa_biblia.frm_menu;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class frm_ver_mis_productos extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Medidas med = new Cl_Medidas();
    Cl_Productos pro = new Cl_Productos();
    Cl_Varios ven = new Cl_Varios();
    Cl_Categoria cat = new Cl_Categoria();
    public static String origen = "";
    public static String tipo = "";
    DefaultTableModel modelo_lotes;
    Integer i;

    int tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();

    /**
     * Creates new form frm_ver_productos
     */
    public frm_ver_mis_productos() {
        initComponents();

        String query = "select p.id_producto, p.descripcion, p.codigo_externo, f.descripcion as nombre_familia, cf.descripcion as nombre_clase, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, p.precio "
                + "from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on "
                + "p.id_und_med = um.id_und_med inner join familia_productos as f on p.familia = f.id  inner join clasificacion_familia as cf "
                + "on cf.id= p.clase_familia and cf.familia=p.familia where pe.id_almacen = '" + tienda + "' and pe.empresa = '" + empresa + "' order by p.descripcion asc limit 0";
        pro.ver_productos(t_productos, query);
        contar_filas();
    }

    private void ver_ingresos(String id_producto, String id_almacen) {
        DefaultTableModel model_ingreso;
        model_ingreso = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        model_ingreso.addColumn("COD");
        model_ingreso.addColumn("FECHA");
        model_ingreso.addColumn("MOTIVO");
        model_ingreso.addColumn("RUC");
        model_ingreso.addColumn("RAZON SOCIAL - NOMBRE");
        model_ingreso.addColumn("TIPO DOC.");
        model_ingreso.addColumn("SERIE - NRO");
        model_ingreso.addColumn("CANT");
        model_ingreso.addColumn("PRE. UNI.");
        model_ingreso.addColumn("SUB. TOTAL.");
        Object[] fila_ing = new Object[10];
        try {
            Statement st = con.conexion();
            String ver_ing = "select k.id_kardex, k.fecha, tm.nombre as tipomov, k.nro_documento, k.denominacion as raz_soc, td.nombre as tipo_doc, k.serie, k.numero, k.cant_ing, k.pre_uni_ing "
                    + "from kardex as k inner join tipo_movimiento as tm on k.id_timo = tm.id_timo inner join tipo_documento as td on k.id_tido = td.id_tido"
                    + " where k.id_producto = '" + id_producto + "' and k.id_almacen = '" + id_almacen + "' and k.cant_sal = '0' and k.pre_uni_sal = '0' order by id_kardex desc";
            ResultSet rs = con.consulta(st, ver_ing);
            while (rs.next()) {
                fila_ing[0] = rs.getString("id_kardex");
                fila_ing[1] = ven.fechaformateada(rs.getString("fecha"));
                fila_ing[2] = rs.getString("tipomov");
                fila_ing[3] = rs.getString("nro_documento");
                fila_ing[4] = rs.getString("raz_soc");
                fila_ing[5] = rs.getString("tipo_doc");
                fila_ing[6] = rs.getString("serie") + " - " + rs.getString("numero");
                fila_ing[7] = ven.formato_numero(rs.getDouble("cant_ing"));
                fila_ing[8] = ven.formato_numero(rs.getDouble("pre_uni_ing"));
                fila_ing[9] = ven.formato_numero(rs.getDouble("cant_ing") * rs.getDouble("pre_uni_ing"));
                model_ingreso.addRow(fila_ing);
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        t_jd_ingresos.setModel(model_ingreso);
        t_jd_ingresos.getColumnModel().getColumn(0).setPreferredWidth(45);
        t_jd_ingresos.getColumnModel().getColumn(1).setPreferredWidth(90);
        t_jd_ingresos.getColumnModel().getColumn(2).setPreferredWidth(150);
        t_jd_ingresos.getColumnModel().getColumn(3).setPreferredWidth(90);
        t_jd_ingresos.getColumnModel().getColumn(4).setPreferredWidth(300);
        t_jd_ingresos.getColumnModel().getColumn(5).setPreferredWidth(100);
        t_jd_ingresos.getColumnModel().getColumn(6).setPreferredWidth(90);
        t_jd_ingresos.getColumnModel().getColumn(7).setPreferredWidth(80);
        t_jd_ingresos.getColumnModel().getColumn(8).setPreferredWidth(60);
        t_jd_ingresos.getColumnModel().getColumn(9).setPreferredWidth(60);
        ven.derecha_celda(t_jd_ingresos, 0);
        ven.centrar_celda(t_jd_ingresos, 1);
        ven.centrar_celda(t_jd_ingresos, 2);
        ven.centrar_celda(t_jd_ingresos, 3);
        ven.centrar_celda(t_jd_ingresos, 5);
        ven.centrar_celda(t_jd_ingresos, 6);
        ven.centrar_celda(t_jd_ingresos, 7);
        ven.derecha_celda(t_jd_ingresos, 8);
        ven.derecha_celda(t_jd_ingresos, 9);
    }

    private void ver_salidas(String id_producto, String id_almacen) {
        DefaultTableModel model_salida;
        model_salida = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        model_salida.addColumn("COD");
        model_salida.addColumn("FECHA");
        model_salida.addColumn("MOTIVO");
        model_salida.addColumn("RUC");
        model_salida.addColumn("RAZON SOCIAL - NOMBRE");
        model_salida.addColumn("TIPO DOC.");
        model_salida.addColumn("SERIE - NRO");
        model_salida.addColumn("CANT");
        model_salida.addColumn("PRE. UNI.");
        model_salida.addColumn("SUB. TOTAL.");
        Object[] fila_ing = new Object[11];
        try {
            Statement st = con.conexion();
            String ver_ing = "select k.id_kardex, k.fecha, tm.nombre as tipomov, k.nro_documento, k.denominacion as raz_soc, td.nombre as tipo_doc, k.serie, k.numero, k.cant_sal, k.pre_uni_sal "
                    + "from kardex as k inner join tipo_movimiento as tm on k.id_timo = tm.id_timo inner join tipo_documento as td on k.id_tido = td.id_tido"
                    + " where k.id_producto = '" + id_producto + "' and k.id_almacen = '" + id_almacen + "' and k.cant_ing = '0' and k.pre_uni_ing = '0' order by id_kardex desc";
            ResultSet rs = con.consulta(st, ver_ing);
            while (rs.next()) {
                fila_ing[0] = rs.getString("id_kardex");
                fila_ing[1] = ven.fechaformateada(rs.getString("fecha"));
                fila_ing[2] = rs.getString("tipomov");
                fila_ing[3] = rs.getString("nro_documento");
                fila_ing[4] = rs.getString("raz_soc");
                fila_ing[5] = rs.getString("tipo_doc");
                fila_ing[6] = rs.getString("serie") + " - " + rs.getString("numero");
                fila_ing[7] = ven.formato_numero(rs.getDouble("cant_sal"));
                fila_ing[8] = ven.formato_numero(rs.getDouble("pre_uni_sal"));
                fila_ing[9] = ven.formato_numero(rs.getDouble("cant_sal") * rs.getDouble("pre_uni_sal"));
                model_salida.addRow(fila_ing);
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        t_jd_salidas.setModel(model_salida);
        t_jd_salidas.getColumnModel().getColumn(0).setPreferredWidth(45);
        t_jd_salidas.getColumnModel().getColumn(1).setPreferredWidth(90);
        t_jd_salidas.getColumnModel().getColumn(2).setPreferredWidth(150);
        t_jd_salidas.getColumnModel().getColumn(3).setPreferredWidth(90);
        t_jd_salidas.getColumnModel().getColumn(4).setPreferredWidth(300);
        t_jd_salidas.getColumnModel().getColumn(5).setPreferredWidth(100);
        t_jd_salidas.getColumnModel().getColumn(6).setPreferredWidth(90);
        t_jd_salidas.getColumnModel().getColumn(7).setPreferredWidth(80);
        t_jd_salidas.getColumnModel().getColumn(8).setPreferredWidth(60);
        t_jd_salidas.getColumnModel().getColumn(9).setPreferredWidth(60);
        ven.derecha_celda(t_jd_salidas, 0);
        ven.centrar_celda(t_jd_salidas, 1);
        ven.centrar_celda(t_jd_salidas, 2);
        ven.centrar_celda(t_jd_salidas, 3);
        ven.centrar_celda(t_jd_salidas, 5);
        ven.centrar_celda(t_jd_salidas, 6);
        ven.centrar_celda(t_jd_salidas, 7);
        ven.derecha_celda(t_jd_salidas, 8);
        ven.derecha_celda(t_jd_salidas, 9);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jd_lotes = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_lotes = new javax.swing.JTable();
        jd_movimientos = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        txt_jd_id = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_jd_descripcion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_jd_laboratorio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_jd_cantidad = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_jd_precio = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        t_jd_ingresos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        t_jd_salidas = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_bus = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_productos = new javax.swing.JTable();
        btn_mod = new javax.swing.JButton();
        btn_eli = new javax.swing.JButton();
        cbx_est = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        btn_ver_movimientos = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        lbl_nro = new javax.swing.JLabel();

        jd_lotes.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_lotes.setTitle("Ver Lotes Disponibles");

        t_lotes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(t_lotes);

        javax.swing.GroupLayout jd_lotesLayout = new javax.swing.GroupLayout(jd_lotes.getContentPane());
        jd_lotes.getContentPane().setLayout(jd_lotesLayout);
        jd_lotesLayout.setHorizontalGroup(
            jd_lotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_lotesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jd_lotesLayout.setVerticalGroup(
            jd_lotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_lotesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setText("Id. ");

        txt_jd_id.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel3.setText("Descripcion");

        jLabel4.setText("Laboratorio:");

        jLabel5.setText("Cant. Actual:");

        txt_jd_cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel6.setText("und_med");

        jLabel7.setText("Precio Venta:");

        txt_jd_precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        t_jd_ingresos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(t_jd_ingresos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Ingresos", jPanel1);

        t_jd_salidas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(t_jd_salidas);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Salidas", jPanel2);

        javax.swing.GroupLayout jd_movimientosLayout = new javax.swing.GroupLayout(jd_movimientos.getContentPane());
        jd_movimientos.getContentPane().setLayout(jd_movimientosLayout);
        jd_movimientosLayout.setHorizontalGroup(
            jd_movimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_movimientosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_movimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(jd_movimientosLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_id, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_descripcion))
                    .addGroup(jd_movimientosLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_laboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addGap(80, 80, 80)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jd_movimientosLayout.setVerticalGroup(
            jd_movimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_movimientosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_movimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_jd_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txt_jd_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_movimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_jd_laboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txt_jd_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(txt_jd_precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        setTitle("Ver Productos");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        jButton3.setText("Cerrar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setText("Buscar:");

        txt_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_busKeyPressed(evt);
            }
        });

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        t_productos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Aceite SAE192", "Castrol", "5", "Glns", "15.20", "Normal"},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Descripcion", "Marca", "Cant.", "Und. Med.", "Precio", "Estado"
            }
        ));
        t_productos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_productos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_productosMouseClicked(evt);
            }
        });
        t_productos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_productosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_productos);
        if (t_productos.getColumnModel().getColumnCount() > 0) {
            t_productos.getColumnModel().getColumn(0).setPreferredWidth(20);
            t_productos.getColumnModel().getColumn(1).setPreferredWidth(150);
            t_productos.getColumnModel().getColumn(2).setPreferredWidth(50);
            t_productos.getColumnModel().getColumn(3).setPreferredWidth(20);
            t_productos.getColumnModel().getColumn(4).setPreferredWidth(30);
            t_productos.getColumnModel().getColumn(5).setPreferredWidth(40);
            t_productos.getColumnModel().getColumn(6).setPreferredWidth(40);
        }

        btn_mod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/application_edit.png"))); // NOI18N
        btn_mod.setText("Modifcar");
        btn_mod.setEnabled(false);
        btn_mod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modActionPerformed(evt);
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

        cbx_est.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DISPONIBLE", "POR TERMINAR", "NO DISPONIBLE" }));
        cbx_est.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbx_estActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        jButton1.setText("Imp. Kardex");
        jButton1.setEnabled(false);

        btn_ver_movimientos.setText("Ver Movimientos");
        btn_ver_movimientos.setEnabled(false);
        btn_ver_movimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ver_movimientosActionPerformed(evt);
            }
        });

        jLabel8.setText("Coincidencias encontradas:");

        lbl_nro.setText("jLabel9");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1038, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_nro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbx_est, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_mod))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_eli)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_ver_movimientos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(btn_mod, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_est, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(lbl_nro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ver_movimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txt_busKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_busKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            txt_bus.setText("");
            txt_bus.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String buscar = txt_bus.getText();
            String query = "select p.id_producto, p.descripcion, p.codigo_externo, f.descripcion as nombre_familia, cf.descripcion as nombre_clase, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, p.precio "
                    + "from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on "
                    + "p.id_und_med = um.id_und_med inner join familia_productos as f on p.familia = f.id  inner join clasificacion_familia as cf "
                    + "on cf.id= p.clase_familia and cf.familia=p.familia where pe.id_almacen = '" + tienda + "' and pe.empresa = '" + empresa + "' and (p.codigo_externo like '%" + buscar + "%' or p.descripcion like '%" + buscar + "%') order by p.descripcion asc";
            pro.ver_productos(t_productos, query);
            contar_filas();
        }
    }//GEN-LAST:event_txt_busKeyPressed

    private void contar_filas() {
        int nro_filas = t_productos.getRowCount();
        lbl_nro.setText(nro_filas + "");
    }

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked

    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void btn_modActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modActionPerformed
//        if (frm_menu.usu.getPpr_mod().equals("0")) {
        frm_reg_productos prod = new frm_reg_productos();
        frm_reg_productos.tipo = tipo;
        frm_reg_productos.origen = "mis_productos";

        try {
            String query = "select p.descripcion, p.caracteristicas, p.codigo_externo, p.afecto_igv, p.cantidad_caja, p.familia, p.clase_familia, p.precio, p.id_und_med, p.costo from productos as p where p.id_producto = '" + pro.getId() + "'";
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);
            if (rs.next()) {
                frm_reg_productos.txt_des.setText(rs.getString("descripcion"));
                frm_reg_productos.txt_des.requestFocus();
                frm_reg_productos.txt_caract.setText(rs.getString("caracteristicas"));
                frm_reg_productos.txt_codigo_externo.setText(rs.getString("codigo_externo"));
                frm_reg_productos.cbx_familia.setSelectedIndex(rs.getInt("familia") - 1);
                cat.setId_familia(rs.getInt("familia"));
                cat.ver_categoria(frm_reg_productos.cbx_categoria);
                frm_reg_productos.cbx_categoria.setSelectedIndex(rs.getInt("clase_familia") - 1);
                frm_reg_productos.cbx_afecto.setSelectedIndex(rs.getInt("afecto_igv"));
                frm_reg_productos.cbx_und.setSelectedIndex(rs.getInt("id_und_med") - 1);
                frm_reg_productos.txt_cant_caja.setText(ven.formato_numero(rs.getDouble("cantidad_caja")));
                frm_reg_productos.txt_pven.setText(ven.formato_numero(rs.getDouble("precio")));
                frm_reg_productos.txt_pcom.setText(ven.formato_numero(rs.getDouble("costo")));
                frm_reg_productos.txt_caract.setEnabled(true);
                frm_reg_productos.txt_pven.setEnabled(true);
                frm_reg_productos.cbx_afecto.setEnabled(true);
                frm_reg_productos.cbx_categoria.setEnabled(true);
                frm_reg_productos.cbx_familia.setEnabled(true);
                frm_reg_productos.cbx_und.setEnabled(true);
                frm_reg_productos.txt_cant_caja.setEnabled(true);
                frm_reg_productos.btn_reg.setText("Modificar");
                frm_reg_productos.btn_reg.setEnabled(true);
                /*
                 try {
                 //cargar imagen 
                 ImagenURL Imagen = new ImagenURL();
                 String imgname = "";
                 imgname = rs.getString("imagen");
                 //JOptionPane.showMessageDialog(null, "el nombre de imagen es " + imgname);
                 System.out.println(imgname);
                 if (imgname == null || "".equals(imgname) || imgname.equals("null")) {
                 imgname = "noimage.jpg";
                 }
                 //JOptionPane.showMessageDialog(null, "el nuevo nombre de imagen es " + imgname);
                 prod.pro.setImg(imgname);
                 String url;
                 String server = "www.lunasystemsperu.com";
                 //String server = ven.leer_archivo("server.txt");
                 url = "http://" + server + "/farmapos/intifarma/imagenes/productos/" + imgname;
                 System.out.println(url + "\n");
                 Imagen.setlink(url);
                 prod.j_img.add(Imagen);
                 prod.j_img.repaint();
                 //fin de carga
                 } catch (Exception ex) {
                 JOptionPane.showMessageDialog(null, "Error al cargar imagen");
                 System.out.println(ex);
                 }
                 */
                frm_reg_productos.accion = "modificar";
                frm_reg_productos.id = pro.getId();
                ven.llamar_ventana(prod);
                this.dispose();
            }
        } catch (SQLException ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        /*   } else {
         JOptionPane.showMessageDialog(null, "Ud. No tiene permisos");
         }
         */

    }//GEN-LAST:event_btn_modActionPerformed

    public class ImagenURL extends javax.swing.JPanel {

        String link;

        public ImagenURL() {
            this.setSize(200, 200); //se selecciona el tamaño del panel
        }

        public void paint(Graphics grafico) {
            Dimension height = getSize();
            URL url;
            ImageIcon Img = null;
            try {
                url = new URL(link);
                BufferedImage c = ImageIO.read(url);
                Img = new ImageIcon(c);
                grafico.drawImage(Img.getImage(), 0, 0, height.width, height.height, null);
                setOpaque(false);
                super.paintComponent(grafico);
            } catch (MalformedURLException ex) {
                System.out.print(ex);
            } catch (IOException ex) {
                System.out.print(ex);
//                JOptionPane.showMessageDialog(null, "no se puede mostrar la imagen \nerror con " + ex);
            }
//            grafico.drawImage(Img.getImage(), 0, 0, height.width, height.height, null);
//            setOpaque(false);
//            super.paintComponent(grafico);
        }

        public void setlink(String url) {
            link = url;
        }

        public String getlink() {
            return link;
        }
    }

    private void btn_eliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliActionPerformed
        if (frm_menu.usu.getPpr_eli().equals("0")) {
            //eliminar
            int confirmado = JOptionPane.showConfirmDialog(null, "¿Confirma eliminar el producto?");

            if (JOptionPane.OK_OPTION == confirmado) {

                //capturar ultimo id de kardex
                int id_kardex = 0;
                try {
                    Statement st = con.conexion();
                    String bus_idk = "select id_kardex from kardex where id_producto = '" + pro.getId() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "' order by id_kardex desc limit 1";
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
                    String ins_kardex = "insert into kardex Values ('" + id_kardex + "', '" + tienda + "', '" + empresa + "', '" + pro.getId() + "','" + ven.getFechaActual() + "', '" + empresa + "', "
                            + "'" + frm_menu.emp.getRazon_social() + "', '1', '0', '0', '0.00', '0.00', '" + pro.getCant_act() + "', '" + pro.getPrecio() + "', '12', NOW())";
                    System.out.println(ins_kardex);
                    con.actualiza(st, ins_kardex);
                    con.cerrar(st);
                } catch (Exception ex) {
                    System.out.print(ex);
                    JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                }

                try {
                    Statement st = con.conexion();
                    String query = "delete from productos_almacenes where id_producto ='" + pro.getId() + "' and id_almacen = '" + frm_menu.alm.getCodigo() + "' and empresa = '" + frm_menu.emp.getRuc() + "'";
                    con.actualiza(st, query);
                    con.cerrar(st);
                } catch (Exception ex) {
                    System.out.print(ex);
                    JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                }

                String buscar = txt_bus.getText();
                String query = "select p.id_producto, p.descripcion, p.codigo_externo, f.descripcion as nombre_familia, cf.descripcion as nombre_clase, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, p.precio "
                        + "from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on "
                        + "p.id_und_med = um.id_und_med inner join familia_productos as f on p.familia = f.id  inner join clasificacion_familia as cf "
                        + "on cf.id= p.clase_familia and cf.familia=p.familia where pe.id_almacen = '" + tienda + "' and pe.empresa = '" + empresa + "' and (p.codigo_externo like '%" + buscar + "%' or p.descripcion like '%" + buscar + "%') order by p.descripcion asc";
                pro.ver_productos(t_productos, query);
                contar_filas();
            }
            btn_eli.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null, "Ud. No tiene permisos");
        }
    }//GEN-LAST:event_btn_eliActionPerformed

    private void t_productosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_productosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_D) {
//            try {
//                Statement st = con.conexion();
//                String query = "select descripcion, marca, modelo, grado, caracteristicas from productos where id_producto  = '" + pro.getId() + "'";
//                ResultSet rs = con.consulta(st, query);
//                if (rs.next()) {
//                    JOptionPane.showMessageDialog(null, rs.getString("descripcion") + " - " + rs.getString("marca") + " - " + rs.getString("modelo") + " - " + rs.getString("grado") + " \n" + rs.getString("caracteristicas"));
//                } else {
//                    JOptionPane.showMessageDialog(null, "No hay Datos");
//                }
//            } catch (SQLException ex) {
//                System.out.print(ex);
//                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
//            }

        }

        if (evt.getKeyCode() == KeyEvent.VK_L) {
            modelo_lotes = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            modelo_lotes.addColumn("Lote");
            modelo_lotes.addColumn("Vencimiento");
            modelo_lotes.addColumn("Cant. Actual");
            modelo_lotes.addColumn("Cant. Ingreso");
            //busqueda de lotes
            try {
                Statement st = con.conexion();
                String ver_lotes = "select * from lotes_productos where id_almacen = '" + frm_menu.alm.getCodigo() + "' and id_producto = '" + pro.getId() + "' order by fecha_vencimiento asc";
                ResultSet rs = con.consulta(st, ver_lotes);
                while (rs.next()) {
                    Object fila[] = new Object[4];
                    fila[0] = rs.getString("lote");
                    fila[1] = ven.fechaformateada(rs.getString("fecha_vencimiento"));
                    fila[2] = ven.formato_numero(rs.getDouble("cantidad_actual"));
                    fila[3] = ven.formato_numero(rs.getDouble("cantidad_ingresada"));
                    modelo_lotes.addRow(fila);
                }
                t_lotes.setModel(modelo_lotes);
                con.cerrar(rs);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
            t_lotes.getColumnModel().getColumn(0).setPreferredWidth(70);
            t_lotes.getColumnModel().getColumn(1).setPreferredWidth(70);
            t_lotes.getColumnModel().getColumn(2).setPreferredWidth(70);
            t_lotes.getColumnModel().getColumn(3).setPreferredWidth(70);
            ven.centrar_celda(t_lotes, 0);
            ven.centrar_celda(t_lotes, 1);
            ven.derecha_celda(t_lotes, 2);
            ven.derecha_celda(t_lotes, 3);

            jd_lotes.setModal(true);
            jd_lotes.setSize(500, 300);
            jd_lotes.setLocationRelativeTo(null);
            jd_lotes.setVisible(true);
        }
    }//GEN-LAST:event_t_productosKeyPressed

    private void cbx_estActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbx_estActionPerformed
        String query = "";
        if (cbx_est.getSelectedIndex() == 0) {
            query = "select p.id_producto, p.descripcion, p.marca, p.modelo, p.caracteristicas, p.referencia, p.grado, p.tipo_producto, p.imagen, um.corto, "
                    + "pe.cantidad_actual, pe.cantidad_minima, pe.precio_venta from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto"
                    + " inner join unidad_medida as um on p.id_und_med = um.id_und_med where pe.id_almacen = '" + frm_menu.alm.getCodigo() + "' and "
                    + "pe.cantidad_actual > pe.cantidad_minima order by p.descripcion asc , p.marca asc, p.modelo asc";

        }
        if (cbx_est.getSelectedIndex() == 1) {
            query = "select p.id_producto, p.descripcion, p.marca, p.modelo, p.caracteristicas, p.referencia, p.grado, p.tipo_producto, p.imagen, um.corto, "
                    + "pe.cantidad_actual, pe.cantidad_minima, pe.precio_venta from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto"
                    + " inner join unidad_medida as um on p.id_und_med = um.id_und_med where pe.id_almacen = '" + frm_menu.alm.getCodigo() + "' and "
                    + "pe.cantidad_actual <= pe.cantidad_minima and pe.cantidad_actual > '0' order by p.descripcion asc , p.marca asc, p.modelo asc";
        }
        if (cbx_est.getSelectedIndex() == 2) {
            query = "select p.id_producto, p.descripcion, p.marca, p.modelo, p.caracteristicas, p.referencia, p.grado, p.tipo_producto, p.imagen, um.corto, "
                    + "pe.cantidad_actual, pe.cantidad_minima, pe.precio_venta from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto"
                    + " inner join unidad_medida as um on p.id_und_med = um.id_und_med where pe.id_almacen = '" + frm_menu.alm.getCodigo() + "' and "
                    + "pe.cantidad_actual <= '0' order by p.descripcion asc , p.marca asc, p.modelo asc";
        }
        pro.ver_productos(t_productos, query);
    }//GEN-LAST:event_cbx_estActionPerformed

    private void t_productosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_productosMouseClicked
        if (evt.getClickCount() == 2) {
            i = t_productos.getSelectedRow();
            int nro_filas = t_productos.getRowCount();

            if (nro_filas >= 0) {
                pro.setCant_act(Double.parseDouble(t_productos.getValueAt(i, 6).toString()));
                pro.setPrecio(Double.parseDouble(t_productos.getValueAt(i, 8).toString()));
                pro.setId(Integer.parseInt(t_productos.getValueAt(i, 0).toString()));
                btn_eli.setEnabled(true);
                btn_ver_movimientos.setEnabled(true);
                tipo = t_productos.getValueAt(i, 1).toString().toLowerCase();
            } else {
                JOptionPane.showMessageDialog(null, "NO HAY FILAS");
            }

            if (origen.equals("mis_productos")) {
                pro.setId(Integer.parseInt(t_productos.getValueAt(i, 0).toString()));
            }

            btn_mod.setEnabled(true);
        }
    }//GEN-LAST:event_t_productosMouseClicked

    private void btn_ver_movimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ver_movimientosActionPerformed
        btn_ver_movimientos.setEnabled(false);
        jd_movimientos.setModal(true);
        jd_movimientos.setSize(950, 400);
        jd_movimientos.setLocationRelativeTo(null);
        ver_ingresos(pro.getId() + "", frm_menu.alm.getCodigo() + "");
        ver_salidas(pro.getId() + "", frm_menu.alm.getCodigo() + "");
        jd_movimientos.setVisible(true);

    }//GEN-LAST:event_btn_ver_movimientosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_eli;
    private javax.swing.JButton btn_mod;
    private javax.swing.JButton btn_ver_movimientos;
    private javax.swing.JComboBox cbx_est;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JDialog jd_lotes;
    private javax.swing.JDialog jd_movimientos;
    private javax.swing.JLabel lbl_nro;
    private javax.swing.JTable t_jd_ingresos;
    private javax.swing.JTable t_jd_salidas;
    private javax.swing.JTable t_lotes;
    public static javax.swing.JTable t_productos;
    private javax.swing.JTextField txt_bus;
    private javax.swing.JTextField txt_jd_cantidad;
    private javax.swing.JTextField txt_jd_descripcion;
    private javax.swing.JTextField txt_jd_id;
    private javax.swing.JTextField txt_jd_laboratorio;
    private javax.swing.JTextField txt_jd_precio;
    // End of variables declaration//GEN-END:variables
}
