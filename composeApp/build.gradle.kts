import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
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
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            //koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            //ktor client engine
            implementation(libs.ktor.client.okhttp)

            //coroutines
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.multiplatform.settings)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)

            //koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            //navigation
            implementation(libs.navigation.compose)

            //serialization
            implementation(libs.kotlinx.serialization.json)

            //ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json )

            //coroutines
            implementation(libs.kotlinx.coroutines.core)

            //multiplatform-settings
            implementation(libs.multiplatform.settings)

            implementation(libs.material.icons.extended)
            implementation(libs.kotlinx.datetime)

            implementation(libs.adaptive)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            //ktor engine
            implementation(libs.ktor.client.okhttp)
            implementation(libs.multiplatform.settings)
        }

        nativeMain.dependencies {
            //ktor engine
            implementation(libs.ktor.client.darwin)
            implementation(libs.multiplatform.settings)
        }

        wasmJsMain.dependencies {
            //ktor engine
            implementation(libs.ktor.client.js)
            implementation(libs.multiplatform.settings)
        }
        webMain.dependencies {
            //ktor engine
            implementation(libs.ktor.client.js)
            implementation(libs.multiplatform.settings)
        }
        jsMain.dependencies {
            //ktor engine
            implementation(libs.ktor.client.js)
            implementation(libs.multiplatform.settings)
        }
    }
}

android {
    namespace = "dev.bugstitch.socionect"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.bugstitch.socionect"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "dev.bugstitch.socionect.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.bugstitch.socionect"
            packageVersion = "1.0.0"
        }
    }
}
