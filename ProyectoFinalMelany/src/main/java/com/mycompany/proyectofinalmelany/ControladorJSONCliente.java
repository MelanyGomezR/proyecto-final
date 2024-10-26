package com.mycompany.proyectofinalmelany;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ControladorJSONCliente implements InterfazCliente {
    private static List<Cliente> clientes = new ArrayList<>();
    private static Map<String, String> map = new HashMap<>();

    public void guardarClientes() throws IOException {
        URL url = new URL("file:src/main/resources/db-cliente.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(url.getPath()), clientes);
    }

    public void agregarCliente(Cliente cliente, Integer pedidoId) {
        if (map.get(cliente.getNit()) != null) {
            clientes.forEach(c -> {
                if (c.getNit().equals(cliente.getNit())) {
                    cliente.getOrdenes().forEach(pedido -> c.getOrdenes().add(pedido));
                }
            });
            return;
        }
        clientes.add(cliente);
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void cargarClientes(JTable jtable) throws IOException {
        URL url = new URL("file:src/main/resources/db-cliente.json");

        ObjectMapper mapper = new ObjectMapper();
        Cliente[] clientesTmp = mapper.readValue(new File(url.getPath()), Cliente[].class);
        clientes = new ArrayList<>();
        map = new HashMap<>();

        String[] columnNames = new String[]{"nitCliente", "nombreCliente", "direccion", "celular"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Cliente cliente : clientesTmp) {
            if (map.get(cliente.getNit()) != null) {
                continue;
            }
            map.put(cliente.getNit(), cliente.getNombreCliente());
            clientes.add(cliente);

            Object[] rowData = {cliente.getNit(), cliente.getNombreCliente(), cliente.getDireccionFacturacion(), cliente.getTelefonoCliente()};
            model.addRow(rowData);
        }
        jtable.setModel(model);
    }
}
