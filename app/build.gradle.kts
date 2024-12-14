plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.susti"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.susti"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}


dependencies {
    // Core library
    implementation("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.navigation:navigation-compose:2.6.0")
    // Compose
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Lifecycle and ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.3")
    // Dependencia de Jetpack Compose Material (versión estable)
    implementation ("androidx.compose.material:material:1.5.0") // Reemplaza con la versión más reciente disponible.

    // Dependencia para Jetpack Compose UI
    implementation ("androidx.compose.ui:ui:1.5.0")// Asegúrate de que las versiones sean consistentes con las de tu proyecto.

    // Dependencias adicionales para Jetpack Compose)
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation ("androidx.compose.material3:material3:1.0.0") // Para usar Material 3 (si es necesario)
    implementation ("androidx.compose.ui:ui:1.5.0")  // o la última versión disponible
    implementation ("androidx.compose.material3:material3:1.1.0")  // o la última versión disponible
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation ("androidx.compose.foundation:foundation:1.5.0")
    implementation ("androidx.compose.material:material:1.5.0")
    // Dependencia para usar ViewModel (opcional)
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // Dependencia para navegar (si usas Navigation)
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    // Room (si lo usas)
    implementation("androidx.room:room-runtime:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    implementation ("androidx.compose.ui:ui:1.5.3")
    implementation ("androidx.compose.material3:material3:1.2.0-alpha03")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.3")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.activity:activity-compose:1.8.0")
    implementation ("androidx.compose.ui:ui:1.5.0") // Usa la versión más reciente de Compose
    implementation ("androidx.compose.material3:material3:1.2.0")// O material2 si no usas material3

    implementation("androidx.compose.runtime:runtime-livedata:1.5.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // Debugging
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    // Testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    testImplementation("junit:junit:4.13.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")

}