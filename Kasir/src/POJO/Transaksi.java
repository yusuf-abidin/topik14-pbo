/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

/**
 *
 * @author yusuf
 */
public class Transaksi extends Barang{
    private int id;
    private String id_transaksi;
    private int jumlah, totalHarga;
    private String tgl_transaksi;
    
    public Transaksi(int id, String id_transaksi, int kode, String nama, int harga, int jumlah, int totalHarga, String tgl_transaksi){
        super(kode, nama, harga);
        this.id = id;
        this.id_transaksi = id_transaksi;
        this.jumlah = jumlah;
        this.totalHarga=totalHarga;
        this.tgl_transaksi=tgl_transaksi;
    }
    
    public Transaksi(String id_transaksi, int kode, String nama, int harga, int jumlah, int totalHarga, String tgl_transaksi){
        super(kode, nama, harga);
        this.id_transaksi = id_transaksi;
        this.jumlah = jumlah;
        this.totalHarga=totalHarga;
        this.tgl_transaksi=tgl_transaksi;
    }
    

    public int getId() {
        return id;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }
    
    
}
