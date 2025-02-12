plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.vfedotov"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass = "com.vfedotov.notification_sender.NotificationSenderApplication"
}

tasks.register("prepareKotlinBuildScriptModel") {

}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    implementation(project(":core"))
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.kafka:spring-kafka:3.3.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("org.springframework.boot:spring-boot-starter-mail:3.4.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}