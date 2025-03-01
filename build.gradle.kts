plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

// ShaneBot version
val projectVersion = "2.0.0"
val jdaVersion = "5.3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("org.slf4j:slf4j-nop:2.0.13")
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
}

tasks {
    processResources {
        expand("version" to projectVersion)
    }
    compileJava {
        options.release = 21
        options.compilerArgs.add("-Xlint:unchecked")
        options.compilerArgs.add("-Xlint:deprecation")
    }
    shadowJar {
        archiveFileName = "ShaneBot-${projectVersion}.jar"
        manifest {
            attributes["Main-Class"] = "com.shanebeestudios.bot.BotMain"
        }
    }
    jar {
        dependsOn(shadowJar)
        archiveFileName.set("ShaneBot.jar")
    }
}
