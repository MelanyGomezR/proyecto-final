package com.mycompany.proyectofinalmelany;

import java.util.List;

public class Cliente {
    private String nit;
    private String nombreCliente;
    private String direccionFacturacion;
    private String telefonoCliente;

    private List<Pedido> ordenes;

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getDireccionFacturacion() {
        return direccionFacturacion;
    }

    public void setDireccionFacturacion(String direccionFacturacion) {
        this.direccionFacturacion = direccionFacturacion;
    }

    public List<Pedido> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Pedido> ordenes) {
        this.ordenes = ordenes;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
