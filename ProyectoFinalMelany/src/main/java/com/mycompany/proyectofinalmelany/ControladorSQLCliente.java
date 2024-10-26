package com.mycompany.proyectofinalmelany;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ControladorSQLCliente implements InterfazCliente {
    private static List<Cliente> clientes = new ArrayList<>();
    @Override
    public void cargarClientes(JTable jtable) throws IOException, SQLException {
        Connection con = MysqlDB.getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT * FROM cliente");
        ResultSet result = statement.executeQuery();

        clientes = new ArrayList<>();
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        while (result.next()) {
            Cliente cliente = new Cliente();
            cliente.setNit(result.getString("nitCliente"));
            cliente.setTelefonoCliente(result.getString("celular"));
            cliente.setNombreCliente(result.getString("nombreCliente"));
            cliente.setDireccionFacturacion(result.getString("direccion"));
            Object[] rowData = {cliente.getNit(), cliente.getNombreCliente(), cliente.getDireccionFacturacion(), cliente.getTelefonoCliente()};
            model.addRow(rowData);

            PreparedStatement statementPedido = con.prepareStatement("SELECT pedido.* FROM pedido INNER JOIN cliente ON cliente.nitCliente = pedido.nitCliente where pedido.nitCliente = ?");
            statementPedido.setString(1, cliente.getNit());
            ResultSet resultPedido = statementPedido.executeQuery();
            List<Pedido> pedidos = new ArrayList<>();
            while (resultPedido.next()) {
                Pedido pedido = new Pedido();
                pedido.setFecha(resultPedido.getDate("fechaOrden"));
                pedido.setPedidoClienteId(resultPedido.getInt("pedidoId"));
                pedido.modificarEstado(resultPedido.getString("estado"));

                PreparedStatement stmtProducto = con.prepareStatement("SELECT producto.* FROM producto INNER JOIN pedido_productos ON pedido_productos.productoId = producto.productoId where pedido_productos.pedidoId = ?");
                stmtProducto.setInt(1, pedido.getPedidoClienteId());
                ResultSet resultProducto = stmtProducto.executeQuery();

                List<Producto> productos = new ArrayList<>();
                while (resultProducto.next()) {
                    Producto producto = new Producto();
                    producto.setEstado(resultProducto.getString("estado"));
                    producto.setNombre(resultProducto.getString("nombre"));
                    producto.setProductoId(resultProducto.getInt("productoId"));
                    productos.add(producto);
                }
                pedido.setProductos(productos);

                pedidos.add(pedido);
            }

            cliente.setOrdenes(pedidos);
            clientes.add(cliente);
        }
        jtable.setModel(model);
    }

    @Override
    public void agregarCliente(Cliente cliente, Integer productoId) throws SQLException, IOException {
        Connection con = MysqlDB.getConnection();
        PreparedStatement statement;

        statement = con.prepareStatement("SELECT * FROM cliente where nitCliente = ?");
        statement.setString(1, cliente.getNit());
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            statement = con.prepareStatement("UPDATE cliente SET nombreCliente = ?, direccion = ?, celular = ? where nitCliente = ?");
            statement.setString(1, cliente.getNombreCliente());
            statement.setString(2, cliente.getDireccionFacturacion());
            statement.setString(3, cliente.getTelefonoCliente());
            statement.setString(4, cliente.getNit());
            statement.executeUpdate();
            this.createPedido(cliente, productoId);
            return;
        }

        statement = con.prepareStatement("INSERT INTO cliente (nitCliente, nombreCliente, direccion, celular) VALUES (?, ?, ?, ?)");

        statement.setString(1, cliente.getNit());
        statement.setString(2, cliente.getNombreCliente());
        statement.setString(3, cliente.getDireccionFacturacion());
        statement.setString(4, cliente.getTelefonoCliente());
        statement.executeUpdate();
        this.createPedido(cliente, productoId);
    }

    @Override
    public void guardarClientes() throws IOException {}

    public List<Cliente> getClientes() {
        return clientes;
    }

    private void createPedido(Cliente cliente, Integer productoId) throws SQLException, IOException {
        Connection con = MysqlDB.getConnection();
        PreparedStatement statement = con.prepareStatement("INSERT INTO pedido (nitCliente, fechaOrden, estado) VALUES (?, ?, 'GENERADO')", PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, cliente.getNit());
        statement.setDate(2, new Date(new java.util.Date().getTime()));
        statement.executeUpdate();

        if (statement.executeUpdate() == 0) {
            throw new SQLException("Error creando pedido.");
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            var pedidoId = generatedKeys.getInt(1);
            statement = con.prepareStatement("INSERT INTO pedido_productos (pedidoId, productoId) VALUES (?, ?)");
            statement.setInt(1, pedidoId);
            statement.setInt(2, productoId);
            statement.executeUpdate();
        }
    }
}
