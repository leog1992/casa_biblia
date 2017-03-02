/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package casa_biblia;

import Clases.Cl_Almacen;
import Clases.Cl_Conectar;
import Clases.Cl_Empresa;
import Clases.Cl_Movimiento;
import Clases.Cl_Notificaciones;
import Clases.Cl_Usuario;
import Clases.Cl_Varios;
import Forms.frm_reg_cobro_pedido;
import Forms.frm_reg_pedido_cliente;
import Forms.frm_reg_productos;
import Vistas.frm_ver_compras;
import Vistas.frm_ver_empresas;
import Vistas.frm_ver_envios;
import Vistas.frm_ver_ingreso;
import Vistas.frm_ver_mis_productos;
import Vistas.frm_ver_movimientos;
import Vistas.frm_ver_pedidos_cliente;
import Vistas.frm_ver_tiendas;
import Vistas.frm_ver_tiendas_productos;
import Vistas.frm_ver_todo_productos;
import Vistas.frm_ver_traslados;
import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.jvnet.substance.SubstanceLookAndFeel;

/**
 *
 * @author pc
 */
public class frm_menu extends javax.swing.JFrame {
    
    Cl_Varios ven = new Cl_Varios();
    Cl_Conectar con = new Cl_Conectar();
    public static Cl_Usuario usu = new Cl_Usuario();
    public static Cl_Almacen alm = new Cl_Almacen();
    public static Cl_Empresa emp = new Cl_Empresa();
    Cl_Movimiento mov = new Cl_Movimiento();
    Cl_Notificaciones noti;
    String tipo_reporte;
    
    double monto_apertura = 0;
    double venta_efectivo = 0;
    double venta_tarjeta = 0;
    double otros_ingresos = 0;
    double cobros = 0;
    double venta_vale = 0;
    double vales_emitidos = 0;
    double otros_gastos = 0;
    
    double total_efectivo = 0;
    double total_tarjeta = 0;
    
    String fecha_cierre = ven.getFechaActual();
    
    boolean caja_cerrada = false;
    
    double fmonto = 0;

    /**
     * Creates new form frm_menu
     */
    public frm_menu() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        JFrame.setDefaultLookAndFeelDecorated(true);
        initComponents();
        SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.OfficeSilver2007Skin");
        //SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceSteelBlueTheme");

        emp.ver_empresas(cbx_jd_empresas);
        
        emp.setRuc(ven.leer_archivo("empresa.txt"));
        emp.setRazon_social(emp.obtener_razon());
        lbl_ruc.setText(emp.getRuc());
        lbl_razon_social.setText(emp.getRazon_social());
        
        alm.setCodigo(Integer.parseInt(ven.leer_archivo("tienda.txt")));
        alm.setEmpresa(emp.getRuc());
        Object datos_tienda[] = alm.obtener_datos();
        alm.setNombre(datos_tienda[0].toString().trim());
        lbl_tienda.setText(datos_tienda[0].toString());
        
