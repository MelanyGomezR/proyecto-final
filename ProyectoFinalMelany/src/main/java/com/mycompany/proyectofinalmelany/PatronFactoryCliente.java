package com.mycompany.proyectofinalmelany;

public class PatronFactoryCliente {
    public static InterfazCliente getFactoryCliente(Integer tipo) {
        if (tipo == 1) {
            return new ControladorJSONCliente();
        } else if (tipo == 2) {
            return new ControladorSQLCliente();
        } else {
            return null;
        }
    }
}
