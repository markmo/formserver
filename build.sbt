name := """formserver"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

//scalaVersion := "2.11.1"

scalaVersion := "2.10.4"

resolvers += Resolver.mavenLocal

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.play" %% "play-jdbc" % "2.3.1",
  "com.typesafe.play" %% "play-json" % "2.3.1",
//  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "com.typesafe.play" %% "play-slick" % "0.8.0-RC2",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
//  "com.typesafe.slick" %% "slick" % "2.1.0",
//  "org.slf4j" % "slf4j-nop" % "1.6.4",
//  "com.github.tminglei" %% "slick-pg" % "0.7.0",
  "com.github.tminglei" %% "slick-pg" % "0.6.0-M2",
  "com.github.tminglei" %% "slick-pg_joda-time" % "0.6.0-M2",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.6.0-M2",
  "com.github.tminglei" %% "slick-pg_jts" % "0.6.0-M2",
//  "joda-time" % "joda-time" % "2.5",
  "joda-time" % "joda-time" % "2.3",
//  "org.joda" % "joda-convert" % "1.7",
  "org.joda" % "joda-convert" % "1.5",
  "com.vividsolutions" % "jts" % "1.13"
)

scalacOptions ++= Seq("-deprecation", "-feature",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-language:higherKinds",
  "-language:postfixOps"
)