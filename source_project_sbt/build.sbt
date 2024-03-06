lazy val root = project
  .in(file("."))
  .settings(
    name := "foobar",
    organization := "org.bla",
    scalaVersion := "2.13.13",
    logLevel := Level.Info,
  )
  .enablePlugins(SbtTwirl, PlayLayoutPlugin)

