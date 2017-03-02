/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImprimirClases;

import Clases.Cl_Conectar;
import Clases.Cl_Varios;
import Clases.leer_numeros;
import br.com.adilson.util.Extenso;
import br.com.adilson.util.PrinterMatrix;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

/**
 *
 * @author gerenciatecnica
 */
public class Print_Venta_Ticket {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();

    public void generar(int venta, String periodo, int tienda, String empresa) {
        PrinterMatrix printer = new PrinterMatrix();

        Extenso e = new Extenso();

        e.setNumber(101.85);

        Date date = new Date();
        String ruc_empresa = "";
        String razon_social = "";
        String direccion = "";
        String ntienda = "";
        String nro_cliente = "";
        String nombre_cliente = "";
        String tipo_documento = "";
        String fecha_registro = "";
        int id_tido = 0;
        int serie = 0;
        int numero = 0;
        int id_cliente = 0;
        int afecto = 0;
        try {
            Statement st = con.conexion();
            String c_cabezera = "select v.empresa, e.razon_social, e.direccion, a.nombre as ntienda, v.id_cliente, c.nro_documento, v.nombre_cliente, td.nombre as documento, v.id_tido, v.serie, v.numero, v.afecto "
                    + "from ventas as v "
                    + "inner join empresas as e on e.ruc_empresa = v.empresa "
                    + "inner join almacenes as a on a.id_almacen = v.id_almacen and a.empresa = v.empresa "
                    + "inner join tipo_documento as td on td.id_tido = v.id_tido "
                    + "inner join clientes as c on c.id_cliente = v.id_cliente "
                    + "where v.id_almacen = '" + tienda + "' and v.empresa = '" + empresa + "' and v.id_venta = '" + venta + "' and v.periodo = '" + periodo + "'";
            System.out.println(c_cabezera);
            ResultSet rs = con.consulta(st, c_cabezera);
            if (rs.next()) {
                ruc_empresa = rs.getString("empresa");
                razon_social = rs.getString("razon_social");
                direccion = rs.getString("direccion");
                ntienda = rs.getString("ntienda");
                nombre_cliente = rs.getString("nombre_cliente");
                tipo_documento = rs.getString("documento");
                nro_cliente = rs.getString("nro_documento");
                //fecha_registro = ven.fecha_larga_tabla(rs.getString("fecha_registro"));
                DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                fecha_registro = hourdateFormat.format(date);
                id_tido = rs.getInt("id_tido");
                serie = rs.getInt("serie");
                numero = rs.getInt("numero");
                afecto = rs.getInt("afecto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        int cantidad_filas_resultado = 0;
        try {
            Statement st = con.conexion();
            String c_filas = "SELECT count(dv.producto) as cantidad_producto, dv.venta "
                    + "FROM detalle_venta as dv "
                    + "where dv.tienda = '" + tienda + "' and dv.empresa = '" + empresa + "' and dv.venta = '" + venta + "' and dv.periodo = '" + periodo + "'";
            ResultSet rs = con.consulta(st, c_filas);
            if (rs.next()) {
                cantidad_filas_resultado = rs.getInt("cantidad_producto");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        System.out.println(cantidad_filas_resultado + "cantidad de productos");
        //Definir el tamanho del papel para la impresion  aca 25 lineas y 80 columnas
        if (id_tido == 9) {
            printer.setOutSize(29,40);
            //printer.setOutSize(20 + cantidad_filas_resultado, 40);
        } else {
            printer.setOutSize(20, 40);
            //printer.setOutSize(20 + cantidad_filas_resultado, 40);
        }

        //Imprimir * de la 2da linea a 25 en la columna 1;
        // printer.printCharAtLin(2, 25, 1, "*");
        //Imprimir * 1ra linea de la columa de 1 a 80
        //printer.printCharAtCol(1, 1, 80, "=");
        //Imprimir Encabezado nombre del La EMpresa
        printer.printTextLinCol(1, 1, varios_impresion.centrar_texto(40, "** CASA DE LA BIBLIA **"));
        printer.printTextLinCol(2, 1, varios_impresion.centrar_texto(40, ruc_empresa + " - " + razon_social.toUpperCase()));
        printer.printTextLinCol(3, 1, varios_impresion.centrar_texto(40, "Tel:  043-320111"));
        printer.printTextLinCol(4, 1, direccion.substring(0, 39)); //aplicar subString(0,39);
        //printer.printTextWrap(linI, linE, colI, colE, null);
        printer.printTextLinCol(6, 1, "TIENDA: " + ntienda.toUpperCase());
        printer.printTextLinCol(7, 1, tipo_documento.toUpperCase() + " #: " + ven.ceros_izquierda(3, serie + "") + " - " + ven.ceros_izquierda(5, numero + ""));
        printer.printTextLinCol(8, 1, "FECHA EMISION: " + fecha_registro);
        printer.printTextLinCol(9, 1, "SERIE: PSSFIKA16080306");

        int aumenta_fila = 0;

        if (id_tido == 9) {
            aumenta_fila = 2;
            int otra_fila = 0;
            printer.printTextLinCol(10, 1, "CLIENTE:");
            if (!nro_cliente.equals("00000000")) {
                printer.printTextLinCol(11, 1, nro_cliente);
                otra_fila++;
            }
            printer.printTextLinCol(11 + otra_fila, 1, nombre_cliente);
        }

        int filas = 0;
        double total = 0;
        try {
            Statement st = con.conexion();
            String c_detalle = "select dv.cantidad, dv.precio, p.descripcion, p.codigo_externo from detalle_venta as dv inner join productos as "
                    + "p on p.id_producto = dv.producto where dv.tienda = '" + tienda + "' and dv.empresa = '" + empresa + "' and dv.venta = "
                    + "'" + venta + "' and dv.periodo = '" + periodo + "' ";
            ResultSet rs = con.consulta(st, c_detalle);
            while (rs.next()) {
                String producto;
                if (rs.getString("codigo_externo").equals("")) {
                    producto = rs.getString("descripcion").trim();
                } else {
                    producto = rs.getString("descripcion").trim() + " - " + rs.getString("codigo_externo").trim();
                }

                int cantidad = rs.getInt("cantidad");
                String parcial = ven.formato_totales(rs.getDouble("cantidad") * rs.getDouble("precio"));
                if (cantidad > 1) {
                    total = total + (cantidad * rs.getDouble("precio"));
                } else {
                    total = total + (rs.getDouble("precio"));
                }
                
                filas = filas + aumenta_fila;
                filas++;
                //printer.printTextLinCol(10 + filas, 1, cantidad + " - ");
                if (producto.length() > 62) {
                    producto = producto.substring(0, 61);
                }
                printer.printTextWrap(9 + filas, 9 + filas + 1, 0, 40, cantidad + " - " + producto.trim());
                if (producto.length() > 24) {
                    filas++;
                }
                printer.printTextLinCol(10 + filas, 33, " x " + varios_impresion.texto_derecha(5, parcial));
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        leer_numeros leer = new leer_numeros();
        String texto_numero = leer.Convertir(ven.formato_numero(total), true) + " SOLES";

        if (id_tido == 9) {
            if (afecto == 0) {
                printer.printTextLinCol(10 + filas + 2, 1, varios_impresion.texto_derecha(30, "SUB TOTAL"));
                printer.printTextLinCol(10 + filas + 2, 31, varios_impresion.texto_derecha(10, ven.formato_totales(total)));
                printer.printTextLinCol(10 + filas + 3, 1, varios_impresion.texto_derecha(30, "IGV"));
                printer.printTextLinCol(10 + filas + 3, 31, varios_impresion.texto_derecha(10, ven.formato_totales(0.00)));
                printer.printTextLinCol(10 + filas + 4, 1, varios_impresion.texto_derecha(30, "TOTAL"));
                printer.printTextLinCol(10 + filas + 4, 31, varios_impresion.texto_derecha(10, ven.formato_totales(total)));
            } else {
                printer.printTextLinCol(10 + filas + 2, 1, varios_impresion.texto_derecha(30, "SUB TOTAL"));
                printer.printTextLinCol(10 + filas + 2, 31, varios_impresion.texto_derecha(10, ven.formato_totales(total / 1.18)));
                printer.printTextLinCol(10 + filas + 3, 1, varios_impresion.texto_derecha(30, "IGV"));
                printer.printTextLinCol(10 + filas + 3, 31, varios_impresion.texto_derecha(10, ven.formato_totales(total / 1.18 * 0.18)));
                printer.printTextLinCol(10 + filas + 4, 1, varios_impresion.texto_derecha(30, "TOTAL"));
                printer.printTextLinCol(10 + filas + 4, 31, varios_impresion.texto_derecha(10, ven.formato_totales(total)));
            }

            printer.printTextLinCol(10 + filas + 5, 1, texto_numero);

        } else {
            printer.printTextLinCol(10 + filas + 2, 1, varios_impresion.texto_derecha(30, "TOTAL"));
            printer.printTextLinCol(10 + filas + 2, 31, varios_impresion.texto_derecha(10, ven.formato_totales(total)));
            printer.printTextLinCol(10 + filas + 3, 1, texto_numero);
        }
        //      printer.printTextLinCol(10 + filas + 2, 1, varios_impresion.texto_derecha(30, "TOTAL"));
        //      printer.printTextLinCol(10 + filas + 2, 31, varios_impresion.texto_derecha(10, ven.formato_totales(total)));

        byte[] initEP = new byte[]{0x1b, '@'};
        byte[] cutP = new byte[] {0x1d, 'V', 1};
        printer.printTextLinCol(10 + filas + 6, 1, (10+filas+6) + "");

        printer.show();
       // printer.toPrinter("BIXOLON SRP-270", cutP);
        printer.toFile("impresion.txt");

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("impresion.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        if (inputStream == null) {
            return;
        }
        
        PrinterService printerService = new PrinterService();
        printerService.printBytes("BIXOLON SRP-270", initEP);
        printerService.printString("BIXOLON SRP-270", inputStream.toString());

        /*
        DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc document = new SimpleDoc(inputStream, docFormat, null);

        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();

        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

        if (defaultPrintService != null) {
            DocPrintJob printJob = defaultPrintService.createPrintJob();
            try {
                printJob.print(document, attributeSet);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.err.println("No existen impresoras instaladas");
        }
        */

        //PrinterService printerService = new PrinterService();
        //System.out.println(printerService.getPrinters());

        printerService.printBytes("BIXOLON SRP-270", cutP);

    }
}
