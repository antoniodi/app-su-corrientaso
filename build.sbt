name := "ms-delivery-su-corrientaso"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.1.2"
  ,"org.scalatest" %% "scalatest" % "3.1.2" % "test"
  ,"com.softwaremill.quicklens" %% "quicklens" % "1.6.0"
  ,"org.mockito"                  %   "mockito-core"                 % "1.10.19"              % "provided"

)
