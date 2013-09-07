-- phpMyAdmin SQL Dump
-- version 4.0.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 07. Sep 2013 um 13:07
-- Server Version: 5.0.95
-- PHP-Version: 5.2.17

-- Changes from Banane9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: 'invscan'
--
CREATE DATABASE IF NOT EXISTS invscan DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci; -- Swedish server ... doesn't matter
USE invscan;

-- --------------------------------------------------------

--
-- Table structure for Table 'chestItems'
--

CREATE TABLE IF NOT EXISTS chestItems (
  chestX int NOT NULL,
  chestY int NOT NULL,
  chestZ int NOT NULL,
  id int NOT NULL,
  damage int NOT NULL,
  rawName text NOT NULL,
  `name` text NOT NULL,
  quantity int NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for Table 'chests'
--

CREATE TABLE IF NOT EXISTS chests (
  x int NOT NULL,
  y int NOT NULL,
  z int NOT NULL,
  playerName text NOT NULL,
  `name` text NOT NULL,
  lastUpdated int NOT NULL
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for Table 'playerItems'
--

CREATE TABLE IF NOT EXISTS playerItems (
  playerName text NOT NULL,
  slot int NOT NULL,
  id int NOT NULL,
  damage int NOT NULL,
  rawName text NOT NULL,
  `name` text NOT NULL,
  quantity int NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for Table 'players'
--

CREATE TABLE IF NOT EXISTS players (
  `name` text NOT NULL,
  x int NOT NULL,
  y int NOT NULL,
  z int NOT NULL,
  lastUpdated int NOT NULL,
  UNIQUE KEY playerName (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
