name := """play-simple-cached-file-stored-todo-list"""
organization := "org.carlosbello"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  guice,
  caffeine
)
