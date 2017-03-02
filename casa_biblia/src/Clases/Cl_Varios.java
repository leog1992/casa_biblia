/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author Administrador
 */
public class Cl_Varios {

    Cl_Conectar con = new Cl_Conectar();
    DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
    DecimalFormat formato = null;

    public String formato_numero(Double number) {
        simbolo.setDecimalSeparator('.');
        formato = new DecimalFormat("######0.00", simbolo);
        String numero = "";
        numero = formato.format(number);
        return numero;
    }

    public String formato_precio(Double number) {
        simbolo.setDecimalSeparator('.');
        formato = new DecimalFormat("######0.0000", simbolo);
        String numero = "";
        numero = formato.format(number);
        return numero;
    }

    public String formato_tc(Double number) {
        simbolo.setDecimalSeparator('.');
        formato = new DecimalFormat("######0.000", simbolo);
        String numero = "";
        numero = formato.format(number);
        return numero;
    }

    public String formato_totales(Double number) {
        simbolo.setDecimalSeparator('.');
        simbolo.setGroupingSeparator(',');
        formato = new DecimalFormat("#,###,##0.00", simbolo);
        String numero = "";
        numero = formato.format(number);
        return numero;
    }

    public void llamar_ventana(JInternalFrame ventana) {
        if (mostrar(ventana)) {
            Dimension desktopSize = casa_biblia.frm_menu.jDesktopPane1.getSize();
            Dimension jInternalFrameSize = ventana.getSize();
            ventana.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                    (desktopSize.height - jInternalFrameSize.height) / 2);
            casa_biblia.frm_menu.jDesktopPane1.add(ventana);
            ventana.show();
        }
    }

    public void solo_numeros(KeyEvent evt) {
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9')) {
            evt.consume();
        }
    }

    public void solo_precio(KeyEvent evt) {
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && car != '.') {
            evt.consume();
        }
    }

    public void limitar_caracteres(KeyEvent evt, JTextField txt, int longitud) {
        if (txt.getText().length() == longitud) {
            evt.consume();
        }
    }

    public static boolean mostrar(JInternalFrame ventana) {
        boolean mostrar = true;
        for (int a = 0; a < casa_biblia.frm_menu.jDesktopPane1.getComponentCount(); a++) {
            // verificar si es instancia de algun componente que ya este en el jdesktoppane
            //if (ventana.getClass().equals(casa_biblia.frm_menu.jDesktopPane1.getComponent(a).getClass())) {
            if (ventana.getClass().isInstance(casa_biblia.frm_menu.jDesktopPane1.getComponent(a))) {
                System.out.println("esta instanciado, ya se abrio, no se debe mostrar");
                JOptionPane.showMessageDialog(null, "ESTA VENTANA YA ESTA ABIERTA, CERRAR Y ABRIR OTRA VEZ");
                mostrar = false;
            }
        }
        return mostrar;
    }

    public String hora_actual() {
        Calendar calendario = new GregorianCalendar();
        int hora, minutos, segundos;
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        String hora_exacta = hora + "" + minutos + "" + segundos;
        System.out.println("hora: " + hora_exacta);
        return hora_exacta;
    }

    public String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
        return formateador.format(ahora);
    }

    public String fechaformateada(String fecha) {
        String m_fecha = null;
        try {
            DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date fec = df.parse(fecha);
            m_fecha = dt.format(fec);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return m_fecha;
    }

    public String mostrar_hora(String fecha) {
        String m_fecha = null;
        try {
            DateFormat dt = new SimpleDateFormat("HH:mm:ss");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fec = df.parse(fecha);
            m_fecha = dt.format(fec);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return m_fecha;
    }

    public String fecha_larga_tabla(String fecha) {
        String m_fecha = null;
        try {
            DateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fec = df.parse(fecha);
            m_fecha = dt.format(fec);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return m_fecha;
    }

    public String fechabase(String fecha) {
        String m_fecha = null;
        try {
            DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date fec = dt.parse(fecha);
            m_fecha = df.format(fec);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return m_fecha;
    }

    public int getMes(String fecha) {
        int mes_;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(casa_biblia.casa_biblia.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mes_ = calendar.get(Calendar.MONTH) + 1;
        return mes_;
    }

    // Suma los días recibidos a la fecha  
    public Date suma_dia(String fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(fecha);
            calendar.setTime(date); // Configuramos la fecha que se recibe
            calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        } catch (ParseException ex) {
            Logger.getLogger(Cl_Varios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

    public void centrar_celda(JTable table, int col) {
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(col).setCellRenderer(tcr);
    }

    public void izquieda_celda(JTable table, int col) {
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.LEFT);
        table.getColumnModel().getColumn(col).setCellRenderer(tcr);
    }

    public void derecha_celda(JTable table, int col) {
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(col).setCellRenderer(tcr);
    }

    public void ver_reporte(String filename, Map<String, Object> parametros) {
        Connection st = con.conx();

        try {
            Date ahora = new Date();
            SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String fecha_com = formateador.format(ahora);

            JasperReport jasperReport;
            JasperPrint jasperPrint;
            jasperReport = JasperCompileManager.compileReport("reports//" + filename + ".jrxml");
            jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, st);
            JasperExportManager.exportReportToPdfFile(
                    jasperPrint, "temp/" + filename + "_" + fecha_com + ".pdf");

            try {

                File file = new File("temp/" + filename + "_" + fecha_com + ".pdf");
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                System.out.print(e + " -- error io");
                JOptionPane.showMessageDialog(null, "Error al Generar el PDF -- \n" + e);
            }

        } catch (JRException ex) {
            System.out.print(ex + " -- error jre");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error con el Reporte -- \n" + ex);
        }
    }

    public void ver_reporte_excel(String filename, Map<String, Object> parametros) {
        Connection st = con.conx();

        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            jasperReport = JasperCompileManager.compileReport("Reports//" + filename + ".jrxml");
            jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, st);
            JasperExportManager.exportReportToPdfFile(
                    jasperPrint, "Reports/" + filename + ".pdf");

            try {
                File file = new File("Reports/" + filename + ".pdf");
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                System.out.print(e + " -- error io");
                JOptionPane.showMessageDialog(null, "Error al Generar el PDF -- " + e);
            }

        } catch (JRException ex) {
            System.out.print(ex + " -- error jre");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error con el Reporte -- " + ex);
        }
    }

    public void imp_reporte(String filename, Map<String, Object> parametros) {
        Connection st = con.conx();

        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            jasperReport = JasperCompileManager.compileReport("reports//" + filename + ".jrxml");
            jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, st);
            //convert_byte(filename);
            JasperPrintManager.printReport(jasperPrint, false);
        } catch (JRException ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());

        }
    }

    private void convert_byte(String archivo) {
        //Especificamos el tipo de dato a imprimir
        //Tipo: bytes; Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        //Aca obtenemos el servicio de impresion por defatul
        //Si no quieres ver el dialogo de seleccionar impresora usa esto
        //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        //Con esto mostramos el dialogo para seleccionar impresora
        //Si quieres ver el dialogo de seleccionar impresora usalo
        //Solo mostrara las impresoras que soporte arreglo de bits
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        PrintService service = ServiceUI.printDialog(null, 700, 200, printService, defaultService, flavor, pras);

        //Creamos un arreglo de tipo byte
        byte[] bytes = null;
        //Aca convertimos el string(cuerpo del ticket) a bytes tal como
        //lo maneja la impresora(mas bien ticketera :p)
        String sourcePATH;
        File file = new File("reports/" + archivo + ".pdf");
        sourcePATH = file.getAbsolutePath();

        try {
            try (InputStream inputStream = new FileInputStream(sourcePATH)) {
                String inputStreamToString = inputStream.toString();
                bytes = inputStreamToString.getBytes();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not found" + e);
        } catch (IOException e) {
            System.out.println("IO Ex" + e);
        }
        //Creamos un documento a imprimir, a el se le appendeara
        //el arreglo de bytes
        Doc doc = new SimpleDoc(bytes, flavor, null);

        //Creamos un trabajo de impresiÃ³n
        DocPrintJob job = service.createPrintJob();

        //Imprimimos dentro de un try de a huevo
        try {
            //El metodo print imprime
            job.print(doc, null);
        } catch (Exception er) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + er.getMessage());
        }
    }

    public String leer_archivo(String nom_arc) {
        String linea = null;
        try {
            File Ffichero = new File(nom_arc);
            /*Si existe el fichero*/
            if (Ffichero.exists()) {
                /*Abre un flujo de lectura a el fichero*/
                BufferedReader Flee = new BufferedReader(new FileReader(Ffichero));
                String Slinea;
                /*Lee el fichero linea a linea hasta llegar a la ultima*/
                while ((Slinea = Flee.readLine()) != null) {
                    /*Imprime la linea leida*/
                    linea = Slinea;
                }
                /*Cierra el flujo*/
                Flee.close();
            } else {
                System.out.println("Fichero No Existe");
                linea = "NO ALMACEN";
            }
        } catch (IOException ex) {
            /*Captura un posible error y le imprime en pantalla*/
            System.out.println(ex.getMessage());
        }
        return linea;
    }

    public String ceros_izquierda(int largo, String string) {
        String ceros = "";
        int cantidad = largo - string.length();
        if (cantidad >= 1) {
            for (int i = 0; i < cantidad; i++) {
                ceros += "0";
            }
            return (ceros + string);
        } else {
            return string;
        }
    }

    public boolean compararFechasConDate(String fecha1, String fechaActual) {
        System.out.println("Parametro String Fecha 1 = " + fecha1 + "\n"
                + "Parametro String fechaActual = " + fechaActual + "\n");
        String resultado = "";
        boolean menor = false;
        try {
            /**
             * Obtenemos las fechas enviadas en el formato a comparar
             */
            SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaDate1 = formateador.parse(fecha1);
            Date fechaDate2 = formateador.parse(fechaActual);

            System.out.println("Parametro Date Fecha 1 = " + fechaDate1 + "\n"
                    + "Parametro Date fechaActual = " + fechaDate2 + "\n");
            if (fechaDate1.before(fechaDate2)) {
                resultado = "La Fecha 1 es menor ";
                menor = true;
            }
            System.out.println(resultado);
//            if (fechaDate1.before(fechaDate2)) {
//                resultado = "La Fecha 1 es menor ";
//            } else {
//                if (fechaDate2.before(fechaDate1)) {
//                    resultado = "La Fecha 1 es Mayor ";
//                } else {
//                    resultado = "Las Fechas Son iguales ";
//                }
//            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  " + e.getMessage());
        }
        return menor;
    }

    public void limpiarTabla(JTable tabla) {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            int filas = tabla.getRowCount();
            for (int i = 0; filas > i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL LIMPIAR LA TABLA");
        }
    }

    public boolean esEntero(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public boolean esDecimal(String numero) {
        try {
            Double.parseDouble(numero);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public Boolean validar_RUC(String ruc) {
        Boolean validado = false;
        int dig[] = new int[10];
        int factores[] = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        //  System.out.println("digitos del ruc");
        for (int i = 0; i < 10; i++) {
            dig[i] = Integer.parseInt(ruc.charAt(i) + "");
            //      System.out.println(dig[i] + "\t");
        }
        int producto[] = new int[10];
        //   System.out.println("producto de cada digito");
        for (int i = 0; i < 10; i++) {
            producto[i] = dig[i] * factores[i];
            //   System.out.println(producto[i]);
        }
        int suma_producto = 0;
        //     System.out.println("suma total del producto");
        for (int i = 0; i < 10; i++) {
            suma_producto += producto[i];
        }
        //     System.out.println(suma_producto);
        //     System.out.println("Resultado de formula");
        int formula = 11 - (suma_producto % 11);
        //       System.out.println(formula);
        String resultado = formula + "";
//        System.out.println("longitud de resultado " + resultado.length());
        int longitud = resultado.length();
        String ultimo = resultado.charAt(longitud - 1) + "";
        //       System.out.println("ultimo digito " + ultimo);
        int dig11 = Integer.parseInt(ruc.charAt(10) + "");
        //       System.out.println("comparando " + ultimo + " = " + dig11);
        if (dig11 == Integer.parseInt(ultimo)) {
            validado = true;
        }
//        System.out.println(validado);
        return validado;
    }
}
