/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author pc
 */
public class Cl_Persona {

    private String nro_doc;
    private String nom_per;
    private String ape_pat;
    private String ape_mat;
    private String dir_per;
    private Integer tel_per;
    private Double diario;
    private String empresa;
    private String cargo;
    private int tienda;
    private String contrasena;
    private String email;
    private String fecha_nacimiento;
    private String est_per = "1";

    public Cl_Persona() {
    }

    public String getNro_doc() {
        return nro_doc;
    }

    public void setNro_doc(String nro_doc) {
        this.nro_doc = nro_doc;
    }

    public String getNom_per() {
        return nom_per;
    }

    public void setNom_per(String nom_per) {
        this.nom_per = nom_per;
    }

    public String getApe_pat() {
        return ape_pat;
    }

    public void setApe_pat(String ape_pat) {
        this.ape_pat = ape_pat;
    }

    public String getApe_mat() {
        return ape_mat;
    }

    public void setApe_mat(String ape_mat) {
        this.ape_mat = ape_mat;
    }

    public String getDir_per() {
        return dir_per;
    }

    public void setDir_per(String dir_per) {
        this.dir_per = dir_per;
    }

    public Integer getTel_per() {
        return tel_per;
    }

    public void setTel_per(Integer tel_per) {
        this.tel_per = tel_per;
    }

    public Double getDiario() {
        return diario;
    }

    public void setDiario(Double diario) {
        this.diario = diario;
    }

    public String getEst_per() {
        return est_per;
    }

    public void setEst_per(String est_per) {
        this.est_per = est_per;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getTienda() {
        return tienda;
    }

    public void setTienda(int tienda) {
        this.tienda = tienda;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
}
