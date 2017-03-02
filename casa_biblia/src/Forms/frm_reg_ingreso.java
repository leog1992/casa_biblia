/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Cl_Compra;
import Clases.Cl_Conectar;
import Clases.Cl_Ingreso;
import Clases.Cl_Moneda;
import Clases.Cl_Productos;
import Clases.Cl_Proveedor;
import Clases.Cl_Tipo_Doc;
import Clases.Cl_Varios;
import Vistas.frm_ver_imagen_mini;
import Vistas.frm_ver_ingreso;
import Vistas.frm_ver_todo_productos;
import casa_biblia.frm_menu;
import com.mxrck.autocompleter.AutoCompleterCallback;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import nicon.notify.core.Notification;

/**
 *
 * @author Dereck
 */
public final class frm_reg_ingreso extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    Cl_Proveedor pro = new Cl_Proveedor();
    Cl_Moneda mon = new Cl_Moneda();
    Cl_Ingreso ing = new Cl_Ingreso();
    Cl_Tipo_Doc tido = new Cl_Tipo_Doc();
    Cl_Productos mat = new Cl_Productos();
    public static DefaultTableModel detalle;
    int i;
    int tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();

    /**
     * Creates new form frm_reg_ingreso
     */
    public frm_reg_ingreso() {
        initComponents();
        txt_fecha_compra.setText(ven.fechaformateada(ven.getFechaActual()));
        tido.ver_tipodoc(cbx_tipo_doc);
        mon.ver_monedas(cbx_moneda);
        cargar_proveedores();
        ver_detalle_ingreso();
        cbx_tipo_doc.setSelectedIndex(2);
    }

    public void ver_detalle_ingreso() {
        try {
            detalle = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return columna == 2 || columna == 4 || columna == 5 || columna == 7;
                }
            };
            //Establecer como cabezeras el nombre de las colimnas
            detalle.addColumn("Cod.");
            detalle.addColumn("Descripcion");
            detalle.addColumn("Cant. Ing.");
            detalle.addColumn("Und. Med.");
            detalle.addColumn("P.U. Compra");
            detalle.addColumn("P.U. Venta");
            detalle.addColumn("Parcial");
            detalle.addColumn("Cant. Caja");
            t_detalle_ingreso.setModel(detalle);
            t_detalle_ingreso.getColumnModel().getColumn(0).setPreferredWidth(50);
            t_detalle_ingreso.getColumnModel().getColumn(1).setPreferredWidth(500);
            t_detalle_ingreso.getColumnModel().getColumn(2).setPreferredWidth(70);
            t_detalle_ingreso.getColumnModel().getColumn(3).setPreferredWidth(70);
            t_detalle_ingreso.getColumnModel().getColumn(4).setPreferredWidth(70);
            t_detalle_ingreso.getColumnModel().getColumn(5).setPreferredWidth(70);
            t_detalle_ingreso.getColumnModel().getColumn(6).setPreferredWidth(70);
            t_detalle_ingreso.getColumnModel().getColumn(7).setPreferredWidth(70);
            detalle.fireTableDataChanged();
            ven.centrar_celda(t_detalle_ingreso, 0);
            ven.derecha_celda(t_detalle_ingreso, 2);
            ven.centrar_celda(t_detalle_ingreso, 3);
            ven.derecha_celda(t_detalle_ingreso, 4);
            ven.derecha_celda(t_detalle_ingreso, 5);
            ven.derecha_celda(t_detalle_ingreso, 6);
            ven.derecha_celda(t_detalle_ingreso, 7);
        } catch (Exception e) {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    private void cargar_productos_txt() {
        try {
            // autocompletar = new TextAutoCompleter(txt_buscar_producto);
            TextAutoCompleter autocompletar = new TextAutoCompleter(txt_buscar_productos, new AutoCompleterCallback() {
                @Override
                public void callback(Object selectedItem) {
                    txt_buscar_productos.setText("");
                    txt_buscar_productos.requestFocus();
                    String cadena = selectedItem.toString();
                    JOptionPane.showMessageDialog(null, "El usuario seleccionó: " + cadena);
                    if (cadena.length() > 0) {
                        boolean contiene_guion = false;
                        for (int j = 0; j < cadena.length(); j++) {
                            if (cadena.charAt(j) == '-') {
                                contiene_guion = true;
                            }
                        }
                        if (contiene_guion == true) {
                            contiene_guion = false;
                            String[] array_producto = cadena.split("-");
                            String id_producto = array_producto[0].trim();
                            //JOptionPane.showMessageDialog(null, "El id del Producto es : " + id_producto);

                            boolean agregar = valida_tabla(Integer.parseInt(id_producto));
                            if (agregar == true) {

                                try {
                                    Statement st = con.conexion();
                                    String ver_producto = "select p.id_producto, p.codigo_externo, p.caracteristicas, p.descripcion, p.tipo_producto, f.descripcion as nombre_familia, cf.descripcion as nombre_clase, p.costo, p.precio, um.corto, p.cantidad_caja "
                                            + "from productos as p inner join unidad_medida as um on p.id_und_med = um.id_und_med inner join familia_productos as f on p.familia = f.id  inner join clasificacion_familia as cf "
                                            + "on cf.id= p.clase_familia and cf.familia=p.familia where p.id_producto = '" + id_producto + "'";
                                    System.out.println(ver_producto);
                                    ResultSet rs = con.consulta(st, ver_producto);
                                    if (rs.next()) {
                                        txt_j_descripcion.setText(rs.getString("descripcion") + " - " + rs.getString("nombre_clase") + " - " + rs.getString("nombre_familia") + " CE: " + rs.getString("codigo_externo"));
                                        txt_j_idproducto.setText(rs.getString("id_producto"));
                                        txt_j_caracteristicas.setText(rs.getString("caracteristicas"));
                                        txt_j_caja.setText(ven.formato_numero(rs.getDouble("cantidad_caja")));
                                        txt_j_venta.setText(ven.formato_numero(rs.getDouble("precio")));
                                        txt_j_compra.setText(ven.formato_numero(rs.getDouble("costo")));
                                        lbl_j_medida.setText(rs.getString("corto"));
                                        txt_j_cantidad.setEnabled(true);
                                        txt_j_cantidad.selectAll();
                                        txt_j_cantidad.requestFocus();
                                    }

                                    con.cerrar(rs);
                                    con.cerrar(st);

                                } catch (SQLException | HeadlessException e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                                }

                                j_ingreso.setModal(true);
                                //limpiar_jdialog();
                                j_ingreso.setSize(700, 300);
                                j_ingreso.setLocationRelativeTo(null);
                                j_ingreso.setVisible(true);

                                //txt_buscar_productos.setText("");
                                //txt_buscar_productos.requestFocus();
                            }

                        }
                        if (contiene_guion == false) {
                            txt_buscar_productos.setText("");
                            txt_buscar_productos.requestFocus();
                        }

                        // calcular_total();
                    }
                }
            });

            autocompletar.setMode(0);
            autocompletar.setCaseSensitive(false);
            Statement st = con.conexion();
            String sql = "select p.id_producto, p.codigo_externo, p.descripcion, p.tipo_producto, f.descripcion as nombre_familia, cf.descripcion as nombre_clase, p.costo, p.precio, um.corto, p.cantidad_caja "
                    + "from productos as p inner join unidad_medida as um on p.id_und_med = um.id_und_med inner join familia_productos as f on p.familia = f.id  inner join clasificacion_familia as cf "
                    + "on cf.id= p.clase_familia and cf.familia=p.familia order by p.descripcion asc";
            ResultSet rs = con.consulta(st, sql);
            while (rs.next()) {
                autocompletar.addItem(rs.getString("p.id_producto") + " - " + rs.getString("p.descripcion") + " " + rs.getString("p.codigo_externo") + " - " + rs.getString("nombre_clase") + " - " + rs.getString("nombre_familia"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getLocalizedMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    private boolean valida_tabla(int producto) {
        //estado de ingreso
        boolean ingresar = false;
        boolean validado = false;
        int cuenta_iguales = 0;

        //verificar fila no se repite
        int contar_filas = t_detalle_ingreso.getRowCount();
        if (contar_filas == 0) {
            ingresar = true;
        }

        if (contar_filas > 0) {
            for (int j = 0; j < contar_filas; j++) {
                int id_producto_fila = Integer.parseInt(t_detalle_ingreso.getValueAt(j, 0).toString());
                if (producto == id_producto_fila) {
                    ingresar = false;
                    cuenta_iguales++;
                    JOptionPane.showMessageDialog(null, "El Producto a Ingresar ya existe en la lista");
                } else {
                    ingresar = true;
                }
            }
        }

        if (ingresar == true && cuenta_iguales == 0) {
            validado = true;
        }
        return validado;
    }

    private void contar_filas() {
        int contar_filas = t_detalle_ingreso.getRowCount();
        lbl_contando.setText(contar_filas + "");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        j_ingreso = new javax.swing.JDialog();
        jLabel13 = new javax.swing.JLabel();
        txt_j_descripcion = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txt_j_cantidad = new javax.swing.JTextField();
        lbl_j_medida = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt_j_compra = new javax.swing.JTextField();
        txt_j_venta = new javax.swing.JTextField();
        btn_agrega_fila = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txt_j_caja = new javax.swing.JTextField();
        txt_j_idproducto = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_j_caracteristicas = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_ruc = new javax.swing.JTextField();
        txt_razon_social = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_serie = new javax.swing.JTextField();
        cbx_tipo_doc = new javax.swing.JComboBox();
        txt_numero = new javax.swing.JTextField();
        txt_fecha_compra = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        cbx_moneda = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        txt_tc = new javax.swing.JTextField();
        btn_cerrar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_detalle_ingreso = new javax.swing.JTable();
        btn_add_productos = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        btn_modificar = new javax.swing.JButton();
        txt_buscar_productos = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btn_grabar = new javax.swing.JButton();
        txt_subtotal = new javax.swing.JTextField();
        txt_igv = new javax.swing.JTextField();
        txt_total = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lbl_contando = new javax.swing.JLabel();

        j_ingreso.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        j_ingreso.setTitle("Agregar Producto");

        jLabel13.setText("Descripcion:");

        txt_j_descripcion.setFocusable(false);
        txt_j_descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_j_descripcionKeyPressed(evt);
            }
        });

        jLabel14.setText("Cantidad:");

        txt_j_cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_cantidad.setText("0.00");
        txt_j_cantidad.setEnabled(false);
        txt_j_cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_j_cantidadKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_j_cantidadKeyTyped(evt);
            }
        });

        lbl_j_medida.setText("jLabel15");

        jLabel18.setText("Precio Compra Caja:");

        jLabel19.setText("Precio Venta Unidad:");

        txt_j_compra.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_compra.setText("0.00");
        txt_j_compra.setEnabled(false);
        txt_j_compra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_j_compraKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_j_compraKeyTyped(evt);
            }
        });

        txt_j_venta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_venta.setText("0.00");
        txt_j_venta.setEnabled(false);
        txt_j_venta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_j_ventaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_j_ventaKeyTyped(evt);
            }
        });

        btn_agrega_fila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/accept.png"))); // NOI18N
        btn_agrega_fila.setText("Agregar");
        btn_agrega_fila.setEnabled(false);
        btn_agrega_fila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agrega_filaActionPerformed(evt);
            }
        });

        jLabel15.setText("Cantidad Caja:");

        txt_j_caja.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_j_caja.setText("0.00");
        txt_j_caja.setEnabled(false);
        txt_j_caja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_j_cajaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_j_cajaKeyTyped(evt);
            }
        });

        txt_j_idproducto.setText("id");

        jLabel16.setText("Caracteristicas:");

        txt_j_caracteristicas.setColumns(20);
        txt_j_caracteristicas.setRows(5);
        txt_j_caracteristicas.setFocusable(false);
        jScrollPane2.setViewportView(txt_j_caracteristicas);

        javax.swing.GroupLayout j_ingresoLayout = new javax.swing.GroupLayout(j_ingreso.getContentPane());
        j_ingreso.getContentPane().setLayout(j_ingresoLayout);
        j_ingresoLayout.setHorizontalGroup(
            j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(j_ingresoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(j_ingresoLayout.createSequentialGroup()
                        .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(j_ingresoLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(6, 6, 6)))
                .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(j_ingresoLayout.createSequentialGroup()
                        .addComponent(txt_j_caja, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91)
                        .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19))
                        .addGap(18, 18, 18)
                        .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_j_compra)
                            .addComponent(txt_j_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                        .addComponent(btn_agrega_fila))
                    .addGroup(j_ingresoLayout.createSequentialGroup()
                        .addComponent(txt_j_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_j_medida, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(j_ingresoLayout.createSequentialGroup()
                        .addComponent(txt_j_descripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_j_idproducto))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        j_ingresoLayout.setVerticalGroup(
            j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(j_ingresoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_j_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_j_idproducto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(j_ingresoLayout.createSequentialGroup()
                        .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_j_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_j_medida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_j_caja, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_agrega_fila, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(j_ingresoLayout.createSequentialGroup()
                        .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_j_compra, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(j_ingresoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_j_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        setTitle("Registrar Ingreso de Productos");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Generales"));

        jLabel1.setText("Proveedor:");

        txt_ruc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_ruc.setEnabled(false);
        txt_ruc.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txt_rucInputMethodTextChanged(evt);
            }
        });
        txt_ruc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_rucKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_rucKeyTyped(evt);
            }
        });

        txt_razon_social.setFocusable(false);

        jLabel2.setText("Tipo Doc.");

        jLabel3.setText("Serie:");

        jLabel4.setText("Numero:");

        jLabel5.setText("Fecha Compra:");

        txt_serie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_serie.setEnabled(false);
        txt_serie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_serieKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_serieKeyTyped(evt);
            }
        });

        cbx_tipo_doc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_tipo_docKeyPressed(evt);
            }
        });

        txt_numero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_numero.setEnabled(false);
        txt_numero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_numeroKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_numeroKeyTyped(evt);
            }
        });

        try {
            txt_fecha_compra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_fecha_compra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_fecha_compra.setEnabled(false);
        txt_fecha_compra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_fecha_compraKeyPressed(evt);
            }
        });

        jLabel7.setText("Moneda:");

        cbx_moneda.setEnabled(false);
        cbx_moneda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_monedaKeyPressed(evt);
            }
        });

        jLabel8.setText("TC. Compra:");

        txt_tc.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_tc.setEnabled(false);
        txt_tc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_tcKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_tcKeyTyped(evt);
            }
        });

        btn_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_cerrar.setText("Cerrar");
        btn_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_serie, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                            .addComponent(txt_numero))
                        .addComponent(txt_fecha_compra, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbx_tipo_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel1))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE)
                                .addComponent(btn_cerrar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbx_moneda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(86, 86, 86)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_tc, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(txt_razon_social))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_tipo_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_razon_social, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbx_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_tc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_fecha_compra, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle de Ingreso"));

        t_detalle_ingreso.setModel(new javax.swing.table.DefaultTableModel(
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
        t_detalle_ingreso.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_detalle_ingreso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_detalle_ingresoMouseClicked(evt);
            }
        });
        t_detalle_ingreso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_detalle_ingresoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_detalle_ingreso);

        btn_add_productos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/add.png"))); // NOI18N
        btn_add_productos.setText("Productos");
        btn_add_productos.setEnabled(false);
        btn_add_productos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_productosActionPerformed(evt);
            }
        });

        btn_eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delete.png"))); // NOI18N
        btn_eliminar.setText("Eliminar");
        btn_eliminar.setEnabled(false);
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });

        btn_modificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/application_edit.png"))); // NOI18N
        btn_modificar.setText("Modificar");
        btn_modificar.setEnabled(false);
        btn_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarActionPerformed(evt);
            }
        });

        txt_buscar_productos.setText("BUSCAR PRODUCTOS o PRESIONAR F2");
        txt_buscar_productos.setToolTipText("BUSCAR PRODUCTOS o PRESIONAR F2");
        txt_buscar_productos.setEnabled(false);
        txt_buscar_productos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_buscar_productosKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_eliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_modificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_buscar_productos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_add_productos)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_add_productos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_modificar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_buscar_productos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Finalizar"));

        jLabel9.setText("Sub Total:");

        jLabel10.setText("IGV:");

        jLabel11.setText("Total:");

        btn_grabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/accept.png"))); // NOI18N
        btn_grabar.setText("Registrar Ingreso");
        btn_grabar.setEnabled(false);
        btn_grabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_grabarActionPerformed(evt);
            }
        });

        txt_subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_subtotal.setText("0.00");
        txt_subtotal.setFocusable(false);

        txt_igv.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_igv.setText("0.00");
        txt_igv.setFocusable(false);

        txt_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_total.setText("0.00");
        txt_total.setFocusable(false);

        jLabel6.setText("Nro Filas:");

        lbl_contando.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_igv, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(lbl_contando)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_grabar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(lbl_contando))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_grabar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_igv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_rucKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rucKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_ruc.getText().length() == 11) {
                pro.setRuc(txt_ruc.getText());
                //validar ruc 
                boolean ruc_valido = pro.validar_RUC(pro.getRuc());
                if (ruc_valido == true) {
                    try {

                        Statement st = con.conexion();
                        String buscar = "select razon_social from proveedores where ruc_proveedor = '" + pro.getRuc() + "'";
                        ResultSet rs = con.consulta(st, buscar);
                        if (rs.next()) {
                            txt_razon_social.setText(rs.getString("razon_social"));
                            //cbx_tienda.requestFocus();
                            cbx_moneda.setEnabled(true);
                            cbx_moneda.requestFocus();
                        } else {
                            Notification.show("Registro de Proveedor", "El proveedor no existe, registrar por favor", Notification.NICON_LIGHT_THEME);
                            frm_reg_proveedor proveedor = new frm_reg_proveedor();
                            frm_reg_proveedor.origen = "ingreso";
                            frm_reg_proveedor.accion = "registrar";
                            ven.llamar_ventana(proveedor);
                        }
                        con.cerrar(rs);
                        con.cerrar(st);
                    } catch (SQLException ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }
                } else {
                    Notification.show("Registro de Proveedor", "EL RUC INGRESADO NO ES VALIDO \nVERIFIQUE POR FAVOR", Notification.NICON_LIGHT_THEME);
                    frm_reg_ingreso.txt_ruc.setText("");
                    frm_reg_ingreso.txt_ruc.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txt_rucKeyPressed

    public static Double subtotal() {
        double monto = 0.0;
        double cantidad;
        double precio;
        int nro_filas = t_detalle_ingreso.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            cantidad = Double.parseDouble(t_detalle_ingreso.getValueAt(j, 2).toString());
            precio = Double.parseDouble(t_detalle_ingreso.getValueAt(j, 4).toString());
            monto += (cantidad * precio);
        }
        return monto;
    }

    private void calcular_parcial() {
        double monto;
        double cantidad;
        double precio;
        int nro_filas = t_detalle_ingreso.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            cantidad = Double.parseDouble(t_detalle_ingreso.getValueAt(j, 2).toString());
            precio = Double.parseDouble(t_detalle_ingreso.getValueAt(j, 4).toString());
            monto = (cantidad * precio);
            t_detalle_ingreso.setValueAt(ven.formato_numero(monto), j, 6);
        }
        txt_subtotal.setText(ven.formato_totales(subtotal()));
        txt_igv.setText(ven.formato_totales(subtotal() * 0.18));
        txt_total.setText(ven.formato_totales(subtotal() * 1.18));
    }


    private void cbx_tipo_docKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_tipo_docKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tido.setId(cbx_tipo_doc.getSelectedIndex() + 1);
            if (tido.getId() == 3) {
                tido.setSer(tido.ver_serie(tienda, empresa));
                txt_serie.setText(tido.getSer() + "");
            }
            txt_serie.setEnabled(true);
            txt_serie.requestFocus();
        }
    }//GEN-LAST:event_cbx_tipo_docKeyPressed

    private void txt_serieKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_serieKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_serie.getText().length() > 0) {
                if (tido.getId() == 3) {
                    tido.setNro(tido.ver_numero(tienda, empresa));
                    txt_numero.setText(tido.getNro() + "");
                }
                txt_numero.setEnabled(true);
                txt_numero.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_serieKeyPressed

    private void txt_numeroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_numeroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_numero.getText().length() > 0) {
                txt_fecha_compra.setEnabled(true);
                txt_fecha_compra.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_numeroKeyPressed

    private void txt_fecha_compraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fecha_compraKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_fecha_compra.getText().length() == 10) {
                txt_ruc.setEnabled(true);
                txt_ruc.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_fecha_compraKeyPressed

    private void cbx_monedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_monedaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                pro.setRuc(txt_ruc.getText());
                int idtido = cbx_tipo_doc.getSelectedIndex() + 1;
                int serie = Integer.parseInt(txt_serie.getText());
                int nro = Integer.parseInt(txt_numero.getText());

                Statement st = con.conexion();
                String ver_nro = "select id_ingreso from ingresos where ruc_proveedor = '" + pro.getRuc() + "' and id_tido = '" + idtido + "' "
                        + "and serie = '" + serie + "' and numero = '" + nro + "'";
                ResultSet rs = con.consulta(st, ver_nro);
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "El documento ya esta registrado, por favor ingrese otro documento");
                    frm_ver_ingreso ingreso = new frm_ver_ingreso();
                    ven.llamar_ventana(ingreso);
                    this.dispose();
                } else {
                    double tc_compra = mon.tc_compra(ven.fechabase(txt_fecha_compra.getText()));
                    txt_tc.setText(ven.formato_tc(tc_compra));
                    txt_tc.setEnabled(true);
                    txt_tc.requestFocus();
                }
                con.cerrar(rs);
                con.cerrar(st);
            } catch (SQLException | HeadlessException e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_cbx_monedaKeyPressed

    private void txt_tcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_tcKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_tc.getText();
            if (ven.esDecimal(texto)) {
                Double tc = Double.parseDouble(texto);
                if (tc > 0) {
                    cargar_productos_txt();
                    btn_add_productos.setEnabled(true);
                    txt_buscar_productos.setEnabled(true);
                    txt_buscar_productos.setText("");
                    txt_buscar_productos.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txt_tcKeyPressed

    private void llenar() {
        Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        ing.setAnio(anio);
        ing.setFecha_ingreso(ven.fechabase(txt_fecha_compra.getText()));
        ing.setTido(cbx_tipo_doc.getSelectedIndex() + 1);
        ing.setSerie(Integer.parseInt(txt_serie.getText()));
        ing.setNumero(Integer.parseInt(txt_numero.getText()));
        ing.setMoneda(cbx_moneda.getSelectedIndex() + 1);
        ing.setTc(Double.parseDouble(txt_tc.getText()));
        ing.setSubtotal(subtotal());
        ing.setProveedor(txt_ruc.getText());
        pro.setRuc(txt_ruc.getText());
        pro.setRaz_soc(txt_razon_social.getText());
    }

    private void btn_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grabarActionPerformed
        llenar();
        int filas = t_detalle_ingreso.getRowCount();

        if (filas > 0) {

            //capturar ultimo id de ingreso
            ing.setId(ing.ultimo_codigo(tienda, empresa));

            //registrar compra
            boolean registrar_ingreso = ing.crear_ingreso(tienda, empresa);
            tido.setNro(Integer.parseInt(txt_numero.getText()));
            tido.act_numero(cbx_tipo_doc.getSelectedIndex() + 1, tido.getNro(), tienda, empresa);

            if (registrar_ingreso) {

                //registrar detalle de ingreso
                for (int j = 0; j < filas; j++) {
                    mat.setId(Integer.parseInt(t_detalle_ingreso.getValueAt(j, 0).toString()));
                    mat.setCan(Double.parseDouble(t_detalle_ingreso.getValueAt(j, 2).toString()));
                    mat.setCantidad_caja(Double.parseDouble(t_detalle_ingreso.getValueAt(j, 7).toString()));
                    mat.setCosto(Double.parseDouble(t_detalle_ingreso.getValueAt(j, 4).toString()));
                    mat.setPrecio(Double.parseDouble(t_detalle_ingreso.getValueAt(j, 5).toString()));
                    mat.setCosto(mat.getCosto() / mat.getCantidad_caja());
                    int id_kardex = 0;

                    //registro de detalle
                    try {
                        Statement st = con.conexion();
                        String ins_det_com = "insert into detalle_ingreso Values ('" + ing.getId() + "','" + ing.getAnio() + "', '" + tienda + "', '" + empresa + "', '" + mat.getId() + "', "
                                + "'" + mat.getCan() + "', '" + mat.getCosto() + "', '" + mat.getCantidad_caja() + "')";
                        System.out.println(ins_det_com);
                        con.actualiza(st, ins_det_com);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }

                    mat.setCan(mat.getCan() * mat.getCantidad_caja());

                    //buscar si existe producto en productos_almacenes, si existe actualzar cantidad, sino ingresar producto
                    Object cantidad_actual[] = mat.obtener_cantidad_actual(tienda, empresa);
                    boolean existe_producto = (boolean) cantidad_actual[0];
                    mat.setCant_act(Double.parseDouble(cantidad_actual[1].toString()));

                    double precio_convertido = ing.getTc() * mat.getCosto();
                    if (mon.getId() == 2) {
                        mat.setCosto(precio_convertido);
                    }

                    //actualizar cantidad_caja_compra
                    try {
                        Statement st = con.conexion();
                        String act_pro = "update productos set cantidad_caja = '" + mat.getCantidad_caja() + "', costo = '" + mat.getCosto() + "', precio = '" + mat.getPrecio() + "' where id_producto = '" + mat.getId() + "'";
                        System.out.println(act_pro);
                        con.actualiza(st, act_pro);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }

                    if (existe_producto == true) {
                        //ACTUALIZAR CANTIDAD ACTUAL
                        mat.setCant_act(mat.getCant_act() + mat.getCan());
                        mat.a_producto_almacen(tienda, empresa);

                    } else {
                        //REGISTRAR PRODUCTO EN ALMACEN
                        mat.setCan_min(3.0);
                        mat.setCan_max(12.0);
                        mat.setUbicacion("-");
                        mat.setUltima_salida("7000-01-01");
                        mat.setUltimo_ingreso(ing.getFecha_ingreso());
                        mat.i_producto_almacen(tienda, empresa);
                    }

                    //capturar ultimo id de kardex
                    try {
                        Statement st = con.conexion();
                        String bus_idk = "select MAX( id_kardex ) as id_kardex from kardex where id_producto = '" + mat.getId() + "' and id_almacen = '" + tienda + "' and empresa = '"+empresa+"'";
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
                        String ins_kardex = "insert into kardex Values ('" + id_kardex + "', '" + tienda + "', '" + empresa + "', '" + mat.getId() + "','" + ing.getFecha_ingreso() + "', '" + pro.getRuc() + "', "
                                + "'" + pro.getRaz_soc() + "', '" + ing.getTido() + "', '" + ing.getSerie() + "', '" + ing.getNumero() + "', '" + mat.getCan() + "', '" + mat.getCosto() + "', '0.00', '0.00',  '2', NOW())";
                        System.out.println(ins_kardex);
                        con.actualiza(st, ins_kardex);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }

                }
                frm_ver_ingreso ingreso = new frm_ver_ingreso();
                ven.llamar_ventana(ingreso);
                this.dispose();
                Notification.show("Ingreso de Productos", "Registro Exitoso", Notification.CONFIRM_MESSAGE, Notification.NICON_LIGHT_THEME);
            }

        }
    }//GEN-LAST:event_btn_grabarActionPerformed

    private void t_detalle_ingresoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_detalle_ingresoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calcular_parcial();
            btn_eliminar.setEnabled(false);
            btn_modificar.setEnabled(false);
            txt_buscar_productos.setText("");
            txt_buscar_productos.requestFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (i > -1) {
                detalle.removeRow(i);
                calcular_parcial();
                btn_eliminar.setEnabled(false);
                btn_modificar.setEnabled(false);
                btn_add_productos.requestFocus();
                i = -1;
            } else {
                JOptionPane.showMessageDialog(null, "NO SE HA SELECCIONADO UNA COLUMNA");
            }
        }
    }//GEN-LAST:event_t_detalle_ingresoKeyPressed

    private void btn_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerrarActionPerformed
        frm_ver_ingreso ingreso = new frm_ver_ingreso();
        ven.llamar_ventana(ingreso);
        this.dispose();
    }//GEN-LAST:event_btn_cerrarActionPerformed

    private void btn_add_productosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_productosActionPerformed
        frm_ver_todo_productos productos = new frm_ver_todo_productos();
        frm_ver_todo_productos.origen = "ingreso";
        ven.llamar_ventana(productos);
    }//GEN-LAST:event_btn_add_productosActionPerformed

    private void txt_j_cantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_cantidadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double cantidad = Double.parseDouble(txt_j_cantidad.getText());
            if (cantidad > 0) {
                txt_j_caja.setEnabled(true);
                txt_j_caja.selectAll();
                txt_j_caja.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_j_cantidadKeyPressed

    private void txt_j_cantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_cantidadKeyTyped
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_j_cantidadKeyTyped

    private void txt_j_compraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_compraKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F12) {
            double monto = 0.001;
            txt_j_compra.setText(monto + "");
            txt_j_venta.setEnabled(true);
            txt_j_venta.selectAll();
            txt_j_venta.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double compra = Double.parseDouble(txt_j_compra.getText());
            if (compra > 0) {
                txt_j_venta.setEnabled(true);
                txt_j_venta.selectAll();
                txt_j_venta.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_j_compraKeyPressed

    private void txt_j_ventaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_ventaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double venta = Double.parseDouble(txt_j_venta.getText());
            if (venta > 0) {
                btn_agrega_fila.setEnabled(true);
                btn_agrega_fila.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_j_ventaKeyPressed

    private void btn_agrega_filaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agrega_filaActionPerformed
        //agregar a objeto
        Object fila[] = new Object[11];
        fila[0] = txt_j_idproducto.getText();
        fila[1] = txt_j_descripcion.getText();
        double cantidad = Double.parseDouble(txt_j_cantidad.getText());
        fila[2] = ven.formato_numero(cantidad);
        fila[3] = lbl_j_medida.getText();
        double compra = Double.parseDouble(txt_j_compra.getText());
        fila[4] = ven.formato_numero(compra);
        double venta = Double.parseDouble(txt_j_venta.getText());
        fila[5] = ven.formato_numero(venta);
        fila[6] = ven.formato_numero(compra * cantidad);
        double cantidad_caja = Double.parseDouble(txt_j_caja.getText());
        fila[7] = ven.formato_numero(cantidad_caja);

        //estado de ingreso
        boolean ingresar = false;

        //verificar fila no se repite
        int contar_filas = t_detalle_ingreso.getRowCount();
        if (contar_filas == 0) {
            ingresar = true;
        }

        if (contar_filas > 0) {
            int id_producto_nuevo = Integer.parseInt(txt_j_idproducto.getText());
            for (int x = 0; x < contar_filas; x++) {
                int id_producto_fila = Integer.parseInt(t_detalle_ingreso.getValueAt(x, 0).toString());
                if (id_producto_nuevo == id_producto_fila) {
                    JOptionPane.showMessageDialog(null, "El Producto a Ingresar ya existe en la lista");
                    limpiar_jdialog();
                } else {
                    ingresar = true;
                }
            }
        }

        if (ingresar == true) {
            detalle.addRow(fila);
            t_detalle_ingreso.setModel(detalle);
            btn_grabar.setEnabled(true);
            calcular_parcial();
            txt_j_idproducto.setText("");
            txt_j_descripcion.setText("");
            txt_j_cantidad.setText("0.00");
            txt_j_caja.setText("");
            btn_agrega_fila.setEnabled(false);
            j_ingreso.dispose();
            contar_filas();
            txt_buscar_productos.setText("");
            txt_buscar_productos.requestFocus();
        }
    }//GEN-LAST:event_btn_agrega_filaActionPerformed

    private void t_detalle_ingresoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_detalle_ingresoMouseClicked
        i = t_detalle_ingreso.getSelectedRow();
    }//GEN-LAST:event_t_detalle_ingresoMouseClicked

    private void txt_rucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rucKeyTyped
        ven.limitar_caracteres(evt, txt_ruc, 11);
        ven.solo_numeros(evt);
    }//GEN-LAST:event_txt_rucKeyTyped

    private void txt_serieKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_serieKeyTyped
        ven.limitar_caracteres(evt, txt_serie, 4);
        ven.solo_numeros(evt);
    }//GEN-LAST:event_txt_serieKeyTyped

    private void txt_numeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_numeroKeyTyped
        ven.limitar_caracteres(evt, txt_numero, 7);
        ven.solo_numeros(evt);
    }//GEN-LAST:event_txt_numeroKeyTyped

    private void txt_tcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_tcKeyTyped
        ven.limitar_caracteres(evt, txt_tc, 5);
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_tcKeyTyped

    private void txt_j_cajaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_cajaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double cantidad = Double.parseDouble(txt_j_caja.getText());
            if (cantidad > 0) {
                txt_j_compra.setEnabled(true);
                txt_j_compra.selectAll();
                txt_j_compra.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_j_cajaKeyPressed

    private void txt_j_cajaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_cajaKeyTyped
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_j_cajaKeyTyped

    private void txt_rucInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txt_rucInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rucInputMethodTextChanged

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        if (i > -1) {
            detalle.removeRow(i);
            calcular_parcial();
            btn_eliminar.setEnabled(false);
            btn_modificar.setEnabled(false);
            btn_add_productos.requestFocus();
            i = -1;
        } else {
            JOptionPane.showMessageDialog(null, "NO SE HA SELECCIONADO UNA COLUMNA");
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        if (i > -1) {

        } else {
            JOptionPane.showMessageDialog(null, "NO SE HA SELECCIONADO UNA COLUMNA");
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void txt_j_descripcionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_descripcionKeyPressed

    }//GEN-LAST:event_txt_j_descripcionKeyPressed

    private void txt_buscar_productosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_productosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            frm_ver_todo_productos productos = new frm_ver_todo_productos();
            frm_ver_todo_productos.origen = "ingreso";
            ven.llamar_ventana(productos);
            //j_ingreso.dispose();
        }

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            txt_buscar_productos.setText("");
            txt_buscar_productos.requestFocus();
        }
    }//GEN-LAST:event_txt_buscar_productosKeyPressed

    private void txt_j_compraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_compraKeyTyped
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_j_compraKeyTyped

    private void txt_j_ventaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_j_ventaKeyTyped
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_j_ventaKeyTyped

    private void limpiar_jdialog() {
        txt_j_caracteristicas.setText("");
        txt_j_descripcion.setText("");
        txt_j_idproducto.setText("");
        txt_j_cantidad.setText("1.00");
        txt_j_caja.setText("1.00");
        txt_j_compra.setText("0.00");
        txt_j_venta.setText("0.00");
        btn_agrega_fila.setEnabled(false);
        txt_j_cantidad.setEnabled(true);
        txt_j_caja.setEnabled(false);
        txt_j_compra.setEnabled(false);
        txt_j_venta.setEnabled(false);
        txt_j_cantidad.requestFocus();
    }

    void cargar_proveedores() {
        try {
            // autocompletar = new TextAutoCompleter(txt_buscar_producto);
            TextAutoCompleter autocompletar = new TextAutoCompleter(txt_ruc, new AutoCompleterCallback() {
                @Override
                public void callback(Object selectedItem) {
                    txt_ruc.setText("");
                    txt_ruc.requestFocus();
                    String cadena = selectedItem.toString();
                    JOptionPane.showMessageDialog(null, "El usuario seleccionó: " + cadena);
                }
            });

            autocompletar.setMode(0);
            autocompletar.setCaseSensitive(false);
            Statement st = con.conexion();
            String sql = "select ruc_proveedor from proveedores order by ruc_proveedor asc";
            ResultSet rs = con.consulta(st, sql);
            while (rs.next()) {
                autocompletar.addItem(rs.getString("ruc_proveedor"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getLocalizedMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void add_productos() {
        j_ingreso.setModal(true);
        j_ingreso.setSize(700, 300);
        j_ingreso.setLocationRelativeTo(null);
        j_ingreso.setVisible(true);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btn_add_productos;
    private javax.swing.JButton btn_agrega_fila;
    private javax.swing.JButton btn_cerrar;
    private javax.swing.JButton btn_eliminar;
    public static javax.swing.JButton btn_grabar;
    private javax.swing.JButton btn_modificar;
    public static javax.swing.JComboBox cbx_moneda;
    private javax.swing.JComboBox cbx_tipo_doc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JDialog j_ingreso;
    private javax.swing.JLabel lbl_contando;
    public static javax.swing.JLabel lbl_j_medida;
    public static javax.swing.JTable t_detalle_ingreso;
    public static javax.swing.JTextField txt_buscar_productos;
    public static javax.swing.JFormattedTextField txt_fecha_compra;
    public static javax.swing.JTextField txt_igv;
    public static javax.swing.JTextField txt_j_caja;
    public static javax.swing.JTextField txt_j_cantidad;
    public static javax.swing.JTextArea txt_j_caracteristicas;
    public static javax.swing.JTextField txt_j_compra;
    public static javax.swing.JTextField txt_j_descripcion;
    public static javax.swing.JLabel txt_j_idproducto;
    public static javax.swing.JTextField txt_j_venta;
    private javax.swing.JTextField txt_numero;
    public static javax.swing.JTextField txt_razon_social;
    public static javax.swing.JTextField txt_ruc;
    private javax.swing.JTextField txt_serie;
    public static javax.swing.JTextField txt_subtotal;
    public static javax.swing.JTextField txt_tc;
    public static javax.swing.JTextField txt_total;
    // End of variables declaration//GEN-END:variables
}
