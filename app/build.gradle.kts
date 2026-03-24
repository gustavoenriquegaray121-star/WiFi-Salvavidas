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

        // CORRECCIÓN: Ahora el nombre coincide exactamente con tu secreto de GitHub
        manifestPlaceholders["MAPS_API_KEY"] = System.getenv("MAPS_API_KEY") ?: ""
    }

    buildTypes {
        release {
            isMinifyEnabled = true 
            isShrinkResources = true
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
    
    @Suppress("DEPRECATION")
    aaptOptions {
        noCompress("png")
    }

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
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    implementation("com.google.android.gms:play-services-ads:23.0.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0") 
    
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0") 
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
