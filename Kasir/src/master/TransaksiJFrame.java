/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package master;

import Control.DBBarang;
import Control.DBKeranjang;
import Control.DBTransaksi;
import POJO.Barang;
import POJO.Transaksi;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author yusuf
 */
public class TransaksiJFrame extends javax.swing.JFrame {
    private final DefaultTableModel model = new DefaultTableModel();
    private final DefaultTableModel modelKeranjang = new DefaultTableModel();
    private final TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(model);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
    private String totalHarga, randomString;
    private int hargaSemuanya;
    private DBTransaksi koneksiTransaksi = new DBTransaksi();
    private DBBarang koneksiBarang = new DBBarang();
    private DBKeranjang koneksiKeranjang = new DBKeranjang();
    
    
     Timer t ;
    SimpleDateFormat st ;
    public void jam(){
        
        t = new Timer(0, (ActionEvent e) -> {

            Date dt  =new Date();
            st = new SimpleDateFormat("hh:mm:ss a");
            String tt = st.format(dt);
            labelJam.setText(tt);
        });
        t.start();
    }
  
    //    Tanggal
    public void tanggal(){
        
        Date d  =new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");

        String dd = sdf.format(d);
        labelTanggal.setText(dd);
        
     }
     private void createTableBarang() throws ParseException{
        model.addColumn("Kode");
        model.addColumn("Nama");
        model.addColumn("Harga");
        model.addColumn("Stok");
        model.addColumn("Last Update");

        tabelBarang.setModel(model);
        tabelBarang.setRowSorter(rowSorter);
        
        tabelBarang.getColumnModel().getColumn(0).setMaxWidth(70);
        
//      Sembunyikan Kolom Stok
        tabelBarang.getColumnModel().getColumn(3).setWidth(0);
        tabelBarang.getColumnModel().getColumn(3).setMinWidth(0);
        tabelBarang.getColumnModel().getColumn(3).setMaxWidth(0); 
//      Sembunyikan Kolom Last Update
        tabelBarang.getColumnModel().getColumn(4).setWidth(0);
        tabelBarang.getColumnModel().getColumn(4).setMinWidth(0);
        tabelBarang.getColumnModel().getColumn(4).setMaxWidth(0); 

        show_barang();
    }
     
    private void createTabelKeranjang() throws ParseException{
        modelKeranjang.addColumn("Kode");
        modelKeranjang.addColumn("Nama Barang");
        modelKeranjang.addColumn("Harga");
        modelKeranjang.addColumn("Jumlah");
        modelKeranjang.addColumn("Total Harga");
       
        tabelKeranjang.setModel(modelKeranjang);
        
        tabelKeranjang.getColumnModel().getColumn(0).setMaxWidth(70); 
        tabelKeranjang.getColumnModel().getColumn(3).setMaxWidth(70);
        
        showKeranjang();
        
    } 
     
