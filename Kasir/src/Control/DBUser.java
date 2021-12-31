/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import POJO.User;
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
public class DBUser {
    private Connection conn;
    private final Koneksi k = new Koneksi();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
    
//    SELECT ALL USER
    public ArrayList<User> getUser() throws SQLException, ParseException{
        ArrayList<User> user = new ArrayList<>();
        
        conn = Koneksi.getConnection();
        
        String query = "SELECT * FROM user";
        
        PreparedStatement ps = conn.prepareStatement(query);
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            int id = Integer.parseInt(rs.getString("id")) ;
            String nama = rs.getString("nama");
            String alamat = rs.getString("alamat");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String role = rs.getString("role");
            String tgl = rs.getString("tgl_pendaftaran");
            User u = new User(id, nama, alamat, username, password, role, tgl);
            user.add(u);
        }
        rs.close();
        ps.close();
        conn.close();
        return user;
    }
    
    
//    INSERT USER
    public boolean inputUser(User u) throws SQLException{
        conn = Koneksi.getConnection();
        String nama = u.getNama();
        String alamat = u.getAlamat();
        String username = u.getUsername();
        String password = u.getPassword();
        String role = u.getRole();
        String tgl = u.getTgl_pendaftaran();
    
        String queri = "INSERT INTO user VALUES (null, '"+nama+"', '"+alamat+"', '"+username+"', '"+password+"', '"+role+"', '"+tgl+"')";
        PreparedStatement ps = conn.prepareStatement(queri);
        
        int rowAffected = ps.executeUpdate();
        
        ps.close();
        conn.close();
        return rowAffected == 1;
         
    }
    
//    UPDATE USER
    public boolean updateUser(User u) throws SQLException{
        conn = Koneksi.getConnection();
        int id = u.getId();
        String nama = u.getNama();
        String alamat = u.getAlamat();
        String username = u.getUsername();
        String password = u.getPassword();
        String role = u.getRole();
        String tgl = u.getTgl_pendaftaran();
        
        String queri = "UPDATE user SET nama ='"+nama+"', alamat='"+alamat+"', username='"+username+"', password = '"+password+"', role = '"+role+"' WHERE id='"+id+"' ";
        PreparedStatement ps = conn.prepareStatement(queri);
        
        int rowAffected = ps.executeUpdate();
        ps.close();
        conn.close();
        return rowAffected == 1;  
    }
    
//    DELETE USER
    public boolean deleteUser(int id) throws SQLException{
        
        conn = Koneksi.getConnection();
        String queri ="DELETE FROM user WHERE id='"+id+"'";
        PreparedStatement ps = conn.prepareStatement(queri);
        
        int rowAffected = ps.executeUpdate();
        ps.close();
        conn.close();
        return rowAffected == 1;  
        
    }
    
//    LOGIN USER
    public ArrayList<User> loginUser(String uname, String pass) throws SQLException, ParseException{
        ArrayList<User> user = new ArrayList<>();
        
        conn = Koneksi.getConnection();
        String query = "SELECT * FROM user WHERE username = ? && password = ?"; 
            
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, uname);
        ps.setString(2, pass);
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            int id = Integer.parseInt(rs.getString("id")) ;
            String nama = rs.getString("nama");
            String alamat = rs.getString("alamat");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String role = rs.getString("role");
            String tgl = rs.getString("tgl_pendaftaran");
            
            User u = new User(id, nama, alamat, username, password, role, tgl);
            user.add(u);
        }
        rs.close();
        ps.close();
        conn.close();
        return user;
    }
}
