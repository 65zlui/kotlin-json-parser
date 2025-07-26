# **Kotlin JSON Parser**

[简体中文](README_zh-CN.md) | English

A lightweight Kotlin JSON parsing library capable of transforming JSON strings into type-safe Kotlin object structures.

## **Description**

This library provides a foundational JSON parser, encompassing both lexical analysis (Lexer) and syntax analysis (Parser) phases. It aims to help developers understand the internal mechanisms of JSON parsing and offers a simple, easy-to-use tool for handling JSON data.

## **Features**

* Supports all fundamental JSON types: objects, arrays, strings, numbers, booleans, and null.  
* Handles escape sequences within strings.  
* Includes error handling for inputs that do not conform to the JSON specification.  
* Parses JSON strings into a type-safe JsonValue sealed class hierarchy.

## **How to Use (As a Dependency)**

If you wish to use this library in your Kotlin project, you need to add it as a dependency to your build.gradle.kts or build.gradle file.

### **1\. Add GitHub Packages Repository**

First, add the GitHub Packages repository configuration to your build.gradle.kts (Kotlin DSL) or build.gradle (Groovy DSL). Please replace YOUR\_GITHUB\_USERNAME and YOUR\_REPO\_NAME with your actual GitHub username and repository name (e.g., 65zlui and kotlin-json-parser).

**Kotlin DSL (build.gradle.kts)**:

// build.gradle.kts

repositories {  
    mavenCentral()  
    maven {  
        name \= "GitHubPackages"  
        url \= uri("https://maven.pkg.github.com/65zlui/kotlin-json-parser")  
        // If your package is public, the credentials block is usually not needed.  
        // If it's a private package, authentication information is required (e.g., via gpr.user and gpr.key in \~/.gradle/gradle.properties).  
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
        // If your package is public, the credentials block is usually not needed.  
        // If it's a private package, authentication information is required.  
        credentials {  
            username \= System.getenv("GITHUB\_ACTOR") ?: project.findProperty("gpr.user")  
            password \= System.getenv("GITHUB\_TOKEN") ?: project.findProperty("gpr.key")  
        }  
    }  
}

### **2\. Add Dependency**

Add the following dependency to the dependencies block of your build.gradle.kts or build.gradle file:

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

## **Example Usage**

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

        // Accessing values from a JSON object  
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

## **Build and Publish (For Library Developers)**

If you are a developer of this library and wish to build, run, or publish it:

### **Project Structure**

Ensure your project structure is as follows:



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

### **Gradle Configuration**

The build.gradle.kts file contains all necessary configurations, including the Kotlin JVM plugin, application plugin, and Maven Publish plugin, as well as GitHub Packages publishing settings.

### **Authentication Information**

Create or edit the gradle.properties file in your Gradle user home directory (e.g., \~/.gradle/), and add your GitHub username and Personal Access Token (PAT):

gpr.user=YOUR\_GITHUB\_USERNAME  
gpr.key=YOUR\_PERSONAL\_ACCESS\_TOKEN

### **Commands**

Run the following commands in the project root directory:

* **Compile Project:**  
  ./gradlew build

* **Run Example:**  
  ./gradlew run

* **Publish to GitHub Packages:**  
  ./gradlew publish

  After successful publication, you can find your library in the "Packages" section of your GitHub repository.

## **License**

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT) (example, please choose and link as appropriate).