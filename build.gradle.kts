plugins {
    id("java")
}

group = "project.qa.reqres"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

//dependencies {
//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//    testImplementation("org.junit.jupiter:junit-jupiter-params:5.13.1")
//    implementation("io.rest-assured:rest-assured:5.5.5")
//    testImplementation("io.rest-assured:rest-assured:5.5.5")
//    implementation("io.rest-assured:json-path:5.5.5")
//    implementation("org.aeonbits.owner:owner:1.0.12")
//    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")
//    compileOnly("org.projectlombok:lombok:1.18.38")
//    annotationProcessor("org.projectlombok:lombok:1.18.38")
//
//    testCompileOnly("org.projectlombok:lombok:1.18.38")
//    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
//
//    testImplementation("io.qameta.allure:allure-junit5:2.29.1")
//}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.rest-assured:rest-assured:6.0.0")
    implementation("io.rest-assured:json-path:6.0.0")
    implementation("com.codeborne:selenide:7.13.0")
    implementation("com.google.code.gson:gson:2.13.2")

    implementation("org.projectlombok:lombok:1.18.42")
    compileOnly("org.projectlombok:lombok:1.18.42")
    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

    testImplementation("org.aeonbits.owner:owner:1.0.12")
    implementation("org.aeonbits.owner:owner:1.0.12")
}

tasks.test {
    useJUnitPlatform()
}