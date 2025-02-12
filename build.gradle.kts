plugins {
    id("java")
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version ("1.1.7")
}

group = "com.vfedotov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.register("runAllModules") {
    dependsOn("notification:run")
    doLast {
        println("All modules have been launched.")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter")
}

tasks.test {
    useJUnitPlatform()
}