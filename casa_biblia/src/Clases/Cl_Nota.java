/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author luis-d
 */
public class Cl_Nota {
    private Integer id;
    private String nota;
    private String fec_reg;
    private String fec_eje;
    private String est = "0";

    public Cl_Nota(Integer id, String nota, String fec_reg, String fec_eje) {
        this.id = id;
        this.nota = nota;
        this.fec_reg = fec_reg;
        this.fec_eje = fec_eje;
    }

    public Cl_Nota() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getFec_reg() {
        return fec_reg;
    }

    public void setFec_reg(String fec_reg) {
        this.fec_reg = fec_reg;
    }

    public String getFec_eje() {
        return fec_eje;
    }

    public void setFec_eje(String fec_eje) {
        this.fec_eje = fec_eje;
    }

    public String getEst() {
        return est;
    }

    public void setEst(String est) {
        this.est = est;
    }
    
    
}
