name := "interceptor"

version := "1.0.0"

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(
	guice,
	javaWs,
	"mysql" % "mysql-connector-java" % "8.0.17",
	"org.projectlombok" % "lombok" % "1.18.8"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)