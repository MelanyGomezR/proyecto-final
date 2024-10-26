/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.proyectofinalmelany;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.JFrame;

public class Main extends JFrame {
   public static void main(String[] args) throws IOException, SQLException {
        ObjectMapper map = new ObjectMapper();
        URL resource = new URL("file:src/main/resources/mysql.json");
        Map<String, String> credentials = map.readValue(new File(resource.getPath()), Map.class);
        
        int off = 1;
        if (credentials.get("sql").equalsIgnoreCase("on")) {
            off = 2;
        }
        ClienteGUI cliente = new ClienteGUI(off);
        cliente.setVisible(true);
        cliente.loadData();
    }
}
