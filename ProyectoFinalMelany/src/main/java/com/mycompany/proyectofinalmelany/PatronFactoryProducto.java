package com.mycompany.proyectofinalmelany;

public class PatronFactoryProducto {
    public static InterfazProducto getFactoryProducto(Integer tipo) {
        switch (tipo) {
            case 1:
                return new ControladorJSONProducto();
            case 2:
                return new ControladorSQLProducto();
            default:
                return null;
        }
    }
}
