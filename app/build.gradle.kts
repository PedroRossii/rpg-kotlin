plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // O plugin do Compose é geralmente adicionado via alias, mas sua declaração está ok.
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
}

android {
    namespace = "com.example.rpg_front"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.meuprojeto"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // É uma boa prática adicionar o test instrumentation runner para testes de UI
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        // Esta versão deve estar alinhada com sua versão do Kotlin (2.0.0), o que está correto.
        kotlinCompilerExtensionVersion = "2.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Dependências Core do Android
    implementation(libs.androidx.core.ktx.v1131)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // --- Compose Bill of Materials (BOM) ---
    // A BOM garante que todas as suas bibliotecas do Compose são compatíveis entre si.
    // Use a versão estável mais recente compatível com sua versão do Kotlin/Compiler.
    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Dependências do Compose (sem especificar a versão, a BOM cuida disso)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Testes de UI com Compose
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // --- Room (Banco de Dados) ---
    val roomVersion = "2.6.1"
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx) // Suporte a Coroutines

    // --- ViewModel (Arquitetura) ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
}