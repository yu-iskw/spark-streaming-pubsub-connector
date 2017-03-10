organization := "com.yu-iskw"

name := "spark-streaming-pubsub-receiver"

publishMavenStyle := true

version := "0.1.0"

sparkVersion := "2.1.0"

scalaVersion := {
  if (sparkVersion.value >= "2.0.0") {
    "2.11.8"
  } else {
    "2.10.6"
  }
}

crossScalaVersions := {
  if (sparkVersion.value > "2.0.0") {
    Seq("2.11.8")
  } else {
    Seq("2.10.6", "2.11.8")
  }
}

// javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

//tag::spName[]
spName := "yu-iskw/spark-streaming-pubsub-receiver"
//end::spName[]

sparkComponents := {
  if (sparkVersion.value >= "2.0.0") Seq("core", "streaming", "sql", "catalyst", "hive", "yarn", "mllib")
  else Seq("core", "streaming", "sql", "catalyst", "hive", "streaming-kafka", "yarn", "mllib")
}

parallelExecution in Test := false
fork := true


coverageHighlighting := {
  if (scalaBinaryVersion.value == "2.10") false
  else true
}

// Allow kafka (and other) utils to have version specific files
unmanagedSourceDirectories in Compile  := {
  if (sparkVersion.value >= "2.0.0") Seq(
    (sourceDirectory in Compile)(_ / "2.0/scala"), (sourceDirectory in Compile)(_ / "2.0/java")
  ).join.value
  else if (sparkVersion.value >= "1.6") Seq(
    (sourceDirectory in Compile)(_ / "2.0/scala"), (sourceDirectory in Compile)(_ / "2.0/java")
  ).join.value
  else Seq(
    (sourceDirectory in Test)(_ / "2.0/scala"), (sourceDirectory in Test)(_ / "2.0/java")
  ).join.value
}

unmanagedSourceDirectories in Test  := {
  if (sparkVersion.value >= "2.0.0") Seq(
    (sourceDirectory in Test)(_ / "2.0/scala"), (sourceDirectory in Test)(_ / "2.0/java")
  ).join.value
  else if (sparkVersion.value >= "1.6") Seq(
    (sourceDirectory in Test)(_ / "2.0/scala"), (sourceDirectory in Test)(_ / "2.0/java")
  ).join.value
  else Seq(
    (sourceDirectory in Test)(_ / "2.0/scala"), (sourceDirectory in Test)(_ / "2.0/java")
  ).join.value
}


javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")

// additional libraries
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1",
  "io.github.nicolasstucki" %% "multisets" % "0.4",
  "org.scalacheck" %% "scalacheck" % "1.13.4",
  "junit" % "junit" % "4.12",
  "org.eclipse.jetty" % "jetty-util" % "9.3.11.v20160721",
  "com.novocode" % "junit-interface" % "0.11" % "test->default")

// Based on Hadoop Mini Cluster tests from Alpine's PluginSDK (Apache licensed)
// javax.servlet signing issues can be tricky, we can just exclude the dep
def excludeFromAll(items: Seq[ModuleID], group: String, artifact: String) =
  items.map(_.exclude(group, artifact))

def excludeJavaxServlet(items: Seq[ModuleID]) =
  excludeFromAll(items, "javax.servlet", "servlet-api")

lazy val miniClusterDependencies = excludeJavaxServlet(Seq(
  "com.google.apis" % "google-api-services-pubsub" % "v1-rev7-1.20.0",
  "com.google.cloud" % "google-cloud-pubsub" % "0.9.4-alpha"
))

libraryDependencies ++= miniClusterDependencies

scalacOptions ++= Seq("-deprecation", "-unchecked")

pomIncludeRepository := { x => false }

resolvers ++= Seq(
  "JBoss Repository" at "http://repository.jboss.org/nexus/content/repositories/releases/",
  "Spray Repository" at "http://repo.spray.cc/",
  "Cloudera Repository" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  "Akka Repository" at "http://repo.akka.io/releases/",
  "Twitter4J Repository" at "http://twitter4j.org/maven2/",
  "Apache HBase" at "https://repository.apache.org/content/repositories/releases",
  "Twitter Maven Repo" at "http://maven.twttr.com/",
  "scala-tools" at "https://oss.sonatype.org/content/groups/scala-tools",
  "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Second Typesafe repo" at "http://repo.typesafe.com/typesafe/maven-releases/",
  "Mesosphere Public Repository" at "http://downloads.mesosphere.io/maven",
  Resolver.sonatypeRepo("public")
)

// publish settings
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

licenses := Seq("Apache License 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

homepage := Some(url("https://github.com/yu-iskw/spark-streaming-pubsub-receiver"))

pomExtra := (
  <scm>
    <url>git@github.com:yu-iskw/spark-streaming-pubsub-receiver.git</url>
    <connection>scm:git@github.com:yu-iskw/spark-streaming-pubsub-receiver.git</connection>
  </scm>
  <developers>
    <developer>
      <id>yu-iskw</id>
      <name>Yu Ishikawa</name>
      <url>https://github.com/yu-iskw</url>
      <email>todo</email>
    </developer>
  </developers>
)

//credentials += Credentials(Path.userHome / ".ivy2" / ".spcredentials")
credentials ++= Seq(Credentials(Path.userHome / ".ivy2" / ".sbtcredentials"), Credentials(Path.userHome / ".ivy2" / ".sparkcredentials"))

spIncludeMaven := true

useGpg := true
