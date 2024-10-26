package com.mycompany.proyectofinalmelany;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JTable;

public interface InterfazCliente {
    void cargarClientes(JTable table) throws IOException, SQLException;
    void guardarClientes() throws IOException;

    void agregarCliente(Cliente cliente, Integer productoId) throws SQLException, IOException;

    List<Cliente> getClientes();
}
