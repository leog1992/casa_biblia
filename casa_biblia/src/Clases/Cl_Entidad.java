/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author gerenciatecnica
 */
public class Cl_Entidad {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String SERVER_PATH = "http://lunasystemsperu.com/lsp/";

    public static String getJSON(String ruc) {

        StringBuffer response = null;

        try {
            //Generar la URL
            String url = SERVER_PATH + "consulta_sunat.php?ruc=" + ruc;
            //Creamos un nuevo objeto URL con la url donde pedir el JSON
            URL obj = new URL(url);
            //Creamos un objeto de conexión
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //Añadimos la cabecera
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            // Enviamos la petición por POST
            con.setDoOutput(true);
            //Capturamos la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Mostramos la respuesta del servidor por consola
            System.out.println("Respuesta del servidor: " + response);
            System.out.println();
            //cerramos la conexión
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public static String [] showJSON(String json) throws ParseException {
        String[] datos = new String [2];
        System.out.println("INFORMACIÓN OBTENIDA DE LA BASE DE DATOS:");

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        JSONArray array = new JSONArray();
        array.add(obj);
        //JSONValue.toJSONString(obj);

        //Iterar el array y extraer la información
        for (Object array_json : array) {
            JSONObject row = (JSONObject) array_json;
            String nombre = (String) row.get("nombre_o_razon_social");
            String apellidos = (String) row.get("direccion_completa");
            //Mostrar la información en pantalla
            datos[0] = nombre;
            datos[1] = apellidos;
        }
        return datos;
    }
}
