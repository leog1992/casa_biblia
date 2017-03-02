/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Cl_Caja;
import Vistas.frm_ver_mis_productos;
import Clases.Cl_Conectar;
import Clases.Cl_Tipo_Doc;
import Clases.Cl_Varios;
import Clases.Cl_Moneda;
import Clases.Cl_Productos;
import Clases.Cl_Usuario;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import casa_biblia.frm_menu;
import com.mxrck.autocompleter.AutoCompleterCallback;

/**
 *
 * @author pc
 */
public final class frm_reg_venta extends javax.swing.JInternalFrame {

    Cl_Varios ven = new Cl_Varios();
    Cl_Conectar con = new Cl_Conectar();
    Cl_Tipo_Doc tido = new Cl_Tipo_Doc();
    Cl_Productos mat = new Cl_Productos();
    Cl_Moneda mon = new Cl_Moneda();
    Cl_Caja caj = new Cl_Caja();
    Cl_Usuario usu = new Cl_Usuario();
    public static DefaultTableModel modelo_detalle;
    boolean reg_cli;
    Integer i;
    int id_cliente = 2;
    double total_new;
    double deuda_actual;
    double vuelto;
    double tc;
    int tienda = frm_menu.alm.getCodigo();
    String empresa = frm_menu.emp.getRuc();

    /**
     * Creates new form frm_reg_venta
     */
    public frm_reg_venta() {
        initComponents();
        txt_fec.setText(ven.fechaformateada(ven.getFechaActual()));

        tido.ver_tipodoc(cbx_tipd);
        mon.ver_monedas(cbx_jd_moneda);
        usu.ver_usuarios(cbx_usuario);
        int contar_cbx = cbx_usuario.getItemCount();
        int repetido = 0;
        for (int j = 0; j < contar_cbx; j++) {
            if (cbx_usuario.getItemAt(j).toString().equals(frm_menu.usu.getNick())) {
                repetido++;
            }
        }
        if (repetido == 0) {
            cbx_usuario.addItem(frm_menu.usu.getNick());
        }

        int id_cbx_documentos = cbx_tipd.getSelectedIndex() + 1;
        tido.setId(id_cbx_documentos);
        tido.setSer(tido.ver_serie(tienda, empresa));
        tido.setNro(tido.ver_numero(tienda, empresa));
        txt_ser.setText(tido.getSer() + "");
        txt_nro.setText(tido.getNro() + "");

        modelo_detalle();

        cargar_productos_txt();
        txt_buscar_producto.setEnabled(true);
    }

