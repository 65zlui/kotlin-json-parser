# **Kotlin JSON 解析器**

[English](README.md) | 简体中文

一个轻量级的 Kotlin JSON 解析库，能够将 JSON 字符串转换为类型安全的 Kotlin 对象结构。

## **描述**

这个库提供了一个基础的 JSON 解析器，包括词法分析（Lexer）和语法分析（Parser）两个阶段。它旨在帮助开发者理解 JSON 解析的内部机制，并提供一个简单易用的工具来处理 JSON 数据。

## **特性**

* 支持所有 JSON 基本类型：对象、数组、字符串、数字、布尔值、null。  
* 处理字符串中的转义序列。  
* 对不符合 JSON 规范的输入进行错误处理。  
* 将 JSON 字符串解析为类型安全的 JsonValue 密封类层次结构。

## **如何使用 (作为依赖)**

如果你想在你的 Kotlin 项目中使用这个库，你需要将它作为依赖添加到你的 build.gradle.kts 或 build.gradle 文件中。

### **1\. 添加 GitHub Packages 仓库**

首先，在你的 build.gradle.kts (Kotlin DSL) 或 build.gradle (Groovy DSL) 中添加 GitHub Packages 仓库配置。请将 YOUR\_GITHUB\_USERNAME 和 YOUR\_REPO\_NAME 替换为实际的 GitHub 用户名和仓库名（例如，65zlui 和 kotlin-json-parser）。

**Kotlin DSL (build.gradle.kts)**:

// build.gradle.kts

repositories {  
    mavenCentral()  
    maven {  
        name \= "GitHubPackages"  
        url \= uri("https://maven.pkg.github.com/65zlui/kotlin-json-parser")  
        // 如果你的包是公共的，通常不需要 credentials 块  
        // 如果是私有包，需要配置认证信息 (例如通过 \~/.gradle/gradle.properties 中的 gpr.user 和 gpr.key)  
        credentials {  
            username \= project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB\_ACTOR")  
            password \= project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB\_TOKEN")  
        }  
    }  
}

**Groovy DSL (build.gradle)**:

// build.gradle

repositories {  
    mavenCentral()  
    maven {  
        name 'GitHubPackages'  
        url "https://maven.pkg.github.com/65zlui/kotlin-json-parser"  
        // 如果你的包是公共的，通常不需要 credentials 块  
        // 如果是私有包，需要配置认证信息  
        credentials {  
            username \= System.getenv("GITHUB\_ACTOR") ?: project.findProperty("gpr.user")  
            password \= System.getenv("GITHUB\_TOKEN") ?: project.findProperty("gpr.key")  
        }  
    }  
}

### **2\. 添加依赖**

在你的 build.gradle.kts 或 build.gradle 文件的 dependencies 块中添加以下依赖：

**Kotlin DSL (build.gradle.kts)**:

// build.gradle.kts

dependencies {  
    implementation("io.github.\_65zlui.jsonparser:kotlin-json-parser:1.0.1")  
}

**Groovy DSL (build.gradle)**:

// build.gradle

dependencies {  
    implementation 'io.github.\_65zlui.jsonparser:kotlin-json-parser:1.0.1'  
}

## **示例用法**

import io.github.\_65zlui.jsonparser.Json  
import io.github.\_65zlui.jsonparser.JsonValue

fun main() {  
    val jsonString \= """  
        {  
            "name": "Alice",  
            "age": 30,  
            "isStudent": true,  
            "courses": \["Math", "Physics"\],  
            "address": {  
                "city": "Exampleville"  
            },  
            "notes": null  
        }  
    """.trimIndent()

    try {  
        val parsedJson \= Json.parse(jsonString)  
        println("Parsed JSON: $parsedJson")

        // 访问 JSON 对象中的值  
        if (parsedJson is JsonValue.JsonObject) {  
            val name \= parsedJson.members\["name"\]  
            if (name is JsonValue.JsonString) {  
                println("Name: ${name.value}")  
            }

            val age \= parsedJson.members\["age"\]  
            if (age is JsonValue.JsonNumber) {  
                println("Age: ${age.value}")  
            }

            val courses \= parsedJson.members\["courses"\]  
            if (courses is JsonValue.JsonArray) {  
                println("Courses: ${courses.elements.map { (it as JsonValue.JsonString).value }}")  
            }  
        }

    } catch (e: Exception) {  
        println("JSON Parsing Error: ${e.message}")  
    }  
}

## **构建和发布 (为库开发者)**

如果你是这个库的开发者，并希望构建、运行或发布它：

### **项目结构**

确保你的项目结构如下：

kotlin-json-parser/  
├── build.gradle.kts  
└── src/  
    └── main/  
        └── kotlin/  
            └── io/github/\_65zlui/jsonparser/  
                ├── JsonParseException.kt  
                ├── TokenType.kt  
                ├── Token.kt  
                ├── JsonValue.kt  
                ├── JsonLexer.kt  
                ├── JsonParser.kt  
                ├── Json.kt  
                └── Main.kt

### **Gradle 配置**

build.gradle.kts 文件包含了所有必要的配置，包括 Kotlin JVM 插件、应用程序插件和 Maven Publish 插件，以及 GitHub Packages 的发布设置。

### **认证信息**

在你的 Gradle 用户主目录（例如 \~/.gradle/）下创建或编辑 gradle.properties 文件，添加你的 GitHub 用户名和 Personal Access Token (PAT)：

gpr.user=你的GitHub用户名  
gpr.key=你生成的PersonalAccessToken

### **命令**

在项目根目录运行以下命令：

* **编译项目：**  
  ./gradlew build

* **运行示例：**  
  ./gradlew run

* **发布到 GitHub Packages：**  
  ./gradlew publish

  发布成功后，你可以在 GitHub 仓库的 "Packages" 部分找到你的库。

## **许可证**

本项目采用 [MIT 许可证](https://opensource.org/licenses/MIT) (示例，请根据实际情况选择并链接)。