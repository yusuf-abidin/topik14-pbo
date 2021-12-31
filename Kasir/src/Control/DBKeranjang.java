/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

/**
 *
 * @author yusuf
 */

import POJO.Transaksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import koneksi.Koneksi;

public class DBKeranjang {
    private Connection conn;
    private final Koneksi k = new Koneksi();
    private String total;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");

    //    SELECT ALL Keranjang
    public ArrayList<Transaksi> getKeranjang() throws SQLException{
        ArrayList<Transaksi> keranjang = new ArrayList<>();

        conn = Koneksi.getConnection();

        String query = "SELECT * FROM keranjang";

        PreparedStatement ps = conn.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){

            String id_transaksi = rs.getString("id_transaksi");
            int kodeBarang = Integer.parseInt(rs.getString("kode_barang")) ;
            String nama = rs.getString("nama_barang");
            int harga = Integer.parseInt(rs.getString("harga")) ;
            int jumlah = Integer.parseInt(rs.getString("jumlah")) ;
            int total_harga = Integer.parseInt(rs.getString("total_harga")) ;
            String tgl = rs.getString("tgl_transaksi");

            Transaksi krj = new Transaksi(id_transaksi, kodeBarang, nama, harga, jumlah, total_harga, tgl);
            keranjang.add(krj);
        }
        rs.close();
        ps.close();
        conn.close();
        return keranjang;

    }

//    INSERT KE KERANJANG
    public boolean inputBarang(Transaksi krj) throws SQLException{
        conn = Koneksi.getConnection();

        String idTransaksi = krj.getId_transaksi();
        int kode = krj.getKode();
        String nama = krj.getNama();
        int harga = krj.getHarga();
        int jumlah = krj.getJumlah();
        int totalHarga = krj.getTotalHarga();
        String tglTransaksi = krj.getTgl_transaksi();
                
        String queri = "INSERT INTO keranjang VALUES ('"+idTransaksi+"', '"+kode+"', '"+nama+"', '"+harga+"', '"+jumlah+"', '"+totalHarga+"', '"+tglTransaksi+"')";
        PreparedStatement ps = conn.prepareStatement(queri);

        int rowAffected = ps.executeUpdate();

        ps.close();
        conn.close();
        return rowAffected == 1;

    }

//    RESET Keranjang
    public boolean resetKeranjang() throws SQLException{

        conn = Koneksi.getConnection();
        String queri ="DELETE FROM keranjang";
        PreparedStatement ps = conn.prepareStatement(queri);

        int rowAffected = ps.executeUpdate();
        ps.close();
        conn.close();
        return rowAffected == 1;

    }
    
//    HAPUS DARI KERANJANG
    public boolean hapusFromKeranjang(int kode) throws SQLException{

        conn = Koneksi.getConnection();
        String queri ="DELETE FROM keranjang WHERE kode_barang='"+kode+"'";
        PreparedStatement ps = conn.prepareStatement(queri);

        int rowAffected = ps.executeUpdate();
        ps.close();
        conn.close();
        return rowAffected == 1;

    }
    
//    HITUNG TOTAL HARGA DI KERANJANG
    
    public String hitungHarga(String id_transaksi){
        try {
            Connection conn = Koneksi.getConnection();
            String queri = "SELECT CONCAT(format(SUM(total_harga), 0)) AS 'total' from keranjang WHERE id_transaksi = '"+id_transaksi+"'";
            
            PreparedStatement ps = conn.prepareStatement(queri);

            ResultSet res = ps.executeQuery();
            
            while(res.next()){
                total = res.getString("total");
            }
   
        } catch (SQLException e) {
            System.out.println(e);
        }
        return total;
    }

}