     private void show_barang() throws ParseException{
        
        model.setRowCount(0);
        try {
            ArrayList<Barang> barang = koneksiBarang.getBarang();
            
            for(Barang b : barang){
                model.addRow(new Object[]{
                b.getKode(),
                b.getNama(),
                b.getHarga(),
                b.getStok(),
                b.getLastUpdate()
                });
                
            }
        } catch (SQLException e) {
           JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
     
      private void showKeranjang() throws ParseException{
        
        modelKeranjang.setRowCount(0);
        try {
            ArrayList<Transaksi> transaksi = koneksiKeranjang.getKeranjang();
            
            for(Transaksi t : transaksi){
                modelKeranjang.addRow(new Object[]{
                t.getKode(),
                t.getNama(),
                t.getHarga(),
                t.getJumlah(),
                t.getTotalHarga(),
                });
                
            }
        } catch (SQLException e) {
           JOptionPane.showMessageDialog(this, e.getMessage());
        }
        
        hitungTotal();
    }

    
    private void search(String text){
        RowFilter<DefaultTableModel, Object> sorter = null;
        if (text.trim().length() == 0) {
        rowSorter.setRowFilter(null);
        } else {
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }
    
    private void subTotal(){
        try{
            int harga = Integer.parseInt(txtHarga.getText());
            int jumlah = Integer.parseInt(txtJumlah.getText());

            int subtotal = (harga*jumlah);
            String subTotalString = Integer.toString(subtotal);
            txttotalHarga.setText(subTotalString);
        }catch (NumberFormatException  ex){
            if(txtNama.getText().equalsIgnoreCase("")){
                
                txtJumlah.setText("");
            }else if(txtJumlah.getText().equals("")){
                
            }else{
                JOptionPane.showMessageDialog(null, "Only Number");
                txtJumlah.setText("1");
                subTotal();
            }
            
        }
    }
    
    private void randomString(){
        randomString = Control.RandomString.generateString();
    }
    
    private void hitungTotal(){
        try{
            totalHarga = koneksiKeranjang.hitungHarga(randomString).replaceAll(",", ".");
            labelTotalHarga.setText(totalHarga);
        }catch(Exception e){
            labelTotalHarga.setText("0");
        }
        
        
    }
    
    private void addToChart(){
        try{
            int select = tabelBarang.getSelectedRow();
            
            String id_transaksi = randomString;
            int kodeBarang = Integer.parseInt(tabelBarang.getValueAt(select, 0).toString());
            String nama = txtNama.getText();
            int harga = Integer.parseInt(txtHarga.getText());
            int jumlah = Integer.parseInt(txtJumlah.getText());
            int totalHarga = Integer.parseInt(txttotalHarga.getText());
            Date d  =new Date();
            String tgl = sdf.format(d);
            
            Transaksi trx = new Transaksi(id_transaksi, kodeBarang, nama, harga, jumlah, totalHarga, tgl);
            
            koneksiKeranjang.inputBarang(trx);
            
            
            resetForm();
            showKeranjang();
            txtSearch.requestFocus();
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Pilih Barang di Tabel");
        }
        
    }
    
    private void bayar(){
        
        try{
            int bayar = Integer.parseInt(txtPembayaran.getText());
            int harusBayar = Integer.parseInt(totalHarga.replace(".", ""));
            int kembalian;
            if(bayar<harusBayar){
                JOptionPane.showMessageDialog(this, "Uang Tidak Cukup!");
            }else{
                kembalian = bayar -harusBayar;
                labelNominalKembalian.setText("Rp " + kembalian);
                
                try{
                    ArrayList<Transaksi> transaksi = koneksiKeranjang.getKeranjang();
                    for(Transaksi t : transaksi){
                        String idtrx = t.getId_transaksi();
                        int kode = t.getKode();
                        String nama = t.getNama();
                        int harga = t.getHarga();
                        int jumlah = t.getJumlah();
                        int totalharga = t.getTotalHarga();
                        String tgl = t.getTgl_transaksi();
                        
                        Transaksi trx = new Transaksi(1, idtrx, kode, nama, harga, jumlah, totalharga, tgl);
                        try{
                            koneksiTransaksi.inputTransaksi(trx);
                        }catch(SQLException e){
                            JOptionPane.showMessageDialog(this, e);
                        }
                        
                    }
                    
                    JOptionPane.showMessageDialog(this, "Berhasil");
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(this, "Error input Transaksi");
                }

                
                
            }
        }catch(Exception e){
            if (txtPembayaran.getText().equals("") || txtPembayaran.getText().equals("0")) {
                JOptionPane.showMessageDialog(this, "Masukkan Nominal Pembayaran");
            }else
            JOptionPane.showMessageDialog(this, "Only Number");
        }
        
    }
    
    private void resetKeranjang(){
        try {
            koneksiKeranjang.resetKeranjang();
            showKeranjang();
            txtPembayaran.setText("");
            labelNominalKembalian.setText("");
        } catch (SQLException ex) {
            Logger.getLogger(TransaksiJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TransaksiJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        randomString();
    }
    
    private void resetForm(){
        txtNama.setText("");
        txtHarga.setText("");
        txtJumlah.setText("");
        txttotalHarga.setText("");
    }

    /**
     * Creates new form Transaksi
     */
    public TransaksiJFrame() throws ParseException {
        initComponents();
        
        createTableBarang();
        createTabelKeranjang();
        resetKeranjang();
        jam();
        tanggal();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelKiri = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelBarang = new javax.swing.JTable();
        labelNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        separatorNama = new javax.swing.JSeparator();
        labelHarga = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        separatorHarga = new javax.swing.JSeparator();
        labelJumlah = new javax.swing.JLabel();
        separatorjumlah = new javax.swing.JSeparator();
        txtJumlah = new javax.swing.JTextField();
        labelSubHarga = new javax.swing.JLabel();
        separatorTotalHarga = new javax.swing.JSeparator();
        txttotalHarga = new javax.swing.JTextField();
        separatorSearch = new javax.swing.JSeparator();
        labelClear = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        labelSearch = new javax.swing.JLabel();
        btnResetForm = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        panelKanan = new javax.swing.JPanel();
        panelTotalHarga = new javax.swing.JPanel();
        labelRP = new javax.swing.JLabel();
        labelTotalHarga = new javax.swing.JLabel();
        labeljudulTotalHarga = new javax.swing.JLabel();
        separatorKanan = new javax.swing.JSeparator();
        separatorKiri = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKeranjang = new javax.swing.JTable();
        btnResetKeranjang = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        panelPembayaran = new javax.swing.JPanel();
        labelPembayaran = new javax.swing.JLabel();
        txtPembayaran = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        labelKembalian = new javax.swing.JLabel();
        labelNominalKembalian = new javax.swing.JLabel();
        btnBayar = new javax.swing.JButton();
        labelJam = new javax.swing.JLabel();
        labelTanggal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelKiri.setBackground(new java.awt.Color(3, 26, 49));

        tabelBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelBarangMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelBarang);

        labelNama.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        labelNama.setForeground(new java.awt.Color(255, 255, 255));
        labelNama.setText("Nama");

        txtNama.setEditable(false);
        txtNama.setBackground(new java.awt.Color(3, 26, 49));
        txtNama.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txtNama.setForeground(new java.awt.Color(255, 255, 255));
        txtNama.setBorder(null);
        txtNama.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtNama.setMinimumSize(new java.awt.Dimension(1, 30));
        txtNama.setPreferredSize(new java.awt.Dimension(1, 30));
        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });
        txtNama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaKeyPressed(evt);
            }
        });

