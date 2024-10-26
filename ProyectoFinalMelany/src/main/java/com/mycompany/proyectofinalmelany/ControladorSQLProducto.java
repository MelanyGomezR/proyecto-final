package com.mycompany.proyectofinalmelany;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ControladorSQLProducto implements InterfazProducto {
    private List<Producto> productos = new ArrayList<>();

    @Override
    public Producto despacharProducto(int idProducto, int cantidadRequerida) throws SQLException, IOException {
        Producto producto = this.getProducto(idProducto);
        Integer cantidad = producto.getCantidad();

        if (cantidad < cantidadRequerida) {
            throw new IllegalArgumentException("No hay suficiente cantidad de producto id: " + idProducto + " nombre: " + producto.getNombre());
        }

        producto.setCantidad(cantidad - cantidadRequerida);

        if (producto.getCantidad() == 0) {
            producto.setEstado("Agotado");
        }
        Connection con = MysqlDB.getConnection();
        PreparedStatement statement = con.prepareStatement("UPDATE producto SET stock = ?, estado = ? WHERE productoId = ?");
        statement.setInt(1, producto.getCantidad());
        statement.setString(2, producto.getEstado());
        statement.setInt(3, idProducto);
        statement.executeUpdate();

        return new Producto(producto.getProductoId(), producto.getNombre(), cantidadRequerida, producto.getEstado());
    }

    @Override
    public List<Producto> getProductos() {
        return productos;
    }

    @Override
    public Producto getProducto(int idProducto) throws SQLException, IOException {
        Connection con = MysqlDB.getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT * FROM producto where productoId = ?");
        statement.setInt(1, idProducto);
        ResultSet result = statement.executeQuery();
        Producto producto = new Producto();
        if (!result.next()) {
            throw new RuntimeException("Producto no encontrado");
        }
        producto.setProductoId(result.getInt("productoId"));
        producto.setCantidad(result.getInt("stock"));
        producto.setEstado(result.getString("estado"));
        producto.setNombre(result.getString("nombre"));
        return producto;
    }

    @Override
    public void cargarProductos(JTable jtable) throws IOException, SQLException {
        Connection con = MysqlDB.getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT * FROM producto");
        ResultSet result = statement.executeQuery();
        
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        productos = new ArrayList<>();

        while (result.next()) {
            Producto producto = new Producto();
            producto.setProductoId(result.getInt("productoId"));
            producto.setCantidad(result.getInt("stock"));
            producto.setEstado(result.getString("estado"));
            producto.setNombre(result.getString("nombre"));
            
            Object[] rowData = {producto.getProductoId(), producto.getNombre(), producto.getCantidad(), producto.getEstado()};
            model.addRow(rowData);
            
            productos.add(producto);
        }
        jtable.setModel(model);
    }

    @Override
    public void guardarProductos() throws IOException {}

    @Override
    public void agregarProducto(Producto producto) throws SQLException, IOException {
        Connection con = MysqlDB.getConnection();
        PreparedStatement statement = con.prepareStatement("INSERT INTO producto (nombre, stock, estado) VALUES (?, ?, ?)");
        statement.setString(1, producto.getNombre());
        statement.setInt(2, producto.getCantidad());
        statement.setString(3, producto.getEstado());
        statement.executeUpdate();
        productos.add(producto);
    }
}
