// build.gradle.kts

// 1. 插件声明
// 声明项目使用的插件。
// kotlin("jvm") 插件用于构建 JVM 上的 Kotlin 项目。
// application 插件用于构建可执行应用（可选，如果只作为库则不需要）。
// maven-publish 插件用于将项目发布到 Maven 仓库。
plugins {
    kotlin("jvm") version "1.9.0" // 请根据你使用的Kotlin版本调整
    application // 如果你想打包成可执行的jar，可以添加 application 插件
    `maven-publish` // 添加 Maven Publish 插件
}

// 2. 仓库配置
// 配置 Gradle 从哪里下载依赖库。
// mavenCentral() 是最常用的 Maven 仓库。
repositories {
    mavenCentral()
}

// 3. 依赖声明
// 声明项目所需的依赖。
// kotlin-stdlib-jdk8 提供了 Kotlin 标准库的 JDK 8 特定功能。
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

// 4. Java 兼容性配置 (可选，但推荐)
// 确保你的项目使用 Java 8 兼容性, 这对于许多 Kotlin 项目来说是常见的。
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// 5. Kotlin 编译选项 (可选)
// 可以配置 Kotlin 编译器的额外选项。
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// 6. Application 插件配置 (如果你添加了 application 插件)
// 配置 application 插件，指定主类的完整路径，这样可以直接运行 `gradle run` 命令。
application {
    mainClass.set("MainKt")
}

// 7. 打包可执行 Jar (可选，如果你想生成一个独立的运行文件)
// 这个任务会创建一个包含所有依赖的可执行 JAR 文件。
tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    archiveBaseName.set("kotlin-json-parser")
    archiveVersion.set("1.0.0")
    archiveClassifier.set("all")
}

// 9. (可选) 源代码和 KDoc Jar - 任务定义
// 这会创建包含源代码和 KDoc 文档的 JAR 包，方便其他开发者查看源码和文档。
tasks.withType<Jar>().configureEach {
    if (name == "jar") { // 确保只对主 JAR 任务进行配置
        // 为主 JAR 任务添加源文件
        from(sourceSets.main.get().allSource)
    }
}

// 创建一个任务来生成源代码 JAR
tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

// 创建一个任务来生成 KDoc JAR
tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("doc") // Use "doc" for KDoc/Javadoc
    // For Kotlin KDoc, you might need to configure dokka or similar plugin
    // For simplicity, this example just creates an empty jar,
    // in a real project, you'd integrate Dokka.
    // See: https://kotlinlang.org/docs/reference/gradle.html#dokka-integration
}


// 8. Maven Publish 配置 - 合并后的单个块
// 配置如何将你的库发布到 Maven 仓库。
publishing {
    // 定义发布物，通常是你的 JAR 包
    publications {
        // 创建一个名为 'mavenJava' 的 publication
        create<MavenPublication>("mavenJava") {
            // 指定要发布的组件，通常是 Java 或 Kotlin 项目生成的默认组件
            from(components["kotlin"]) // 对于 Kotlin JVM 项目，使用 "kotlin" 组件

            // 定义 Group ID, Artifact ID, Version (GAV 坐标)
            // 这是其他项目引用你的库时需要的信息
            groupId = "com.65zlui.jsonparser" // 替换为你的公司或项目的 Group ID
            artifactId = "kotlin-json-parser"      // 替换为你的库的 Artifact ID
            version = "1.0.0"                      // 替换为你的库的版本

            // 将源代码 JAR 和 KDoc JAR 添加到发布物中
            artifact(tasks.named<Jar>("sourcesJar")) // <-- 修正了这里
            // artifact(tasks.javadocJar) // 如果你配置了 Dokka 并生成了实际的 KDoc JAR，请取消注释
        }
    }
    // 定义发布仓库
    repositories {
        // 发布到 GitHub Packages
        maven {
            name = "GitHubPackages"
            // GitHub Packages 的 URL 格式：https://maven.pkg.github.com/OWNER/REPOSITORY
            // OWNER 是你的 GitHub 用户名或组织名
            // REPOSITORY 是你的 GitHub 仓库名
            url = uri("https://maven.pkg.github.com/65zlui/YOUR_REPO_NAME") // <-- 替换为你的 GitHub 用户名和仓库名

            // 认证信息：使用 GitHub 用户名和 Personal Access Token (PAT)
            // 这些信息应该从 gradle.properties 中读取，而不是硬编码
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
        // 如果你仍然需要发布到本地 Maven 仓库，可以保留这一行
        // mavenLocal()
    }
}
