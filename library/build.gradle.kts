plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("io.deepmedia.tools.deployer") version "0.13.0"
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

deployer {
    // 1. Artifact definition.
    // https://opensource.deepmedia.io/deployer/artifacts
    content {
        kotlinComponents()
    }

    // 2. Project details.
    // https://opensource.deepmedia.io/deployer/configuration
    projectInfo {
        description = "A lightweight library to play with canvas particle emitter"
        url = "https://github.com/PiotrPrus/ComposeParticleEmitter"
        scm.fromGithub("PiotrPrus", "ComposeParticleEmitter")
        license(apache2)
        developer("PiotrPrus", "prus.piotr@gmail.com", "PiotrPrus", "https://github.com")
        groupId = "dev.piotrprus"
    }

    // 3. Central Portal configuration.
    // https://opensource.deepmedia.io/deployer/repos/central-portal
    centralPortalSpec {
        signing.key = secret("SIGNING_KEY")
        signing.password = secret("SIGNING_PASSPHRASE")
        auth.user = secret("UPLOAD_USERNAME")
        auth.password = secret("UPLOAD_PASSWORD")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
}