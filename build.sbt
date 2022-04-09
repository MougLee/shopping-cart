import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "dev.mougli"
ThisBuild / organizationName := "Mougli"

ThisBuild / evictionErrorLevel := Level.Warn
ThisBuild / scalafixDependencies += Libraries.organizeImports

lazy val core = (project in file("modules/core"))
//  .enablePlugins(DockerPlugin)
//  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "shopping-cart-core",
//    Docker / packageName := "shopping-cart",
    scalacOptions ++= List("-Ymacro-annotations", "-Yrangepos", "-Wconf:cat=unused:info"),
//    scalafmtOnCompile := true,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    Defaults.itSettings,
//    scalafixCommonSettings,
//    dockerBaseImage := "openjdk:11-jre-slim-buster",
//    dockerExposedPorts ++= Seq(8080),
//    makeBatScripts := Seq(),
//    dockerUpdateLatest := true,
    libraryDependencies ++= Seq(
      CompilerPlugin.kindProjector,
      CompilerPlugin.betterMonadicFor,
      CompilerPlugin.semanticDB,
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.catsRetry,
      Libraries.circeCore,
      Libraries.circeGeneric,
      Libraries.circeParser,
      Libraries.circeRefined,
      Libraries.cirisCore,
      Libraries.cirisEnum,
      Libraries.cirisRefined,
      Libraries.derevoCore,
      Libraries.derevoCats,
      Libraries.derevoCirce,
      Libraries.fs2,
      Libraries.http4sDsl,
      Libraries.http4sServer,
      Libraries.http4sClient,
      Libraries.http4sCirce,
      Libraries.http4sJwtAuth,
      Libraries.javaxCrypto,
      Libraries.log4cats,
      Libraries.logback % Runtime,
      Libraries.monocleCore,
      Libraries.newtype,
      Libraries.redis4catsEffects,
      Libraries.redis4catsLog4cats,
      Libraries.refinedCore,
      Libraries.refinedCats,
      Libraries.skunkCore,
      Libraries.skunkCirce,
      Libraries.squants
    )
  )

addCommandAlias("runLinter", ";scalafixAll --rules OrganizeImports")