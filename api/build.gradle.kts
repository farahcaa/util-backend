plugins {
    java
    id("org.springframework.boot") version "3.5.3"
    id("com.google.cloud.tools.jib") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

version = providers.gradleProperty("apiVersion").get()

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    dependencies {
        implementation(platform("software.amazon.awssdk:bom:2.25.43")) // or latest version
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("software.amazon.awssdk:ses")
        implementation("software.amazon.awssdk:auth")
    }

}

jib {
    from {
        image = "eclipse-temurin:21-jre"
    }
    to {
        image = "ghcr.io/farahcaa/util-backend/api:${project.version}"
        tags = setOf("${project.version}", "latest")
    }
    container {
        ports = listOf("8081")
        jvmFlags = listOf("-XX:MaxRAMPercentage=75")
        user = "1000:1000"
        environment = mapOf("SPRING_PROFILES_ACTIVE" to "prod")
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}

/**
 * This may be needed.
 * Some unique bug.
 * Thanks gradle/IntelliJ
 * See: https://stackoverflow.com/questions/64290545/task-preparekotlinbuildscriptmodel-not-found-in-project-app
 */
tasks.register("prepareKotlinBuildScriptModel") {}