import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.6"
    id( "org.openapi.generator") version "7.6.0"
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.5.0")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") // Deprecated警告の解消
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("org.app.MainAppKt")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        // Configure compiler options
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_17) // JVMターゲットを17に設定
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}

openApiGenerate {
    // 使用するジェネレーターを指定します。この場合、「kotlin-spring」を使用して、KotlinとSpringに適したコードを生成します。
    generatorName.set("kotlin-spring")

    // OpenAPI仕様ファイルのパスを指定します。$projectDirはプロジェクトのルートディレクトリを指し、openapi/XXX.yamlファイルを指定しています。
    inputSpec.set("$projectDir/openapi/product.yaml")

    // 生成されたコードの出力ディレクトリを指定します。$buildDirはビルドディレクトリを指し、generatedフォルダに出力されます。
    outputDir.set(layout.buildDirectory.dir("generated").map { it.asFile.path })

    // 生成されたAPIコードのパッケージ名を指定します。ここではcom.app.apiパッケージに生成されます。
    apiPackage.set("com.app.api")

    // 生成されたインボーカーコード（APIリクエストを行うコード）のパッケージ名を指定します。ここではcom.app.invokerパッケージに生成されます。
    invokerPackage.set("com.app.invoker")

    // 生成されたモデルクラスのパッケージ名を指定します。ここではcom.app.modelパッケージに生成されます。
    modelPackage.set("com.app.model")
    
    configOptions.putAll(
        mapOf(
            // 日付ライブラリに「java11」を使用するように設定しています。
            "dateLibrary" to "java11",
        )
    )

    // カスタムテンプレートのディレクトリを指定
    templateDir.set("$projectDir/openapi/templates/")

    
}