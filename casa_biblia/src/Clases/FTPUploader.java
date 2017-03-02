/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUploader {

    Cl_Varios ven = new Cl_Varios();
    FTPClient ftp = null;
    
    public FTPUploader() throws Exception {
        String host = "lunasystemsperu.com";
        //String host = ven.leer_archivo("server.txt");
        int port = 21;
        String user = "lunasystems";
        String pwd = "LunaSystems@123";
        //String user = "daemon";
        //String pwd = "xampp";
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host, port);
        
        reply = ftp.getReplyCode();
        System.out.print(reply);
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(user, pwd);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        //ftp.enterLocalActiveMode();
    }

    public void uploadFile(String localFileFullName, String fileName, String hostDir)
            throws Exception {
        try {
            InputStream input = null;
            File f1 = new File(localFileFullName);
            input = new FileInputStream(f1);
            ftp.changeWorkingDirectory(hostDir);
            ftp.storeFile(fileName, input);
        } catch (Exception ex) {
            System.out.print("ERROR EN UPLOADFILE" + ex);
            JOptionPane.showMessageDialog(null, "ERROR EN UPLOADFILE" + ex);
        }
        
    }
    
    public void disconnect() {
        if (this.ftp.isConnected()) {
            try {
                
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException ex) {
                // do nothing as file is already saved to server
            }
        }
    }
}
