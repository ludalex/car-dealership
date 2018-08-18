name := "scout24-task"
 
version := "1.0" 
      
lazy val `scout24-task` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  filters,
  cache,
  ws,
  specs2 % Test,
  "com.h2database" % "h2" % "1.4.187",
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

routesGenerator := InjectedRoutesGenerator