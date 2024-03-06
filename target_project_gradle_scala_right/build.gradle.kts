plugins {
    scala
    id("org.playframework.twirl") version "2.0.4"
}

group = "org.bla"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.playframework.twirl", "twirl-api_2.13", "2.0.4")
}

sourceSets {
    main {
        twirl {
            srcDir("src/main/scala")
        }
    }
}

tasks.withType<ScalaCompile>().configureEach {
    scalaCompileOptions.additionalParameters!!.addAll(
        listOf("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")
    )

    scalaCompileOptions.forkOptions.apply {
        memoryMaximumSize = "1g"
        jvmArgs!!.addAll(listOf("-XX:MaxMetaspaceSize=512m"))
    }
}

tasks.test {
    useJUnitPlatform()
}