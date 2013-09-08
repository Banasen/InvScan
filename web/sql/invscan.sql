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
  chestX int(11) NULL,
  chestY int(11) NULL,
  chestZ int(11) NULL,
  chestName text NOT NULL,
  id int(11) NULL,
  damage int(11) NULL,
  rawName text NOT NULL,
  `name` text NOT NULL,
  quantity int(11) NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for Table 'chests'
--

CREATE TABLE IF NOT EXISTS chests (
  x int(11) NULL,
  y int(11) NULL,
  z int(11) NULL,
  playerName text NULL,
  `name` text NOT NULL,
  lastUpdated TIMESTAMP(11) DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for Table 'playerItems'
--

CREATE TABLE IF NOT EXISTS playerItems (
  playerName text NOT NULL,
  slot int(11) NOT NULL,
  id int(11) NULL,
  damage int(11) NULL,
  rawName text NOT NULL,
  `name` text NOT NULL,
  quantity int(11) NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for Table 'players'
--

CREATE TABLE IF NOT EXISTS players (
  `name` VARCHAR(255) NOT NULL,
  x int(11) NULL,
  y int(11) NULL,
  z int(11) NULL,
  lastUpdated TIMESTAMP(11) DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY playerName (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
