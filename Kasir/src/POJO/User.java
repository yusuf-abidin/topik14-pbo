/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

/**
 *
 * @author yusuf
 */
public class User {
    private int id;
    private String nama, alamat, username, password, role;
    private String tgl_pendaftaran;

    public User(int id, String nama, String alamat, String username, String password, String role, String tgl_pendaftaran) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.username = username;
        this.password = password;
        this.role = role;
        this.tgl_pendaftaran = tgl_pendaftaran;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getTgl_pendaftaran() {
        return tgl_pendaftaran;
    }
    
    

    
    
    
}
