import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.ncipollo.transcribe"
version = if (System.getenv("TRANSCRIBE_SNAPSHOT") != null) "1.0.0-SNAPSHOT" else "0.3.5"

kotlin {
    jvm {
        // Set JVM target to Java 11 for main compilation
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    androidLibrary {
        namespace = "io.github.ncipollo.transcribe"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        // Android needs to target JVM 17 to compile with Kotlin 2.2.20
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(
                        JvmTarget.JVM_17,
                    )
                }
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    // Configure Java toolchain for all JVM targets
    // Using JDK 17 allows compiling both Android (JVM 17) and pure JVM (JVM 11)
    jvmToolchain(17)

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.serialization.json)
            implementation(libs.markdown)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        linuxX64Main.dependencies {
            implementation(libs.ktor.client.curl)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmTest.dependencies {
            implementation(libs.serialization.json)
            implementation(libs.mockk)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "transcribe", version.toString())

    pom {
        name = "Transcribe"
        description = "Kotlin MPP library for interacting with Confluence, converting between Atlassian Document Format and Markdown."
        inceptionYear = "2026"
        url = "https://github.com/ncipollo/transcribe"
        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "ncipollo"
                name = "Nick Cipollo"
                email = "njc115@gmail.com"
            }
        }
        scm {
            url = "https://github.com/ncipollo/transcribe.git"
        }
    }
}
