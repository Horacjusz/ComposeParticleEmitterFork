import com.vanniktech.maven.publish.SonatypeHost

val VERSION_NAME: String by project

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.vanniktech.maven.publish") version "0.30.0"
}

android {
    namespace = "dev.piotrprus.composeparticleemitter"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
}

afterEvaluate {
    mavenPublishing {
        publishToMavenCentral(
            host = SonatypeHost.CENTRAL_PORTAL,
            automaticRelease = true
        )
        signAllPublications()
        coordinates("io.github.horacjusz", "composeparticleemitter", VERSION_NAME)

        pom {
            name.set("ComposeParticleEmitter")
            description.set("A lightweight library to play with canvas particle emitter")
            inceptionYear.set("2025")
            url.set("https://github.com/PiotrPrus/ComposeParticleEmitter")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("PiotrPrus")
                    name.set("Piotr Prus")
                    email.set("prus.piotr@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/PiotrPrus/ComposeParticleEmitter")
                connection.set("scm:git:git://github.com/PiotrPrus/ComposeParticleEmitter.git")
                developerConnection.set("scm:git:ssh://git@github.com:PiotrPrus/ComposeParticleEmitter.git")
            }
        }
    }
}
