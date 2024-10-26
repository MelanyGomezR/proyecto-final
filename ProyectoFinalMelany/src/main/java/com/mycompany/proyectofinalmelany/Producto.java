package com.mycompany.proyectofinalmelany;

public class Producto {
    private Integer productoId;
    private String nombre;
    private Integer cantidad;

    private String estado;

    public Producto() {
    }

    public Producto(Integer idProducto, String nombre, Integer cantidad, String estado) {
        this.productoId = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
