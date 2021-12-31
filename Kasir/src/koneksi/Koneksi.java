/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koneksi;

/**
 *
 * @author yusuf
 */

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {
    private static Connection connection;

    public static Connection getConnection(){
        MysqlDataSource dataSource = new MysqlDataSource();
        String DB_URL = "jdbc:mysql://localhost:3306/kasir";
        String DB_USERNAME = "root";
        String DB_PASSWORD = "";

        dataSource.setUrl(DB_URL);
        dataSource.setUser(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        
        try {               
            
            connection = dataSource.getConnection();
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return connection;
    }
}
