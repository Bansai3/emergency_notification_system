plugins {
    id("java")
}

group = "com.vfedotov"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}


tasks.register("prepareKotlinBuildScriptModel") {

}

dependencies {
    implementation("org.projectlombok:lombok:1.18.26")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
