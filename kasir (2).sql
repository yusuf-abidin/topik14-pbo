-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 31 Des 2021 pada 14.00
-- Versi server: 10.4.22-MariaDB
-- Versi PHP: 8.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kasir`
--

DELIMITER $$
--
-- Prosedur
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `countTransaksi` ()  SELECT COUNT(DISTINCT (id_transaksi)) as 'jumlah_transaksi', CONCAT('Rp ', format (SUM(total_harga), 0))  AS 'penjualan', SUM(jumlah) AS 'barang_terjual'  FROM transaksi$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `countUser` ()  SELECT COUNT(id) as 'jumlah_user' FROM user$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang`
--

CREATE TABLE `barang` (
  `kode` int(6) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `harga` int(10) NOT NULL,
  `stok` int(10) NOT NULL,
  `last_update` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `barang`
--

INSERT INTO `barang` (`kode`, `nama`, `harga`, `stok`, `last_update`) VALUES
(1, 'Pensil 2B', 3500, 120, '2021-12-31'),
(2, 'Buku Tulis KIKI', 4000, 96, '2021-12-31'),
(3, 'Penghapus', 4000, 108, '2021-12-31');

-- --------------------------------------------------------

--
-- Struktur dari tabel `keranjang`
--

CREATE TABLE `keranjang` (
  `id_transaksi` char(9) NOT NULL,
  `kode_barang` int(6) NOT NULL,
  `nama_barang` varchar(100) NOT NULL,
  `harga` int(10) NOT NULL,
  `jumlah` int(10) NOT NULL,
  `total_harga` int(10) NOT NULL,
  `tgl_transaksi` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi`
--

CREATE TABLE `transaksi` (
  `id` int(5) NOT NULL,
  `id_transaksi` char(9) NOT NULL,
  `kode_barang` int(6) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `harga` int(10) NOT NULL,
  `jumlah` int(10) NOT NULL,
  `total_harga` int(10) NOT NULL,
  `tgl_transaksi` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `transaksi`
--

INSERT INTO `transaksi` (`id`, `id_transaksi`, `kode_barang`, `nama`, `harga`, `jumlah`, `total_harga`, `tgl_transaksi`) VALUES
(1, 'GVDF38G4D', 1, 'Bakso Bakar', 3500, 2, 7000, '2021-12-30'),
(2, 'GVDF38G4D', 2, 'Nila Goreng', 5000, 2, 10000, '2021-12-30'),
(3, 'FD34FG78H', 2, 'Nila Goreng', 5000, 2, 10000, '2021-12-30'),
(4, 'NTVELRL9H', 1, 'Pensil 2B', 3500, 2, 7000, '2021-12-30'),
(5, 'NTVELRL9H', 3, 'Penghapus', 4000, 4, 16000, '2021-12-30'),
(6, 'J77DCRXQH', 2, 'Buku Tulis KIKI', 4000, 5, 20000, '2021-12-31'),
(7, 'J77DCRXQH', 3, 'Penghapus', 4000, 1, 4000, '2021-12-31'),
(8, 'M7HSNPWIY', 1, 'Pensil 2B', 3500, 1, 3500, '2021-12-31'),
(9, 'GFY0V8P4G', 2, 'Buku Tulis KIKI', 4000, 20, 80000, '2021-12-31'),
(10, 'Z4CQJDHMG', 1, 'Pensil 2B', 3500, 10, 35000, '2021-12-31'),
(11, 'LSFF3ZKOJ', 2, 'Buku Tulis KIKI', 4000, 1, 4000, '2021-12-31'),
(12, 'HWFBSICM5', 3, 'Penghapus', 4000, 1, 4000, '2021-12-31'),
(13, 'FMGOR9CV1', 3, 'Penghapus', 4000, 1, 4000, '2021-12-31');

--
-- Trigger `transaksi`
--
DELIMITER $$
CREATE TRIGGER `updateStok` AFTER INSERT ON `transaksi` FOR EACH ROW BEGIN
UPDATE barang SET stok = stok - NEW.jumlah, last_update = NOW() WHERE kode = NEW.kode_barang;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `id` int(4) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `alamat` varchar(200) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `role` varchar(10) NOT NULL,
  `tgl_pendaftaran` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`id`, `nama`, `alamat`, `username`, `password`, `role`, `tgl_pendaftaran`) VALUES
(1, 'Admin', '', 'admin', 'admin', 'Admin', '2021-12-19'),
(3, 'Yusuf', 'Wonogiri', 'yusuf', 'yusuf', 'Petugas', '2021-12-19');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`kode`);

--
-- Indeks untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `barang`
--
ALTER TABLE `barang`
  MODIFY `kode` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT untuk tabel `user`
--
ALTER TABLE `user`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
