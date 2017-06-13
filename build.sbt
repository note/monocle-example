name := """monocle-example"""

version := "1.0"

scalaVersion := "2.12.2"

val monocleVersion  = "1.4.0"
val circeVersion    = "0.8.0"

libraryDependencies ++= Seq(
  "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
  "io.circe"                   %% "circe-core"    % circeVersion,
  "io.circe"                   %% "circe-parser"  % circeVersion,
  "io.circe"                   %% "circe-optics"  % circeVersion,
  "com.github.julien-truffaut" %% "monocle-law"   % monocleVersion % "test",
  "org.scalatest"              %% "scalatest"     % "3.0.3"        % "test"
)
