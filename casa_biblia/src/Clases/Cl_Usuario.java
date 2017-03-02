/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import casa_biblia.frm_menu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;

/**
 *
 * @author CONTABILIDAD 02
 */
public class Cl_Usuario {

    Cl_Conectar con = new Cl_Conectar();
    private String nick;
    private String clave;
    private String email;
    private String estado = "1";
    private String pve_reg = "0";
    private String pve_eli = "0";
    private String pco_reg = "0";
    private String pco_eli = "0";
    private String pem_emp = "0";
    private String pem_usu = "0";
    private String pem_dat = "0";
    private String ppr_reg = "0";
    private String ppr_mod = "0";
    private String ppr_eli = "0";
    private String pcon_cdoc = "0";
    private String pal_cal = "0";
    private String pal_tras = "0";
    private String pal_elg = "0";
    private String pcaj_mov = "0";
    private String prep_ver = "0";

    public Cl_Usuario() {
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPve_reg() {
        return pve_reg;
    }

    public void setPve_reg(String pve_reg) {
        this.pve_reg = pve_reg;
    }

    public String getPve_eli() {
        return pve_eli;
    }

    public void setPve_eli(String pve_eli) {
        this.pve_eli = pve_eli;
    }

    public String getPco_reg() {
        return pco_reg;
    }

    public void setPco_reg(String pco_reg) {
        this.pco_reg = pco_reg;
    }

    public String getPco_eli() {
        return pco_eli;
    }

    public void setPco_eli(String pco_eli) {
        this.pco_eli = pco_eli;
    }

    public String getPem_emp() {
        return pem_emp;
    }

    public void setPem_emp(String pem_emp) {
        this.pem_emp = pem_emp;
    }

    public String getPem_usu() {
        return pem_usu;
    }

    public void setPem_usu(String pem_usu) {
        this.pem_usu = pem_usu;
    }

    public String getPem_dat() {
        return pem_dat;
    }

    public void setPem_dat(String pem_dat) {
        this.pem_dat = pem_dat;
    }

    public String getPpr_reg() {
        return ppr_reg;
    }

    public void setPpr_reg(String ppr_reg) {
        this.ppr_reg = ppr_reg;
    }

    public String getPpr_mod() {
        return ppr_mod;
    }

    public void setPpr_mod(String ppr_mod) {
        this.ppr_mod = ppr_mod;
    }

    public String getPpr_eli() {
        return ppr_eli;
    }

    public void setPpr_eli(String ppr_eli) {
        this.ppr_eli = ppr_eli;
    }

    public String getPcon_cdoc() {
        return pcon_cdoc;
    }

    public void setPcon_cdoc(String pcon_cdoc) {
        this.pcon_cdoc = pcon_cdoc;
    }

    public String getPal_cal() {
        return pal_cal;
    }

    public void setPal_cal(String pal_cal) {
        this.pal_cal = pal_cal;
    }

    public String getPal_tras() {
        return pal_tras;
    }

    public void setPal_tras(String pal_tras) {
        this.pal_tras = pal_tras;
    }

    public String getPal_elg() {
        return pal_elg;
    }

    public void setPal_elg(String pal_elg) {
        this.pal_elg = pal_elg;
    }

    public String getPcaj_mov() {
        return pcaj_mov;
    }

    public void setPcaj_mov(String pcaj_mov) {
        this.pcaj_mov = pcaj_mov;
    }

    public String getPrep_ver() {
        return prep_ver;
    }

    public void setPrep_ver(String prep_ver) {
        this.prep_ver = prep_ver;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void ver_usuarios(JComboBox cb_moneda) {
        cb_moneda.removeAllItems();
        try {
            Statement st = con.conexion();
            String c_tienda = "select * from empleados where estado = '1' and almacen = '" + frm_menu.alm.getCodigo() + "' and "
                    + "empresa = '" + frm_menu.emp.getRuc() + "' order by dni asc";
            System.out.println(c_tienda);
            ResultSet rs = con.consulta(st, c_tienda);
            while (rs.next()) {
                cb_moneda.addItem(rs.getString("dni"));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public String nombre_usuario() {
        String nombres = "";
        try {
            Statement st = con.conexion();
            String c_tienda = "select nombres, ape_pat, ape_mat from empleados where dni = '" + nick + "'";
            ResultSet rs = con.consulta(st, c_tienda);
            while (rs.next()) {
                nombres = rs.getString("nombres") + " " + rs.getString("ape_pat") + " " + rs.getString("ape_mat");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return nombres;
    }
}
