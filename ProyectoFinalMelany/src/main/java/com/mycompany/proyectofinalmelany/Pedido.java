package com.mycompany.proyectofinalmelany;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private String nitCliente;

    private List<Producto> productos;
    private Integer pedidoClienteId;
    private Date fecha;
    private String estado;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void agregarProducto(Producto producto) {
        if (productos == null) {
            productos = new ArrayList<>();
        }
        productos.add(producto);
    }

    public void modificarEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPedidoClienteId() {
        return pedidoClienteId;
    }

    public void setPedidoClienteId(Integer pedidoClienteId) {
        this.pedidoClienteId = pedidoClienteId;
    }
}
