plugins {
    id("java")
}

group = "project.qa.reqres"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.rest-assured:rest-assured:6.0.0")
    implementation("io.rest-assured:json-path:6.0.0")
    implementation("com.codeborne:selenide:7.13.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")

    implementation("org.projectlombok:lombok:1.18.42")
    compileOnly("org.projectlombok:lombok:1.18.42")
    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

    testImplementation("org.aeonbits.owner:owner:1.0.12")
    implementation("org.aeonbits.owner:owner:1.0.12")

    implementation("org.postgresql:postgresql:42.7.9")

    implementation("net.datafaker:datafaker:2.5.3")

}

tasks.test {
    useJUnitPlatform()
}

// Что сделано в сравнении с пред. версией:
// - почищены лишние зависимости (сделать!)ко