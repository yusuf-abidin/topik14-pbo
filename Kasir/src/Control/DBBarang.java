/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import POJO.Barang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import koneksi.Koneksi;

/**
 *
 * @author yusuf
 */
public class DBBarang {
    private Connection conn;
    private final Koneksi k = new Koneksi();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
    
    //    SELECT ALL BARANG
    public ArrayList<Barang> getBarang() throws SQLException, ParseException{
        ArrayList<Barang> barang = new ArrayList<>();
        
        conn = Koneksi.getConnection();
        
        String query = "SELECT * FROM barang";
        
        PreparedStatement ps = conn.prepareStatement(query);
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            int kode = Integer.parseInt(rs.getString("kode"));
            String nama = rs.getString("nama");
            int harga = Integer.parseInt(rs.getString("harga"));
            int stok = Integer.parseInt(rs.getString("stok")) ;
            String tgl = rs.getString("last_update");
            Barang b = new Barang(kode, nama, harga, stok, tgl);
            barang.add(b);
        }
        rs.close();
        ps.close();
        conn.close();
        return barang;

    }
    
    //    INSERT USER
    public boolean inputBarang(Barang b) throws SQLException{
        conn = Koneksi.getConnection();
        int kode = b.getKode();
        String nama = b.getNama();
        int harga = b.getHarga();
        int stok = b.getStok();
        String tgl = b.getLastUpdate();
       
        String queri = "INSERT INTO barang VALUES (null, '"+nama+"', '"+harga+"', '"+stok+"', '"+tgl+"')";
        PreparedStatement ps = conn.prepareStatement(queri);
        
        int rowAffected = ps.executeUpdate();
        
        ps.close();
        conn.close();
        return rowAffected == 1;
         
    }
    
   
//    UPDATE BARANG
    public boolean updateBarang(Barang b) throws SQLException{
        
        conn = Koneksi.getConnection();
        int kode = b.getKode();
        String nama = b.getNama();
        int harga = b.getHarga();
        int stok = b.getStok();
        String tgl = b.getLastUpdate();
        
        String queri = "UPDATE barang SET nama ='"+nama+"', harga='"+harga+"', stok='"+stok+"', last_update = '"+tgl+"'  WHERE kode='"+kode+"' ";
        PreparedStatement ps = conn.prepareStatement(queri);
        
        int rowAffected = ps.executeUpdate();
        ps.close();
        conn.close();
        return rowAffected == 1;  
    }
    
//    DELETE BARANG
    public boolean deleteBarang(int kode) throws SQLException{
        
        conn = k.getConnection();
        String queri ="DELETE FROM barang WHERE kode='"+kode+"'";
        PreparedStatement ps = conn.prepareStatement(queri);
        
        int rowAffected = ps.executeUpdate();
        ps.close();
        conn.close();
        return rowAffected == 1;  
        
    }
    
}
