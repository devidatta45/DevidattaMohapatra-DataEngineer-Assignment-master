name := "DevidattaMohapatra-DataEngineer-Assignment"

version := "0.1"

scalaVersion := "2.12.11"

val sttpVersion = "1.7.2"
val catsVersion = "2.0.0"
val loggingVersion = "3.9.0"
val refinedVersion = "0.9.14"
val zioVersion = "1.0.0-RC17"
val akkaHttpVersion = "10.1.12"
val akkaStreamVersion = "2.6.7"
val configVersion = "1.3.4"
val jsonNativeVersion = "3.6.8"
val slfVersion = "1.7.28"
val scalaTestVersion = "3.2.0"

libraryDependencies := Seq (
  "com.softwaremill.sttp"      %% "core"                            % sttpVersion,
  "com.softwaremill.sttp"      %% "async-http-client-backend-future"% sttpVersion,
  "com.softwaremill.sttp"      %% "json4s"                          % sttpVersion,
  "org.typelevel"              %% "cats-core"                       % catsVersion,
  "com.typesafe.scala-logging" %% "scala-logging"                   % loggingVersion,
  "eu.timepit"                 %% "refined"                         % refinedVersion,
  "eu.timepit"                 %% "refined-scalacheck"              % refinedVersion,
  "dev.zio"                    %% "zio"                             % zioVersion,
  "com.typesafe.akka"          %% "akka-http"                       % akkaHttpVersion,
  "com.typesafe.akka"          %% "akka-stream"                     % akkaStreamVersion,
  "com.typesafe"                % "config"                          % configVersion,
  "org.json4s"                 %% "json4s-native"                   % jsonNativeVersion,
  "org.slf4j"                   % "slf4j-api"                       % slfVersion,
  "org.slf4j"                   % "slf4j-nop"                       % slfVersion,
  "org.scalatest"              %% "scalatest"                       % scalaTestVersion % Test

)