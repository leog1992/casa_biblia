/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Cl_Conectar;
import Clases.Cl_Envio;
import Clases.Cl_Productos;
import Clases.Cl_Proveedor;
import Clases.Cl_Tipo_Doc;
import Clases.Cl_Varios;
import Vistas.frm_ver_envios;
import Vistas.frm_ver_mis_productos;
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
public final class frm_reg_envio extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    Cl_Proveedor pro = new Cl_Proveedor();
    Cl_Envio env = new Cl_Envio();
    Cl_Tipo_Doc tido = new Cl_Tipo_Doc();
    Cl_Productos mat = new Cl_Productos();
    DefaultTableModel detalle = null;
    int tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();
    int i;

    /**
     * Creates new form frm_reg_envio
     */
    public frm_reg_envio() {
        initComponents();
        txt_fecha.setText(ven.fechaformateada(ven.getFechaActual()));
        String ver_tido = "select nombre from tipo_documento order by id_tido";
        ver_tipodoc(ver_tido);
        String ver_movimiento = "select nombre from tipo_movimiento order by id_timo";
        ver_movimiento(ver_movimiento);

        cargar_proveedores();
        txt_partida.setText(frm_menu.alm.getDireccion());

        Object datos_tienda[] = frm_menu.alm.obtener_datos();
        txt_partida.setText(datos_tienda[1].toString());
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

    private void ver_movimiento(String query) {
        try {
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);

            while (rs.next()) {
                String fila;
                fila = rs.getString("nombre");
                cbx_movimiento.addItem(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
        } catch (SQLException e) {
            System.out.print(e);
        }
    }

    public void ver_detalle_ingreso() {
        try {
            detalle = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return columna == 4;
                }
            };
            //Establecer como cabezeras el nombre de las colimnas
            detalle.addColumn("Cod.");
            detalle.addColumn("Descripcion");
            detalle.addColumn("Cant. Act.");
            detalle.addColumn("Und. Med");
            detalle.addColumn("Cant. Sal.");
            detalle.addColumn("Precio");
            t_detalle_salida.setModel(detalle);
            t_detalle_salida.getColumnModel().getColumn(0).setPreferredWidth(50);
            t_detalle_salida.getColumnModel().getColumn(1).setPreferredWidth(500);
            t_detalle_salida.getColumnModel().getColumn(2).setPreferredWidth(70);
            t_detalle_salida.getColumnModel().getColumn(3).setPreferredWidth(70);
            t_detalle_salida.getColumnModel().getColumn(4).setPreferredWidth(70);
            t_detalle_salida.getColumnModel().getColumn(5).setPreferredWidth(70);
            detalle.fireTableDataChanged();
            ven.centrar_celda(t_detalle_salida, 0);
            ven.derecha_celda(t_detalle_salida, 2);
            ven.centrar_celda(t_detalle_salida, 3);
            ven.derecha_celda(t_detalle_salida, 4);
            ven.derecha_celda(t_detalle_salida, 5);
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
                                    String ver_producto = "select p.id_producto, p.codigo_externo, p.caracteristicas, p.descripcion, p.tipo_producto, f.descripcion as nombre_familia, cf.descripcion as nombre_clase, p.costo, p.precio, um.corto, p.cantidad_caja, "
                                            + " pa.cantidad_actual from productos_almacenes as pa inner join productos as p on pa.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = um.id_und_med inner join familia_productos as f on p.familia = f.id  inner join clasificacion_familia as cf "
                                            + "on cf.id= p.clase_familia and cf.familia=p.familia where p.id_producto = '" + id_producto + "' and pa.id_almacen = '" + tienda + "' and pa.empresa = '" + empresa + "'";
                                    System.out.println(ver_producto);
                                    ResultSet rs = con.consulta(st, ver_producto);
                                    if (rs.next()) {
                                        txt_jd_descripcion.setText(rs.getString("descripcion") + " - " + rs.getString("nombre_clase") + " - " + rs.getString("nombre_familia") + " CE: " + rs.getString("codigo_externo"));
                                        txt_jd_id.setText(rs.getString("id_producto"));
                                        //txt_j_caracteristicas.setText(rs.getString("caracteristicas"));
                                        //txt_j_caja.setText(ven.formato_numero(rs.getDouble("cantidad_caja")));
                                        txt_jd_precio.setText(ven.formato_numero(rs.getDouble("precio")));
                                        txt_jd_compra.setText(ven.formato_numero(rs.getDouble("costo")));
                                        txt_jd_actual.setText(ven.formato_numero(rs.getDouble("cantidad_actual")));
                                        txt_jd_medida.setText(rs.getString("corto"));
                                        txt_jd_enviada.setEnabled(true);
                                        txt_jd_enviada.setText("");
                                        txt_jd_enviada.requestFocus();
                                    }

                                    con.cerrar(rs);
                                    con.cerrar(st);

                                } catch (SQLException | HeadlessException e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                                }

                                jd_add_producto.setModal(true);
                                //limpiar_jdialog();
                                jd_add_producto.setSize(730, 200);
                                jd_add_producto.setLocationRelativeTo(null);
                                jd_add_producto.setVisible(true);

                                txt_buscar_productos.setText("");
                                txt_buscar_productos.requestFocus();
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
            String sql = "select p.id_producto, p.codigo_externo, p.descripcion, p.tipo_producto, p.costo, p.precio, um.corto, pa.cantidad_actual "
                    + "from productos_almacenes as pa inner join productos as p on pa.id_producto = p.id_producto inner join unidad_medida as um on "
                    + "p.id_und_med = um.id_und_med where pa.id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            ResultSet rs = con.consulta(st, sql);
            while (rs.next()) {
                autocompletar.addItem(rs.getString("p.id_producto") + " - " + rs.getString("p.descripcion") + " " + rs.getString("p.codigo_externo") + "  - Cant. " + rs.getString("cantidad_actual") + " - S/: " + rs.getString("precio"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getLocalizedMessage());
            System.out.println(e.getLocalizedMessage());
        }
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

    private boolean valida_tabla(int producto) {
        //estado de ingreso
        boolean ingresar = false;
        boolean validado = false;
        int cuenta_iguales = 0;

        //verificar fila no se repite
        int contar_filas = t_detalle_salida.getRowCount();
        if (contar_filas == 0) {
            ingresar = true;
        }

        if (contar_filas > 0) {
            for (int j = 0; j < contar_filas; j++) {
                int id_producto_fila = Integer.parseInt(t_detalle_salida.getValueAt(j, 0).toString());
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
        int contar_filas = t_detalle_salida.getRowCount();
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
        jLabel24 = new javax.swing.JLabel();
        txt_jd_precio = new javax.swing.JTextField();
        jd_productos = new javax.swing.JDialog();
        jLabel25 = new javax.swing.JLabel();
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
        jLabel5 = new javax.swing.JLabel();
        txt_ruc = new javax.swing.JTextField();
        txt_razon_social = new javax.swing.JTextField();
        btn_cerrar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbx_motivo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        cbx_movimiento = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_partida = new javax.swing.JTextField();
        txt_llegada = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_detalle_salida = new javax.swing.JTable();
        btn_registrar = new javax.swing.JButton();
        btn_add_transportista = new javax.swing.JButton();
        btn_add_producto = new javax.swing.JButton();
        txt_buscar_productos = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        lbl_contando = new javax.swing.JLabel();

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

        jLabel24.setText("Precio de Venta:");

        txt_jd_precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_precio.setFocusable(false);

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
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_jd_enviada, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jd_compra)
                            .addComponent(txt_jd_precio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_jd_agrega)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jd_add_productoLayout.setVerticalGroup(
            jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_add_productoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jd_add_productoLayout.createSequentialGroup()
                        .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txt_jd_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jd_id, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(73, 73, 73))
                    .addGroup(jd_add_productoLayout.createSequentialGroup()
                        .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jd_actual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jd_enviada, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txt_jd_compra, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jd_medida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_add_productoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_jd_agrega, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel25.setText("Buscar");

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
                        .addComponent(jLabel25)
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
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );

        setTitle("Registro de Salida de Tienda");

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

        txt_serie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_serie.setEnabled(false);
        txt_serie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_serieKeyPressed(evt);
            }
        });

        txt_numero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_numero.setEnabled(false);
        txt_numero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_numeroKeyPressed(evt);
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

        jLabel5.setText("DNI / RUC");

        txt_ruc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_ruc.setEnabled(false);
        txt_ruc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_rucKeyPressed(evt);
            }
        });

        txt_razon_social.setFocusable(false);

        btn_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_cerrar.setText("Cerrar");
        btn_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerrarActionPerformed(evt);
            }
        });

        jLabel6.setText("Motivo:");

        cbx_motivo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "VENTA", "COMPRA", "DEVOLUCION", "CONSIGNACION", "IMPORTACION", "EXPORTACION", "VENTA SUJETA A CONFIRMACION", "TRASLADO ENTRE ALMACENES", "PARA TRASNFORMACION", "VENTA CON ENTREGA A TERCEROS", "OTRAS" }));
        cbx_motivo.setEnabled(false);
        cbx_motivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_motivoKeyPressed(evt);
            }
        });

        jLabel7.setText("Tipo de Movimiento:");

        cbx_movimiento.setEnabled(false);
        cbx_movimiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_movimientoKeyPressed(evt);
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
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(89, 89, 89)
                        .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                        .addComponent(btn_cerrar))
                    .addComponent(txt_razon_social)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbx_motivo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbx_movimiento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_tido, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_razon_social, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_motivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_movimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Direcciones"));

        jLabel8.setText("Punto de Partida:");

        jLabel9.setText("Punto de Llegada:");

        txt_partida.setEnabled(false);

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
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle de Envio"));

        t_detalle_salida.setModel(new javax.swing.table.DefaultTableModel(
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
        t_detalle_salida.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_detalle_salida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_detalle_salidaMouseClicked(evt);
            }
        });
        t_detalle_salida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_detalle_salidaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_detalle_salida);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
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

        txt_buscar_productos.setText("BUSCAR PRODUCTOS");
        txt_buscar_productos.setEnabled(false);
        txt_buscar_productos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_buscar_productosKeyPressed(evt);
            }
        });

        jLabel19.setText("Cantidad de Filas:");

        lbl_contando.setText("0");

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
                        .addComponent(btn_registrar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_add_transportista)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_buscar_productos)
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
                    .addComponent(txt_buscar_productos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_add_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_add_transportista, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(lbl_contando))
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
        frm_ver_envios envios = new frm_ver_envios();
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
            if (txt_fecha.getText().length() > 0) {
                txt_ruc.setEnabled(true);
                txt_ruc.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_fechaKeyPressed

    private void txt_rucKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rucKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_ruc.getText().length() == 11) {
                pro.setRuc(txt_ruc.getText());
                //validar ruc 
                boolean ruc_valido = pro.validar_RUC(pro.getRuc());
                if (ruc_valido == true) {
                    //BUSCA EN PROVEEDORES
                    try {
                        Statement st = con.conexion();
                        String buscar = "select razon_social, direccion from proveedores where ruc_proveedor = '" + pro.getRuc() + "'";
                        ResultSet rs = con.consulta(st, buscar);
                        if (rs.next()) {
                            txt_razon_social.setText(rs.getString("razon_social"));
                            txt_llegada.setText(rs.getString("direccion"));
                            cbx_motivo.setEnabled(true);
                            cbx_motivo.requestFocus();
                        } else {
                            Notification.show("Registro de Proveedor", "El proveedor no existe, registrar por favor", Notification.NICON_LIGHT_THEME);
                            frm_reg_proveedor proveedor = new frm_reg_proveedor();
                            frm_reg_proveedor.origen = "envio";
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
                }
            } else {
                Notification.show("Registro de Proveedor", "FALTAN DIGITOS AL NUMERO DE RUC", Notification.NICON_LIGHT_THEME);
            }
        }
    }//GEN-LAST:event_txt_rucKeyPressed

    private void cbx_motivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_motivoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbx_movimiento.setEnabled(true);
            cbx_movimiento.requestFocus();
        }
    }//GEN-LAST:event_cbx_motivoKeyPressed

    private void cbx_movimientoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_movimientoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn_add_producto.setEnabled(true);
            cargar_productos_txt();
            txt_buscar_productos.setEnabled(true);
            txt_buscar_productos.setText("");
            txt_buscar_productos.requestFocus();
        }
    }//GEN-LAST:event_cbx_movimientoKeyPressed

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
        fila[2] = ven.formato_numero(Double.parseDouble(txt_jd_actual.getText()));
        fila[3] = txt_jd_medida.getText();
        fila[4] = ven.formato_numero(Double.parseDouble(txt_jd_enviada.getText()));
        fila[5] = ven.formato_numero(Double.parseDouble(txt_jd_precio.getText()));
        detalle.addRow(fila);
        t_detalle_salida.setModel(detalle);
        txt_buscar_productos.setText("");
        txt_buscar_productos.requestFocus();
        jd_add_producto.dispose();
        contar_filas();
        btn_registrar.setEnabled(true);
    }//GEN-LAST:event_btn_jd_agregaActionPerformed

    private void llenar() {
        Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        env.setAnio(anio);
        env.setNro_documento(txt_ruc.getText());
        env.setDenominacion(txt_razon_social.getText());
        env.setDireccion(txt_llegada.getText());
        env.setFecha(ven.fechabase(txt_fecha.getText()));
        env.setMotivo(cbx_motivo.getSelectedItem().toString());
        env.setTimo(cbx_movimiento.getSelectedIndex() + 1);
        tido.setId(cbx_tido.getSelectedIndex() + 1);
        tido.setSer(Integer.parseInt(txt_serie.getText()));
        tido.setNro(Integer.parseInt(txt_numero.getText()));
    }

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
        btn_registrar.setEnabled(false);
        llenar();
        int filas = t_detalle_salida.getRowCount();

        if (filas > 0) {
            //capturar id de envio
            try {
                Statement st = con.conexion();
                String ver_id = "select id_envio from envios where anio = '" + env.getAnio() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "' order by id_envio desc limit 1";
                System.out.println(ver_id);
                ResultSet rs = con.consulta(st, ver_id);
                if (rs.next()) {
                    env.setCodigo(rs.getInt("id_envio") + 1);
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
                String ins_envio = "insert into envios values ('" + env.getCodigo() + "', '" + env.getAnio() + "', '" + tienda + "', '" + empresa + "' ,'" + env.getFecha() + "', '" + tido.getId() + "', '" + tido.getSer() + "', "
                        + "'" + tido.getNro() + "', '" + env.getNro_documento() + "', '" + env.getDenominacion() + "', '" + env.getDireccion() + "', '" + env.getTimo() + "', '" + env.getMotivo() + "', "
                        + "'" + frm_menu.usu.getNick() + "', '1')";
                registro = con.actualiza(st, ins_envio);
                System.out.println(ins_envio);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }

            if (registro > -1) {
                Notification.show("Envio de Productos", "Registro Exitoso", Notification.CONFIRM_MESSAGE, Notification.NICON_LIGHT_THEME);
                tido.act_numero(tido.getId(), tido.getNro(), tienda, empresa);
                for (int i = 0; i < filas; i++) {
                    mat.setId(Integer.parseInt(t_detalle_salida.getValueAt(i, 0).toString()));
                    mat.setCan(Double.parseDouble(t_detalle_salida.getValueAt(i, 4).toString()));
                    mat.setCosto(Double.parseDouble(t_detalle_salida.getValueAt(i, 5).toString()));

                    //guardar detalle de envio
                    try {
                        Statement st = con.conexion();
                        String ins_detalle = "insert into detalle_envio values ('" + env.getCodigo() + "', '" + env.getAnio() + "', '" + tienda + "', '" + empresa + "', '" + mat.getId() + "', '" + mat.getCan() + "')";
                        con.actualiza(st, ins_detalle);
                        System.out.println(ins_detalle);
                    } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
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
                    mat.setCant_act(mat.getCant_act() - mat.getCan());
                    try {
                        Statement st = con.conexion();
                        String act_pro = "update productos_almacenes set cantidad_actual = '" + mat.getCant_act() + "', ultima_salida = '" + env.getFecha() + "' where id_producto = '" + mat.getId() + "' "
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
                                + "'" + env.getDenominacion() + "', '" + tido.getId() + "', '" + tido.getSer() + "', '" + tido.getNro() + "', '0.00', '0.00', '" + mat.getCan() + "', '" + mat.getCosto() + "', '" + env.getTimo() + "', NOW())";
                        System.out.println(ins_kardex);
                        con.actualiza(st, ins_kardex);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }
                }

                frm_ver_envios envios = new frm_ver_envios();
                ven.llamar_ventana(envios);
                this.dispose();
            }

        }
    }//GEN-LAST:event_btn_registrarActionPerformed

    private void txt_buscar_productosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_productosKeyPressed
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
            txt_buscar_productos.setText("");
            txt_buscar_productos.requestFocus();
        }
    }//GEN-LAST:event_txt_buscar_productosKeyPressed

    private void t_detalle_salidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_detalle_salidaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (i > -1) {
                detalle.removeRow(i);
                txt_buscar_productos.requestFocus();
                i = -1;
            } else {
                JOptionPane.showMessageDialog(null, "NO SE HA SELECCIONADO UNA COLUMNA");
            }
        }
    }//GEN-LAST:event_t_detalle_salidaKeyPressed

    private void t_detalle_salidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_detalle_salidaMouseClicked
        i = t_detalle_salida.getSelectedRow();
    }//GEN-LAST:event_t_detalle_salidaMouseClicked

    private void txt_jd_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_buscarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            jd_productos.dispose();
            txt_buscar_productos.setText("");
            txt_buscar_productos.requestFocus();
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

                            detalle.addRow(fila);
                            t_detalle_salida.setModel(detalle);
                            contar_filas();
                        }

                        con.cerrar(rs);
                        con.cerrar(st);

                    } catch (SQLException | HeadlessException e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                    }

                    txt_buscar_productos.setText("");
                    txt_buscar_productos.requestFocus();
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_producto;
    private javax.swing.JButton btn_add_transportista;
    private javax.swing.JButton btn_cerrar;
    private javax.swing.JButton btn_jd_agrega;
    private javax.swing.JButton btn_registrar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbx_motivo;
    private javax.swing.JComboBox cbx_movimiento;
    private javax.swing.JComboBox cbx_tido;
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
    private javax.swing.JLabel jLabel25;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JDialog jd_add_producto;
    private javax.swing.JDialog jd_productos;
    private javax.swing.JDialog jd_transportista;
    private javax.swing.JLabel lbl_contando;
    private javax.swing.JRadioButton rbt_no;
    private javax.swing.JRadioButton rbt_si;
    private javax.swing.JTable t_detalle_salida;
    private javax.swing.JTable t_jd_productos;
    private javax.swing.JTextField txt_brevete;
    public static javax.swing.JTextField txt_buscar_productos;
    private javax.swing.JTextField txt_chofer;
    private javax.swing.JFormattedTextField txt_fecha;
    private javax.swing.JTextField txt_inscripcion;
    private javax.swing.JTextField txt_jd_actual;
    private javax.swing.JTextField txt_jd_buscar;
    private javax.swing.JTextField txt_jd_compra;
    private javax.swing.JTextField txt_jd_descripcion;
    private javax.swing.JTextField txt_jd_enviada;
    private javax.swing.JTextField txt_jd_id;
    private javax.swing.JTextField txt_jd_medida;
    private javax.swing.JTextField txt_jd_precio;
    private javax.swing.JTextField txt_llegada;
    private javax.swing.JTextField txt_marca;
    private javax.swing.JTextField txt_modelo;
    private javax.swing.JTextField txt_numero;
    private javax.swing.JTextField txt_partida;
    private javax.swing.JTextField txt_placa;
    private javax.swing.JTextField txt_razon_social;
    private javax.swing.JTextField txt_razon_transporte;
    private javax.swing.JTextField txt_ruc;
    private javax.swing.JTextField txt_ruc_transporte;
    private javax.swing.JTextField txt_serie;
    // End of variables declaration//GEN-END:variables
}
