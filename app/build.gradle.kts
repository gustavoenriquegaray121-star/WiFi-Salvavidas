plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gustavo.wifisalvavidas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gustavo.wifisalvavidas"
        minSdk = 24 
        targetSdk = 34
        versionCode = 2 
        versionName = "1.1" 
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Conexión segura con el Secret de GitHub para la API de Google Maps
        manifestPlaceholders["MAPS_API_KEY"] = System.getenv("GOOGLE_MAPS_API_KEY") ?: ""
    }

    buildTypes {
        release {
            isMinifyEnabled = true 
            isShrinkResources = true
            // Desactivamos el proceso de 'crunch' para evitar errores con el logo
            isCrunchPngs = false
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true 
    }
    
    // Configuración para el motor AAPT2 (Corregida para Kotlin DSL)
    @Suppress("DEPRECATION")
    aaptOptions {
        noCompress("png")
    }

    // Evita que el build se detenga por errores menores de recursos o linting
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Librerías base actualizadas para 2026
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Google Play Services actualizados
    implementation("com.google.android.gms:play-services-ads:23.0.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0") 
    
    // Componentes de interfaz de usuario
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0") 
    
    // Pruebas unitarias e instrumentales
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
