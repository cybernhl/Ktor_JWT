@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
}
version = "1.0"

kotlin {
    jvmToolchain(17)
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialization)
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3")
                implementation(libs.coroutines.core)
                api(libs.kotlinx.datetime)
                implementation(libs.koin.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.coroutines.test)
                implementation(libs.koin.test)
            }
        }

        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }

    targets.withType<KotlinNativeTarget> {
        binaries.all {
            freeCompilerArgs += listOf(
                "-linker-option", "-framework", "-linker-option", "Metal",
                "-linker-option", "-framework", "-linker-option", "CoreText",
                "-linker-option", "-framework", "-linker-option", "CoreGraphics",
                // TODO: the current compose binary surprises LLVM, so disable checks for now.
                "-Xdisable-phases=VerifyBitcode"
            )
        }
    }
}
