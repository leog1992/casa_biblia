/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Forms;

import Vistas.frm_ver_usuario;
import Clases.Cl_Conectar;
import Clases.Cl_Persona;
import Clases.Cl_Usuario;
import Clases.Cl_Varios;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author pc
 */
public class frm_reg_usuario extends javax.swing.JInternalFrame {
Cl_Conectar con = new Cl_Conectar();
Cl_Varios ven = new Cl_Varios();
Cl_Persona per = new Cl_Persona();
Cl_Usuario usu = new Cl_Usuario();
public static String accion = "reg";
    /**
     * Creates new form frm_reg_usuario
     */
    public frm_reg_usuario() {
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_dni = new javax.swing.JTextField();
        txt_nom = new javax.swing.JTextField();
        txt_usu = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        txt_con = new javax.swing.JPasswordField();
        txt_con1 = new javax.swing.JPasswordField();

        setTitle("Registrar Usuario");
        setToolTipText("");

        jLabel1.setText("DNI:");

        jLabel2.setText("Nombres:");

        jLabel4.setText("Usuario");

        jLabel5.setText("Contraseña");

        jLabel6.setText("Repite contraseña:");

        txt_dni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_dni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_dniKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_dniKeyTyped(evt);
            }
        });

        txt_nom.setEditable(false);

        txt_usu.setEditable(false);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        jButton1.setText("Cerrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txt_con.setEditable(false);
        txt_con.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_conKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_conKeyTyped(evt);
            }
        });

        txt_con1.setEditable(false);
        txt_con1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_con1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_con1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_nom)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_con, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_con1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                            .addComponent(txt_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(413, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_con, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_con1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_dniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dniKeyPressed

        if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
    if (txt_dni.getText().length()<=8){
        try {per.setNro_doc(txt_dni.getText());
            Statement st = con.conexion();
            String query ="select nom_per from Persona where nro_doc = '"+per.getNro_doc()+"'";
            ResultSet rs = con.consulta(st, query);
        
           if (rs.next())
           { 
               txt_nom.setText(rs.getString(1));
               String nick;
               nick=per.getNro_doc();
               txt_usu.setText(nick);
               txt_con.setEditable(true);
               txt_con.requestFocus();

           }
           else 
           {
               this.dispose();
               JOptionPane.showMessageDialog(null, "No Existe Usuario, registre al personal \nVaya a Personas / Registrar Empleado");
               
           }
         } catch (SQLException ex) {
              System.out.println(ex);
         }
    }
}
    }//GEN-LAST:event_txt_dniKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    this.dispose();
    frm_ver_usuario usuario = new frm_ver_usuario();
    ven.llamar_ventana(usuario);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txt_dniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dniKeyTyped
if(txt_dni.getText().length()==8) evt.consume();
char car = evt.getKeyChar();
if((car<'0' || car>'9')) evt.consume();   

    }//GEN-LAST:event_txt_dniKeyTyped

    private void txt_conKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_conKeyPressed
if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
    if (!txt_con.getText().isEmpty()){
        txt_con1.setEditable(true);
        txt_con1.requestFocus();
    }
}
    }//GEN-LAST:event_txt_conKeyPressed

    public void llenar () {
        per.setNro_doc(txt_dni.getText());
        usu.setNick(txt_usu.getText());
        usu.setClave(txt_con1.getText());
    }
    private void txt_con1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_con1KeyPressed
if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
    if (accion.equals("reg")) {
        if (!txt_con1.getText().isEmpty()){
            if (txt_con.getText().equals(txt_con1.getText())) {
                llenar();
                Statement st = con.conexion();
                JOptionPane.showMessageDialog(null, "Se procedio a  ingresar los datos.");
                String insert_per = "insert into Usuario (nick, contra, estado, nro_doc) Values ('"+usu.getNick()+"', '"+usu.getClave()+"', '1', '"+per.getNro_doc()+"')";
                con.actualiza(st, insert_per);
                con.cerrar(st);
                JOptionPane.showMessageDialog(null, "Se ha creado su usuario correctamente \nVuelva a ingresar al Sistema");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese su contraseña");
                txt_con.setText("");
                txt_con1.setText("");
                txt_con1.setEditable(false);
                txt_con.requestFocus();
            }
        }
    }
}
    }//GEN-LAST:event_txt_con1KeyPressed

    private void txt_conKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_conKeyTyped
if(txt_con.getText().length()==12) evt.consume();
    }//GEN-LAST:event_txt_conKeyTyped

    private void txt_con1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_con1KeyTyped
if(txt_con1.getText().length()==12) evt.consume();
    }//GEN-LAST:event_txt_con1KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPasswordField txt_con;
    private javax.swing.JPasswordField txt_con1;
    private javax.swing.JTextField txt_dni;
    private javax.swing.JTextField txt_nom;
    private javax.swing.JTextField txt_usu;
    // End of variables declaration//GEN-END:variables
}
