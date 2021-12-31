/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import POJO.Transaksi;
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
public class DBTransaksi {
    private Connection conn;
    private final Koneksi k = new Koneksi();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
    
    //    SELECT ALL Transaksi
    public ArrayList<Transaksi> getTransaksi() throws SQLException, ParseException{
        ArrayList<Transaksi> transaksi = new ArrayList<>();
        
        conn = Koneksi.getConnection();
        
        String query = "SELECT * FROM transaksi";
        
        PreparedStatement ps = conn.prepareStatement(query);
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            int id = Integer.parseInt(rs.getString("id")    ) ;
            String id_transaksi = rs.getString("id_transaksi");
            int kodeBarang = Integer.parseInt(rs.getString("kode_barang")) ;
            String nama = rs.getString("nama");
            int harga = Integer.parseInt(rs.getString("harga")) ;
            int jumlah = Integer.parseInt(rs.getString("jumlah")) ;
            int total_harga = Integer.parseInt(rs.getString("total_harga")) ;
            String tgl = rs.getString("tgl_transaksi");
            
            Transaksi tr = new Transaksi(id, id_transaksi, kodeBarang, nama, harga, jumlah, total_harga, tgl);
            transaksi.add(tr);
        }
        rs.close();
        ps.close();
        conn.close();
        return transaksi;

    }
    
//    INSERT KE TRANSAKSI
    public boolean inputTransaksi(Transaksi trx) throws SQLException{
        conn = Koneksi.getConnection();

        String idTransaksi = trx.getId_transaksi();
        int kode = trx.getKode();
        String nama = trx.getNama();
        int harga = trx.getHarga();
        int jumlah = trx.getJumlah();
        int totalHarga = trx.getTotalHarga();
        String tglTransaksi = trx.getTgl_transaksi();
                
        String queri = "INSERT INTO transaksi VALUES (null, '"+idTransaksi+"', '"+kode+"', '"+nama+"', '"+harga+"', '"+jumlah+"', '"+totalHarga+"', '"+tglTransaksi+"')";
        PreparedStatement ps = conn.prepareStatement(queri);

        int rowAffected = ps.executeUpdate();

        ps.close();
        conn.close();
        return rowAffected == 1;

    }
    
}
