name := """monocle-example"""

version := "1.0"

scalaVersion := "2.12.2"

val monocleVersion = "1.4.0"

libraryDependencies ++= Seq(
  "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-law"   % monocleVersion % "test",
  "org.scalatest"              %% "scalatest"      % "3.0.1"        % "test"
)
