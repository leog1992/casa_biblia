/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Cl_Conectar;
import Clases.Cl_Entidad;
import Clases.Cl_Proveedor;
import Clases.Cl_Varios;
import Vistas.frm_ver_proveedor;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pc
 */
public class frm_reg_proveedor extends javax.swing.JInternalFrame {

    Cl_Varios ven = new Cl_Varios();
    Cl_Conectar con = new Cl_Conectar();
    Cl_Proveedor pro = new Cl_Proveedor();
    public static String accion = "";
    public static String origen = "";

    /**
     * Creates new form frm_reg_proveedor
     */
    public frm_reg_proveedor() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_ruc = new javax.swing.JTextField();
        txt_raz = new javax.swing.JTextField();
        txt_dir = new javax.swing.JTextField();
        txt_web = new javax.swing.JTextField();
        btn_cer = new javax.swing.JButton();
        btn_reg = new javax.swing.JButton();
        btn_lim = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txt_contacto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_telefono = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();

        setTitle("Registrar Proveedor");

        jLabel1.setText("RUC:");

        jLabel2.setText("Razon Social:");

        jLabel3.setText("Direccion:");

        jLabel5.setText("Pagina Web:");

        txt_ruc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_rucKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_rucKeyTyped(evt);
            }
        });

        txt_raz.setEditable(false);
        txt_raz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_razKeyPressed(evt);
            }
        });

        txt_dir.setEditable(false);
        txt_dir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_dirKeyPressed(evt);
            }
        });

        txt_web.setEditable(false);
        txt_web.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_webKeyPressed(evt);
            }
        });

        btn_cer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_cer.setText("Cerrar");
        btn_cer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerActionPerformed(evt);
            }
        });

        btn_reg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/accept.png"))); // NOI18N
        btn_reg.setText("Registrar");
        btn_reg.setEnabled(false);
        btn_reg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regActionPerformed(evt);
            }
        });

        btn_lim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/asterisk_orange.png"))); // NOI18N
        btn_lim.setText("Limpiar");
        btn_lim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limActionPerformed(evt);
            }
        });

        jLabel4.setText("Contacto:");

        txt_contacto.setEnabled(false);
        txt_contacto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_contactoKeyPressed(evt);
            }
        });

        jLabel6.setText("Telefono:");

        txt_telefono.setEnabled(false);
        txt_telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_telefonoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefonoKeyTyped(evt);
            }
        });

        jLabel7.setText("Email:");

        txt_email.setEnabled(false);
        txt_email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_emailKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_raz)
                            .addComponent(txt_dir, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_contacto)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_web, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 364, Short.MAX_VALUE))
                            .addComponent(txt_email)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_lim)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_reg)
                        .addGap(18, 18, 18)
                        .addComponent(btn_cer)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_raz, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_web, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_contacto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_reg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_lim, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerActionPerformed
        frm_ver_proveedor proveedor = new frm_ver_proveedor();
        ven.llamar_ventana(proveedor);
        this.dispose();
    }//GEN-LAST:event_btn_cerActionPerformed

    private void llenar() {
        pro.setRuc(txt_ruc.getText());
        pro.setRaz_soc(txt_raz.getText().toUpperCase());
        pro.setDir(txt_dir.getText().toUpperCase());
        pro.setWeb(txt_web.getText().toLowerCase());
        pro.setContacto(txt_contacto.getText().toUpperCase());
        pro.setTel(Integer.parseInt(txt_telefono.getText()));
        pro.setEmail(txt_email.getText().toLowerCase());
    }
    private void btn_regActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regActionPerformed
        llenar();
        if (accion.equals("registrar")) {
            try {
                Statement st = con.conexion();
                String insertar_pro = "insert into proveedores values ('" + pro.getRuc() + "', '" + pro.getRaz_soc() + "', "
                        + "'" + pro.getDir() + "', '" + pro.getWeb() + "', '" + pro.getContacto() + "', '" + pro.getTel() + "', '" + pro.getEmail() + "')";
                System.out.println(insertar_pro);
                con.actualiza(st, insertar_pro);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.print(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }

            JOptionPane.showMessageDialog(null, "Se procedio a ingresar los datos");
            btn_lim.doClick();
        }
        if (accion.equals("modificar")) {
            try {
                Statement st = con.conexion();
                String act_pro = "update proveedores set razon_social = '" + pro.getRaz_soc() + "', direccion = '" + pro.getDir() + "', web = '" + pro.getWeb() + "', "
                        + "contacto = '" + pro.getContacto() + "', telefono = '" + pro.getTel() + "', email = '" + pro.getEmail() + "' "
                        + " where ruc_proveedor = '" + pro.getRuc() + "'";
                System.out.println(act_pro);
                con.actualiza(st, act_pro);
                con.cerrar(st);
            } catch (Exception e) {
                System.out.print(e);
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }

            accion = "";
            JOptionPane.showMessageDialog(null, "Se procedio a modificar al proveedor");
            btn_cer.doClick();
        }

        if (origen.equals("ingreso")) {
            origen = "";
            accion = "";
            this.dispose();
        }
        if (origen.equals("compra")) {
            origen = "";
            accion = "";
            this.dispose();
        }
    }//GEN-LAST:event_btn_regActionPerformed

    private void btn_limActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limActionPerformed
        txt_ruc.setText("");
        txt_raz.setText("");
        txt_dir.setText("");
        txt_web.setText("");
        txt_ruc.requestFocus();

    }//GEN-LAST:event_btn_limActionPerformed

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
                            JOptionPane.showMessageDialog(null, "EL PROVEEDOR YA EXISTE, \nVERIFIQUE EL NUMERO DE RUC");
                            txt_ruc.setText("");
                            txt_ruc.requestFocus();
                        } else {

                            String json = Cl_Entidad.getJSON(pro.getRuc());
                            //Lo mostramos
                            String [] datos = Cl_Entidad.showJSON(json);

                            txt_raz.setText(datos[0].trim());
                            txt_dir.setText(datos[1].trim());
                            txt_web.setEditable(true);
                            txt_web.requestFocus();
                        }
                        con.cerrar(rs);
                        con.cerrar(st);
                    } catch (SQLException ex) {
                        System.out.print(ex);
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    } catch (ParseException ex) {
                        Logger.getLogger(frm_reg_proveedor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "EL RUC INGRESADO NO ES VALIDO \nVERIFIQUE POR FAVOR");
                }
            } else {
                JOptionPane.showMessageDialog(null, "EL RUC INGRESADO ESTA INCOMPLETO \nVERIFIQUE POR FAVOR");
            }
        }
    }//GEN-LAST:event_txt_rucKeyPressed

    private void txt_rucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rucKeyTyped
        if (txt_ruc.getText().length() == 11) {
            evt.consume();
        }
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9')) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_rucKeyTyped

    private void txt_razKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_razKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txt_raz.getText().isEmpty()) {
                txt_dir.setEditable(true);
            }
            txt_dir.requestFocus();
        }
    }//GEN-LAST:event_txt_razKeyPressed

    private void txt_dirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dirKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txt_dir.getText().isEmpty()) {
                txt_web.setEditable(true);
                txt_web.requestFocus();
            }

        }
    }//GEN-LAST:event_txt_dirKeyPressed

    private void txt_webKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_webKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txt_web.getText().isEmpty()) {
                txt_contacto.setEnabled(true);
                txt_contacto.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_webKeyPressed

    private void txt_contactoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_contactoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txt_contacto.getText().isEmpty()) {
                txt_telefono.setEnabled(true);
                txt_telefono.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_contactoKeyPressed

    private void txt_telefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefonoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txt_telefono.getText().isEmpty()) {
                txt_email.setEnabled(true);
                txt_email.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_telefonoKeyPressed

    private void txt_emailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_emailKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txt_email.getText().isEmpty()) {
                btn_reg.setEnabled(true);
                btn_reg.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_emailKeyPressed

    private void txt_telefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefonoKeyTyped
        ven.solo_numeros(evt);
    }//GEN-LAST:event_txt_telefonoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cer;
    private javax.swing.JButton btn_lim;
    public static javax.swing.JButton btn_reg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField txt_contacto;
    public static javax.swing.JTextField txt_dir;
    private javax.swing.JTextField txt_email;
    public static javax.swing.JTextField txt_raz;
    public static javax.swing.JTextField txt_ruc;
    private javax.swing.JTextField txt_telefono;
    public static javax.swing.JTextField txt_web;
    // End of variables declaration//GEN-END:variables
}