        labelHarga.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        labelHarga.setForeground(new java.awt.Color(255, 255, 255));
        labelHarga.setText("Harga");

        txtHarga.setEditable(false);
        txtHarga.setBackground(new java.awt.Color(3, 26, 49));
        txtHarga.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txtHarga.setForeground(new java.awt.Color(255, 255, 255));
        txtHarga.setBorder(null);
        txtHarga.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtHarga.setMinimumSize(new java.awt.Dimension(1, 30));
        txtHarga.setPreferredSize(new java.awt.Dimension(1, 30));
        txtHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHargaActionPerformed(evt);
            }
        });
        txtHarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHargaKeyPressed(evt);
            }
        });

        labelJumlah.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        labelJumlah.setForeground(new java.awt.Color(255, 255, 255));
        labelJumlah.setText("Jumlah");

        txtJumlah.setBackground(new java.awt.Color(3, 26, 49));
        txtJumlah.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txtJumlah.setForeground(new java.awt.Color(255, 255, 255));
        txtJumlah.setBorder(null);
        txtJumlah.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtJumlah.setMinimumSize(new java.awt.Dimension(1, 30));
        txtJumlah.setPreferredSize(new java.awt.Dimension(1, 30));
        txtJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJumlahActionPerformed(evt);
            }
        });
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJumlahKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
        });

        labelSubHarga.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        labelSubHarga.setForeground(new java.awt.Color(255, 255, 255));
        labelSubHarga.setText("Total Harga");

        txttotalHarga.setEditable(false);
        txttotalHarga.setBackground(new java.awt.Color(3, 26, 49));
        txttotalHarga.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txttotalHarga.setForeground(new java.awt.Color(255, 255, 255));
        txttotalHarga.setBorder(null);
        txttotalHarga.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txttotalHarga.setMinimumSize(new java.awt.Dimension(1, 30));
        txttotalHarga.setPreferredSize(new java.awt.Dimension(1, 30));
        txttotalHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalHargaActionPerformed(evt);
            }
        });
        txttotalHarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttotalHargaKeyPressed(evt);
            }
        });

        labelClear.setBackground(new java.awt.Color(28, 42, 57));
        labelClear.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelClear.setForeground(new java.awt.Color(255, 255, 255));
        labelClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClear.setText("X");
        labelClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelClearMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labelClearMouseEntered(evt);
            }
        });

        txtSearch.setBackground(new java.awt.Color(3, 26, 49));
        txtSearch.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txtSearch.setForeground(new java.awt.Color(255, 255, 255));
        txtSearch.setBorder(null);
        txtSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtSearch.setMinimumSize(new java.awt.Dimension(1, 30));
        txtSearch.setPreferredSize(new java.awt.Dimension(1, 30));
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        labelSearch.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        labelSearch.setForeground(new java.awt.Color(255, 255, 255));
        labelSearch.setText("Search");

        btnResetForm.setBackground(new java.awt.Color(204, 153, 0));
        btnResetForm.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnResetForm.setForeground(new java.awt.Color(255, 255, 255));
        btnResetForm.setText("Reset");
        btnResetForm.setBorder(null);
        btnResetForm.setContentAreaFilled(false);
        btnResetForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetForm.setOpaque(true);
        btnResetForm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnResetFormMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnResetFormMouseExited(evt);
            }
        });
        btnResetForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetFormActionPerformed(evt);
            }
        });

        btnTambah.setBackground(new java.awt.Color(26, 177, 136));
        btnTambah.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.setBorder(null);
        btnTambah.setContentAreaFilled(false);
        btnTambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambah.setOpaque(true);
        btnTambah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTambahMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTambahMouseExited(evt);
            }
        });
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnBack.setBackground(new java.awt.Color(0, 159, 201));
        btnBack.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.setBorder(null);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.setOpaque(true);
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBackMouseExited(evt);
            }
        });
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        panelKanan.setBackground(new java.awt.Color(28, 42, 57));

        panelTotalHarga.setBackground(new java.awt.Color(0, 51, 102));
        panelTotalHarga.setMaximumSize(new java.awt.Dimension(360, 32767));
        panelTotalHarga.setMinimumSize(new java.awt.Dimension(360, 99));
        panelTotalHarga.setPreferredSize(new java.awt.Dimension(360, 99));

        labelRP.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 48)); // NOI18N
        labelRP.setForeground(new java.awt.Color(255, 255, 255));
        labelRP.setText("Rp.");

        labelTotalHarga.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 48)); // NOI18N
        labelTotalHarga.setForeground(new java.awt.Color(255, 255, 255));
        labelTotalHarga.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTotalHarga.setText("90.000.000");

        labeljudulTotalHarga.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        labeljudulTotalHarga.setForeground(new java.awt.Color(255, 255, 255));
        labeljudulTotalHarga.setText("Total Harga");

        javax.swing.GroupLayout panelTotalHargaLayout = new javax.swing.GroupLayout(panelTotalHarga);
        panelTotalHarga.setLayout(panelTotalHargaLayout);
        panelTotalHargaLayout.setHorizontalGroup(
            panelTotalHargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalHargaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTotalHargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTotalHargaLayout.createSequentialGroup()
                        .addComponent(separatorKiri, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labeljudulTotalHarga)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(separatorKanan))
                    .addGroup(panelTotalHargaLayout.createSequentialGroup()
                        .addComponent(labelRP, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelTotalHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelTotalHargaLayout.setVerticalGroup(
            panelTotalHargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalHargaLayout.createSequentialGroup()
                .addGroup(panelTotalHargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labeljudulTotalHarga)
                    .addGroup(panelTotalHargaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelTotalHargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(separatorKanan, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(separatorKiri, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelTotalHargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelTotalHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelRP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabelKeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabelKeranjang);

        btnResetKeranjang.setBackground(new java.awt.Color(204, 153, 0));
        btnResetKeranjang.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnResetKeranjang.setForeground(new java.awt.Color(255, 255, 255));
        btnResetKeranjang.setText("Reset");
        btnResetKeranjang.setBorder(null);
        btnResetKeranjang.setContentAreaFilled(false);
        btnResetKeranjang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetKeranjang.setOpaque(true);
        btnResetKeranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnResetKeranjangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnResetKeranjangMouseExited(evt);
            }
        });
        btnResetKeranjang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKeranjangActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 26, 26));
        btnDelete.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.setBorder(null);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setOpaque(true);
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDeleteMouseExited(evt);
            }
        });
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        panelPembayaran.setBackground(new java.awt.Color(0, 102, 102));

        labelPembayaran.setBackground(new java.awt.Color(255, 255, 255));
        labelPembayaran.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        labelPembayaran.setForeground(new java.awt.Color(255, 255, 255));
        labelPembayaran.setText("Pembayaran");

        txtPembayaran.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtPembayaran.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPembayaran.setBorder(null);
        txtPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPembayaranActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Rp");

        javax.swing.GroupLayout panelPembayaranLayout = new javax.swing.GroupLayout(panelPembayaran);
        panelPembayaran.setLayout(panelPembayaranLayout);
        panelPembayaranLayout.setHorizontalGroup(
            panelPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPembayaranLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPembayaranLayout.createSequentialGroup()
                        .addComponent(labelPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelPembayaranLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))))
        );
        panelPembayaranLayout.setVerticalGroup(
            panelPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPembayaranLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelPembayaran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPembayaran))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));

        labelKembalian.setBackground(new java.awt.Color(255, 255, 255));
        labelKembalian.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        labelKembalian.setForeground(new java.awt.Color(255, 255, 255));
        labelKembalian.setText("Kembalian");

        labelNominalKembalian.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        labelNominalKembalian.setForeground(new java.awt.Color(255, 255, 255));
        labelNominalKembalian.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelNominalKembalian.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelNominalKembalian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(labelKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelKembalian)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNominalKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        btnBayar.setBackground(new java.awt.Color(26, 177, 136));
        btnBayar.setFont(new java.awt.Font("Century Gothic", 0, 16)); // NOI18N
        btnBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar.setText("BAYAR");
        btnBayar.setBorder(null);
        btnBayar.setContentAreaFilled(false);
        btnBayar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBayar.setOpaque(true);
        btnBayar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBayarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBayarMouseExited(evt);
            }
        });
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });

        labelJam.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        labelJam.setForeground(new java.awt.Color(255, 255, 255));
        labelJam.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelJam.setText("Label Jam");

        labelTanggal.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        labelTanggal.setForeground(new java.awt.Color(255, 255, 255));
        labelTanggal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTanggal.setText("Label Tanggal");

        javax.swing.GroupLayout panelKananLayout = new javax.swing.GroupLayout(panelKanan);
        panelKanan.setLayout(panelKananLayout);
        panelKananLayout.setHorizontalGroup(
            panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKananLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addGroup(panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelKananLayout.createSequentialGroup()
                        .addGroup(panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelPembayaran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelJam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelTanggal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKananLayout.createSequentialGroup()
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnResetKeranjang, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnBayar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(42, 42, 42))
        );
        panelKananLayout.setVerticalGroup(
            panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKananLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(panelTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnResetKeranjang, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelKananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelKananLayout.createSequentialGroup()
                        .addComponent(labelJam, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(79, 79, 79))
        );

        javax.swing.GroupLayout panelKiriLayout = new javax.swing.GroupLayout(panelKiri);
        panelKiri.setLayout(panelKiriLayout);
        panelKiriLayout.setHorizontalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(panelKiriLayout.createSequentialGroup()
                            .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtJumlah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelNama, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(separatorjumlah, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelJumlah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(separatorNama, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelHarga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(separatorHarga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(separatorTotalHarga, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txttotalHarga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelSubHarga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnTambah, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnResetForm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelClear, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(separatorSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(panelKanan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelKiriLayout.setVerticalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(labelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(separatorSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelNama)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separatorNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelHarga)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separatorHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelJumlah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(separatorjumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelSubHarga)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txttotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addComponent(btnResetForm, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2)
                .addComponent(separatorTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addComponent(panelKanan, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelKiri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelKiri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    private void txtNamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaKeyPressed

    private void txtHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaActionPerformed

    private void txtHargaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaKeyPressed

    private void txtJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJumlahActionPerformed

    private void txtJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode()== KeyEvent.VK_ENTER && !txtJumlah.equals("")){
             addToChart();
         }
    }//GEN-LAST:event_txtJumlahKeyPressed

    private void txttotalHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalHargaActionPerformed

    private void txttotalHargaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttotalHargaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalHargaKeyPressed

    private void labelClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelClearMouseClicked
        // TODO add your handling code here:
        txtSearch.setText("");
        search("");
    }//GEN-LAST:event_labelClearMouseClicked

    private void labelClearMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelClearMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_labelClearMouseEntered

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchKeyPressed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        String text = txtSearch.getText();

        search(text);
    }//GEN-LAST:event_txtSearchKeyReleased

    private void tabelBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelBarangMouseClicked
        // TODO add your handling code here:
        
        int select = tabelBarang.getSelectedRow();
        
        String nama = tabelBarang.getValueAt(select, 1).toString();
        txtNama.setText(nama);
        
        String harga = tabelBarang.getValueAt(select, 2).toString();
        txtHarga.setText(harga);
        txttotalHarga.setText(harga);
        
        txtJumlah.setText("1");
        txtJumlah.requestFocus();
        
    }//GEN-LAST:event_tabelBarangMouseClicked

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        // TODO add your handling code here:  
        subTotal();
        
    }//GEN-LAST:event_txtJumlahKeyReleased

    private void btnResetKeranjangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetKeranjangMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetKeranjangMouseEntered

    private void btnResetKeranjangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetKeranjangMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetKeranjangMouseExited

    private void btnResetKeranjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKeranjangActionPerformed
        
            // TODO add your handling code here:
        resetKeranjang();
        tabelBarang.clearSelection();
        txtSearch.requestFocus();
    }//GEN-LAST:event_btnResetKeranjangActionPerformed

    private void btnResetFormMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetFormMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetFormMouseEntered

    private void btnResetFormMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetFormMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetFormMouseExited

    private void btnResetFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetFormActionPerformed
        // TODO add your handling code here:
        resetForm();
        txtSearch.requestFocus();
        tabelBarang.clearSelection();
    }//GEN-LAST:event_btnResetFormActionPerformed

    private void btnTambahMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTambahMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTambahMouseEntered

    private void btnTambahMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTambahMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTambahMouseExited

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        addToChart();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteMouseEntered

    private void btnDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteMouseExited

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        try{
            int select = tabelKeranjang.getSelectedRow();
        
            int kode = Integer.parseInt(tabelKeranjang.getValueAt(select, 0).toString());

            try {
                koneksiKeranjang.hapusFromKeranjang(kode);
            } catch (SQLException ex) {
                Logger.getLogger(TransaksiJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            showKeranjang();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Pilih Barang");
        }
        
        
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPembayaranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPembayaranActionPerformed

    private void btnBayarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBayarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBayarMouseEntered

    private void btnBayarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBayarMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBayarMouseExited

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        // TODO add your handling code here:
        bayar();
    }//GEN-LAST:event_btnBayarActionPerformed

    private void btnBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackMouseEntered

    private void btnBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackMouseExited

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        new MenuJFrame().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransaksiJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransaksiJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransaksiJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransaksiJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TransaksiJFrame().setVisible(true);
                } catch (ParseException ex) {
                    Logger.getLogger(TransaksiJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnResetForm;
    private javax.swing.JButton btnResetKeranjang;
    private javax.swing.JButton btnTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelClear;
    private javax.swing.JLabel labelHarga;
    private javax.swing.JLabel labelJam;
    private javax.swing.JLabel labelJumlah;
    private javax.swing.JLabel labelKembalian;
    private javax.swing.JLabel labelNama;
    private javax.swing.JLabel labelNominalKembalian;
    private javax.swing.JLabel labelPembayaran;
    private javax.swing.JLabel labelRP;
    private javax.swing.JLabel labelSearch;
    private javax.swing.JLabel labelSubHarga;
    private javax.swing.JLabel labelTanggal;
    private javax.swing.JLabel labelTotalHarga;
    private javax.swing.JLabel labeljudulTotalHarga;
    private javax.swing.JPanel panelKanan;
    private javax.swing.JPanel panelKiri;
    private javax.swing.JPanel panelPembayaran;
    private javax.swing.JPanel panelTotalHarga;
    private javax.swing.JSeparator separatorHarga;
    private javax.swing.JSeparator separatorKanan;
    private javax.swing.JSeparator separatorKiri;
    private javax.swing.JSeparator separatorNama;
    private javax.swing.JSeparator separatorSearch;
    private javax.swing.JSeparator separatorTotalHarga;
    private javax.swing.JSeparator separatorjumlah;
    private javax.swing.JTable tabelBarang;
    private javax.swing.JTable tabelKeranjang;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtPembayaran;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txttotalHarga;
    // End of variables declaration//GEN-END:variables
}
