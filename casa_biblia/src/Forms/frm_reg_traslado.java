/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Cl_Almacen;
import Clases.Cl_Conectar;
import Clases.Cl_Empresa;
import Clases.Cl_Envio;
import Clases.Cl_Productos;
import Clases.Cl_Proveedor;
import Clases.Cl_Tipo_Doc;
import Clases.Cl_Varios;
import Vistas.frm_ver_mis_productos;
import Vistas.frm_ver_traslados;
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
public final class frm_reg_traslado extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    Cl_Proveedor pro = new Cl_Proveedor();
    Cl_Envio env = new Cl_Envio();
    Cl_Tipo_Doc tido = new Cl_Tipo_Doc();
    Cl_Productos mat = new Cl_Productos();
    Cl_Empresa emp = new Cl_Empresa();
    Cl_Almacen alm = new Cl_Almacen();
    public static DefaultTableModel modelo_detalle = null;
    public static String accion;
    int fila_seleccionada;
    int tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();

    public static String vperiodo, vempresa;
    public static int vcodigo, vtienda;

    /**
     * Creates new form frm_reg_envio
     */
    public frm_reg_traslado() {
        initComponents();
        txt_fecha.setText(ven.fechaformateada(ven.getFechaActual()));
        String ver_tido = "select nombre from tipo_documento order by id_tido";
        ver_tipodoc(ver_tido);
        emp.ver_empresas(cbx_empresa);
        //txt_partida.setText(frm_menu.alm.getDireccion());

        ver_detalle_ingreso();
    }

    private void ver_tipodoc(String query) {
        try {
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);

            while (rs.next()) {
                String fila;
                fila = rs.getString("nombre");
                cbx_tido.addItem(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
        } catch (SQLException e) {
            System.out.print(e);
        }
    }

    public void ver_detalle_ingreso() {
        try {
            modelo_detalle = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    if (accion.equals("registrar")) {
                        return columna == 4 || columna == 5;
                    } else {
                        return false;
                    }
                }
            };
            //Establecer como cabezeras el nombre de las colimnas
            modelo_detalle.addColumn("Cod.");
            modelo_detalle.addColumn("Descripcion");
            modelo_detalle.addColumn("Can. Act.");
            modelo_detalle.addColumn("Und. Med");
            modelo_detalle.addColumn("Can. Sal.");
            modelo_detalle.addColumn("Precio");
            t_detalle.setModel(modelo_detalle);
            t_detalle.getColumnModel().getColumn(0).setPreferredWidth(50);
            t_detalle.getColumnModel().getColumn(1).setPreferredWidth(400);
            t_detalle.getColumnModel().getColumn(2).setPreferredWidth(70);
            t_detalle.getColumnModel().getColumn(3).setPreferredWidth(70);
            t_detalle.getColumnModel().getColumn(4).setPreferredWidth(70);
            t_detalle.getColumnModel().getColumn(5).setPreferredWidth(70);
            modelo_detalle.fireTableDataChanged();
            ven.centrar_celda(t_detalle, 0);
            ven.derecha_celda(t_detalle, 2);
            ven.centrar_celda(t_detalle, 3);
            ven.derecha_celda(t_detalle, 4);
            ven.derecha_celda(t_detalle, 5);
        } catch (Exception e) {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    void cargar_productos_txt() {
        try {
            // autocompletar = new TextAutoCompleter(txt_buscar_producto);
            TextAutoCompleter autocompletar = new TextAutoCompleter(txt_buscar_producto, new AutoCompleterCallback() {
                @Override
                public void callback(Object selectedItem) {
                    txt_buscar_producto.setText("");
                    txt_buscar_producto.requestFocus();
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
                                String texto = JOptionPane.showInputDialog("Ingrese Cantidad");
                                double cantidad_nueva;
                                if (texto != null) {
                                    if (ven.esDecimal(texto)) {
                                        cantidad_nueva = Double.parseDouble(texto);
                                    } else {
                                        cantidad_nueva = 1;
                                    }
                                } else {
                                    cantidad_nueva = 1;
                                }

                                try {
                                    Statement st = con.conexion();
                                    String ver_producto = "select p.id_producto, p.descripcion, p.codigo_externo, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, p.precio, f.descripcion as nombre_familia, cf.descripcion as nombre_clase from productos_almacenes as pe "
                                            + "inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = um.id_und_med inner join familia_productos as f on p.familia = f.id inner join clasificacion_familia as cf "
                                            + "on cf.id= p.clase_familia and cf.familia=p.familia where pe.id_almacen = '" + frm_menu.alm.getCodigo() + "' "
                                            + "and empresa = '" + frm_menu.emp.getRuc() + "' and p.id_producto = '" + id_producto + "'";
                                    System.out.println(ver_producto);
                                    ResultSet rs = con.consulta(st, ver_producto);
                                    if (rs.next()) {
                                        Object fila[] = new Object[8];
                                        fila[0] = id_producto;
                                        fila[1] = rs.getString("descripcion") + " - " + rs.getString("codigo_externo");
                                        double cantidad = rs.getDouble("cantidad_actual");
                                        fila[2] = ven.formato_numero(cantidad);
                                        fila[3] = rs.getString("corto");
                                        fila[4] = ven.formato_numero(cantidad_nueva);
                                        fila[5] = ven.formato_numero(rs.getDouble("precio"));

                                        modelo_detalle.addRow(fila);
                                        t_detalle.setModel(modelo_detalle);
                                    }

                                    con.cerrar(rs);
                                    con.cerrar(st);

                                } catch (SQLException | HeadlessException e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                                }

                                txt_buscar_producto.setText("");
                                txt_buscar_producto.requestFocus();
                                contar_filas();
                            }

                        }
                        if (contiene_guion == false) {
                            txt_buscar_producto.setText("");
                            txt_buscar_producto.requestFocus();
                        }

                        // calcular_total();
                    }
                }
            });

            autocompletar.setMode(0);
            autocompletar.setCaseSensitive(false);
            Statement st = con.conexion();
            String sql = "select p.id_producto, p.descripcion, p.codigo_externo, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, p.precio from productos_almacenes as pe "
                    + "inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = um.id_und_med "
                    + "where pe.id_almacen = '" + frm_menu.alm.getCodigo() + "' and empresa = '" + frm_menu.emp.getRuc() + "'";
            ResultSet rs = con.consulta(st, sql);
            while (rs.next()) {
                autocompletar.addItem(rs.getString("p.id_producto") + " - " + rs.getString("p.descripcion") + " - " + rs.getString("p.codigo_externo") + " -- S/ " + rs.getString("p.precio") + " -- Cant.: " + rs.getString("pe.cantidad_actual"));
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
        int contar_filas = t_detalle.getRowCount();
        if (contar_filas == 0) {
            ingresar = true;
        }

        if (contar_filas > 0) {
            for (int j = 0; j < contar_filas; j++) {
                int id_producto_fila = Integer.parseInt(t_detalle.getValueAt(j, 0).toString());
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
        int contar_filas = t_detalle.getRowCount();
        if (contar_filas > 0) {
            btn_registrar.setEnabled(true);
        }
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

        jd_transportista = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_marca = new javax.swing.JTextField();
        txt_modelo = new javax.swing.JTextField();
        txt_placa = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        rbt_si = new javax.swing.JRadioButton();
        rbt_no = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        txt_ruc_transporte = new javax.swing.JTextField();
        txt_razon_transporte = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txt_brevete = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_inscripcion = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txt_chofer = new javax.swing.JTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jd_add_producto = new javax.swing.JDialog();
        jLabel18 = new javax.swing.JLabel();
        txt_jd_id = new javax.swing.JTextField();
        txt_jd_descripcion = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txt_jd_actual = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txt_jd_medida = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txt_jd_enviada = new javax.swing.JTextField();
        btn_jd_agrega = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        txt_jd_compra = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_jd_venta = new javax.swing.JTextField();
        jd_productos = new javax.swing.JDialog();
        jLabel24 = new javax.swing.JLabel();
        txt_jd_buscar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        t_jd_productos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbx_tido = new javax.swing.JComboBox();
        txt_serie = new javax.swing.JTextField();
        txt_numero = new javax.swing.JTextField();
        txt_fecha = new javax.swing.JFormattedTextField();
        lbl_empresa = new javax.swing.JLabel();
        txt_razon_social = new javax.swing.JTextField();
        btn_cerrar = new javax.swing.JButton();
        cbx_empresa = new javax.swing.JComboBox();
        lbl_tienda = new javax.swing.JLabel();
        cbx_tiendas = new javax.swing.JComboBox();
        btn_verificar = new javax.swing.JButton();
        btn_recibir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_partida = new javax.swing.JTextField();
        txt_llegada = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_detalle = new javax.swing.JTable();
        btn_registrar = new javax.swing.JButton();
        btn_add_transportista = new javax.swing.JButton();
        btn_add_producto = new javax.swing.JButton();
        txt_buscar_producto = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        lbl_contando = new javax.swing.JLabel();
        lbl_ayuda = new javax.swing.JLabel();

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Vehiculo"));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Marca:");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Modelo:");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Placa:");

        txt_marca.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txt_modelo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txt_placa.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel13.setText("Vehiculo Externo??");

        buttonGroup1.add(rbt_si);
        rbt_si.setText("Si");

        buttonGroup1.add(rbt_no);
        rbt_no.setText("No");

        jLabel14.setText("RUC:");

        txt_ruc_transporte.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(51, 51, 51)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_placa, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_modelo, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(txt_marca))
                .addGap(79, 79, 79)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_ruc_transporte, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(177, 177, 177))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(41, 41, 41)
                        .addComponent(rbt_si)
                        .addGap(18, 18, 18)
                        .addComponent(rbt_no)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txt_razon_transporte)
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbt_si, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbt_no, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ruc_transporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_placa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_razon_transporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Chofer"));

        jLabel15.setText("Brevete:");

        txt_brevete.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel16.setText("Certificado de Inscripcion:");

        txt_inscripcion.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel17.setText("Chofer:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17))
                .addGap(48, 48, 48)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txt_brevete, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(jLabel16)
                        .addGap(32, 32, 32)
                        .addComponent(txt_inscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 113, Short.MAX_VALUE))
                    .addComponent(txt_chofer))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_brevete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_inscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_chofer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jd_transportistaLayout = new javax.swing.GroupLayout(jd_transportista.getContentPane());
        jd_transportista.getContentPane().setLayout(jd_transportistaLayout);
        jd_transportistaLayout.setHorizontalGroup(
            jd_transportistaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_transportistaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_transportistaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jd_transportistaLayout.setVerticalGroup(
            jd_transportistaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_transportistaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel18.setText("Producto");

        txt_jd_id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_id.setText("Id.");
        txt_jd_id.setEnabled(false);

        txt_jd_descripcion.setText("Descripcion");
        txt_jd_descripcion.setFocusable(false);

        jLabel20.setText("Cantidad Actual:");

        txt_jd_actual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_actual.setText("0.00");
        txt_jd_actual.setFocusable(false);

        jLabel21.setText("Und Medida:");

        txt_jd_medida.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_medida.setEnabled(false);

        jLabel22.setText("Cantidad Enviada:");

        txt_jd_enviada.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_enviada.setEnabled(false);
        txt_jd_enviada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jd_enviadaKeyPressed(evt);
            }
        });

        btn_jd_agrega.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/disk.png"))); // NOI18N
        btn_jd_agrega.setText("Agregar");
        btn_jd_agrega.setEnabled(false);
        btn_jd_agrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_agregaActionPerformed(evt);
            }
        });

        jLabel23.setText("Precio de Compra:");

        txt_jd_compra.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_compra.setFocusable(false);

        jLabel7.setText("Precio de Venta:");

        txt_jd_venta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_venta.setFocusable(false);

        javax.swing.GroupLayout jd_add_productoLayout = new javax.swing.GroupLayout(jd_add_producto.getContentPane());
        jd_add_producto.getContentPane().setLayout(jd_add_productoLayout);
        jd_add_productoLayout.setHorizontalGroup(
            jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_add_productoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jd_add_productoLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_id, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jd_add_productoLayout.createSequentialGroup()
                        .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jd_add_productoLayout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(102, 102, 102))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_add_productoLayout.createSequentialGroup()
                                .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jd_add_productoLayout.createSequentialGroup()
                                        .addGap(93, 93, 93)
                                        .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txt_jd_actual, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_jd_medida, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jd_add_productoLayout.createSequentialGroup()
                                .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_jd_compra, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_jd_enviada, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jd_add_productoLayout.createSequentialGroup()
                                .addComponent(txt_jd_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_jd_agrega)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jd_add_productoLayout.setVerticalGroup(
            jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_add_productoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txt_jd_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_id, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txt_jd_actual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_enviada, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(txt_jd_compra, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_medida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txt_jd_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_jd_agrega, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel24.setText("Buscar");

        txt_jd_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jd_buscarKeyPressed(evt);
            }
        });

        t_jd_productos.setModel(new javax.swing.table.DefaultTableModel(
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
        t_jd_productos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_jd_productosKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(t_jd_productos);

        jButton1.setText("Cerrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_productosLayout = new javax.swing.GroupLayout(jd_productos.getContentPane());
        jd_productos.getContentPane().setLayout(jd_productosLayout);
        jd_productosLayout.setHorizontalGroup(
            jd_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_productosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1047, Short.MAX_VALUE)
                    .addGroup(jd_productosLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jd_productosLayout.setVerticalGroup(
            jd_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_productosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );

        setTitle("Registro de Traslado de Mercancia");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Generales"));

        jLabel1.setText("Tipo Doc.");

        jLabel2.setText("Serie");

        jLabel3.setText("Numero");

        jLabel4.setText("Fecha");

        cbx_tido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_tidoKeyPressed(evt);
            }
        });

        txt_serie.setForeground(new java.awt.Color(102, 102, 102));
        txt_serie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_serie.setEnabled(false);
        txt_serie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_serieKeyPressed(evt);
            }
        });

        txt_numero.setForeground(new java.awt.Color(102, 102, 102));
        txt_numero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_numero.setEnabled(false);
        txt_numero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_numeroKeyPressed(evt);
            }
        });

        txt_fecha.setForeground(new java.awt.Color(102, 102, 102));
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

        lbl_empresa.setText("Empresa Destino:");

        txt_razon_social.setForeground(new java.awt.Color(102, 102, 102));
        txt_razon_social.setEnabled(false);
        txt_razon_social.setFocusable(false);

        btn_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_cerrar.setText("Cerrar");
        btn_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerrarActionPerformed(evt);
            }
        });

        cbx_empresa.setForeground(new java.awt.Color(102, 102, 102));
        cbx_empresa.setEnabled(false);
        cbx_empresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_empresaKeyPressed(evt);
            }
        });

        lbl_tienda.setText("Tienda Destino:");

        cbx_tiendas.setForeground(new java.awt.Color(102, 102, 102));
        cbx_tiendas.setEnabled(false);
        cbx_tiendas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_tiendasKeyPressed(evt);
            }
        });

        btn_verificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/arrow_redo.png"))); // NOI18N
        btn_verificar.setText("Verificar Cantidad");
        btn_verificar.setEnabled(false);
        btn_verificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_verificarActionPerformed(evt);
            }
        });

        btn_recibir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/compras.png"))); // NOI18N
        btn_recibir.setText("Recibir");
        btn_recibir.setEnabled(false);
        btn_recibir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_recibirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbx_tido, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txt_numero, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                        .addComponent(txt_serie, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_empresa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbx_empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                                .addComponent(btn_cerrar))
                            .addComponent(txt_razon_social)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_tienda)
                                .addGap(27, 27, 27)
                                .addComponent(cbx_tiendas, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_recibir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_verificar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_tido, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_razon_social, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_tienda)
                    .addComponent(cbx_tiendas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_verificar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_recibir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Direcciones"));

        jLabel8.setText("Punto de Partida:");

        jLabel9.setText("Punto de Llegada:");

        txt_partida.setForeground(new java.awt.Color(102, 102, 102));
        txt_partida.setEnabled(false);

        txt_llegada.setForeground(new java.awt.Color(102, 102, 102));
        txt_llegada.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_partida)
                    .addComponent(txt_llegada))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_partida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_llegada, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle de Envio"));

        t_detalle.setModel(new javax.swing.table.DefaultTableModel(
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
        t_detalle.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_detalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_detalleMouseClicked(evt);
            }
        });
        t_detalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_detalleKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_detalle);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_registrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/add.png"))); // NOI18N
        btn_registrar.setText("Registrar");
        btn_registrar.setEnabled(false);
        btn_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrarActionPerformed(evt);
            }
        });

        btn_add_transportista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delivery.png"))); // NOI18N
        btn_add_transportista.setText("Transportista");
        btn_add_transportista.setEnabled(false);
        btn_add_transportista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_transportistaActionPerformed(evt);
            }
        });

        btn_add_producto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/asterisk_orange.png"))); // NOI18N
        btn_add_producto.setText("Añadir Producto");
        btn_add_producto.setEnabled(false);
        btn_add_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_productoActionPerformed(evt);
            }
        });

        txt_buscar_producto.setText("BUSCAR PRODUCTOS");
        txt_buscar_producto.setEnabled(false);
        txt_buscar_producto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_buscar_productoFocusGained(evt);
            }
        });
        txt_buscar_producto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_buscar_productoKeyPressed(evt);
            }
        });

        jLabel19.setText("Cantidad de Filas:");

        lbl_contando.setText("0");

        lbl_ayuda.setText("Ayuda");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_contando)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_ayuda)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_registrar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_add_transportista)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_buscar_producto)
                        .addGap(8, 8, 8)
                        .addComponent(btn_add_producto)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txt_buscar_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_add_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_add_transportista, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(lbl_contando)
                    .addComponent(lbl_ayuda))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_add_transportistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_transportistaActionPerformed

    }//GEN-LAST:event_btn_add_transportistaActionPerformed

    private void cbx_tidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_tidoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tido.setId(cbx_tido.getSelectedIndex() + 1);
            txt_serie.setText(tido.ver_serie(tienda, empresa) + "");
            txt_serie.setEnabled(true);
            txt_serie.requestFocus();
        }
    }//GEN-LAST:event_cbx_tidoKeyPressed

    private void btn_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerrarActionPerformed
        frm_ver_traslados envios = new frm_ver_traslados();
        ven.llamar_ventana(envios);
        this.dispose();
    }//GEN-LAST:event_btn_cerrarActionPerformed

    private void txt_serieKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_serieKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_serie.getText().length() > 0) {
                txt_numero.setText(tido.ver_numero(tienda, empresa) + "");
                txt_numero.setEnabled(true);
                txt_numero.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_serieKeyPressed

    private void txt_numeroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_numeroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_numero.getText().length() > 0) {
                txt_fecha.setEnabled(true);
                txt_fecha.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_numeroKeyPressed

    private void txt_fechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fechaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_fecha.getText().length() == 10) {
                cbx_empresa.setEnabled(true);
                cbx_empresa.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_fechaKeyPressed

    private void btn_add_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_productoActionPerformed
        jd_productos.setModal(true);
        jd_productos.setTitle("Listar Productos");
        jd_productos.setSize(1100, 550);
        jd_productos.setLocationRelativeTo(null);
        txt_jd_buscar.setText("");
        txt_jd_buscar.requestFocus();
        Cl_Productos pro = new Cl_Productos();
        String query = "select p.id_producto, p.descripcion, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, p.precio "
                + "from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = "
                + "um.id_und_med where pe.id_almacen = '" + tienda + "' and pe.empresa = '" + empresa + "' order by p.descripcion asc limit 0";
        System.out.println(query);
        pro.ver_productos(t_jd_productos, query);
        jd_productos.setVisible(true);
    }//GEN-LAST:event_btn_add_productoActionPerformed

    private void txt_jd_enviadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_enviadaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_jd_enviada.getText().length() > 0) {
                double enviada = Double.parseDouble(txt_jd_enviada.getText());
                double actual = Double.parseDouble(txt_jd_actual.getText());
                if (enviada > 0.0) {
                    if (enviada <= actual) {
                        txt_jd_enviada.setText(ven.formato_numero(enviada));
                        btn_jd_agrega.setEnabled(true);
                        btn_jd_agrega.requestFocus();
                    } else {
                        Notification.show("Envio de Producto", "No puede enviar mas de lo permitido, supera cantidad actual", Notification.WARNING_MESSAGE, Notification.NICON_LIGHT_THEME);
                    }
                } else {
                    Notification.show("Envio de Producto", "Cantidad enviada debe ser mayor a cero(0.0)", Notification.WARNING_MESSAGE, Notification.NICON_LIGHT_THEME);
                }
            }
        }
    }//GEN-LAST:event_txt_jd_enviadaKeyPressed

    private void btn_jd_agregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_agregaActionPerformed
        Object fila[] = new Object[6];
        fila[0] = txt_jd_id.getText();
        fila[1] = txt_jd_descripcion.getText();
        fila[2] = ven.formato_numero(Double.parseDouble(txt_jd_enviada.getText()));
        fila[3] = txt_jd_medida.getText();
        fila[4] = ven.formato_numero(Double.parseDouble(txt_jd_actual.getText()));
        fila[5] = ven.formato_numero(Double.parseDouble(txt_jd_compra.getText()));
        modelo_detalle.addRow(fila);
        t_detalle.setModel(modelo_detalle);
        jd_add_producto.dispose();
        contar_filas();
        btn_registrar.setEnabled(true);
    }//GEN-LAST:event_btn_jd_agregaActionPerformed

    private void llenar() {
        Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        env.setAnio(anio);
        env.setNro_documento(cbx_empresa.getSelectedItem().toString());
        if (accion.equals("verificar")) {
            env.setDenominacion("DESDE TIENDA: " + cbx_tiendas.getSelectedItem().toString().toUpperCase().trim());
        }
        if (accion.equals("registrar")) {
            env.setDenominacion("PARA TIENDA: " + cbx_tiendas.getSelectedItem().toString().toUpperCase().trim());
        }
        env.setDireccion(txt_llegada.getText());
        env.setFecha(ven.fechabase(txt_fecha.getText()));
        tido.setId(cbx_tido.getSelectedIndex() + 1);
        tido.setSer(Integer.parseInt(txt_serie.getText()));
        tido.setNro(tido.ver_numero(tienda, empresa));
    }

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
//        if (accion.equals("registrar")) {
        btn_registrar.setEnabled(false);
        String fecha = ven.getFechaActual();
        String anio_periodo = fecha.charAt(0) + "" + fecha.charAt(1) + fecha.charAt(2) + fecha.charAt(3);
        String mes_periodo = fecha.charAt(5) + "" + fecha.charAt(6);
        String periodo = mes_periodo + "" + anio_periodo;
        String empresa_destino = cbx_empresa.getSelectedItem().toString();
        int tienda_destino = cbx_tiendas.getSelectedIndex() + 1;
        llenar();
        int filas = t_detalle.getRowCount();

        if (filas > 0) {
            //capturar id de envio
            try {
                Statement st = con.conexion();
                String ver_id = "select MAX(id_traslado) as id_traslado from traslados_almacen where periodo = '" + periodo + "' and id_almacen_origen = '" + tienda + "' and empresa_origen = '" + empresa + "'";
                System.out.println(ver_id);
                ResultSet rs = con.consulta(st, ver_id);
                if (rs.next()) {
                    env.setCodigo(rs.getInt("id_traslado") + 1);
                } else {
                    env.setCodigo(1);
                }
                con.cerrar(rs);
                con.cerrar(st);
            } catch (SQLException ex) {
                System.out.print(ex);
                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            }

            //guardar tabla envio
            int registro = -1;
            try {
                Statement st = con.conexion();
                String i_traslado = "insert into traslados_almacen values ('" + env.getCodigo() + "', '" + periodo + "', '" + tienda + "', '" + empresa + "' , '" + tienda_destino + "', '" + empresa_destino + "', '" + env.getFecha() + "', '7000-01-01', "
                        + "'" + tido.getId() + "', '" + tido.getSer() + "', '" + tido.getNro() + "', '" + frm_menu.usu.getNick() + "', '0', NOW())";
                registro = con.actualiza(st, i_traslado);
                System.out.println(i_traslado);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }

            if (registro > -1) {
                Notification.show("Envio de Productos", "Registro Exitoso", Notification.CONFIRM_MESSAGE, Notification.NICON_LIGHT_THEME);
                tido.act_numero(tido.getId(), tido.getNro(), tienda, empresa);
                for (int i = 0; i < filas; i++) {
                    mat.setId(Integer.parseInt(t_detalle.getValueAt(i, 0).toString()));
                    mat.setCan(Double.parseDouble(t_detalle.getValueAt(i, 4).toString()));
                    mat.setCosto(Double.parseDouble(t_detalle.getValueAt(i, 5).toString()));

                    //guardar detalle de envio
                    try {
                        Statement st = con.conexion();
                        String ins_detalle = "insert into detalle_traslado values ('" + env.getCodigo() + "', '" + periodo + "', '" + empresa + "', '" + tienda + "', '" + mat.getId() + "', '" + mat.getCan() + "', '0')";
                        con.actualiza(st, ins_detalle);
                        System.out.println(ins_detalle);
                    } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                    }

                    //capturar cantidad actual en la tienda
//                    try {
//                        Statement st = con.conexion();
//                        String bus_pro = "select cantidad_actual from productos_almacenes where id_producto = '" + mat.getId() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
//                        System.out.println(bus_pro);
//                        ResultSet rs = con.consulta(st, bus_pro);
//                        if (rs.next()) {
//                            mat.setCant_act(rs.getDouble("cantidad_actual"));
//                        }
//                        con.cerrar(rs);
//                        con.cerrar(st);
//                    } catch (SQLException ex) {
//                        System.out.print(ex);
//                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
//                    }
                    //ACTUALIZAR CANTIDAD ACTUAL
                    // mat.setCant_act(mat.getCant_act() - mat.getCan());
                    try {
                        Statement st = con.conexion();
                        String act_pro = "update productos_almacenes set cantidad_actual = cantidad_actual - '" + mat.getCan() + "', ultima_salida = '" + env.getFecha() + "' where id_producto = '" + mat.getId() + "' "
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
                        String bus_idk = "select MAX(id_kardex) as id_kardex from kardex where id_producto = '" + mat.getId() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
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
                                + "'" + env.getDenominacion() + "', '" + tido.getId() + "', '" + tido.getSer() + "', '" + tido.getNro() + "', '0.00', '0.00', '" + mat.getCan() + "', '" + mat.getCosto() + "', '11', NOW())";
                        System.out.println(ins_kardex);
                        con.actualiza(st, ins_kardex);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }
                }

                frm_ver_traslados envios = new frm_ver_traslados();
                ven.llamar_ventana(envios);
                this.dispose();
            }
        }
