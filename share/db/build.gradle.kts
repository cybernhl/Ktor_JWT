@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") 
    id("app.cash.sqldelight")
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-resources")
}
version = "1.0"

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
    packagingOptions.resources {
        excludes += setOf(
            "META-INF/*.kotlin_module", "**/attach_hotspot_windows.dll", "META-INF/licenses/**"
        )
        pickFirsts += setOf("META-INF/AL2.0", "META-INF/LGPL2.1")
    }
    testOptions.unitTests.isReturnDefaultValues = true
    namespace = "com.softartdev.notedelight.shared"
}
multiplatformResources {
    multiplatformResourcesPackage = "tw.idv.neo.multiplatform.db"
}
kotlin {
    jvmToolchain(17)
    jvm()
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.coroutines.core)
                implementation(libs.sqlDelight.coroutinesExt)
                
                implementation(libs.kotlin.serialization)
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3")




                api(libs.kotlinx.datetime)
                api(libs.napier)
                api(libs.mokoResources)
                implementation(libs.koin.core)
                api(libs.material.theme.prefs)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.coroutines.test)
                implementation(libs.koin.test)
                implementation(libs.mokoResources.test)
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.coroutines.android)
                api(libs.sqlDelight.android)
                implementation(libs.bundles.androidx.sqlite)
                api(libs.commonsware.saferoom)
                api(libs.android.sqlcipher)
                api(libs.androidx.lifecycle.viewmodel)
                implementation(libs.koin.android)
                implementation(libs.espresso.idling.resource)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.sqlDelight.native)
//                api(libs.sqlcipherKtnPod)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.sqlDelight.jvm)
                //Unresolved reference: coroutinesJvmExt SQLDelight Gradle plugin was applied but there are no databases set up.
//                implementation(libs.sqlDelight.coroutinesJvmExt)
            }
        }
        val jvmTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
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
sqldelight {
    databases {
        create("KtorDb") {
            packageName.set("tw.idv.neo.multiplatform.shared.db")
        }
    }
    linkSqlite.set(false)
}
