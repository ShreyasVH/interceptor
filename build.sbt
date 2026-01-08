name := "interceptor"

version := "1.0.0"

scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
	guice,
	javaWs,
	"com.mysql" % "mysql-connector-j" % "9.5.0",
	"org.projectlombok" % "lombok" % "1.18.24" % "provided"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)