    private void cargar_clientes() {
        try {
            TextAutoCompleter autocompletar = new TextAutoCompleter(txt_nom);
            autocompletar.setMode(0);
            Statement st = con.conexion();
            String sql = "select id_cliente, nro_documento, nombre from clientes order by nombre asc";
            ResultSet rs = con.consulta(st, sql);
            while (rs.next()) {
                autocompletar.addItem(rs.getString("id_cliente") + " - " + rs.getString("nro_documento") + " - " + rs.getString("nombre"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error" + e.getLocalizedMessage());
            System.out.println(e);
        }
    }

    private void modelo_detalle() {
        //formato de tabla detalle de venta
        modelo_detalle = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 5;
            }
        };
        modelo_detalle.addColumn("Id");
        modelo_detalle.addColumn("Tip. Pro");
        modelo_detalle.addColumn("Descripcion");
        modelo_detalle.addColumn("Cant. Act.");
        modelo_detalle.addColumn("Cant.");
        modelo_detalle.addColumn("Und. Med");
        modelo_detalle.addColumn("Precio");
        modelo_detalle.addColumn("Parcial");
        t_detalle.setModel(modelo_detalle);
        t_detalle.getColumnModel().getColumn(0).setPreferredWidth(10);
        t_detalle.getColumnModel().getColumn(1).setPreferredWidth(20);
        t_detalle.getColumnModel().getColumn(2).setPreferredWidth(350);
        t_detalle.getColumnModel().getColumn(3).setPreferredWidth(30);
        t_detalle.getColumnModel().getColumn(4).setPreferredWidth(30);
        t_detalle.getColumnModel().getColumn(5).setPreferredWidth(30);
        t_detalle.getColumnModel().getColumn(6).setPreferredWidth(50);
        t_detalle.getColumnModel().getColumn(7).setPreferredWidth(50);
        ven.centrar_celda(t_detalle, 0);
        ven.centrar_celda(t_detalle, 1);
        ven.derecha_celda(t_detalle, 3);
        ven.derecha_celda(t_detalle, 4);
        ven.centrar_celda(t_detalle, 5);
        ven.derecha_celda(t_detalle, 6);
        ven.derecha_celda(t_detalle, 7);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jd_fin_venta = new javax.swing.JDialog();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txt_jd_documento = new javax.swing.JTextField();
        txt_jd_serie = new javax.swing.JTextField();
        txt_jd_numero = new javax.swing.JTextField();
        lbl_total = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cbx_jd_moneda = new javax.swing.JComboBox();
        btn_jd_cerrar = new javax.swing.JButton();
        btn_jd_registrar = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_jd_efectivo = new javax.swing.JTextField();
        txt_jd_tarjeta = new javax.swing.JTextField();
        txt_jd_vale = new javax.swing.JTextField();
        txt_jd_suma_pago = new javax.swing.JTextField();
        txt_jd_vuelto = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txt_jd_deuda_actual = new javax.swing.JTextField();
        jd_productos = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        txt_jd_buscar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        t_jd_productos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_detalle = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txt_subt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_igv = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lbl_tot = new javax.swing.JLabel();
        btn_reg = new javax.swing.JButton();
        btn_clo = new javax.swing.JButton();
        btn_cam_can = new javax.swing.JButton();
        btn_eli = new javax.swing.JButton();
        txt_buscar_producto = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbx_tipd = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_dni = new javax.swing.JTextField();
        txt_nom = new javax.swing.JTextField();
        txt_dir = new javax.swing.JTextField();
        txt_fec = new javax.swing.JFormattedTextField();
        chk_igv = new javax.swing.JCheckBox();
        txt_ser = new javax.swing.JTextField();
        txt_nro = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_totalme = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbx_usuario = new javax.swing.JComboBox();
        txt_usuario = new javax.swing.JTextField();

        jLabel23.setText("Total");

        jLabel24.setText("Documento:");

        txt_jd_documento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_documento.setFocusable(false);

        txt_jd_serie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_serie.setFocusable(false);

        txt_jd_numero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_numero.setFocusable(false);

        lbl_total.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        lbl_total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_total.setText("S/. 1,888.00");

        jLabel15.setText("Moneda:");

        cbx_jd_moneda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_jd_monedaKeyPressed(evt);
            }
        });

        btn_jd_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_jd_cerrar.setText("Cerrar");
        btn_jd_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_cerrarActionPerformed(evt);
            }
        });

        btn_jd_registrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/accept.png"))); // NOI18N
        btn_jd_registrar.setText("Registrar");
        btn_jd_registrar.setEnabled(false);
        btn_jd_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_registrarActionPerformed(evt);
            }
        });

        jLabel17.setText("Efectivo:");

        jLabel18.setText("Tarjeta:");

        jLabel19.setText("Vale:");

        jLabel20.setText("Suma Pago:");

        jLabel21.setText("Vuelto:");

        txt_jd_efectivo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_efectivo.setEnabled(false);
        txt_jd_efectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jd_efectivoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_jd_efectivoKeyTyped(evt);
            }
        });

        txt_jd_tarjeta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_tarjeta.setEnabled(false);
        txt_jd_tarjeta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jd_tarjetaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_jd_tarjetaKeyTyped(evt);
            }
        });

        txt_jd_vale.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_vale.setText("0.00");
        txt_jd_vale.setEnabled(false);

        txt_jd_suma_pago.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_suma_pago.setText("0.00");
        txt_jd_suma_pago.setFocusable(false);

        txt_jd_vuelto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_vuelto.setText("0.00");
        txt_jd_vuelto.setFocusable(false);

        jLabel22.setText("Deuda Actual:");

        txt_jd_deuda_actual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jd_deuda_actual.setText("0.00");
        txt_jd_deuda_actual.setFocusable(false);

        javax.swing.GroupLayout jd_fin_ventaLayout = new javax.swing.GroupLayout(jd_fin_venta.getContentPane());
        jd_fin_venta.getContentPane().setLayout(jd_fin_ventaLayout);
        jd_fin_ventaLayout.setHorizontalGroup(
            jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                        .addComponent(btn_jd_registrar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_jd_cerrar))
                    .addComponent(lbl_total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(123, 123, 123)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jd_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                        .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(cbx_jd_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19))
                                .addGap(38, 38, 38)
                                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_jd_vale, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_jd_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_jd_efectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(52, 52, 52)
                        .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel20))
                                .addGap(58, 58, 58)
                                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_jd_suma_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_jd_vuelto, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_jd_deuda_actual, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jd_fin_ventaLayout.setVerticalGroup(
            jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_fin_ventaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jd_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_efectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_suma_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_tarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_vuelto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_vale, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(txt_jd_deuda_actual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jd_fin_ventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_jd_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_jd_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel1.setText("Buscar");

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
                        .addComponent(jLabel1)
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
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );

        setTitle("Registro de Venta");

        jScrollPane1.setFocusable(false);

        t_detalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Descripcion", "Cant.", "Und. Med.", "Precio"
            }
        ));
        t_detalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_detalleMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                t_detalleMousePressed(evt);
            }
        });
        t_detalle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_detalleFocusLost(evt);
            }
        });
        t_detalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_detalleKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_detalle);
        if (t_detalle.getColumnModel().getColumnCount() > 0) {
            t_detalle.getColumnModel().getColumn(0).setPreferredWidth(10);
            t_detalle.getColumnModel().getColumn(1).setPreferredWidth(350);
            t_detalle.getColumnModel().getColumn(2).setPreferredWidth(30);
            t_detalle.getColumnModel().getColumn(3).setPreferredWidth(20);
            t_detalle.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        jLabel2.setText("Sub Total:");
        jLabel2.setFocusable(false);

        txt_subt.setEditable(false);
        txt_subt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_subt.setFocusable(false);
        txt_subt.setPreferredSize(new java.awt.Dimension(50, 20));

        jLabel7.setText("IGV");
        jLabel7.setFocusable(false);

        txt_igv.setEditable(false);
        txt_igv.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_igv.setFocusable(false);
        txt_igv.setPreferredSize(new java.awt.Dimension(50, 20));

        jLabel8.setText("Total:");
        jLabel8.setFocusable(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Detalle de Venta:");
        jLabel9.setFocusable(false);

        lbl_tot.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lbl_tot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_tot.setText("S/. 0.00");
        lbl_tot.setFocusable(false);
        lbl_tot.setPreferredSize(new java.awt.Dimension(120, 52));

        btn_reg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/add.png"))); // NOI18N
        btn_reg.setText("Registrar");
        btn_reg.setEnabled(false);
        btn_reg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_reg.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btn_reg.setPreferredSize(new java.awt.Dimension(85, 25));
        btn_reg.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btn_reg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regActionPerformed(evt);
            }
        });

        btn_clo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_clo.setText("Cancelar Venta");
        btn_clo.setFocusable(false);
        btn_clo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btn_clo.setPreferredSize(new java.awt.Dimension(85, 25));
        btn_clo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cloActionPerformed(evt);
            }
        });

        btn_cam_can.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/sockets.png"))); // NOI18N
        btn_cam_can.setText("Cambiar Cantidad");
        btn_cam_can.setEnabled(false);
        btn_cam_can.setFocusable(false);
        btn_cam_can.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cam_canActionPerformed(evt);
            }
        });

        btn_eli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/bin_closed.png"))); // NOI18N
        btn_eli.setText("Eliminar Producto");
        btn_eli.setEnabled(false);
        btn_eli.setFocusable(false);
        btn_eli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliActionPerformed(evt);
            }
        });

        txt_buscar_producto.setEnabled(false);
        txt_buscar_producto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_buscar_productoKeyPressed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setText("Tipo Comprobante");
        jLabel5.setFocusable(false);

        cbx_tipd.setEnabled(false);
        cbx_tipd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_tipdKeyPressed(evt);
            }
        });

        jLabel4.setText("Fecha de Venta:");
        jLabel4.setFocusable(false);

        jLabel12.setText("Nro. Doc:");

        txt_dni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_dni.setEnabled(false);
        txt_dni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_dniKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_dniKeyTyped(evt);
            }
        });

        txt_nom.setEnabled(false);
        txt_nom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_nomKeyPressed(evt);
            }
        });

        txt_dir.setEnabled(false);
        txt_dir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_dirKeyPressed(evt);
            }
        });

        txt_fec.setEditable(false);
        try {
            txt_fec.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_fec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_fec.setFocusable(false);
        txt_fec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_fecKeyPressed(evt);
            }
        });

        chk_igv.setText("+ IGV");
        chk_igv.setEnabled(false);
        chk_igv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_igvActionPerformed(evt);
            }
        });

        txt_ser.setEditable(false);
        txt_ser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_ser.setFocusable(false);

        txt_nro.setEditable(false);
        txt_nro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nro.setFocusable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_tipd, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chk_igv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addComponent(txt_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nro, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(111, 111, 111)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_fec, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_dir, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txt_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_nom)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_nro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbx_tipd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_fec, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chk_igv)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_dir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jLabel6.setText("Total M.E.");

        txt_totalme.setEditable(false);
        txt_totalme.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_totalme.setFocusable(false);

        jLabel3.setText("Vendedor:");

        cbx_usuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_usuarioKeyPressed(evt);
            }
        });

        txt_usuario.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel8))
                                        .addGap(24, 24, 24)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_subt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txt_igv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(btn_clo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl_tot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn_reg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(1, 1, 1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_totalme))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_cam_can)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_eli)
                        .addGap(207, 207, 207))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_buscar_producto)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbx_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_usuario)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbx_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_buscar_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_subt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_igv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_tot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_totalme, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_reg, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_clo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btn_eli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cam_can, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cloActionPerformed
        frm_ver_mis_productos.origen = "";
        this.dispose();
    }//GEN-LAST:event_btn_cloActionPerformed

    private void btn_regActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regActionPerformed
        btn_reg.setEnabled(false);
        jd_fin_venta.setModal(true);
        jd_fin_venta.setTitle("PAGO DE VENTA");
        jd_fin_venta.setSize(530, 380);
        jd_fin_venta.setLocationRelativeTo(null);
        txt_jd_documento.setText(cbx_tipd.getSelectedItem().toString());
        txt_jd_serie.setText(txt_ser.getText());
        txt_jd_numero.setText(txt_nro.getText());
        lbl_total.setText("S/. " + ven.formato_totales(total()));
        cbx_jd_moneda.requestFocus();
        jd_fin_venta.setVisible(true);
    }//GEN-LAST:event_btn_regActionPerformed

    private double monto_vales(String codigo) {
        double monto = 0.0;
        try {
            Statement st = con.conexion();
            String ver_vale = "select valor from cupon_pago where codigo = '" + codigo + "' and estado = '1'";
            ResultSet rs = con.consulta(st, ver_vale);
            while (rs.next()) {
                monto = monto + rs.getDouble("valor");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        return monto;
    }
    private void btn_cam_canActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cam_canActionPerformed
        String texto = JOptionPane.showInputDialog("Ingrese Cantidad");
        if (texto != null) {
            if (ven.esDecimal(texto)) {
                double cantidad = Double.parseDouble(texto);
                t_detalle.setValueAt(ven.formato_numero(cantidad), i, 3);
                calcular_total();
            }
        }

    }//GEN-LAST:event_btn_cam_canActionPerformed

    void calcular_total() {
        txt_subt.setText(ven.formato_numero(subtotal()));
        txt_igv.setText(ven.formato_numero(subtotal() * 0.18));
        lbl_tot.setText("S/. " + ven.formato_numero(subtotal() * 1.18));
        txt_buscar_producto.setText("");
        txt_buscar_producto.requestFocus();
    }

    private void t_detalleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_detalleMousePressed

    }//GEN-LAST:event_t_detalleMousePressed

    private void btn_eliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliActionPerformed
        modelo_detalle.removeRow(i);
        calcular_total();
    }//GEN-LAST:event_btn_eliActionPerformed

    private void t_detalleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_detalleFocusLost
        btn_cam_can.setEnabled(false);
        btn_eli.setEnabled(false);
    }//GEN-LAST:event_t_detalleFocusLost

    private void txt_fecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fecKeyPressed

    }//GEN-LAST:event_txt_fecKeyPressed

    private void txt_buscar_productoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_productoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            txt_buscar_producto.setText("");
            txt_buscar_producto.requestFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            jd_productos.setModal(true);
            jd_productos.setTitle("Listar Productos");
            jd_productos.setSize(1100, 550);
            jd_productos.setLocationRelativeTo(null);
            txt_jd_buscar.setText("");
            txt_jd_buscar.requestFocus();
            Cl_Productos pro = new Cl_Productos();
            String query = "select p.id_producto, p.descripcion, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, pe.precio_venta "
                    + "from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = "
                    + "um.id_und_med where pe.id_almacen = '" + tienda + "' and pe.empresa = '" + empresa + "' order by p.descripcion asc limit 0";
            System.out.println(query);
            pro.ver_productos(t_jd_productos, query);
            jd_productos.setVisible(true);
        }

        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            txt_buscar_producto.requestFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            int nro_filas = t_detalle.getRowCount();
            if (nro_filas > 0) {
                cbx_tipd.setEnabled(true);
                cbx_tipd.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_buscar_productoKeyPressed

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
                                String ver_producto = "select p.id_producto, p.descripcion, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, pe.precio_venta from productos_almacenes as pe "
                                        + "inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = um.id_und_med where pe.id_almacen = '" + frm_menu.alm.getCodigo() + "' "
                                        + "and empresa = '" + frm_menu.emp.getRuc() + "'";
                                System.out.println(ver_producto);
                                ResultSet rs = con.consulta(st, ver_producto);
                                if (rs.next()) {
                                    Object fila[] = new Object[8];
                                    fila[0] = id_producto;
                                    fila[1] = rs.getString("tipo_producto");
                                    fila[2] = rs.getString("descripcion");
                                    double cantidad = rs.getDouble("cantidad_actual");
                                    fila[3] = ven.formato_numero(cantidad);
                                    if (cantidad >= cantidad_nueva) {
                                        fila[4] = ven.formato_numero(cantidad_nueva);
                                    } else {
                                        double exceso = cantidad_nueva - cantidad;
                                        cantidad_nueva = cantidad;
                                        fila[4] = ven.formato_numero(cantidad_nueva);
                                        JOptionPane.showMessageDialog(null, "NO HAY DEMASIADOS PRODUCTOS \n EXCESO DE " + exceso + " UNIDADES");
                                    }
                                    fila[5] = rs.getString("corto");
                                    fila[6] = ven.formato_numero(rs.getDouble("precio_venta"));
                                    fila[7] = ven.formato_numero(rs.getDouble("precio_venta") * cantidad_nueva);

                                    if (cantidad > 0.0) {
                                        valida_tabla(Integer.parseInt(id_producto), fila);
                                        //   calcular_total();
                                    } else {
                                        JOptionPane.showMessageDialog(null, "No existe suficiente cantidad para agregar el producto.");
                                    }
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
            String sql = "select p.id_producto, p.descripcion, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, pe.precio_venta from productos_almacenes as pe "
                    + "inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = um.id_und_med where pe.id_almacen = '" + frm_menu.alm.getCodigo() + "' "
                    + "and empresa = '" + frm_menu.emp.getRuc() + "'";
            ResultSet rs = con.consulta(st, sql);
            while (rs.next()) {
                autocompletar.addItem(rs.getString("p.id_producto") + " - " + rs.getString("p.descripcion") + " -- S/ " + rs.getString("pe.precio_venta") + " -- Cant.: " + rs.getString("pe.cantidad_actual"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getLocalizedMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void valida_tabla(int producto, Object[] objeto) {
        //estado de ingreso
        boolean ingresar = false;
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
            modelo_detalle.addRow(objeto);
            t_detalle.setModel(modelo_detalle);
            calcular_total();
        }
    }

    private void t_detalleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_detalleKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int nro_filas = t_detalle.getRowCount();
            for (int j = 0; j < nro_filas; j++) {
                String tipo = t_detalle.getValueAt(j, 1).toString().toLowerCase();
                Double cantidad_actual = Double.parseDouble(t_detalle.getValueAt(j, 4).toString());
                Double cantidad_vendida = Double.parseDouble(t_detalle.getValueAt(j, 5).toString());
                Double precio_vendido = Double.parseDouble(t_detalle.getValueAt(j, 7).toString());
                if (tipo.equals("bien")) {
                    if (cantidad_vendida <= cantidad_actual) {
                        t_detalle.setValueAt(ven.formato_numero(cantidad_vendida), j, 5);
                    } else {
                        JOptionPane.showMessageDialog(null, "NO SE PUEDE SOBREPASAR CANTIDAD ACTUAL");
                        t_detalle.setValueAt("1.00", j, 5);
                    }
                }
                t_detalle.setValueAt(ven.formato_numero(precio_vendido), j, 7);

            }
            parcial();
            txt_buscar_producto.setText("");
            txt_buscar_producto.requestFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            modelo_detalle.removeRow(i);
            parcial();
            txt_buscar_producto.setText("");
            txt_buscar_producto.requestFocus();
        }
    }//GEN-LAST:event_t_detalleKeyPressed

    private void cbx_tipdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_tipdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            parcial();
            String doc = cbx_tipd.getSelectedItem().toString();
            String nro_documento = txt_dni.getText();
            switch (cbx_tipd.getSelectedIndex()) {
                case 0:
                    JOptionPane.showMessageDialog(null, "No se puede seleccionar");
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "No se puede seleccionar");
                    break;
                case 5:
                    JOptionPane.showMessageDialog(null, "No se puede seleccionar");
                    break;
                case 6:
                    JOptionPane.showMessageDialog(null, "No se puede seleccionar");
                    break;
                case 7:
                    JOptionPane.showMessageDialog(null, "No se puede seleccionar");
                    break;
                case 8:
                    JOptionPane.showMessageDialog(null, "No se puede seleccionar");
                    break;
                default:
                    if (doc.equals("BOLETA")) {
                        txt_dni.setText("00000000");
                        txt_nom.setText("CLIENTES GENERALES");
                        txt_dir.setText("-");
                        btn_reg.setEnabled(true);
                        btn_reg.requestFocus();
                        chk_igv.setEnabled(false);
                    }
                    if (doc.equals("FACTURA")) {
                        chk_igv.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "INGRESE UN NUMERO DE RUC POR FAVOR");
                        txt_dni.setText("");
                        txt_dni.setEnabled(true);
                        txt_dni.requestFocus();
                    }
                    if (doc.equals("NOTA DE VENTA")) {
                        txt_dni.setEnabled(false);
                        txt_nom.setEnabled(false);
                        txt_dni.setText("00000000");
                        txt_nom.setText("CLIENTES GENERALES");
                        txt_dir.setText("-");
                        btn_reg.setEnabled(true);
                        btn_reg.requestFocus();
                        chk_igv.setEnabled(false);
                    }
                    break;
            }
            int id_cbx_documentos = cbx_tipd.getSelectedIndex() + 1;
            tido.setId(id_cbx_documentos);
            tido.setSer(tido.ver_serie(tienda, empresa));
            tido.setNro(tido.ver_numero(tienda, empresa));
            txt_ser.setText(tido.getSer() + "");
            txt_nro.setText(tido.getNro() + "");
        }
    }//GEN-LAST:event_cbx_tipdKeyPressed

    private void txt_dniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dniKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int nro_letras = txt_dni.getText().length();
            String doc = cbx_tipd.getSelectedItem().toString();
            if (doc.equals("BOLETA")) {
                if (nro_letras <= 8 && nro_letras > 0) {
                    cargar_datos_cliente(id_cliente + "");
                }
            }
            if (doc.equals("FACTURA")) {
                if (nro_letras == 11) {
                    String nro_doc = txt_dni.getText();
                    try {
                        Statement st = con.conexion();
                        String ver_tienda = "select nombre, direccion, id_cliente from clientes where nro_documento = '" + nro_doc + "'";
                        ResultSet rs = con.consulta(st, ver_tienda);
                        if (rs.next()) {
                            txt_nom.setText(rs.getString("nombre"));
                            txt_dir.setText(rs.getString("direccion"));
                            id_cliente = rs.getInt("id_cliente");
                        } else {
                            txt_nom.setEnabled(true);
                            reg_cli = true;
                            txt_nom.setText("");
                            txt_dir.setText("");
                            txt_nom.requestFocus();
                        }
                        con.cerrar(rs);
                        con.cerrar(st);
                    } catch (SQLException e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Faltan completar numeros");
                }
            }
        }
    }//GEN-LAST:event_txt_dniKeyPressed

    private void cargar_datos_cliente(String id_cliente) {
        try {
            Statement st = con.conexion();
            String ver_cli = "select direccion from clientes where id_cliente = '" + id_cliente + "'";
            ResultSet rs = con.consulta(st, ver_cli);
            if (rs.next()) {
                txt_dir.setText(rs.getString("direccion"));
                btn_reg.setEnabled(true);
                btn_reg.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "El cliente no existe");
                reg_cli = true;
                txt_nom.setText("");
                txt_nom.setEnabled(true);
                txt_nom.requestFocus();
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException | HeadlessException e) {
            System.out.println(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }
    private void txt_dniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dniKeyTyped
        if (cbx_tipd.getSelectedItem().toString().equals("BOLETA")) {
            if (txt_dni.getText().length() == 8) {
                evt.consume();
            }
        }
        if (cbx_tipd.getSelectedItem().toString().equals("FACTURA")) {
            if (txt_dni.getText().length() == 11) {
                evt.consume();
            }
        }
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9')) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_dniKeyTyped

    private void txt_nomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nomKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_nom.getText().length() > 5) {
                if (cbx_tipd.getSelectedItem().equals("BOLETA")) {
                    String cadena = txt_nom.getText();
                    if (cadena.length() > 0) {
                        boolean existe_guion = false;
                        for (int j = 0; j < cadena.length(); j++) {
                            if (cadena.charAt(j) == '-') {
                                existe_guion = true;
                            }
                        }
                        if (existe_guion == true) {
                            String array_cliente[] = cadena.split("-");
                            String idcliente = array_cliente[0];
                            String nrodoc = array_cliente[1];
                            String nombre = array_cliente[2];
                            txt_nom.setEnabled(false);
                            txt_dni.setText(nrodoc.trim());
                            txt_nom.setText(nombre.trim());
                            id_cliente = Integer.parseInt(idcliente);
                            existe_guion = false;
                        } else {
                            reg_cli = true;
                            txt_dir.setText("");
                            txt_dir.setEnabled(true);
                            txt_dir.requestFocus();
                        }
                    } else {
                        System.out.println("cadena vacia");
                    }
                    btn_reg.setEnabled(true);
                    btn_reg.requestFocus();
                    txt_dir.setText("-");
                }
                if (cbx_tipd.getSelectedItem().equals("FACTURA")) {
                    txt_nom.setText(txt_nom.getText().toUpperCase());
                    txt_dir.setEnabled(true);
                    txt_dir.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txt_nomKeyPressed

    private void txt_dirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dirKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_dir.getText().length() > 3) {
                if (reg_cli == true) {
                    try {
                        Statement st = con.conexion();
                        String ins_cli = "insert into clientes values (null, '" + txt_dni.getText() + "', '" + txt_nom.getText() + "', '" + txt_dir.getText() + "', '')";
                        System.out.println(ins_cli);
                        con.actualiza(st, ins_cli);
                        con.cerrar(st);
                        btn_reg.setEnabled(true);
                        btn_reg.requestFocus();
                        reg_cli = false;
                    } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_txt_dirKeyPressed

    private void chk_igvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_igvActionPerformed
        if (chk_igv.isSelected()) {
            parcial();
            txt_subt.setText(ven.formato_numero(subtotal_igv()));
            txt_igv.setText(ven.formato_numero(igv_igv()));
            lbl_tot.setText("S/. " + ven.formato_numero(total_igv()));
        } else {
            parcial();
            txt_subt.setText(ven.formato_numero(subtotal()));
            txt_igv.setText(ven.formato_numero(igv()));
            lbl_tot.setText("S/. " + ven.formato_numero(total()));
        }
    }//GEN-LAST:event_chk_igvActionPerformed

    private void cbx_jd_monedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_jd_monedaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cbx_jd_moneda.getSelectedIndex() == 0) {
                lbl_total.setText("S/. " + ven.formato_numero(total()));
                total_new = Double.parseDouble(ven.formato_numero(total()));
            } else {
                Double convertido = mon.cambio_venta_dolar(ven.fechabase(txt_fec.getText()), total());
                lbl_total.setText("US$ " + ven.formato_numero(convertido));
                total_new = Double.parseDouble(ven.formato_numero(convertido));
            }
            tc = tc_venta(ven.fechabase(txt_fec.getText()));
            txt_jd_efectivo.setEnabled(true);
            txt_jd_efectivo.requestFocus();
        }
    }//GEN-LAST:event_cbx_jd_monedaKeyPressed

    private Double tc_venta(String fecha) {
        double compra = 0.0;
        try {
            Statement st = con.conexion();
            String ver_tc = "select venta from tipo_cambio where fecha = '" + fecha + "'";
            ResultSet rs = con.consulta(st, ver_tc);
            if (rs.next()) {
                compra = rs.getDouble("venta");
            } else {
                compra = 0.0;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return compra;
    }

    private void btn_jd_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_cerrarActionPerformed
        jd_fin_venta.dispose();
    }//GEN-LAST:event_btn_jd_cerrarActionPerformed

    private void btn_jd_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_registrarActionPerformed
        btn_jd_registrar.setEnabled(false);
        btn_jd_cerrar.setEnabled(false);
        btn_clo.setEnabled(false);
        String fecha = ven.fechabase(txt_fec.getText());
        tido.setId(cbx_tipd.getSelectedIndex() + 1);
        tido.setSer(Integer.parseInt(txt_ser.getText()));
        tido.setNro(Integer.parseInt(txt_nro.getText()));
        String fecha_pago;
        String estado;
        if (deuda_actual > 0) {
            fecha_pago = "7000-01-01";
            estado = "0";
        } else {
            fecha_pago = fecha;
            estado = "1";
        }
        int id_venta = 0;
        int id_moneda = cbx_jd_moneda.getSelectedIndex() + 1;
        String nro_documento = txt_dni.getText();
        String nom_cliente = txt_nom.getText().toUpperCase();
        String anio_periodo = fecha.charAt(0) + "" + fecha.charAt(1) + fecha.charAt(2) + fecha.charAt(3);
        String mes_periodo = fecha.charAt(5) + "" + fecha.charAt(6);
        String periodo = mes_periodo + "-" + anio_periodo;

        //capturar id_venta
        try {
            Statement st = con.conexion();
            String ver_id = "select id_venta from ventas where id_almacen = '" + tienda + "' and periodo = '" + periodo + "' and empresa = '" + empresa + "' order by id_venta desc limit 1";
            System.out.println(ver_id);
            ResultSet rs = con.consulta(st, ver_id);
            if (rs.next()) {
                id_venta = rs.getInt("id_venta") + 1;
            } else {
                id_venta = 1;
            }
            con.cerrar(st);
            con.cerrar(rs);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }

        //registrar venta
        int registro = -1;
        try {
            Statement st = con.conexion();
            String ins_venta = "insert into ventas values ('" + id_venta + "', '" + periodo + "', '" + tienda + "', '" + empresa + "','" + id_cliente + "', '" + tido.getId() + "',"
                    + " '" + tido.getSer() + "', '" + tido.getNro() + "', '" + fecha + "', '" + fecha_pago + "', '" + id_moneda + "', '" + tc + "', '" + total_new + "', "
                    + "'" + estado + "', NOW(), '" + frm_menu.usu.getNick() + "')";
            System.out.println(ins_venta);
            registro = con.actualiza(st, ins_venta);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        if (registro > -1) {

            int nro_filas_detalle = t_detalle.getRowCount();
            if (nro_filas_detalle > 0) {
                for (int j = 0; j < nro_filas_detalle; j++) {
                    String tipo_prod = t_detalle.getValueAt(j, 1).toString();
                    mat.setId(Integer.parseInt(t_detalle.getValueAt(j, 0).toString()));
                    mat.setCan(Double.parseDouble(t_detalle.getValueAt(j, 4).toString()));
                    mat.setPrecio(Double.parseDouble(t_detalle.getValueAt(j, 6).toString()));
                    mat.setTipo(t_detalle.getValueAt(j, 1).toString());

                    //registrar detalle de venta;
                    try {
                        Statement st = con.conexion();
                        String ins_det_venta = "insert into detalle_venta values ('" + id_venta + "', '" + periodo + "', '" + tienda + "', '" + empresa + "','" + mat.getId() + "', '" + mat.getPrecio() + "',"
                                + " '" + mat.getCan() + "')";
                        System.out.println(ins_det_venta);
                        con.actualiza(st, ins_det_venta);
                        con.cerrar(st);
                    } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                    }

                    if (tipo_prod.equals("BIEN")) {
                        //seleccionar cantidad actual
                        try {
                            Statement st = con.conexion();
                            String ver_cant = "select cantidad_actual from productos_almacenes where id_producto = '" + mat.getId() + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
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

                        //restar y modificar cantidad actual
                        mat.setCant_act(mat.getCant_act() - mat.getCan());
                        try {
                            Statement st = con.conexion();
                            String act_cantidad = "update productos_almacenes set cantidad_actual = '" + mat.getCant_act() + "', ultima_salida = '" + fecha + "' where id_producto = '" + mat.getId() + "' "
                                    + "and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
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
                            String ins_kardex = "insert into kardex Values ('" + id_kardex + "', '" + tienda + "', '" + empresa + "','" + mat.getId() + "', '" + fecha + "', '" + nro_documento + "', "
                                    + "'" + nom_cliente + "', '" + tido.getId() + "', '" + tido.getSer() + "', '" + tido.getNro() + "', '0.00', '0.00', '" + mat.getCan() + "', "
                                    + "'" + mat.getPrecio() + "',   '1', NOW())";
                            System.out.println(ins_kardex);
                            con.actualiza(st, ins_kardex);
                            con.cerrar(st);
                        } catch (Exception ex) {
                            System.out.print(ex);
                            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                        }
                    }
                }

                //                // imprimir reporte
                //                Map<String, Object> parametros = new HashMap<>();
                //                parametros.put("id_venta", id_venta);
                //                parametros.put("periodo", periodo);
                //                parametros.put("id_almacen", tienda);
                //                leer_numeros leer = new leer_numeros();
                //                String texto_numero = leer.Convertir(ven.formato_numero(total_new), true) + " SOLES";
                //                parametros.put("monto_letras", texto_numero);
                //                String filename = "rpt_ticket_venta";
                //                ven.imp_reporte(filename, parametros);
                //ven.imp_reporte(filename, parametros);
                //ImprimirClases.Ticket.llenar(id_venta + "", periodo, tienda + "");
                //ImprimirClases.Ticket.generar();
                // ImprimirClases.Imprimir_Ticket.imprimirCocina("TICKET DE PRUEBA");
                //guardar pago de la venta
                double efectivo = Double.parseDouble(txt_jd_efectivo.getText());
                double tarjeta = Double.parseDouble(txt_jd_tarjeta.getText());
                double vale = Double.parseDouble(txt_jd_vale.getText());
                double suma_pagos = efectivo + tarjeta + vale;
                double nuevo_efectivo;
                if (suma_pagos > total_new) {
                    double exceso = suma_pagos - total_new;
                    nuevo_efectivo = efectivo - exceso;
                } else {
                    nuevo_efectivo = efectivo;
                }

                tido.setDesc(cbx_tipd.getSelectedItem().toString());

                int id_pago = 0;
                if (nuevo_efectivo > 0) {
                    try {
                        id_pago++;
                        Statement st = con.conexion();
                        String ins_pago = "insert into pago_ventas value ('" + id_pago + "', '" + id_venta + "', '" + periodo + "', '" + tienda + "', '" + empresa + "','" + fecha + "', '" + id_moneda + "', "
                                + "'" + tc + "', '" + nuevo_efectivo + "', 'EFECTIVO')";
                        System.out.println(ins_pago);
                        con.actualiza(st, ins_pago);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }

                    //REGISTRAR MOVIMIENTO EN CAJA CHICA
                    String concepto = "PAGO DE VENTA - " + tido.getDesc() + "/  " + tido.getSer() + "-" + tido.getNro();
                    int nro_caja = caj.codigo_movimiento(fecha);
                    try {
                        Statement st = con.conexion();
                        String ins_caja = "insert into caja_chica value ('" + nro_caja + "', '" + tienda + "', '" + empresa + "','" + fecha + "', '" + id_moneda + "', '" + concepto + "', 'I', 'V', 'E',"
                                + "'" + nuevo_efectivo + "', '" + frm_menu.usu.getNick() + "', NOW())";
                        System.out.println(ins_caja);
                        con.actualiza(st, ins_caja);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }
                }

                if (tarjeta > 0) {
                    try {
                        id_pago++;
                        Statement st = con.conexion();
                        String ins_pago = "insert into pago_ventas value ('" + id_pago + "', '" + id_venta + "', '" + periodo + "', '" + tienda + "', '" + empresa + "','" + fecha + "', '" + id_moneda + "', "
                                + "'" + tc + "', '" + tarjeta + "', 'TARJETA')";
                        System.out.println(ins_pago);
                        con.actualiza(st, ins_pago);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }

                    //REGISTRAR MOVIMIENTO EN CAJA CHICA
                    String concepto = "PAGO DE VENTA - " + tido.getDesc() + "/  " + tido.getSer() + "-" + tido.getNro();
                    int nro_caja = caj.codigo_movimiento(fecha);
                    try {
                        Statement st = con.conexion();
                        String ins_caja = "insert into caja_chica value ('" + nro_caja + "', '" + tienda + "', '" + empresa + "', '" + fecha + "', '" + id_moneda + "', '" + concepto + "', 'I', 'V', 'T',"
                                + "'" + tarjeta + "', '" + frm_menu.usu.getNick() + "', NOW())";
                        System.out.println(ins_caja);
                        con.actualiza(st, ins_caja);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }
                }

                if (vale > 0) {
                    try {
                        id_pago++;
                        Statement st = con.conexion();
                        String ins_pago = "insert into pago_ventas value ('" + id_pago + "', '" + id_venta + "', '" + periodo + "', '" + tienda + "', '" + empresa + "','" + fecha + "', '" + id_moneda + "', "
                                + "'" + tc + "', '" + vale + "', 'CUPON')";
                        System.out.println(ins_pago);
                        con.actualiza(st, ins_pago);
                        con.cerrar(st);
                    } catch (Exception ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }
                }
                //actualizar documento de la tienda
                tido.act_numero(tido.getId(), tido.getNro(), tienda, empresa);

                jd_fin_venta.dispose();
                this.dispose();
                frm_reg_venta venta = new frm_reg_venta();
                ven.llamar_ventana(venta);
            }
        }
    }//GEN-LAST:event_btn_jd_registrarActionPerformed

    private void txt_jd_efectivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_efectivoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double efectivo = 0;
            if (txt_jd_efectivo.getText().length() > 0) {
                efectivo = Double.parseDouble(txt_jd_efectivo.getText());
            }
            txt_jd_efectivo.setText(ven.formato_numero(efectivo));
            double cupon = Double.parseDouble(txt_jd_vale.getText());
            double tarjeta = 0.0;
            double suma_pago = tarjeta + cupon + efectivo;
            vuelto = suma_pago - total_new;
            deuda_actual = total_new - suma_pago;

            txt_jd_suma_pago.setText(ven.formato_totales(suma_pago));

            if (vuelto > 0) {
                txt_jd_vuelto.setText(ven.formato_totales(vuelto));
            } else {
                txt_jd_vuelto.setText("0.00");
            }

            if (deuda_actual > 0) {
                txt_jd_deuda_actual.setText(ven.formato_totales(deuda_actual));
            } else {
                txt_jd_deuda_actual.setText("0.00");
            }

            txt_jd_tarjeta.setEnabled(true);
            txt_jd_tarjeta.requestFocus();
        }
    }//GEN-LAST:event_txt_jd_efectivoKeyPressed

    private void txt_jd_efectivoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_efectivoKeyTyped
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_jd_efectivoKeyTyped

    private void txt_jd_tarjetaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_tarjetaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double tarjeta = 0;
            if (txt_jd_tarjeta.getText().length() > 0) {
                tarjeta = Double.parseDouble(txt_jd_tarjeta.getText());
            }
            txt_jd_tarjeta.setText(ven.formato_numero(tarjeta));

            //sumar digitos
            double efectivo = Double.parseDouble(txt_jd_efectivo.getText());
            double cupon = Double.parseDouble(txt_jd_vale.getText());

            double suma_pago = tarjeta + cupon + efectivo;
            vuelto = suma_pago - total_new;
            deuda_actual = total_new - suma_pago;

            txt_jd_suma_pago.setText(ven.formato_totales(suma_pago));

            if (vuelto > 0) {
                txt_jd_vuelto.setText(ven.formato_totales(vuelto));
            } else {
                txt_jd_vuelto.setText("0.00");
            }

            if (deuda_actual > 0) {
                txt_jd_deuda_actual.setText(ven.formato_totales(deuda_actual));
            } else {
                txt_jd_deuda_actual.setText("0.00");
            }

            if (suma_pago > 0.0) {
                btn_jd_registrar.setEnabled(true);
                btn_jd_registrar.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_jd_tarjetaKeyPressed

    private void txt_jd_tarjetaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_tarjetaKeyTyped
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_jd_tarjetaKeyTyped

    private void t_detalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_detalleMouseClicked
        if (evt.getClickCount() == 2) {
            int nro_filas = t_detalle.getRowCount();
            if (nro_filas > 0) {
                i = t_detalle.getSelectedRow();
                mat.setId(Integer.parseInt(t_detalle.getValueAt(i, 0).toString()));
                btn_cam_can.setEnabled(true);
                btn_eli.setEnabled(true);
            }
        }
    }//GEN-LAST:event_t_detalleMouseClicked

    private void txt_jd_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_buscarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            jd_productos.dispose();
            txt_buscar_producto.setText("");
            txt_buscar_producto.requestFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String bus = txt_jd_buscar.getText();
            String query = "select p.id_producto, p.descripcion, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, pe.precio_venta "
                    + "from productos_almacenes as pe inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = "
                    + "um.id_und_med where pe.id_almacen = '" + tienda + "' and pe.empresa = '" + empresa + "' and (p.descripcion like '%" + bus + "%' or "
                    + " p.caracteristicas like '%" + bus + "%') order by p.descripcion asc limit 30";
            mat.ver_productos(t_jd_productos, query);
            t_jd_productos.requestFocus();
        }
    }//GEN-LAST:event_txt_jd_buscarKeyPressed

    private void t_jd_productosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_jd_productosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            int contar_filas = t_jd_productos.getRowCount();
            if (contar_filas > 0) {
                int nro_fila = t_jd_productos.getSelectedRow();
                int id_producto = Integer.parseInt(t_jd_productos.getValueAt(nro_fila, 0).toString());
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
                    String ver_producto = "select p.id_producto, p.descripcion, pe.ubicacion, p.tipo_producto, um.corto, pe.cantidad_actual, pe.cantidad_minima, pe.precio_venta from productos_almacenes as pe "
                            + "inner join productos as p on pe.id_producto = p.id_producto inner join unidad_medida as um on p.id_und_med = um.id_und_med where pe.id_almacen = '" + tienda + "' "
                            + "and empresa = '" + empresa + "'";
                    System.out.println(ver_producto);
                    ResultSet rs = con.consulta(st, ver_producto);
                    if (rs.next()) {
                        Object fila[] = new Object[8];
                        fila[0] = id_producto;
                        fila[1] = rs.getString("tipo_producto");
                        fila[2] = rs.getString("descripcion");
                        double cantidad = rs.getDouble("cantidad_actual");
                        fila[3] = ven.formato_numero(cantidad);
                        if (cantidad >= cantidad_nueva) {
                            fila[4] = ven.formato_numero(cantidad_nueva);
                        } else {
                            double exceso = cantidad_nueva - cantidad;
                            cantidad_nueva = cantidad;
                            fila[4] = ven.formato_numero(cantidad_nueva);
                            JOptionPane.showMessageDialog(null, "NO HAY DEMASIADOS PRODUCTOS \n EXCESO DE " + exceso + " UNIDADES");
                        }
                        fila[5] = rs.getString("corto");
                        fila[6] = ven.formato_numero(rs.getDouble("precio_venta"));
                        fila[7] = ven.formato_numero(rs.getDouble("precio_venta") * cantidad_nueva);

                        if (cantidad > 0.0) {
                            valida_tabla(id_producto, fila);
                            //   calcular_total();
                        } else {
                            JOptionPane.showMessageDialog(null, "No existe suficiente cantidad para agregar el producto.");
                        }
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

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            txt_jd_buscar.setText("");
            txt_jd_buscar.requestFocus();
        }
    }//GEN-LAST:event_t_jd_productosKeyPressed

    private void cbx_usuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_usuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            usu.setNick(cbx_usuario.getSelectedItem().toString());
            txt_usuario.setText(usu.nombre_usuario());
            txt_buscar_producto.setEnabled(true);
            txt_buscar_producto.requestFocus();
        }
    }//GEN-LAST:event_cbx_usuarioKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jd_productos.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void parcial() {
        double monto = 0;
        double cantidad;
        double precio;
        int nro_filas = t_detalle.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            cantidad = Double.parseDouble(t_detalle.getValueAt(j, 4).toString());
            precio = Double.parseDouble(t_detalle.getValueAt(j, 6).toString());
            monto = (cantidad * precio);
            t_detalle.setValueAt(ven.formato_totales(monto), j, 7);
        }
        txt_subt.setText(ven.formato_totales(subtotal()));
        txt_igv.setText(ven.formato_totales(igv()));
        lbl_tot.setText("S/. " + ven.formato_totales(total()));
        txt_totalme.setText(ven.formato_numero(mon.cambio_venta_dolar(ven.fechabase(txt_fec.getText()), total())));
    }

    public static double total() {
        double monto = 0;
        double cantidad;
        double precio;
        int nro_filas = t_detalle.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            cantidad = Double.parseDouble(t_detalle.getValueAt(j, 4).toString());
            precio = Double.parseDouble(t_detalle.getValueAt(j, 6).toString());
            monto += cantidad * precio;
        }
        return monto;
    }

    public static double total_igv() {
        double monto = 0;
        double cantidad;
        double precio;
        int nro_filas = t_detalle.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            cantidad = Double.parseDouble(t_detalle.getValueAt(j, 4).toString());
            precio = Double.parseDouble(t_detalle.getValueAt(j, 6).toString());
            monto += cantidad * precio * 1.18;
        }
        return monto;
    }

    public static double igv() {
        double monto = 0;
        double cantidad;
        double precio;
        int nro_filas = t_detalle.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            cantidad = Double.parseDouble(t_detalle.getValueAt(j, 4).toString());
            precio = Double.parseDouble(t_detalle.getValueAt(j, 6).toString());
            monto += (cantidad * precio) / 6.5555;
        }
        return monto;
    }

    public static double igv_igv() {
        double monto = 0;
        double cantidad;
        double precio;
        int nro_filas = t_detalle.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            cantidad = Double.parseDouble(t_detalle.getValueAt(j, 4).toString());
            precio = Double.parseDouble(t_detalle.getValueAt(j, 6).toString());
            monto += (cantidad * precio) * 0.18;
        }
        return monto;
    }

    public static double subtotal() {
        double monto = 0;
        double cantidad;
        double precio;
        int nro_filas = t_detalle.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            cantidad = Double.parseDouble(t_detalle.getValueAt(j, 4).toString());
            precio = Double.parseDouble(t_detalle.getValueAt(j, 6).toString());
            if (chk_igv.isSelected()) {
                monto += (cantidad * precio);
            } else {
                monto += (cantidad * precio) / 1.18;
            }
        }
        return monto;
    }

    public static double subtotal_igv() {
        double monto = 0;
        double cantidad;
        double precio;
        int nro_filas = t_detalle.getRowCount();
        for (int j = 0; j < nro_filas; j++) {
            cantidad = Double.parseDouble(t_detalle.getValueAt(j, 4).toString());
            precio = Double.parseDouble(t_detalle.getValueAt(j, 6).toString());

        }
        return monto;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cam_can;
    public static javax.swing.JButton btn_clo;
    private javax.swing.JButton btn_eli;
    private javax.swing.JButton btn_jd_cerrar;
    private javax.swing.JButton btn_jd_registrar;
    public static javax.swing.JButton btn_reg;
    private javax.swing.JComboBox cbx_jd_moneda;
    public static javax.swing.JComboBox cbx_tipd;
    private javax.swing.JComboBox cbx_usuario;
    public static javax.swing.JCheckBox chk_igv;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JDialog jd_fin_venta;
    private javax.swing.JDialog jd_productos;
    public static javax.swing.JLabel lbl_tot;
    public static javax.swing.JLabel lbl_total;
    public static javax.swing.JTable t_detalle;
    private javax.swing.JTable t_jd_productos;
    private javax.swing.JTextField txt_buscar_producto;
    public static javax.swing.JTextField txt_dir;
    public static javax.swing.JTextField txt_dni;
    public static javax.swing.JFormattedTextField txt_fec;
    public static javax.swing.JTextField txt_igv;
    private javax.swing.JTextField txt_jd_buscar;
    private javax.swing.JTextField txt_jd_deuda_actual;
    private javax.swing.JTextField txt_jd_documento;
    private javax.swing.JTextField txt_jd_efectivo;
    private javax.swing.JTextField txt_jd_numero;
    private javax.swing.JTextField txt_jd_serie;
    private javax.swing.JTextField txt_jd_suma_pago;
    private javax.swing.JTextField txt_jd_tarjeta;
    private javax.swing.JTextField txt_jd_vale;
    private javax.swing.JTextField txt_jd_vuelto;
    public static javax.swing.JTextField txt_nom;
    public static javax.swing.JTextField txt_nro;
    public static javax.swing.JTextField txt_ser;
    public static javax.swing.JTextField txt_subt;
    public static javax.swing.JTextField txt_totalme;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}