        if (alm.getCodigo() == 1 && emp.getRuc().equals("10180909350")) {
            //aperturar_caja();
        } else {
            aperturar_caja();
        }
        // carga todas las notificacion primera vez
        noti = new Cl_Notificaciones();
        noti.start();
        
    }
    
    private void aperturar_caja() {
        boolean existe_caja = false;
        double efectivo = 0;
        try {
            Statement st = con.conexion();
            String c_cierre = "select monto_apertura, sistema_efectivo from cierre_caja where fecha = CURRENT_DATE() and id_almacen = '" + alm.getCodigo() + "' and empresa = '" + emp.getRuc() + "'";
            ResultSet rs = con.consulta(st, c_cierre);
            if (rs.next()) {
                efectivo = rs.getDouble("sistema_efectivo");
                existe_caja = true;
            } else {
                existe_caja = false;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        if (existe_caja == false) {
            jd_apertura_caja.setModal(true);
            jd_apertura_caja.setSize(291, 180);
            jd_apertura_caja.setLocationRelativeTo(null);
            jd_apertura_caja.setResizable(false);
            txt_fecha_apertura.setText(ven.fechaformateada(ven.getFechaActual()));
            txt_tienda_nombre.setText(alm.getNombre());
            jd_apertura_caja.setVisible(true);
        }
        
        if (efectivo > 0) {
            JOptionPane.showMessageDialog(null, "LA CAJA YA HA SIDO CERRADA, POR FAVOR INTENTE MAÑANA");
            System.exit(0);
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

        jd_cierre_caja = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        txt_jc_fecha = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txt_jc_saldo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_jc_vefectivo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_jc_vtarjeta = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_jc_vvale = new javax.swing.JTextField();
        txt_jc_cobros = new javax.swing.JTextField();
        txt_jc_ingresos = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txt_jc_svales = new javax.swing.JTextField();
        txt_jc_sgastos = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txt_jc_tefectivo = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_jc_ttarjeta = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txt_jc_aefectivo = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txt_jc_atarjeta = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txt_jc_total = new javax.swing.JTextField();
        btn_jc_grabar = new javax.swing.JButton();
        btn_jc_cerrar = new javax.swing.JButton();
        chb_cerrar = new javax.swing.JCheckBox();
        jd_empresas = new javax.swing.JDialog();
        jLabel20 = new javax.swing.JLabel();
        cbx_jd_empresas = new javax.swing.JComboBox();
        txt_jd_razon_social = new javax.swing.JTextField();
        btn_jd_aceptar = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        txt_jd_periodo = new javax.swing.JFormattedTextField();
        jd_rpt_fecha = new javax.swing.JDialog();
        jLabel22 = new javax.swing.JLabel();
        txt_je_fecha = new javax.swing.JFormattedTextField();
        btn_je_aceptar = new javax.swing.JButton();
        jd_apertura_caja = new javax.swing.JDialog();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txt_fecha_apertura = new javax.swing.JFormattedTextField();
        txt_monto_apertura = new javax.swing.JTextField();
        btn_je_apeturar = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txt_tienda_nombre = new javax.swing.JTextField();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        lbl_ruc = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        lbl_razon_social = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbl_tienda = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        btn_traslados = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        m_principal = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem21 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        m_conf_doc = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        m_usu = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();

        jd_cierre_caja.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jd_cierre_caja.setTitle("Cierre de Caja");
        jd_cierre_caja.setResizable(false);

        jLabel4.setText("Fecha:");

        try {
            txt_jc_fecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_jc_fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jc_fecha.setEnabled(false);
        txt_jc_fecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jc_fechaKeyPressed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresos"));

        jLabel6.setText("Saldo Inicial:");

        txt_jc_saldo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_saldo.setText("0.00");
        txt_jc_saldo.setEnabled(false);

        jLabel7.setText("Ventas Efectivo:");

        txt_jc_vefectivo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_vefectivo.setText("0.00");
        txt_jc_vefectivo.setEnabled(false);

        jLabel8.setText("Ventas Tarjeta:");

        txt_jc_vtarjeta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_vtarjeta.setText("0.00");
        txt_jc_vtarjeta.setEnabled(false);

        jLabel9.setText("Ventas Vale:");

        txt_jc_vvale.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_vvale.setText("0.00");
        txt_jc_vvale.setEnabled(false);

        txt_jc_cobros.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_cobros.setText("0.00");
        txt_jc_cobros.setEnabled(false);

        txt_jc_ingresos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_ingresos.setText("0.00");
        txt_jc_ingresos.setEnabled(false);

        jLabel13.setText("Otros Ingresos:");

        jLabel28.setText("Cobros:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_jc_vvale, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jc_vtarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_jc_vefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_jc_saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel28))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_jc_ingresos)
                    .addComponent(txt_jc_cobros, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_jc_saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_jc_vefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jc_vtarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_jc_vvale, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_jc_ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_jc_cobros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Salidas"));

        jLabel11.setText("Vales p/Cambio");

        txt_jc_svales.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_svales.setText("0.00");
        txt_jc_svales.setEnabled(false);

        txt_jc_sgastos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_sgastos.setText("0.00");
        txt_jc_sgastos.setEnabled(false);

        jLabel12.setText("Otros Gastos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(txt_jc_svales, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(txt_jc_sgastos, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_jc_svales, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_jc_sgastos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Totales"));

        jLabel15.setText("Efectivo:");

        txt_jc_tefectivo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_tefectivo.setText("0.00");
        txt_jc_tefectivo.setEnabled(false);

        jLabel16.setText("Tarjeta:");

        txt_jc_ttarjeta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_ttarjeta.setText("0.00");
        txt_jc_ttarjeta.setEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_jc_tefectivo)
                    .addComponent(txt_jc_ttarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jc_tefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jc_ttarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Monto Real"));

        jLabel18.setText("Efectivo:");

        txt_jc_aefectivo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel19.setText("Tarjeta:");

        txt_jc_atarjeta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_jc_atarjeta, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(txt_jc_aefectivo))
                .addGap(24, 24, 24))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jc_aefectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jc_atarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel17.setText("Suma Total");

        txt_jc_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_jc_total.setText("0.00");
        txt_jc_total.setEnabled(false);

        btn_jc_grabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/disk.png"))); // NOI18N
        btn_jc_grabar.setText("Grabar");
        btn_jc_grabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jc_grabarActionPerformed(evt);
            }
        });

        btn_jc_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_jc_cerrar.setText("Aun NO !!");
        btn_jc_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jc_cerrarActionPerformed(evt);
            }
        });

        chb_cerrar.setText("Cerrar Otra Fecha");
        chb_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chb_cerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_cierre_cajaLayout = new javax.swing.GroupLayout(jd_cierre_caja.getContentPane());
        jd_cierre_caja.getContentPane().setLayout(jd_cierre_cajaLayout);
        jd_cierre_cajaLayout.setHorizontalGroup(
            jd_cierre_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_cierre_cajaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_cierre_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jd_cierre_cajaLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(31, 31, 31)
                        .addComponent(txt_jc_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chb_cerrar))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_cierre_cajaLayout.createSequentialGroup()
                        .addGroup(jd_cierre_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jd_cierre_cajaLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(50, 50, 50)
                                .addComponent(txt_jc_total, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jd_cierre_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jd_cierre_cajaLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(btn_jc_cerrar)
                                .addGap(18, 18, 18)
                                .addComponent(btn_jc_grabar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jd_cierre_cajaLayout.setVerticalGroup(
            jd_cierre_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_cierre_cajaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_cierre_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jc_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chb_cerrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_cierre_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_cierre_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_jc_total, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jd_cierre_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_jc_grabar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_jc_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jd_empresas.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jd_empresas.setTitle("Seleccionar Empresa");

        jLabel20.setText("Empresa:");

        cbx_jd_empresas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbx_jd_empresasKeyPressed(evt);
            }
        });

        txt_jd_razon_social.setEnabled(false);

        btn_jd_aceptar.setText("Aceptar");
        btn_jd_aceptar.setEnabled(false);
        btn_jd_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jd_aceptarActionPerformed(evt);
            }
        });

        jLabel21.setText("Periodo:");

        try {
            txt_jd_periodo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_jd_periodo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_jd_periodo.setEnabled(false);
        txt_jd_periodo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jd_periodoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jd_empresasLayout = new javax.swing.GroupLayout(jd_empresas.getContentPane());
        jd_empresas.getContentPane().setLayout(jd_empresasLayout);
        jd_empresasLayout.setHorizontalGroup(
            jd_empresasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_empresasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_empresasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_jd_razon_social)
                    .addGroup(jd_empresasLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbx_jd_empresas, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 246, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_empresasLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_jd_periodo, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_jd_aceptar)))
                .addContainerGap())
        );
        jd_empresasLayout.setVerticalGroup(
            jd_empresasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_empresasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_empresasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbx_jd_empresas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_jd_razon_social, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_empresasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_jd_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jd_periodo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel22.setText("Ingresar Fecha:");

        try {
            txt_je_fecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_je_fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_je_fecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_je_fechaKeyPressed(evt);
            }
        });

        btn_je_aceptar.setText("Aceptar");
        btn_je_aceptar.setEnabled(false);
        btn_je_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_je_aceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jd_rpt_fechaLayout = new javax.swing.GroupLayout(jd_rpt_fecha.getContentPane());
        jd_rpt_fecha.getContentPane().setLayout(jd_rpt_fechaLayout);
        jd_rpt_fechaLayout.setHorizontalGroup(
            jd_rpt_fechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_rpt_fechaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_rpt_fechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_je_aceptar)
                    .addGroup(jd_rpt_fechaLayout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(27, 27, 27)
                        .addComponent(txt_je_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jd_rpt_fechaLayout.setVerticalGroup(
            jd_rpt_fechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_rpt_fechaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_rpt_fechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_je_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_je_aceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jd_apertura_caja.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jLabel24.setText("Fecha:");

        jLabel26.setText("Monto de Apertura:");

        try {
            txt_fecha_apertura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_fecha_apertura.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_fecha_apertura.setEnabled(false);

        txt_monto_apertura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_monto_apertura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_monto_aperturaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_monto_aperturaKeyTyped(evt);
            }
        });

        btn_je_apeturar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/accept.png"))); // NOI18N
        btn_je_apeturar.setText("Continuar");
        btn_je_apeturar.setEnabled(false);
        btn_je_apeturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_je_apeturarActionPerformed(evt);
            }
        });

        jLabel27.setText("Tienda:");

        txt_tienda_nombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_tienda_nombre.setText("TIENDA");
        txt_tienda_nombre.setEnabled(false);

        javax.swing.GroupLayout jd_apertura_cajaLayout = new javax.swing.GroupLayout(jd_apertura_caja.getContentPane());
        jd_apertura_caja.getContentPane().setLayout(jd_apertura_cajaLayout);
        jd_apertura_cajaLayout.setHorizontalGroup(
            jd_apertura_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_apertura_cajaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_apertura_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_apertura_cajaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_je_apeturar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jd_apertura_cajaLayout.createSequentialGroup()
                        .addGroup(jd_apertura_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jd_apertura_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_fecha_apertura)
                            .addComponent(txt_monto_apertura)
                            .addComponent(txt_tienda_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jd_apertura_cajaLayout.setVerticalGroup(
            jd_apertura_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jd_apertura_cajaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jd_apertura_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_fecha_apertura, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_apertura_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tienda_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jd_apertura_cajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_monto_apertura, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_je_apeturar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("lunaPOS - Jesucristo VIVE - Luna Systems Peru");
        setIconImage(Toolkit.getDefaultToolkit().getImage("lsp_nb.png"));

        jDesktopPane1.setBackground(new java.awt.Color(153, 153, 153));

        jLabel23.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/house.png"))); // NOI18N
        jLabel23.setText("RUC:");

        lbl_ruc.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbl_ruc.setText("jLabel24");

        jLabel25.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel25.setText("RAZON SOCIAL:");

        lbl_razon_social.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbl_razon_social.setText("jLabel26");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delivery.png"))); // NOI18N
        jLabel1.setText("TIENDA:");

        lbl_tienda.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lbl_tienda.setText("jLabel4");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_ruc)
                .addGap(18, 18, 18)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_razon_social)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 324, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_tienda)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(lbl_ruc)
                    .addComponent(jLabel25)
                    .addComponent(lbl_razon_social)
                    .addComponent(jLabel1)
                    .addComponent(lbl_tienda))
                .addContainerGap())
        );

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconos48x48/shopping-cart-icon.png"))); // NOI18N
        jButton1.setText("Tomar Pedido");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton1.setBorderPainted(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconos48x48/Pay-icon.png"))); // NOI18N
        jButton2.setText("Cobrar Venta");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.setBorderPainted(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconos48x48/folder-documents-icon.png"))); // NOI18N
        jButton4.setText("Compras");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.setBorderPainted(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconos48x48/shipping-icon.png"))); // NOI18N
        jButton5.setText("Ingresos");
        jButton5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton5.setBorderPainted(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconos48x48/delivery-box-icon.png"))); // NOI18N
        jButton6.setText("Salidas");
        jButton6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton6.setBorderPainted(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconos48x48/dinero.png"))); // NOI18N
        jButton7.setText("Mov. Diario");
        jButton7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton7.setBorderPainted(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconos48x48/product-icon.png"))); // NOI18N
        jButton8.setText("Productos");
        jButton8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton8.setBorderPainted(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        btn_traslados.setBackground(new java.awt.Color(255, 255, 255));
        btn_traslados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconos48x48/transfer-icon.png"))); // NOI18N
        btn_traslados.setText("Traslados");
        btn_traslados.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btn_traslados.setBorderPainted(false);
        btn_traslados.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_traslados.setMargin(new java.awt.Insets(10, 10, 10, 10));
        btn_traslados.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_traslados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_trasladosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btn_traslados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_traslados)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/iconos48x48/red-cross-icon.png"))); // NOI18N
        jButton3.setText("Cerrar");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.setBorderPainted(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(0, 574, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jDesktopPane1.setLayer(jPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        m_principal.setEnabled(false);

        jMenu2.setText("Facturacion");

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/user_thief_baldie.png"))); // NOI18N
        jMenuItem11.setText("Ver Clientes");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);
        jMenu2.add(jSeparator1);

        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/application.png"))); // NOI18N
        jMenuItem17.setText("Ver Pedidos");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem17);

        jMenuItem16.setText("Registrar Pedido");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem16);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cart.png"))); // NOI18N
        jMenuItem2.setText("Registrar Venta");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/basket.png"))); // NOI18N
        jMenuItem22.setText("Ventas");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem22);
        jMenu2.add(jSeparator4);

        jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        jMenuItem19.setText("Rpt. Reg. Ventas - SUNAT");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem19);

        jMenuItem20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        jMenuItem20.setText("Rpt. Reg. Ventas - Tienda");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem20);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        jMenuItem9.setText("Productos Vendidos - Dia Anterior");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        m_principal.add(jMenu2);

        jMenu4.setText("Gastos y Compras");

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/user_thief_baldie.png"))); // NOI18N
        jMenuItem5.setText("Ver Proveedor");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);
        jMenu4.add(jSeparator8);

        jMenuItem21.setText("Compras");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem21);
        jMenu4.add(jSeparator3);

        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        jMenuItem18.setText("Rpt. Reg. Compras - SUNAT");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem18);

        m_principal.add(jMenu4);

        jMenu1.setText("Almacenes");

        jMenuItem12.setText("Tiendas");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);
        jMenu1.add(jSeparator2);

        jMenuItem4.setText("Ingreso de Productos");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem7.setText("Registro de Servicios");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem6.setText("Envios");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem10.setText("Reg. Traslado entre Tiendas");
        jMenu1.add(jMenuItem10);
        jMenu1.add(jSeparator14);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delivery.png"))); // NOI18N
        jMenuItem1.setText("Productos en esta Tienda");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delivery.png"))); // NOI18N
        jMenuItem3.setText("Productos en otras Tiendas");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/delivery.png"))); // NOI18N
        jMenuItem23.setText("Productos en General");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem23);
        jMenu1.add(jSeparator5);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        jMenuItem8.setText("Rpt. Productos en Tienda");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuItem26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        jMenuItem26.setText("Rpt. Productos en Tienda por Precio");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem26);

        m_principal.add(jMenu1);

        jMenu10.setText("Contabilidad");

        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/coins.png"))); // NOI18N
        jMenuItem13.setText("Cierre de Caja");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem13);

        jMenuItem25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/clipboard_text.png"))); // NOI18N
        jMenuItem25.setText("Movimientos");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem25);

        m_conf_doc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/hammer_screwdriver.png"))); // NOI18N
        m_conf_doc.setText("Configurar Documentos");
        m_conf_doc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_conf_docActionPerformed(evt);
            }
        });
        jMenu10.add(m_conf_doc);

        m_principal.add(jMenu10);

        jMenu3.setText("Empresa");

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/house.png"))); // NOI18N
        jMenuItem15.setText("Empresas");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem15);

        jMenuItem24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/user.png"))); // NOI18N
        jMenuItem24.setText("Empleados");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem24);

        m_usu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/vcard.png"))); // NOI18N
        m_usu.setText("Usuario");
        m_usu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_usuActionPerformed(evt);
            }
        });
        jMenu3.add(m_usu);

        m_principal.add(jMenu3);

        jMenu5.setText("Ayuda");

        jMenuItem14.setText("Acerca de");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem14);

        m_principal.add(jMenu5);

        setJMenuBar(m_principal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Vistas.frm_ver_mis_productos productos = new Vistas.frm_ver_mis_productos();
        productos.origen = "mis_productos";
        ven.llamar_ventana(productos);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        Vistas.frm_ver_proveedor proveedor = new Vistas.frm_ver_proveedor();
        ven.llamar_ventana(proveedor);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Forms.frm_reg_cobro_pedido venta = new Forms.frm_reg_cobro_pedido();
        ven.llamar_ventana(venta);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        Vistas.frm_ver_clientes cliente = new Vistas.frm_ver_clientes();
        ven.llamar_ventana(cliente);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        Vistas.frm_ver_compras compra = new Vistas.frm_ver_compras();
        ven.llamar_ventana(compra);
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        Vistas.frm_ver_empleados empleado = new Vistas.frm_ver_empleados();
        ven.llamar_ventana(empleado);
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void m_usuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_usuActionPerformed
        Vistas.frm_ver_usuario usuario = new Vistas.frm_ver_usuario();
        ven.llamar_ventana(usuario);
    }//GEN-LAST:event_m_usuActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        Vistas.frm_ver_movimientos movimientos = new Vistas.frm_ver_movimientos();
        ven.llamar_ventana(movimientos);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        Vistas.frm_ver_ventas ventas = new Vistas.frm_ver_ventas();
        ven.llamar_ventana(ventas);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void m_conf_docActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_conf_docActionPerformed
        Generales.frm_configurar_documentos conf_doc = new Generales.frm_configurar_documentos();
        ven.llamar_ventana(conf_doc);
    }//GEN-LAST:event_m_conf_docActionPerformed
    

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Vistas.frm_ver_ingreso compra = new Vistas.frm_ver_ingreso();
        ven.llamar_ventana(compra);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        //frm_reg_cierre_caja caja = new frm_reg_cierre_caja();
        //ven.llamar_ventana(caja);
        boolean existe_caja = false;
        try {
            Statement st = con.conexion();
            String c_cierre = "select monto_apertura from cierre_caja where fecha = CURRENT_DATE() and id_almacen = '" + alm.getCodigo() + "' and empresa = '" + emp.getRuc() + "'";
            ResultSet rs = con.consulta(st, c_cierre);
            if (rs.next()) {
                existe_caja = true;
            } else {
                existe_caja = false;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        if (existe_caja == true) {
            jd_cierre_caja.setModal(true);
            jd_cierre_caja.setSize(538, 483);
            jd_cierre_caja.setLocationRelativeTo(null);
            jd_cierre_caja.setResizable(false);
            
            String fecha_actual = ven.getFechaActual();
            txt_jc_fecha.setText(ven.fechaformateada(fecha_actual));
            
            mov.setEmpresa(emp.getRuc());
            mov.setTienda(alm.getCodigo());
            mov.setFecha(fecha_actual);
            
            monto_apertura = mov.saldo_inicial();
            venta_efectivo = mov.venta_efectivo();
            venta_tarjeta = mov.venta_tarjeta();
            otros_ingresos = mov.otros_ingresos();
            cobros = mov.cobros();
            venta_vale = mov.venta_vales();
            vales_emitidos = mov.vales_emitidos();
            otros_gastos = mov.otros_gastos();
            
            total_efectivo = monto_apertura + venta_efectivo + otros_ingresos + cobros - otros_gastos;
            total_tarjeta = venta_tarjeta;
            
            jd_cierre_caja.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "ESTA TIENDA NO HA APERTURADO UNA CAJA");
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        frm_info info = new frm_info();
        ven.llamar_ventana(info);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        frm_reg_productos servicio = new frm_reg_productos();
        frm_reg_productos.origen = "mis_productos";
        frm_reg_productos.tipo = "servicio";
        frm_reg_productos.accion = "registrar";
        frm_reg_productos.cbx_tipo.setSelectedIndex(1);
        frm_reg_productos.cbx_tipo.setEnabled(false);
        ven.llamar_ventana(servicio);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        frm_ver_envios envios = new frm_ver_envios();
        ven.llamar_ventana(envios);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        frm_ver_tiendas tienda = new frm_ver_tiendas();
        ven.llamar_ventana(tienda);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        frm_ver_empresas empresa = new frm_ver_empresas();
        ven.llamar_ventana(empresa);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        frm_reg_pedido_cliente preventa = new frm_reg_pedido_cliente();;
        ven.llamar_ventana(preventa);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        frm_reg_cobro_pedido cobrar = new frm_reg_cobro_pedido();
        ven.llamar_ventana(cobrar);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        frm_ver_compras compras = new frm_ver_compras();
        ven.llamar_ventana(compras);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        frm_ver_mis_productos mis_productos = new frm_ver_mis_productos();
        ven.llamar_ventana(mis_productos);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        frm_ver_ingreso ingresos = new frm_ver_ingreso();
        ven.llamar_ventana(ingresos);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        frm_ver_envios envios = new frm_ver_envios();
        ven.llamar_ventana(envios);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btn_trasladosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_trasladosActionPerformed
        frm_ver_traslados traslado = new frm_ver_traslados();
        ven.llamar_ventana(traslado);
    }//GEN-LAST:event_btn_trasladosActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        frm_ver_movimientos movimiento = new frm_ver_movimientos();
        ven.llamar_ventana(movimiento);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        frm_ver_pedidos_cliente pedidos = new frm_ver_pedidos_cliente();
        ven.llamar_ventana(pedidos);
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        frm_reg_pedido_cliente pedido = new frm_reg_pedido_cliente();
        ven.llamar_ventana(pedido);
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void cbx_jd_empresasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbx_jd_empresasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Cl_Empresa empresa = new Cl_Empresa();
            empresa.setRuc(cbx_jd_empresas.getSelectedItem().toString());
            txt_jd_razon_social.setText(empresa.obtener_razon());
            if (tipo_reporte.equals("ventas_sunat")) {
                txt_jd_periodo.setEnabled(true);
                txt_jd_periodo.requestFocus();
            }
            if (tipo_reporte.equals("compras_sunat")) {
                txt_jd_periodo.setEnabled(true);
                txt_jd_periodo.requestFocus();
            }
        }
    }//GEN-LAST:event_cbx_jd_empresasKeyPressed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        jd_empresas.setModal(true);
        jd_empresas.setSize(576, 146);
        jd_empresas.setResizable(false);
        jd_empresas.setLocationRelativeTo(null);
        tipo_reporte = "ventas_sunat";
        jd_empresas.setVisible(true);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void txt_jd_periodoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jd_periodoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_jd_periodo.getText().length() == 7) {
                btn_jd_aceptar.setEnabled(true);
                btn_jd_aceptar.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_jd_periodoKeyPressed

    private void btn_jd_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jd_aceptarActionPerformed
        if (tipo_reporte.equals("ventas_sunat")) {
            Map<String, Object> parametros = new HashMap<>();
            String rpt_empresa = cbx_jd_empresas.getSelectedItem().toString();
            String rpt_periodo = txt_jd_periodo.getText();
            parametros.put("empresa", rpt_empresa);
            parametros.put("periodo", rpt_periodo);
            ven.ver_reporte("rpt_ventas_empresa", parametros);
        }
        if (tipo_reporte.equals("compras_sunat")) {
            Map<String, Object> parametros = new HashMap<>();
            String rpt_empresa = cbx_jd_empresas.getSelectedItem().toString();
            String rpt_periodo = txt_jd_periodo.getText();
            parametros.put("ruc", rpt_empresa);
            parametros.put("periodo", rpt_periodo);
            ven.ver_reporte("rpt_compras_empresa", parametros);
        }
        jd_empresas.dispose();
    }//GEN-LAST:event_btn_jd_aceptarActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        jd_empresas.setModal(true);
        jd_empresas.setSize(576, 146);
        jd_empresas.setResizable(false);
        jd_empresas.setLocationRelativeTo(null);
        tipo_reporte = "compras_sunat";
        jd_empresas.setVisible(true);
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void txt_je_fechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_je_fechaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt_je_fecha.getText().length() == 10) {
                btn_je_aceptar.setEnabled(true);
                btn_je_aceptar.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_je_fechaKeyPressed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        jd_rpt_fecha.setModal(true);
        jd_rpt_fecha.setSize(351, 101);
        jd_rpt_fecha.setResizable(false);
        jd_rpt_fecha.setLocationRelativeTo(null);
        tipo_reporte = "ventas_detallada_tienda";
        jd_rpt_fecha.setVisible(true);

    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void btn_je_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_je_aceptarActionPerformed
        if (tipo_reporte.equals("ventas_detallada_tienda")) {
            File miDir = new File(".");
            try {
                System.out.println("Directorio actual: " + miDir.getCanonicalPath());
                Map<String, Object> parametros = new HashMap<>();
                String rpt_empresa = emp.getRuc();
                String rpt_fecha = ven.fechabase(txt_je_fecha.getText());
                int rpt_tienda = alm.getCodigo();
                String path = miDir.getCanonicalPath();
                String direccion = path + "//reports//subreports//";
                //String direccion = path + "\\reports\\subreports\\";
                System.out.println(rpt_fecha);
                System.out.println(direccion);
                parametros.put("SUBREPORT_DIR", direccion);
                parametros.put("tienda", rpt_tienda);
                parametros.put("empresa", rpt_empresa);
                parametros.put("fecha", rpt_fecha);
                ven.ver_reporte("rpt_ventas_detallada_tienda", parametros);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
        }
        if (tipo_reporte.equals("productos_vendidos_dia")) {
            Map<String, Object> parametros = new HashMap<>();
            String rpt_empresa = emp.getRuc();
            String rpt_fecha = ven.fechabase(txt_je_fecha.getText());
            int rpt_tienda = alm.getCodigo();
            parametros.put("tienda", rpt_tienda);
            parametros.put("empresa", rpt_empresa);
            parametros.put("fecha", rpt_fecha);
            ven.ver_reporte("rpt_productos_venta_dia", parametros);
        }
        jd_rpt_fecha.dispose();
    }//GEN-LAST:event_btn_je_aceptarActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        frm_ver_todo_productos productos = new frm_ver_todo_productos();
        ven.llamar_ventana(productos);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        Map<String, Object> parametros = new HashMap<>();
        String rpt_empresa = emp.getRuc();
        int rpt_tienda = alm.getCodigo();
        parametros.put("tienda", rpt_tienda);
        parametros.put("empresa", rpt_empresa);
        ven.ver_reporte("rpt_productos_tienda", parametros);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        frm_ver_tiendas_productos tiendas = new frm_ver_tiendas_productos();
        ven.llamar_ventana(tiendas);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        jd_rpt_fecha.setModal(true);
        jd_rpt_fecha.setSize(351, 101);
        jd_rpt_fecha.setResizable(false);
        jd_rpt_fecha.setLocationRelativeTo(null);
        tipo_reporte = "productos_vendidos_dia";
        jd_rpt_fecha.setVisible(true);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        Map<String, Object> parametros = new HashMap<>();
        String rpt_empresa = emp.getRuc();
        int rpt_tienda = alm.getCodigo();
        parametros.put("tienda", rpt_tienda);
        parametros.put("empresa", rpt_empresa);
        ven.ver_reporte("rpt_productos_tienda_precio", parametros);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void btn_jc_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jc_cerrarActionPerformed
        jd_cierre_caja.dispose();
        if (caja_cerrada == true) {
            System.exit(0);
        }
    }//GEN-LAST:event_btn_jc_cerrarActionPerformed

    private void btn_jc_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jc_grabarActionPerformed
        //desabilitar textbox
        txt_jc_aefectivo.setEnabled(false);
        txt_jc_atarjeta.setEnabled(false);
        
        String efectivo = txt_jc_aefectivo.getText();
        String tarjeta = txt_jc_atarjeta.getText();
        double nefectivo = 0;
        double ntarjeta = 0;
        if (ven.esDecimal(efectivo)) {
            nefectivo = Double.parseDouble(efectivo);
        }
        
        if (ven.esDecimal(tarjeta)) {
            ntarjeta = Double.parseDouble(tarjeta);
        }
        
        if (nefectivo > 0 || ntarjeta > 0) {
            int res_actualiza = -1;
            Statement st = con.conexion();
            String update_caja = "update cierre_caja set sistema_efectivo = '" + total_efectivo + "', sistema_tarjeta = '" + total_tarjeta + "', monto_efectivo = "
                    + "'" + nefectivo + "', monto_tarjeta = '" + ntarjeta + "' where fecha = '" + fecha_cierre +"' and id_almacen = '" + alm.getCodigo() + "  ' and empresa = '" + emp.getRuc() + "  '";

            System.out.println(update_caja);
            res_actualiza = con.actualiza(st, update_caja);
            con.cerrar(st);
            if (res_actualiza > -1) {
                JOptionPane.showMessageDialog(null, "CAJA CERRADA CORRECTAMENTE");
                //cargar totales
                txt_jc_saldo.setText(ven.formato_totales(monto_apertura));
                txt_jc_vefectivo.setText(ven.formato_totales(venta_efectivo));
                txt_jc_vtarjeta.setText(ven.formato_totales(venta_tarjeta));
                txt_jc_ingresos.setText(ven.formato_totales(otros_ingresos));
                txt_jc_vvale.setText(ven.formato_totales(venta_vale));
                txt_jc_cobros.setText(ven.formato_totales(cobros));
                txt_jc_sgastos.setText(ven.formato_totales(otros_gastos));
                txt_jc_svales.setText(ven.formato_totales(vales_emitidos));
                
                txt_jc_tefectivo.setText(ven.formato_totales(total_efectivo));
                txt_jc_ttarjeta.setText(ven.formato_totales(total_tarjeta));
                
                txt_jc_total.setText(ven.formato_totales(total_tarjeta + total_efectivo));
                
                caja_cerrada = true;
                btn_jc_grabar.setEnabled(false);
                btn_jc_cerrar.setText("CERRAR Y SALIR");
            }
        }
    }//GEN-LAST:event_btn_jc_grabarActionPerformed

    private void txt_monto_aperturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_monto_aperturaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txt_monto_apertura.getText();
            if (texto.length() > 0) {
                if (ven.esDecimal(texto)) {
                    fmonto = Double.parseDouble(texto);
                    btn_je_apeturar.setEnabled(true);
                    btn_je_aceptar.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(null, "POR FAVOR INGRESE UN NUMERO VALIDO");
            }
        }
    }//GEN-LAST:event_txt_monto_aperturaKeyPressed

    private void btn_je_apeturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_je_apeturarActionPerformed
        //aperturar caja
        Statement st = con.conexion();
        String i_caja = "insert into cierre_caja values (CURRENT_DATE(), '" + alm.getCodigo() + "', '" + emp.getRuc() + "', '" + fmonto + "', '0', '0', '0', '0')";
        con.actualiza(st, i_caja);
        con.cerrar(st);
        jd_apertura_caja.dispose();

    }//GEN-LAST:event_btn_je_apeturarActionPerformed

    private void txt_monto_aperturaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_monto_aperturaKeyTyped
        ven.solo_precio(evt);
    }//GEN-LAST:event_txt_monto_aperturaKeyTyped

    private void txt_jc_fechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jc_fechaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            fecha_cierre = ven.fechabase(txt_jc_fecha.getText());
            //comprobar que caja este cerrada
            try {
                Statement st = con.conexion();
                
            } catch (Exception e) {
            }
        }        
    }//GEN-LAST:event_txt_jc_fechaKeyPressed

    private void chb_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chb_cerrarActionPerformed
        if (chb_cerrar.isSelected()) {
            txt_jc_aefectivo.setEnabled(false);
            txt_jc_atarjeta.setEnabled(false);
            btn_jc_grabar.setEnabled(false);
            txt_jc_fecha.setEnabled(true);
            txt_jc_fecha.requestFocus();
        } else {
            txt_jc_aefectivo.setEnabled(true);
            txt_jc_atarjeta.setEnabled(true);
            btn_jc_grabar.setEnabled(true);
            txt_jc_fecha.setText(ven.fechaformateada(ven.getFechaActual()));
            txt_jc_fecha.setEnabled(false);
            txt_jc_aefectivo.requestFocus();
        }
        
    }//GEN-LAST:event_chb_cerrarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frm_menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_jc_cerrar;
    private javax.swing.JButton btn_jc_grabar;
    private javax.swing.JButton btn_jd_aceptar;
    private javax.swing.JButton btn_je_aceptar;
    private javax.swing.JButton btn_je_apeturar;
    public static javax.swing.JButton btn_traslados;
    private javax.swing.JComboBox cbx_jd_empresas;
    private javax.swing.JCheckBox chb_cerrar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    public static javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JDialog jd_apertura_caja;
    private javax.swing.JDialog jd_cierre_caja;
    private javax.swing.JDialog jd_empresas;
    private javax.swing.JDialog jd_rpt_fecha;
    private javax.swing.JLabel lbl_razon_social;
    private javax.swing.JLabel lbl_ruc;
    private javax.swing.JLabel lbl_tienda;
    private javax.swing.JMenuItem m_conf_doc;
    public static javax.swing.JMenuBar m_principal;
    private javax.swing.JMenuItem m_usu;
    private javax.swing.JFormattedTextField txt_fecha_apertura;
    private javax.swing.JTextField txt_jc_aefectivo;
    private javax.swing.JTextField txt_jc_atarjeta;
    private javax.swing.JTextField txt_jc_cobros;
    private javax.swing.JFormattedTextField txt_jc_fecha;
    private javax.swing.JTextField txt_jc_ingresos;
    private javax.swing.JTextField txt_jc_saldo;
    private javax.swing.JTextField txt_jc_sgastos;
    private javax.swing.JTextField txt_jc_svales;
    private javax.swing.JTextField txt_jc_tefectivo;
    private javax.swing.JTextField txt_jc_total;
    private javax.swing.JTextField txt_jc_ttarjeta;
    private javax.swing.JTextField txt_jc_vefectivo;
    private javax.swing.JTextField txt_jc_vtarjeta;
    private javax.swing.JTextField txt_jc_vvale;
    private javax.swing.JFormattedTextField txt_jd_periodo;
    private javax.swing.JTextField txt_jd_razon_social;
    private javax.swing.JFormattedTextField txt_je_fecha;
    private javax.swing.JTextField txt_monto_apertura;
    private javax.swing.JTextField txt_tienda_nombre;
    // End of variables declaration//GEN-END:variables
}
