name := """play-simple-in-memory-todo-list"""
organization := "org.carlosbello"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.3"

libraryDependencies += guice
