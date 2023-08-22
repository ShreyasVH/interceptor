name := "interceptor"

version := "1.0.0"

scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
	guice,
	javaWs,
	"mysql" % "mysql-connector-java" % "8.0.31",
	"org.projectlombok" % "lombok" % "1.18.24" % "provided"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)