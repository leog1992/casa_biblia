/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Clases.Cl_Conectar;
import Clases.Cl_Usuario;
import Clases.Cl_Varios;
import Forms.frm_reg_usuario;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luis-d
 */
public class frm_ver_usuario extends javax.swing.JInternalFrame {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    Cl_Usuario usu = new Cl_Usuario();
    Integer i;
    DefaultTableModel mostrar;

    /**
     * Creates new form frm_ver_usuario
     */
    public frm_ver_usuario() {
        initComponents();
        String query = "select u.dni, p.nombres, p.ape_pat, p.ape_mat, u.estado, a.nombre from usuarios as u inner join empleados as p on u.dni = p.dni inner join  almacenes as a on u.id_almacen = a.id_almacen";
        ver_usuarios(query);
    }

    private void ver_usuarios(String query) {
        try {
            mostrar = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);
            //Establecer como cabezeras el nombre de las colimnas
            mostrar.addColumn("Tienda");
            mostrar.addColumn("Nick");
            mostrar.addColumn("Nombre y Apellidos");
            mostrar.addColumn("Estado");

            //Creando las filas para el JTable
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getObject("nombre");
                fila[1] = rs.getObject("dni");
                fila[2] = rs.getString("ape_pat") + " " + rs.getString("ape_mat") + " " + rs.getString("nombres");
                String estado = rs.getString("estado");
                if (estado.equals("1")) {
                    fila[3] = "ACTIVO";
                } else {
                    fila[3] = "-";
                }

                mostrar.addRow(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
            t_usuario.setModel(mostrar);
            t_usuario.getColumnModel().getColumn(0).setPreferredWidth(50);
            t_usuario.getColumnModel().getColumn(1).setPreferredWidth(50);
            t_usuario.getColumnModel().getColumn(2).setPreferredWidth(280);
            t_usuario.getColumnModel().getColumn(3).setPreferredWidth(50);
            mostrar.fireTableDataChanged();
        } catch (SQLException e) {
            System.out.print(e);
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
        t_usuario = new javax.swing.JTable();
        btn_sus = new javax.swing.JButton();
        btn_act = new javax.swing.JButton();
        btn_cer = new javax.swing.JButton();
        btn_cc = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setTitle("Listar Usuario");

        t_usuario.setModel(new javax.swing.table.DefaultTableModel(
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
        t_usuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                t_usuarioMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_usuario);

        btn_sus.setText("Suspender");
        btn_sus.setEnabled(false);
        btn_sus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_susActionPerformed(evt);
            }
        });

        btn_act.setText("Activar");
        btn_act.setEnabled(false);
        btn_act.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actActionPerformed(evt);
            }
        });

        btn_cer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cancel.png"))); // NOI18N
        btn_cer.setText("Cerrar");
        btn_cer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerActionPerformed(evt);
            }
        });

        btn_cc.setText("Cambiar Contraseña");
        btn_cc.setEnabled(false);
        btn_cc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ccActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/add.png"))); // NOI18N
        jButton1.setText("Registrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_sus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_act)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_cer))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 15, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_sus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_act, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_cerActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        frm_reg_usuario usuario = new frm_reg_usuario();
        ven.llamar_ventana(usuario);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void t_usuarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_usuarioMousePressed
        i = t_usuario.getSelectedRow();
        usu.setEstado(t_usuario.getValueAt(i, 2).toString());
        if (!usu.getEstado().equals("-")) {
            btn_cc.setEnabled(true);
            btn_sus.setEnabled(true);
            btn_act.setEnabled(false);
        } else {
            btn_act.setEnabled(true);
            btn_cc.setEnabled(false);
            btn_sus.setEnabled(false);
        }
    }//GEN-LAST:event_t_usuarioMousePressed

    private void btn_susActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_susActionPerformed
        usu.setNick(t_usuario.getValueAt(i, 0).toString());
        Statement st = con.conexion();
        String retira = "update Usuario set estado = '0' where nick = '" + usu.getNick() + "'";
        con.actualiza(st, retira);
        con.cerrar(st);
        String query = "select u.nick, p.nom_per, u.estado from Usuario as u inner join Persona as p on u.nro_doc = p.nro_doc";
        ver_usuarios(query);
    }//GEN-LAST:event_btn_susActionPerformed

    private void btn_actActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actActionPerformed
        usu.setNick(t_usuario.getValueAt(i, 0).toString());
        Statement st = con.conexion();
        String retira = "update Usuario set estado = '1' where nick = '" + usu.getNick() + "'";
        con.actualiza(st, retira);
        con.cerrar(st);
        String query = "select u.nick, p.nom_per, u.estado from Usuario as u inner join Persona as p on u.nro_doc = p.nro_doc";
        ver_usuarios(query);
    }//GEN-LAST:event_btn_actActionPerformed

    private void btn_ccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ccActionPerformed
        frm_reg_usuario usuario = new frm_reg_usuario();
        usuario.accion = "mod";
        ven.llamar_ventana(usuario);
        this.dispose();
    }//GEN-LAST:event_btn_ccActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_act;
    private javax.swing.JButton btn_cc;
    private javax.swing.JButton btn_cer;
    private javax.swing.JButton btn_sus;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable t_usuario;
    // End of variables declaration//GEN-END:variables
}