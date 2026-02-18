import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinxSerialization)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    jvmToolchain(17)

    androidLibrary {
//        https://developer.android.com/kotlin/multiplatform/plugin
        namespace = "org.example.project.composeApp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

//        compilerOptions {
//            jvmTarget = JvmTarget.JVM_11
//        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            execution = "HOST"
        }
        withJava() // enable java compilation support
//        withHostTestBuilder {}.configure {}
//        withDeviceTestBuilder { sourceSetTreeName = "test" }
        // Configure the JVM target for both Kotlin and Java sources
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

//    @OptIn(ExperimentalKotlinGradlePluginApi::class)
//    dependencies {
//        // commonMain.dependencies
//    }

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.time.ExperimentalTime")
            }
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.kotlinx.datetime)

            implementation(libs.touchlab.kermit)

            implementation(libs.composables.composeunstyled)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)

            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            // Put Android-only libraries here (e.g., Ktor OkHttp engine)
            implementation(libs.ktor.client.okhttp)
        }
        androidUnitTest.dependencies {
            // Put Android-only libraries here (e.g., Ktor OkHttp engine)
        }

        iosMain.dependencies {
            // Put iOS-only libraries here (e.g., Ktor Darwin engine)
            implementation(libs.ktor.client.darwin)
        }
        iosTest.dependencies {
            // Put iOS-only libraries here (e.g., Ktor Darwin engine)
        }

        jvmMain.dependencies {
            // Put Desktop/JVM-only libraries here
        }
        jvmTest.dependencies {
            // Put Desktop/JVM-only libraries here
        }


        jsMain.dependencies {
            // Put JS-only libraries here
        }
        jsTest.dependencies {
            // Put JS-only libraries here
        }

        wasmJsMain.dependencies {
            // Put JS-only libraries here
        }
        wasmJsTest.dependencies {
            // Put JS-only libraries here
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
