package com.mycompany.proyectofinalmelany;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ControladorJSONProducto implements InterfazProducto {
    private static List<Producto> productos = new ArrayList<>();
    private static Map<Integer, Integer> map = new HashMap<>();
    public Producto despacharProducto(int idProducto, int cantidadRequerida)  {
        Producto producto = getProducto(idProducto);
        Integer cantidad = producto.getCantidad();

        if (cantidad < cantidadRequerida) {
            throw new IllegalArgumentException("No hay suficiente cantidad de producto id: " + idProducto + " nombre: " + producto.getNombre());
        }

        producto.setCantidad(cantidad - cantidadRequerida);

        if (producto.getCantidad() == 0) {
            producto.setEstado("Agotado");
        }
        return new Producto(producto.getProductoId(), producto.getNombre(), cantidadRequerida, producto.getEstado());
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public Producto getProducto(int idProducto) {
        Integer productoId = map.get(idProducto);
        if (productoId == null) {
            throw new IllegalArgumentException("Producto no encontrado id: " + idProducto);
        }
        return productos.get(productoId);
    }

    public void cargarProductos(JTable jtable) throws IOException {
        URL url = new URL("file:src/main/resources/db-producto.json");
        productos = new ArrayList<>();
        map = new HashMap<>();

        String[] columnNames = new String[]{"productoId", "nombre", "stock", "estado"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        ObjectMapper mapper = new ObjectMapper();
        Producto[] produtosTemp = mapper.readValue(new File(url.getPath()), Producto[].class);

        Integer index = 0;
        for (Producto producto : produtosTemp) {
            if (map.get(producto.getProductoId()) != null) {
                continue;
            }
            map.put(producto.getProductoId(), index++);
            productos.add(producto);

            Object[] rowData = {producto.getProductoId(), producto.getNombre(), producto.getCantidad(), producto.getEstado()};
            model.addRow(rowData);
        }
        jtable.setModel(model);
    }

    public void guardarProductos() throws IOException {
        URL url = new URL("file:src/main/resources/db-producto.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(url.getPath()), productos);
    }

    public void agregarProducto(Producto producto) {
        if (map.get(producto.getProductoId()) != null) {
            throw new IllegalArgumentException("Producto con ID duplicado id: " + producto.getProductoId() + " nombre: " + producto.getNombre());
        }
        productos.add(producto);
    }
}
