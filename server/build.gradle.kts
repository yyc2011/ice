plugins {
    java
    id("org.springframework.boot") version "4.0.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("nu.studer.jooq") version "10.2.1"
}

group = "com.ice"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

sourceSets {
    named("main") {
        java.srcDir("build/generated-src/jooq/main")
    }
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // JOOQ
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    jooqGenerator("com.mysql:mysql-connector-j")

    // Database
    runtimeOnly("com.mysql:mysql-connector-j")

    // Flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // OpenAPI / SpringDoc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // Password hashing (admin login)
    implementation("org.springframework.security:spring-security-crypto")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jooq {
    version.set(dependencyManagement.importedProperties["jooq.version"])
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = "jdbc:mysql://localhost:3306/ice"
                    user = System.getenv("DB_USER") ?: "root"
                    password = System.getenv("DB_PASSWORD") ?: "password"
                }
                generator.apply {
                    name = "org.jooq.codegen.JavaGenerator"
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = "ice"
                        includes = "user|tag|category|article|article_image|article_tag|content_review|notification|book_coin_config|book_coin_log|announcement|report|feature_config|topic|ranking_config|hotrank_daily|sms_code"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = false
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "com.ice.generated.jooq"
                        directory = "build/generated-src/jooq/main"
                    }
                }
            }
        }
    }
}

// 生成 openapi.json 到 ../openapi/ 目录（需服务已启动）
tasks.register<Exec>("generateOpenApi") {
    description = "从运行中的服务导出 openapi.json 到 ../openapi/ 目录"
    commandLine(
        "curl", "-s",
        "http://localhost:8080/v3/api-docs",
        "-o", "../openapi/openapi.json"
    )
    doFirst {
        println("正在从 http://localhost:8080/v3/api-docs 获取 OpenAPI 规范...")
    }
}
