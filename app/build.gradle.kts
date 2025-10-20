plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // O plugin do Compose é geralmente adicionado via alias, mas sua declaração está ok.
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
}

android {
    namespace = "com.example.meuprojeto"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.meuprojeto"
        minSdk = 21
        targetSdk = 34
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
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    // --- Compose Bill of Materials (BOM) ---
    // A BOM garante que todas as suas bibliotecas do Compose são compatíveis entre si.
    // Use a versão estável mais recente compatível com sua versão do Kotlin/Compiler.
    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Dependências do Compose (sem especificar a versão, a BOM cuida disso)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Testes de UI com Compose
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // --- Room (Banco de Dados) ---
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Suporte a Coroutines

    // --- ViewModel (Arquitetura) ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
}