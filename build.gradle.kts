plugins {
    `java-library`
    `maven-publish`
}

group = "com.ohusiev"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("BBSE Java")
                description.set("Binary search encoding for sorted integer domains.")
                url.set("https://github.com/ohusiev/bbse-java")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("ohusiev")
                        name.set("Oleksandr Husiev")
                        email.set("ohusiev@icloud.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/ohusiev/bbse-java.git")
                    developerConnection.set("scm:git:ssh://github.com:ohusiev/bbse-java.git")
                    url.set("https://github.com/ohusiev/bbse-java")
                }
            }
        }
    }
}
