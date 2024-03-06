plugins {
    id("java")
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
            srcDir("src/main/java")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}