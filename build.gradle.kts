plugins {
    id("java")
}

group = "io.github.koblizekxd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
    implementation("org.ow2.asm:asm-util:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
    implementation("org.ow2.asm:asm-analysis:9.5")

    implementation("commons-io:commons-io:2.12.0")
    implementation("org.apache.commons:commons-compress:1.23.0")
    implementation("com.google.guava:guava:32.0.0-jre")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}