/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package casa_biblia;

import Clases.Cl_Conectar;
import java.net.ServerSocket;
import javax.swing.JFrame;

/**
 *
 * @author pc
 */
public class casa_biblia {

    Cl_Conectar con = new Cl_Conectar();
    private static ServerSocket SERVER_SOCKET;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame login = new frm_login();
        login.setVisible(true);
    }

}
