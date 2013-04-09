name := "play-simple-rest"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "play" %% "play" % "2.1.1",
  "eu.teamon" % "play-navigator_2.10.0" % "0.4.0",
  "org.webjars" % "jquery" % "1.9.1"
)

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "scalajars.org repo" at "http://scalajars.org/repository"
)
