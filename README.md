### Source project

Originally, we had a source SBT project with the play layout plugin enabled:

`source_project_sbt` 

In `source_project_sbt`, the app path contained twirl templates and Java templates.

The client class is an example: Import the Scala template, that will be generated by Twirl, render it.

### First attempt: Gradle project with Java Sourceset

If we port to Gradle and use the Java compile plugin, it will not work:

```
Initialized native services in: /home/hh/.gradle/native
Initialized jansi services in: /home/hh/.gradle/native
Received JVM installation metadata from '/opt/openjdk-bin-17.0.8.1_p1': {JAVA_HOME=/opt/openjdk-bin-17.0.8.1_p1, JAVA_VERSION=17.0.8.1, JAVA_VENDOR=Eclipse Adoptium, RUNTIME_NAME=OpenJDK Runtime Environment, RUNTIME_VERSION=17.0.8.1+1, VM_NAME=OpenJDK 64-Bit Server VM, VM_VERSION=17.0.8.1+1, VM_VENDOR=Eclipse Adoptium, OS_ARCH=amd64}
The client will now receive all logging from the daemon (pid: 148942). The daemon log file: /home/hh/.gradle/daemon/8.6/daemon-148942.out.log
Starting 8th build in daemon [uptime: 10 mins 41.838 secs, performance: 100%, GC rate: 0.00/s, heap usage: 0% of 512 MiB, non-heap usage: 35% of 384 MiB]
Using 12 worker leases.
Now considering [/home/hh/Projekte/cto/test-project-gradle-twirl] as hierarchies to watch
Watching the file system is configured to be enabled if available
File system watching is active
Starting Build
Settings evaluated using settings file '/home/hh/Projekte/cto/test-project-gradle-twirl/settings.gradle.kts'.
Projects loaded. Root project using build file '/home/hh/Projekte/cto/test-project-gradle-twirl/build.gradle'.
Included projects: [root project 'test-project-gradle-twirl', project ':target_project_gradle_java_wrong']

> Configure project :
Evaluating root project 'test-project-gradle-twirl' using build file '/home/hh/Projekte/cto/test-project-gradle-twirl/build.gradle'.

> Configure project :target_project_gradle_java_wrong
Evaluating project ':target_project_gradle_java_wrong' using build file '/home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_java_wrong/build.gradle.kts'.
Transforming gradle-twirl-2.0.4.jar (org.playframework.twirl:gradle-twirl:2.0.4) with ExternalDependencyInstrumentingArtifactTransform
All projects evaluated.
Task name matched 'build'
Selected primary task 'build' from project :
Tasks to be executed: [task ':target_project_gradle_java_wrong:compileJava', task ':target_project_gradle_java_wrong:compileTwirl', task ':target_project_gradle_java_wrong:compileScala', task ':target_project_gradle_java_wrong:processResources', task ':target_project_gradle_java_wrong:classes', task ':target_project_gradle_java_wrong:jar', task ':target_project_gradle_java_wrong:assemble', task ':target_project_gradle_java_wrong:compileTestJava', task ':target_project_gradle_java_wrong:compileTestTwirl', task ':target_project_gradle_java_wrong:compileTestScala', task ':target_project_gradle_java_wrong:processTestResources', task ':target_project_gradle_java_wrong:testClasses', task ':target_project_gradle_java_wrong:test', task ':target_project_gradle_java_wrong:check', task ':target_project_gradle_java_wrong:build']
Tasks that were excluded: []
Resolve mutations for :target_project_gradle_java_wrong:compileJava (Thread[Execution worker,5,main]) started.
:target_project_gradle_java_wrong:compileJava (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_java_wrong:compileJava FAILED
Caching disabled for task ':target_project_gradle_java_wrong:compileJava' because:
  Build cache is disabled
Task ':target_project_gradle_java_wrong:compileJava' is not up-to-date because:
  Task has failed previously.
The input changes require a full rebuild for incremental task ':target_project_gradle_java_wrong:compileJava'.
Full recompilation is required because no incremental change information is available. This is usually caused by clean builds or changing compiler arguments.
Compiling with toolchain '/opt/openjdk-bin-17.0.8.1_p1'.
Compiling with JDK Java compiler API.
/home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_java_wrong/src/main/java/org/bla/foobar/client/Client.java:5: error: package org.bla.foobar.template.txt does not exist
        final String finalRole = org.bla.foobar.template.txt.final_role.render("company").toString();
                                                            ^
1 error

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':target_project_gradle_java_wrong:compileJava'.
> Compilation failed; see the compiler error output for details.

* Try:
> Run with --scan to get full insights.

BUILD FAILED in 760ms
1 actionable task: 1 executed

``` 

