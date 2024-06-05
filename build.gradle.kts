plugins {
    id("java")
}

group = "com.bookmap"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("com.amazonaws:aws-java-sdk-rekognition:1.12.522")
    implementation ("commons-io:commons-io:2.8.0")
}

tasks.test {
    useJUnitPlatform()
}