import java.util.*

plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "no.hvl.dat250.g13"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.3.5")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    runtimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}


fun loadProperties(path: String): Properties {
    val properties = Properties()
    file(path).inputStream().use { properties.load(it) }
    return properties
}

val applicationProperties = loadProperties("src/main/resources/application.properties")

val clientResources = file("../client-authdemo")
val serverResources = file("src/main/resources")

val clientBuildSrc = File(clientResources, "dist")
val clientBuildDst = File(serverResources, applicationProperties.getProperty("resources.client.index")).parent.orEmpty()

val clientConfigSrc = File(clientResources, "src/config/clientConfig.json")
val clientConfigName = File(applicationProperties.getProperty("resources.client.config")).name.ifBlank { "config.json" }!!

tasks.register("compileSvelte") {
    doLast {
        exec {
            workingDir = clientResources
            commandLine("npm.cmd", "run", "build")
        }

        delete(clientBuildDst)

        copy {
            from(clientBuildSrc)
            into(clientBuildDst)
        }
        copy {
            from(clientConfigSrc)
            into(clientBuildDst)
            rename { clientConfigName }
        }
    }
}

tasks.named("assemble") {
    dependsOn("compileSvelte")
}

tasks.named("processResources") {
    mustRunAfter("compileSvelte")
}