So it seems the Java compiler doesn't pick up the generated classes, as it cannot find the Scala file.


`Tasks to be executed: [task ':target_project_gradle_java_wrong:compileJava', task ':target_project_gradle_java_wrong:compileTwirl', task ':target_project_gradle_java_wrong:compileScala', task ':target_project_gradle_java_wrong:processResources', task ':target_project_gradle_java_wrong:classes', task ':target_project_gradle_java_wrong:jar', task ':target_project_gradle_java_wrong:assemble', task ':target_project_gradle_java_wrong:compileTestJava', task ':target_project_gradle_java_wrong:compileTestTwirl', task ':target_project_gradle_java_wrong:compileTestScala', task ':target_project_gradle_java_wrong:processTestResources', task ':target_project_gradle_java_wrong:testClasses', task ':target_project_gradle_java_wrong:test', task ':target_project_gradle_java_wrong:check', task ':target_project_gradle_java_wrong:build']`

The compileTwirl should be executed, however I get no output - maybe the task order is wrong?

If I execute the task "compileTwirl" for the project directly (thus not build), I get the following output:
```
> Task :target_project_gradle_java_wrong:compileTwirl
Caching disabled for task ':target_project_gradle_java_wrong:compileTwirl' because:
  Build cache is disabled
Task ':target_project_gradle_java_wrong:compileTwirl' is not up-to-date because:
  Output property 'destinationDirectory' file /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_java_wrong/build/generated/sources/twirl/main has been removed.
  Output property 'destinationDirectory' file /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_java_wrong/build/generated/sources/twirl/main/main has been removed.
  Output property 'destinationDirectory' file /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_java_wrong/build/generated/sources/twirl/main/main/java has been removed.
The input changes require a full rebuild for incremental task ':target_project_gradle_java_wrong:compileTwirl'.
Compile Twirl template [play.twirl.api.TxtFormat/UTF-8] final_role.scala.txt from /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_java_wrong/src into /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_java_wrong/build/generated/sources/twirl/main

```
`into /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_java_wrong/build/generated/sources/twirl/main`
(last line)

So the source configuration seems to be correct, the file is generated with the right path.

But either it is not executed or not picked up by the java compiler.

### Third attempt: Gradle project with Scala source set

(switch the project in settings.gradle.kts in root folder)

