/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author pc
 */
public class Cl_Productos {

    Cl_Conectar con = new Cl_Conectar();
    Cl_Varios ven = new Cl_Varios();
    private Integer id = 0;
    private String codigo_externo;
    private String descripcion;
    private String ubicacion;
    private String tipo;
    private String marca;
    private String autor;
    private Double costo = 0.00;
    private Double precio;
    private Double can = 0.00;
    private Double cant_act;
    private Double can_min;
    private Double can_max;
    private String estado = "1";
    private String caracteristicas;
    private String img;
    private Double cantidad_caja;
    private String ultima_salida;
    private String ultimo_ingreso;
    private String afecto_igv;
    private int id_familia;
    private int id_sub_familia;
    private int id_und_med;
    private String proveedor;

    public Cl_Productos() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getCan() {
        return can;
    }

    public void setCan(Double can) {
        this.can = can;
    }

    public Double getCant_act() {
        return cant_act;
    }

    public void setCant_act(Double cant_act) {
        this.cant_act = cant_act;
    }

    public Double getCan_min() {
        return can_min;
    }

    public void setCan_min(Double can_min) {
        this.can_min = can_min;
    }

    public Double getCan_max() {
        return can_max;
    }

    public void setCan_max(Double can_max) {
        this.can_max = can_max;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Double getCantidad_caja() {
        return cantidad_caja;
    }

    public void setCantidad_caja(Double cantidad_caja) {
        this.cantidad_caja = cantidad_caja;
    }

    public String getUltima_salida() {
        return ultima_salida;
    }

    public void setUltima_salida(String ultima_salida) {
        this.ultima_salida = ultima_salida;
    }

    public String getUltimo_ingreso() {
        return ultimo_ingreso;
    }

    public void setUltimo_ingreso(String ultimo_ingreso) {
        this.ultimo_ingreso = ultimo_ingreso;
    }

    public String getAfecto_igv() {
        return afecto_igv;
    }

    public void setAfecto_igv(String afecto_igv) {
        this.afecto_igv = afecto_igv;
    }

    public int getId_familia() {
        return id_familia;
    }

    public void setId_familia(int id_familia) {
        this.id_familia = id_familia;
    }

    public int getId_sub_familia() {
        return id_sub_familia;
    }

    public void setId_sub_familia(int id_sub_familia) {
        this.id_sub_familia = id_sub_familia;
    }

    public int getId_und_med() {
        return id_und_med;
    }

    public void setId_und_med(int id_und_med) {
        this.id_und_med = id_und_med;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCodigo_externo() {
        return codigo_externo;
    }

    public void setCodigo_externo(String codigo_externo) {
        this.codigo_externo = codigo_externo;
    }

    public boolean crear_producto() {
        boolean creado = false;
        int resultado;
        Statement st = con.conexion();
        String i_producto = "insert into productos values ('" + id + "', '" + tipo + "', '" + codigo_externo + "', '" + descripcion + "', '" + caracteristicas + "', "
                + "'" + cantidad_caja + "', '" + afecto_igv + "', '" + id_familia + "', '" + id_sub_familia + "', '" + id_und_med + "', '" + costo + "', "
                + "'" + precio + "', '" + proveedor + "', '" + img + "', '1')";
        resultado = con.actualiza(st, i_producto);
        if (resultado > -1) {
            creado = true;
        } else {
            JOptionPane.showMessageDialog(null, "ERROR AL INSERTAR");
        }
        con.cerrar(st);
        return creado;
    }

    public int ultimo_producto() {
        int id_producto = 1;
        try {
            Statement st = con.conexion();
            String c_producto = "select id_producto from productos order by id_producto desc limit 1";
            ResultSet rs = con.consulta(st, c_producto);
            if (rs.next()) {
                id_producto = rs.getInt("id_producto") + 1;
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        return id_producto;
    }

    public String ver_caracteristicas() {
        String detalle = null;
        try {
            Statement st = con.conexion();
            String c_producto = "select caracteristicas from productos where id_producto = '" + id + "'";
            ResultSet rs = con.consulta(st, c_producto);
            if (rs.next()) {
                detalle = rs.getString("caracteristicas");
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        return detalle;
    }

    public Object[] obtener_cantidad_actual(int tienda, String empresa) {
        Object datos[] = new Object[2];
        try {
            Statement st = con.conexion();
            String bus_pro = "select cantidad_actual from productos_almacenes where id_producto = '" + id + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            System.out.println(bus_pro);
            ResultSet rs = con.consulta(st, bus_pro);
            if (rs.next()) {
                datos[0] = true;
                datos[1] = rs.getDouble("cantidad_actual");
            } else {
                datos[0] = false;
                datos[1] = "0.00";
            }
            con.cerrar(rs);
            con.cerrar(st);
        } catch (SQLException ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return datos;
    }

    public void a_producto_almacen(int tienda, String empresa) {
        try {
            Statement st = con.conexion();
            String act_pro = "update productos_almacenes set cantidad_actual = '" + cant_act + "' where id_producto = '" + id + "' and id_almacen = '" + tienda + "' and empresa = '" + empresa + "'";
            System.out.println(act_pro);
            con.actualiza(st, act_pro);
            con.cerrar(st);
        } catch (Exception ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }

    public void i_producto_almacen(int tienda, String empresa) {
        try {
            Statement st = con.conexion();
            String query = "insert into productos_almacenes values ('" + tienda + "', '" + empresa + "', '" + id + "', '" + ubicacion + "', '" + can_min + "', "
                    + "'" + can_max + "', '" + can + "', '" + ultimo_ingreso + "', '" + ultima_salida + "')";
            con.actualiza(st, query);
            System.out.println(query);
            con.cerrar(st);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    public void ver_productos(JTable tabla, String query) {
        try {
            DefaultTableModel mostrar = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            TableRowSorter sorter = new TableRowSorter(mostrar);
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);
            //Establecer como cabezeras el nombre de las colimnas
            mostrar.addColumn("Cod.");
            mostrar.addColumn("Tipo P.");
            mostrar.addColumn("Descripcion");
            mostrar.addColumn("Clasificacion");
            mostrar.addColumn("Familia");
            mostrar.addColumn("Ubicacion");
            mostrar.addColumn("Cant. Act.");
            mostrar.addColumn("Und. Med.");
            mostrar.addColumn("Pre. Unit.");
            mostrar.addColumn("Estado");

            //Creando las filas para el JTable
            while (rs.next()) {
                Object[] fila = new Object[10];
                fila[0] = rs.getString("id_producto");
                String tipo_producto = rs.getString("tipo_producto");
                fila[1] = tipo_producto;
                fila[2] = rs.getString("descripcion") + " " + rs.getString("codigo_externo");
                fila[3] = rs.getString("nombre_clase");
                fila[4] = rs.getString("nombre_familia");
                fila[5] = rs.getString("ubicacion");
                setCan(rs.getDouble("cantidad_actual"));
                setCan_min(rs.getDouble("cantidad_minima"));
                fila[6] = ven.formato_numero(rs.getDouble("cantidad_actual"));
                //fila[6] = rs.getInt("cantidad_actual");
                fila[7] = rs.getString("corto");
                fila[8] = ven.formato_numero(rs.getDouble("precio"));
                if (tipo_producto.equals("BIEN")) {
                    if (getCan() > getCan_min()) {
                        fila[9] = "NORMAL";
                    }
                    if (getCan() <= getCan_min() && getCan() > 0) {
                        fila[9] = "POR TERMINAR";
                    }
                    if (getCan() <= 0) {
                        fila[9] = "NO DISPONIBLE";
                    }
                } else {
                    fila[10] = "NORMAL";
                }
                mostrar.addRow(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
            tabla.setModel(mostrar);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(550);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(120);
            tabla.getColumnModel().getColumn(4).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(5).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(6).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(7).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(8).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(9).setPreferredWidth(90);
            mostrar.fireTableDataChanged();
            tabla.setRowSorter(sorter);
            tabla.setDefaultRenderer(Object.class, new render_mis_productos());
        } catch (SQLException e) {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    public void ver_todo_productos(JTable tabla, String query) {
        try {
            DefaultTableModel mostrar = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            TableRowSorter sorter = new TableRowSorter(mostrar);
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);
            //Establecer como cabezeras el nombre de las colimnas
            mostrar.addColumn("Cod.");
            mostrar.addColumn("Tipo P.");
            mostrar.addColumn("Descripcion");
            mostrar.addColumn("Familia");
            mostrar.addColumn("Clasificacion");
            mostrar.addColumn("Und. Med.");
            mostrar.addColumn("Cant. Caja");
            mostrar.addColumn("Costo");
            mostrar.addColumn("Venta");

            //Creando las filas para el JTable
            while (rs.next()) {
                Object[] fila = new Object[9];
                fila[0] = rs.getString("id_producto");
                fila[1] = rs.getString("tipo_producto");
                String codigoexterno = rs.getString("codigo_externo");
                if (codigoexterno.isEmpty() || codigoexterno.equals("")) {
                    fila[2] = rs.getString("descripcion");
                } else {
                    fila[2] = rs.getString("descripcion") + " - CE:" + rs.getString("codigo_externo");
                }
                // fila[2] = rs.getString("descripcion") + " - CE:" + rs.getString("codigo_externo");
                fila[3] = rs.getString("nombre_familia");
                fila[4] = rs.getString("nombre_clase");
                fila[5] = rs.getString("corto");
                fila[6] = ven.formato_numero(rs.getDouble("cantidad_caja"));
                fila[7] = ven.formato_numero(rs.getDouble("costo"));
                fila[8] = ven.formato_numero(rs.getDouble("precio"));
                mostrar.addRow(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
            tabla.setModel(mostrar);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(550);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(90);
            tabla.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabla.getColumnModel().getColumn(5).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(6).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(7).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(8).setPreferredWidth(70);
            ven.derecha_celda(tabla, 0);
            ven.centrar_celda(tabla, 1);
            ven.izquieda_celda(tabla, 2);
            ven.centrar_celda(tabla, 3);
            ven.centrar_celda(tabla, 4);
            ven.centrar_celda(tabla, 5);
            ven.derecha_celda(tabla, 6);
            ven.derecha_celda(tabla, 7);
            ven.derecha_celda(tabla, 8);
            mostrar.fireTableDataChanged();
            tabla.setRowSorter(sorter);
        } catch (SQLException e) {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }
    
    public void ver_productos_tiendas(JTable tabla, String query) {
        try {
            DefaultTableModel mostrar = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            TableRowSorter sorter = new TableRowSorter(mostrar);
            Statement st = con.conexion();
            ResultSet rs = con.consulta(st, query);
            //Establecer como cabezeras el nombre de las colimnas
            mostrar.addColumn("Cod.");
            mostrar.addColumn("Tienda.");
            mostrar.addColumn("Tipo P.");
            mostrar.addColumn("Descripcion");
            mostrar.addColumn("Clasificacion");
            mostrar.addColumn("Familia");
            mostrar.addColumn("Ubicacion");
            mostrar.addColumn("Cant. Act.");
            mostrar.addColumn("Und. Med.");
            mostrar.addColumn("Pre. Unit.");
            mostrar.addColumn("Estado");

            //Creando las filas para el JTable
            while (rs.next()) {
                Object[] fila = new Object[11];
                fila[0] = rs.getString("id_producto");
                fila[1] = rs.getString("tienda");
                String tipo_producto = rs.getString("tipo_producto");
                fila[2] = tipo_producto;
                fila[3] = rs.getString("descripcion") + " " + rs.getString("codigo_externo");
                fila[4] = rs.getString("nombre_clase");
                fila[5] = rs.getString("nombre_familia");
                fila[6] = rs.getString("ubicacion");
                setCan(rs.getDouble("cantidad_actual"));
                setCan_min(rs.getDouble("cantidad_minima"));
                //fila[6] = ven.formato_numero(rs.getDouble("cantidad_actual"));
                fila[7] = rs.getDouble("cantidad_actual");
                fila[8] = rs.getString("corto");
                fila[9] = ven.formato_numero(rs.getDouble("precio"));
                if (tipo_producto.equals("BIEN")) {
                    if (getCan() > getCan_min()) {
                        fila[10] = "NORMAL";
                    }
                    if (getCan() <= getCan_min() && getCan() > 0) {
                        fila[10] = "POR TERMINAR";
                    }
                    if (getCan() <= 0) {
                        fila[10] = "NO DISPONIBLE";
                    }
                } else {
                    fila[10] = "NORMAL";
                }
                mostrar.addRow(fila);
            }
            con.cerrar(st);
            con.cerrar(rs);
            tabla.setModel(mostrar);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(100);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(550);
            tabla.getColumnModel().getColumn(4).setPreferredWidth(120);
            tabla.getColumnModel().getColumn(5).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(6).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(7).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(8).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(9).setPreferredWidth(70);
            tabla.getColumnModel().getColumn(10).setPreferredWidth(90);
            mostrar.fireTableDataChanged();
            tabla.setRowSorter(sorter);
            tabla.setDefaultRenderer(Object.class, new render_productos_tiendas());
        } catch (SQLException e) {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }
}
