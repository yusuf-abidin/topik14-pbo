/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

import java.util.Date;

/**
 *
 * @author yusuf
 */
public class Barang {
    protected int kode;
    protected String nama;
    protected int harga, stok;
    protected String lastUpdate;
    


    public Barang(int kode, String nama, int harga, int stok, String lastUpdate) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.lastUpdate = lastUpdate;
    }
    
    public Barang(int kode, String nama, int harga) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
    }

    
   
    public int getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    
}