```
BUILD SUCCESSFUL in 1s
1 actionable task: 1 executed
hh@ezekiel ~/Projekte/cto/test-project-gradle-twirl $ ./gradlew build --info
Initialized native services in: /home/hh/.gradle/native
Initialized jansi services in: /home/hh/.gradle/native
Received JVM installation metadata from '/opt/openjdk-bin-17.0.8.1_p1': {JAVA_HOME=/opt/openjdk-bin-17.0.8.1_p1, JAVA_VERSION=17.0.8.1, JAVA_VENDOR=Eclipse Adoptium, RUNTIME_NAME=OpenJDK Runtime Environment, RUNTIME_VERSION=17.0.8.1+1, VM_NAME=OpenJDK 64-Bit Server VM, VM_VERSION=17.0.8.1+1, VM_VENDOR=Eclipse Adoptium, OS_ARCH=amd64}
The client will now receive all logging from the daemon (pid: 148942). The daemon log file: /home/hh/.gradle/daemon/8.6/daemon-148942.out.log
Starting 5th build in daemon [uptime: 1 mins 29.651 secs, performance: 100%, GC rate: 0.00/s, heap usage: 0% of 512 MiB, non-heap usage: 32% of 384 MiB]
Using 12 worker leases.
Now considering [/home/hh/Projekte/cto/test-project-gradle-twirl] as hierarchies to watch
Watching the file system is configured to be enabled if available
File system watching is active
Starting Build
Settings evaluated using settings file '/home/hh/Projekte/cto/test-project-gradle-twirl/settings.gradle.kts'.
Projects loaded. Root project using build file '/home/hh/Projekte/cto/test-project-gradle-twirl/build.gradle'.
Included projects: [root project 'test-project-gradle-twirl', project ':target_project_gradle_scala_right']

> Configure project :
Evaluating root project 'test-project-gradle-twirl' using build file '/home/hh/Projekte/cto/test-project-gradle-twirl/build.gradle'.

> Configure project :target_project_gradle_scala_right
Evaluating project ':target_project_gradle_scala_right' using build file '/home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build.gradle.kts'.
Transforming gradle-twirl-2.0.4.jar (org.playframework.twirl:gradle-twirl:2.0.4) with ExternalDependencyInstrumentingArtifactTransform
All projects evaluated.
Task name matched 'build'
Selected primary task 'build' from project :
Tasks to be executed: [task ':target_project_gradle_scala_right:compileJava', task ':target_project_gradle_scala_right:compileTwirl', task ':target_project_gradle_scala_right:compileScala', task ':target_project_gradle_scala_right:processResources', task ':target_project_gradle_scala_right:classes', task ':target_project_gradle_scala_right:jar', task ':target_project_gradle_scala_right:assemble', task ':target_project_gradle_scala_right:compileTestJava', task ':target_project_gradle_scala_right:compileTestTwirl', task ':target_project_gradle_scala_right:compileTestScala', task ':target_project_gradle_scala_right:processTestResources', task ':target_project_gradle_scala_right:testClasses', task ':target_project_gradle_scala_right:test', task ':target_project_gradle_scala_right:check', task ':target_project_gradle_scala_right:build']
Tasks that were excluded: []
Resolve mutations for :target_project_gradle_scala_right:compileJava (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:compileJava (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:compileJava NO-SOURCE
Skipping task ':target_project_gradle_scala_right:compileJava' as it has no source files and no previous output files.
Resolve mutations for :target_project_gradle_scala_right:compileTwirl (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:compileTwirl (Thread[Execution worker,5,main]) started.
Resolve mutations for :target_project_gradle_scala_right:processResources (Thread[Execution worker Thread 2,5,main]) started.
:target_project_gradle_scala_right:processResources (Thread[Execution worker Thread 2,5,main]) started.

> Task :target_project_gradle_scala_right:processResources NO-SOURCE
Skipping task ':target_project_gradle_scala_right:processResources' as it has no source files and no previous output files.
Resolve mutations for :target_project_gradle_scala_right:compileTestTwirl (Thread[Execution worker Thread 2,5,main]) started.
:target_project_gradle_scala_right:compileTestTwirl (Thread[Execution worker Thread 10,5,main]) started.

> Task :target_project_gradle_scala_right:compileTestTwirl
Caching disabled for task ':target_project_gradle_scala_right:compileTestTwirl' because:
  Build cache is disabled
Task ':target_project_gradle_scala_right:compileTestTwirl' is not up-to-date because:
  Output property 'destinationDirectory' file /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build/generated/sources/twirl/test has been removed.
The input changes require a full rebuild for incremental task ':target_project_gradle_scala_right:compileTestTwirl'.
Resolve mutations for :target_project_gradle_scala_right:processTestResources (Thread[Execution worker Thread 10,5,main]) started.
:target_project_gradle_scala_right:processTestResources (Thread[Execution worker Thread 10,5,main]) started.

> Task :target_project_gradle_scala_right:processTestResources NO-SOURCE
Skipping task ':target_project_gradle_scala_right:processTestResources' as it has no source files and no previous output files.

> Task :target_project_gradle_scala_right:compileTwirl
Caching disabled for task ':target_project_gradle_scala_right:compileTwirl' because:
  Build cache is disabled
Task ':target_project_gradle_scala_right:compileTwirl' is not up-to-date because:
  Output property 'destinationDirectory' file /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build/generated/sources/twirl/main has been removed.
  Output property 'destinationDirectory' file /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build/generated/sources/twirl/main/main has been removed.
  Output property 'destinationDirectory' file /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build/generated/sources/twirl/main/main/scala has been removed.
The input changes require a full rebuild for incremental task ':target_project_gradle_scala_right:compileTwirl'.
Compile Twirl template [play.twirl.api.TxtFormat/UTF-8] final_role.scala.txt from /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/src/main/scala into /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build/generated/sources/twirl/main
Resolve mutations for :target_project_gradle_scala_right:compileScala (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:compileScala (Thread[Execution worker,5,main]) started.
This JVM does not support getting OS memory, so no OS memory status updates will be broadcast

> Task :target_project_gradle_scala_right:compileScala
Caching disabled for task ':target_project_gradle_scala_right:compileScala' because:
  Build cache is disabled
Task ':target_project_gradle_scala_right:compileScala' is not up-to-date because:
  Task has failed previously.
Zinc is doing a full recompile since the analysis file doesn't exist
Starting process 'Gradle Worker Daemon 3'. Working directory: /home/hh/.gradle/workers Command: /opt/openjdk-bin-17.0.8.1_p1/bin/java -XX:MaxMetaspaceSize=512m @/home/hh/.gradle/.tmp/gradle-worker-classpath14487092996170863311txt -Xmx1024m -Dfile.encoding=UTF-8 -Duser.country=DE -Duser.language=de -Duser.variant worker.org.gradle.process.internal.worker.GradleWorkerMain 'Gradle Worker Daemon 3'
Successfully started process 'Gradle Worker Daemon 3'
Started Gradle worker daemon (0.249 secs) with fork options DaemonForkOptions{executable=/opt/openjdk-bin-17.0.8.1_p1/bin/java, minHeapSize=null, maxHeapSize=1024m, jvmArgs=[-XX:MaxMetaspaceSize=512m], keepAliveMode=SESSION}.
Compiling with Zinc Scala compiler.
Prepared Zinc Scala inputs: 0.042 secs
compiling 1 Scala source and 1 Java source to /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build/classes/scala/main ...
[Warn] : -target is deprecated: Use -release instead to compile against the correct platform API.
[Warn] /home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build/generated/sources/twirl/main/org/bla/foobar/template/txt/final_role.template.scala:22:24: Auto-application to `()` is deprecated. Supply the empty argument list `()` explicitly to invoke method finalRole,
or remove the empty argument list from its definition (Java-defined methods are exempt).
In Scala 3, an unapplied method like this will be eta-expanded into a function. [quickfixable]
two warnings found
done compiling
Completed Scala compilation: 2.71 secs
Resolve mutations for :target_project_gradle_scala_right:classes (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:classes (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:classes
Skipping task ':target_project_gradle_scala_right:classes' as it has no actions.
Resolve mutations for :target_project_gradle_scala_right:jar (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:jar (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:jar
Caching disabled for task ':target_project_gradle_scala_right:jar' because:
  Build cache is disabled
Task ':target_project_gradle_scala_right:jar' is not up-to-date because:
  No history is available.
file or directory '/home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build/classes/java/main', not found
file or directory '/home/hh/Projekte/cto/test-project-gradle-twirl/target_project_gradle_scala_right/build/resources/main', not found
Resolve mutations for :target_project_gradle_scala_right:assemble (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:assemble (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:assemble
Skipping task ':target_project_gradle_scala_right:assemble' as it has no actions.
Resolve mutations for :target_project_gradle_scala_right:compileTestJava (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:compileTestJava (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:compileTestJava NO-SOURCE
Skipping task ':target_project_gradle_scala_right:compileTestJava' as it has no source files and no previous output files.
Resolve mutations for :target_project_gradle_scala_right:compileTestScala (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:compileTestScala (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:compileTestScala NO-SOURCE
Skipping task ':target_project_gradle_scala_right:compileTestScala' as it has no source files and no previous output files.
Resolve mutations for :target_project_gradle_scala_right:testClasses (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:testClasses (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:testClasses UP-TO-DATE
Skipping task ':target_project_gradle_scala_right:testClasses' as it has no actions.
Resolve mutations for :target_project_gradle_scala_right:test (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:test (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:test NO-SOURCE
Skipping task ':target_project_gradle_scala_right:test' as it has no source files and no previous output files.
Resolve mutations for :target_project_gradle_scala_right:check (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:check (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:check UP-TO-DATE
Skipping task ':target_project_gradle_scala_right:check' as it has no actions.
Resolve mutations for :target_project_gradle_scala_right:build (Thread[Execution worker,5,main]) started.
:target_project_gradle_scala_right:build (Thread[Execution worker,5,main]) started.

> Task :target_project_gradle_scala_right:build
Skipping task ':target_project_gradle_scala_right:build' as it has no actions.

BUILD SUCCESSFUL in 4s
4 actionable tasks: 4 executed
Stopped 1 worker daemon(s).
```

Now the compile twirl task generates output, the scala compiler starts the compilation of the Java files.
The generated scala template files are picked up by the Scala compiler, thus the build runs successfully.
