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
public class Cl_Requerimiento {
    private int id;
    private String fec;
    private String est = "1";

    public Cl_Requerimiento(int id, String fec) {
        this.id = id;
        this.fec = fec;
    }

    public Cl_Requerimiento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFec() {
        return fec;
    }

    public void setFec(String fec) {
        this.fec = fec;
    }

    public String getEst() {
        return est;
    }

    public void setEst(String est) {
        this.est = est;
    }
    
    
}
