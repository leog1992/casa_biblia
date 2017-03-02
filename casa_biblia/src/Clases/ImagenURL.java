/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;



/**
 *
 * @author CONTABILIDAD 02
 */

//imagen
public class ImagenURL extends javax.swing.JPanel {
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
public ImagenURL() {
this.setSize(115, 151); //se selecciona el tamaño del panel
}
 
//Se crea un método cuyo parámetro debe ser un objeto Graphics
 
public void paint(Graphics grafico) {
        Dimension height = getSize();
        
//Se selecciona la imagen que tenemos en el paquete de la //ruta del programa
                    URL url;
                    ImageIcon Img = null;
                try {
                    url = new URL(getUrl());
                    BufferedImage c = ImageIO.read(url);
                    Img = new ImageIcon(c);
                    grafico.drawImage(Img.getImage(), 0, 0, height.width, height.height, null);
                    setOpaque(false);
                    super.paintComponent(grafico);
                } catch (MalformedURLException ex) {
                   System.out.print(ex);
                } catch (IOException ex) {
                   System.out.print(ex);
                }
                  
        
//ImageIcon Img = new ImageIcon("Images/cara.jpg");
 
//se dibuja la imagen que tenemos en el paquete Images //dentro de un panel
        
        grafico.drawImage(Img.getImage(), 0, 0, height.width, height.height, null);
        
        setOpaque(false);
        super.paintComponent(grafico);
}
}
    