//        }
    }//GEN-LAST:event_btn_registrarActionPerformed

    private void txt_buscar_productoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_productoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            jd_productos.setModal(true);
            jd_productos.setTitle("Listar Productos");
            jd_productos.setSize(1100, 550);
            jd_productos.setLocationRelativeTo(null);
            txt_jd_buscar.setText("");
            txt_jd_buscar.requestFocus();
            Cl_Productos pro = new Cl_Productos();
            String query = "select p.id_producto, p.descripcion, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, p.precio "
                    + "from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = "
                    + "um.id_und_med where pe.id_almacen = '" + tienda + "' and pe.empresa = '" + empresa + "' order by p.descripcion asc limit 0";
            System.out.println(query);
            pro.ver_productos(t_jd_productos, query);
            jd_productos.setVisible(true);
        }

        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            txt_buscar_producto.setText("");
            txt_buscar_producto.requestFocus();
        }
    }//GEN-LAST:event_txt_buscar_productoKeyPressed

    private void cbx_empresaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_empresaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            emp.setRuc(cbx_empresa.getSelectedItem().toString());
            Object datos_tienda[] = frm_menu.alm.obtener_datos();
            txt_partida.setText(datos_tienda[1].toString());
            txt_razon_social.setText(emp.obtener_razon());
            alm.ver_tiendas_empresa(cbx_tiendas, emp.getRuc());
            cbx_tiendas.setEnabled(true);
            cbx_tiendas.requestFocus();
        }
    }//GEN-LAST:event_cbx_empresaKeyPressed

    private void cbx_tiendasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_tiendasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            alm.setCodigo(cbx_tiendas.getSelectedIndex() + 1);
            alm.setEmpresa(cbx_empresa.getSelectedItem().toString());
            if (tienda == alm.getCodigo() && empresa.equals(alm.getEmpresa())) {
                JOptionPane.showMessageDialog(null, "NO SE PUEDE SELECCIONAR ESTA TIENDA");
                cbx_tiendas.requestFocus();
            } else {
                Object datos[] = alm.obtener_datos();
                txt_llegada.setText(datos[1].toString());
                cargar_productos_txt();
                txt_buscar_producto.setEnabled(true);
                txt_buscar_producto.selectAll();
                txt_buscar_producto.requestFocus();
            }
        }
    }//GEN-LAST:event_cbx_tiendasKeyPressed

    private void txt_jd_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_buscarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            jd_productos.dispose();
            txt_buscar_producto.setText("");
            txt_buscar_producto.requestFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String bus = txt_jd_buscar.getText();
            String query = "select p.id_producto, p.descripcion, p.codigo_externo, f.descripcion as nombre_familia, cf.descripcion as nombre_clase, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, p.precio "
                    + "from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on "
                    + "p.id_und_med = um.id_und_med inner join familia_productos as f on p.familia = f.id  inner join clasificacion_familia as cf "
                    + "on cf.id= p.clase_familia and cf.familia=p.familia where pe.id_almacen = '" + tienda + "' and pe.empresa = '" + empresa + "' and (p.codigo_externo like '%" + bus + "%' or p.descripcion like '%" + bus + "%' or "
                    + " p.caracteristicas like '%" + bus + "%') order by p.descripcion asc";
            System.out.println(query);
            mat.ver_productos(t_jd_productos, query);
            t_jd_productos.requestFocus();
        }
    }//GEN-LAST:event_txt_jd_buscarKeyPressed

    private void t_jd_productosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_jd_productosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int contar_filas = t_jd_productos.getRowCount();
            if (contar_filas > 0) {
                int nro_fila = t_jd_productos.getSelectedRow();
                int id_producto = Integer.parseInt(t_jd_productos.getValueAt(nro_fila, 0).toString());
                boolean agregar = valida_tabla(id_producto);

                if (agregar) {

                    String texto = JOptionPane.showInputDialog("Ingrese Cantidad");
                    double cantidad_nueva = 1;
                    if (texto != null) {
                        if (ven.esDecimal(texto)) {
                            cantidad_nueva = Double.parseDouble(texto);
                        } else {
                            cantidad_nueva = 1;
                        }
                    } else {
                        cantidad_nueva = 1;
                    }

                    try {
                        Statement st = con.conexion();
                        String ver_producto = "select p.id_producto, p.descripcion, p.codigo_externo, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, p.precio, f.descripcion as nombre_familia, cf.descripcion as nombre_clase from productos_almacenes as pe "
                                + "inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = um.id_und_med inner join familia_productos as f on p.familia = f.id inner join clasificacion_familia as cf "
                                + "on cf.id= p.clase_familia and cf.familia=p.familia where pe.id_almacen = '" + frm_menu.alm.getCodigo() + "' "
                                + "and empresa = '" + frm_menu.emp.getRuc() + "' and p.id_producto = '" + id_producto + "'";
                        System.out.println(ver_producto);
                        ResultSet rs = con.consulta(st, ver_producto);
                        if (rs.next()) {
                            Object fila[] = new Object[8];
                            fila[0] = id_producto;
                            fila[1] = rs.getString("descripcion") + " - " + rs.getString("codigo_externo") + " - " + rs.getString("nombre_clase") + " - " + rs.getString("nombre_familia");
                            double cantidad = rs.getDouble("cantidad_actual");
                            fila[2] = ven.formato_numero(cantidad);
                            fila[3] = rs.getString("corto");
                            fila[4] = ven.formato_numero(cantidad_nueva);
                            fila[5] = ven.formato_numero(rs.getDouble("precio"));

                            modelo_detalle.addRow(fila);
                            t_detalle.setModel(modelo_detalle);
                            contar_filas();
                        }

                        con.cerrar(rs);
                        con.cerrar(st);

                    } catch (SQLException | HeadlessException e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                    }

                    txt_buscar_producto.setText("");
                    txt_buscar_producto.requestFocus();
                }
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            txt_jd_buscar.setText("");
            txt_jd_buscar.requestFocus();
        }
    }//GEN-LAST:event_t_jd_productosKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jd_productos.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txt_buscar_productoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_buscar_productoFocusGained
        lbl_ayuda.setText("F1 - limpiar busqueda.     Enter: Seleccionar Producto       F2: Cargar Tabla Productos");
    }//GEN-LAST:event_txt_buscar_productoFocusGained

    private void t_detalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_detalleMouseClicked
        if (evt.getClickCount() == 2) {
            if (accion.equals("verificar")) {
                int nro_fila = t_detalle.getSelectedRow();
                String id_producto = t_detalle.getValueAt(nro_fila, 0).toString();
                double cantidad_actual = Double.parseDouble(t_detalle.getValueAt(nro_fila, 4).toString());
                String texto = JOptionPane.showInputDialog("Ingrese Cantidad");
                double cantidad_nueva;
                if (texto != null) {
                    if (ven.esDecimal(texto)) {
                        cantidad_nueva = Double.parseDouble(texto);
                        if (cantidad_actual >= cantidad_nueva) {
                            t_detalle.setValueAt(ven.formato_numero(cantidad_nueva), nro_fila, 6);
                        } else {
                            double exceso = cantidad_nueva - cantidad_actual;
                            cantidad_nueva = cantidad_actual;
                            t_detalle.setValueAt(ven.formato_numero(cantidad_nueva), nro_fila, 6);
                            JOptionPane.showMessageDialog(null, "NO HAY DEMASIADOS PRODUCTOS \n EXCESO DE " + exceso + " UNIDADES");
                        }
                    }
                }
            }
            if (accion.equals("registrar")) {
                fila_seleccionada = t_detalle.getSelectedRow();
            }
        }
    }//GEN-LAST:event_t_detalleMouseClicked

    private void btn_verificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_verificarActionPerformed
        int cantidad_filas = t_detalle.getRowCount();
        int contar_ceros = 0;
        int contar_mayores = 0;
        for (int j = 0; j < cantidad_filas; j++) {
            double cantidad_aceptada = Double.parseDouble(t_detalle.getValueAt(j, 6).toString());
            double cantidad_recibida = Double.parseDouble(t_detalle.getValueAt(j, 4).toString());
            if (cantidad_aceptada == 0.0) {
                contar_ceros++;
            }
            if (cantidad_aceptada > cantidad_recibida) {
                JOptionPane.showMessageDialog(null, "Cantidad Aceptada no puede ser mayor a lo recibido en el Producto " + t_detalle.getValueAt(j, 1).toString());
                contar_mayores++;
            }
        }

        if (contar_ceros <= (cantidad_filas / 2) && contar_mayores == 0) {
            btn_recibir.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "ERROR!!: CANTIDAD CERO EN LA MAYORIA DE PRODUCTOS RECIBIDOS");
        }

    }//GEN-LAST:event_btn_verificarActionPerformed

    private void btn_recibirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_recibirActionPerformed
        String fecha = ven.getFechaActual();
        llenar();
        int filas = t_detalle.getRowCount();

        if (filas > 0) {
            //guardar tabla envio
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

            if (registro > -1) {
                Notification.show("Aceptacion de Mercaderia", "Registro Exitoso", Notification.CONFIRM_MESSAGE, Notification.NICON_LIGHT_THEME);
                for (int i = 0; i < filas; i++) {
                    mat.setId(Integer.parseInt(t_detalle.getValueAt(i, 0).toString()));
                    //mat.setCant_act(Double.parseDouble(t_detalle.getValueAt(i, 4).toString()));
                    mat.setCan(Double.parseDouble(t_detalle.getValueAt(i, 6).toString()));
                    mat.setPrecio(Double.parseDouble(t_detalle.getValueAt(i, 5).toString()));

                    //guardar detalle de envio
                    try {
                        Statement st = con.conexion();
                        String u_detalle = "update detalle_traslado set cantidad_recibida = '" + mat.getCan() + "' where id_traslado = '" + vcodigo + "' and periodo = '" + vperiodo + "' "
                                + "and tienda = '" + vtienda + "' and empresa = '" + vempresa + "' and id_producto = '" + mat.getId() + "'";
                        con.actualiza(st, u_detalle);
                        System.out.println(u_detalle);
                    } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                    }

                    //capturar cantidad actual en la tienda
                    boolean existe_producto = true;
                    try {
                        Statement st = con.conexion();
                        String bus_pro = "select cantidad_actual from productos_almacenes where id_producto = '" + mat.getId() + "' and id_almacen = '" + tienda + "' "
                                + "and empresa = '" + empresa + "'";
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
                    if (existe_producto == true) {
                        try {
                            Statement st = con.conexion();
                            String act_pro = "update productos_almacenes set cantidad_actual = '" + mat.getCant_act() + "', ultimo_ingreso = CURRENT_DATE()"
                                    + " where id_producto = '" + mat.getId() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
                            System.out.println(act_pro);
                            con.actualiza(st, act_pro);
                            con.cerrar(st);
                        } catch (Exception ex) {
                            System.out.print(ex);
                            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                        }
                    } else {
                        try {
                            Statement st = con.conexion();
                            String i_pro = "insert into productos_almacenes values ('" + tienda + "', '" + empresa + "', '" + mat.getId() + "', '-', '3', '10',"
                                    + " '" + mat.getCant_act() + "', CURRENT_DATE(), '2070-01-01')";
                            System.out.println(i_pro);
                            con.actualiza(st, i_pro);
                            con.cerrar(st);
                        } catch (Exception ex) {
                            System.out.print(ex);
                            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                        }
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
                                + "'" + env.getDenominacion() + "', '" + tido.getId() + "', '" + tido.getSer() + "', '" + tido.getNro() + "', '" + mat.getCan() + "', '" + mat.getCosto() + "', '0.00', '0.00', '11', NOW())";
                        System.out.println(ins_kardex);
                        con.actualiza(st, ins_kardex);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }
                }

                frm_ver_traslados envios = new frm_ver_traslados();
                ven.llamar_ventana(envios);
                this.dispose();
            }
        }
    }//GEN-LAST:event_btn_recibirActionPerformed

    private void t_detalleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_detalleKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (fila_seleccionada > -1) {
                if (accion.equals("registrar")) {
                    modelo_detalle.removeRow(fila_seleccionada);
                    txt_buscar_producto.requestFocus();
                    fila_seleccionada = -1;
                } else {
                    JOptionPane.showMessageDialog(null, "NO SE DEBE ELIMINAR");
                }
            } else {
                JOptionPane.showMessageDialog(null, "NO SE HA SELECCIONADO UNA COLUMNA");
            }
        }
    }//GEN-LAST:event_t_detalleKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_producto;
    private javax.swing.JButton btn_add_transportista;
    private javax.swing.JButton btn_cerrar;
    private javax.swing.JButton btn_jd_agrega;
    public static javax.swing.JButton btn_recibir;
    private javax.swing.JButton btn_registrar;
    public static javax.swing.JButton btn_verificar;
    private javax.swing.ButtonGroup buttonGroup1;
    public static javax.swing.JComboBox cbx_empresa;
    public static javax.swing.JComboBox cbx_tido;
    public static javax.swing.JComboBox cbx_tiendas;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JDialog jd_add_producto;
    private javax.swing.JDialog jd_productos;
    private javax.swing.JDialog jd_transportista;
    private javax.swing.JLabel lbl_ayuda;
    private javax.swing.JLabel lbl_contando;
    public static javax.swing.JLabel lbl_empresa;
    public static javax.swing.JLabel lbl_tienda;
    private javax.swing.JRadioButton rbt_no;
    private javax.swing.JRadioButton rbt_si;
    public static javax.swing.JTable t_detalle;
    private javax.swing.JTable t_jd_productos;
    private javax.swing.JTextField txt_brevete;
    public static javax.swing.JTextField txt_buscar_producto;
    private javax.swing.JTextField txt_chofer;
    public static javax.swing.JFormattedTextField txt_fecha;
    private javax.swing.JTextField txt_inscripcion;
    private javax.swing.JTextField txt_jd_actual;
    private javax.swing.JTextField txt_jd_buscar;
    private javax.swing.JTextField txt_jd_compra;
    private javax.swing.JTextField txt_jd_descripcion;
    private javax.swing.JTextField txt_jd_enviada;
    private javax.swing.JTextField txt_jd_id;
    private javax.swing.JTextField txt_jd_medida;
    private javax.swing.JTextField txt_jd_venta;
    public static javax.swing.JTextField txt_llegada;
    private javax.swing.JTextField txt_marca;
    private javax.swing.JTextField txt_modelo;
    public static javax.swing.JTextField txt_numero;
    public static javax.swing.JTextField txt_partida;
    private javax.swing.JTextField txt_placa;
    public static javax.swing.JTextField txt_razon_social;
    private javax.swing.JTextField txt_razon_transporte;
    private javax.swing.JTextField txt_ruc_transporte;
    public static javax.swing.JTextField txt_serie;
    // End of variables declaration//GEN-END:variables
}
