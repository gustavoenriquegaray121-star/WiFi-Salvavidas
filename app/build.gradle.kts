plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gustavo.wifisalvavidas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gustavo.wifisalvavidas"
        minSdk = 24 // Subimos ligeramente a 24 para mejor compatibilidad con las APIs de WiFi modernas
        targetSdk = 34
        versionCode = 2 // Aumentamos el código de versión para que el sistema reconozca la actualización
        versionName = "1.1" // Actualizamos a 1.1 por los cambios en el escaneo
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Activamos la optimización para que la app pese menos y sea más difícil de copiar
            isMinifyEnabled = true 
            isShrinkResources = true
            isCrunchPngs = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        // Actualizamos a Java 17, que es el estándar actual para Android Studio y GitHub Actions
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true // Habilitamos esto por si necesitas usar variables de entorno
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Librerías base actualizadas
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Google Play Services - Versiones optimizadas para 2026
    implementation("com.google.android.gms:play-services-ads:23.0.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0") // Versión más robusta para el GPS
    
    // Interfaz de usuario
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0") // Útil para que las redes se vean en tarjetitas
    
    // Pruebas
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
