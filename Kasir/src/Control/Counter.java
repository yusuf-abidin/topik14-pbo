/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import koneksi.Koneksi;

/**
 *
 * @author yusuf
 */
public class Counter {
    private Connection conn;
    private final Koneksi k = new Koneksi();
    private static String user;
    
    public static String countUser(){
        try {
            Connection conn = Koneksi.getConnection();
            String queri = "CALL `countUser`()";
            
            PreparedStatement ps = conn.prepareStatement(queri);

            ResultSet res = ps.executeQuery();
            
            while(res.next()){
                user = res.getString("jumlah_user");
            }
   
        } catch (SQLException e) {
            System.out.println(e);
        }
        return user;
    }
    
    private static String transaksi;
    
    public static String countTransaksi(){
        try {
            Connection conn = Koneksi.getConnection();
            String queri = "CALL `countTransaksi`()";
            
            PreparedStatement ps = conn.prepareStatement(queri);

            ResultSet res = ps.executeQuery();
            
            while(res.next()){
                transaksi = res.getString("jumlah_transaksi");
                transaksi = transaksi +  " " + res.getString("penjualan");
                transaksi = transaksi + " " + res.getString("barang_terjual");
            }
   
        } catch (SQLException e) {
            System.out.println(e);
        }
        return transaksi;
    }
    
}
