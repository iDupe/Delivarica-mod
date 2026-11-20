plugins {
    java
}

sourceSets.main {
    java.setSrcDirs(listOf("src"))
}

// Версия Mindustry для сборки (latest, be или v<номер>) | Mindustry version to depend on (latest, be, or v<number>)
val mindustryVersion = "v160"
val isWindows = System.getProperty("os.name").lowercase().contains("windows")
val sdkRoot: String? = System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
val projectName = project.name

repositories {
    mavenCentral()

    // Загрузка JAR-зависимостей напрямую из релизов GitHub | Download dependencies JAR directly from GitHub releases
    ivy {
        url = uri("https://github.com")
        patternLayout {
            val pattern = when (mindustryVersion) {
                "latest" -> "/[organisation]/[module]/releases/[revision]/download/dependencies.jar" // Последний релиз | Latest stable release
                "be" -> "/[organisation]/[module]/releases/download/master/[revision].jar"           // Последний коммит (BE) | Latest commit (BE)
                else -> "/[organisation]/[module]/releases/download/[revision]/dependencies.jar"     // Конкретная версия | Specific release
            }
            val path = pattern.substring(0, pattern.lastIndexOf('/'))

            artifact("$path/[classifier].jar")
            artifact(pattern.replaceFirst(Regex("""\.jar$"""), "(-[classifier]).jar"))
        }
        metadataSources { artifact() }

        content {
            if (mindustryVersion == "be") {
                // Для BE всегда используется тег 'latest' | BE artifact version is always 'latest'
                includeVersion("Anuken", "MindustryBuilds", "latest")
            } else {
                includeVersion("Anuken", "Mindustry", mindustryVersion)
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    compileOnly(
        if (mindustryVersion == "be") "Anuken:MindustryBuilds:latest"
        else "Anuken:Mindustry:$mindustryVersion"
    )
}

// Сборка JAR-файла для ПК | Build Desktop JAR
tasks.jar {
    archiveFileName.set("${projectName}Desktop.jar")

    from(configurations.runtimeClasspath.map { files ->
        files.map { if (it.isDirectory) it else zipTree(it) }
    })

    from(projectDir) {
        include("mod.hjson")
        include("icon.png")
    }

    from("assets/") {
        include("**")
    }
}

// Конвертация в DEX для Android через d8 | Convert to Android DEX via d8
val jarAndroid = tasks.register("jarAndroid") {
    dependsOn(tasks.jar)

    doLast {
        val sdk = sdkRoot?.let { File(it) }
        if (sdk == null || !sdk.exists()) {
            throw GradleException("No valid Android SDK found. Ensure that ANDROID_HOME is set to your Android SDK directory.")
        }

        val platformRoot = File(sdk, "platforms").listFiles()
            ?.sortedDescending()
            ?.firstOrNull { File(it, "android.jar").exists() }
            ?: throw GradleException("No android.jar found. Ensure that you have an Android platform installed.")

        // Сбор зависимостей для desugaring | Collect dependencies needed for desugaring
        val classpath = (
            configurations.compileClasspath.get().files +
            configurations.runtimeClasspath.get().files +
            File(platformRoot, "android.jar")
        )

        val d8 = if (isWindows) "d8.bat" else "d8"

        val command = buildList {
            add(d8)
            classpath.forEach { addAll(listOf("--classpath", it.path)) }
            addAll(listOf("--min-api", "21"))
            addAll(listOf("--output", "${projectName}Android.jar"))
            add("${projectName}Desktop.jar")
        }

        // Запуск d8 (требует наличие d8 в PATH) | Run d8 (requires d8 in PATH)
        val exitCode = ProcessBuilder(command)
            .directory(layout.buildDirectory.dir("libs").get().asFile)
            .inheritIO()
            .start()
            .waitFor()

        if (exitCode != 0) throw GradleException("d8 failed with exit code $exitCode")
    }
}

// Объединение Desktop и Android сборок в итоговый JAR | Merge Desktop and Android builds into final JAR
tasks.register<Jar>("deploy") {
    dependsOn(jarAndroid, tasks.jar)
    archiveFileName.set("$projectName.jar")

    val libs = layout.buildDirectory.dir("libs")
    from(provider {
        listOf(
            zipTree(libs.get().file("${projectName}Desktop.jar")),
            zipTree(libs.get().file("${projectName}Android.jar"))
        )
    })

    doLast {
        libs.get().file("${projectName}Android.jar").asFile.delete()
    }
}