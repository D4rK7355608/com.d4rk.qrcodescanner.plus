plugins {
    alias(notation = libs.plugins.androidApplication)
    alias(notation = libs.plugins.jetbrainsKotlinAndroid)
    alias(notation = libs.plugins.googlePlayServices)
    alias(notation = libs.plugins.googleFirebase)
    alias(notation = libs.plugins.compose.compiler)
    alias(notation = libs.plugins.devToolsKsp)
    alias(notation = libs.plugins.about.libraries)
}

android {
    compileSdk = 35
    namespace = "com.d4rk.qrcodescanner.plus"
    defaultConfig {
        applicationId = "com.d4rk.qrcodescanner.plus"
        minSdk = 26
        targetSdk = 35
        versionCode = 30
        versionName = "4.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resourceConfigurations += listOf(
            "en" ,
            "de" ,
            "es" ,
            "fr" ,
            "hi" ,
            "hu" ,
            "in" ,
            "it" ,
            "ja" ,
            "ro" ,
            "ru" ,
            "th" ,
            "tr" ,
            "sv" ,
            "bg" ,
            "pl" ,
            "uk" ,
            "pt-rBR" ,
        )
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
        }

        debug {
            isDebuggable = true
        }
    }

    buildTypes.forEach { buildType ->
        with(buildType) {
            multiDexEnabled = true
            proguardFiles(
                getDefaultProguardFile(name = "proguard-android-optimize.txt") ,
                "proguard-rules.pro"
            )
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
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    bundle {
        storeArchive {
            enable = true
        }
    }
}

dependencies {
    // App Core
    implementation(dependencyNotation = "com.github.D4rK7355608:AppToolkit:0.0.18") {
        isTransitive = true
    }

    // Google
    implementation(dependencyNotation = libs.ez.vcard)
    implementation(dependencyNotation = libs.core)
    implementation(dependencyNotation = libs.play.services.ads)
    implementation(dependencyNotation = libs.billing)
    implementation(dependencyNotation = libs.app.update.ktx)
    implementation(dependencyNotation = libs.review.ktx)
    implementation(dependencyNotation = libs.play.services.code.scanner)

    // Firebase
    implementation(dependencyNotation = platform(libs.firebase.bom))
    implementation(dependencyNotation = libs.firebase.analytics.ktx)
    implementation(dependencyNotation = libs.firebase.crashlytics.ktx)
    implementation(dependencyNotation = libs.firebase.perf)

    // KSP
    ksp(dependencyNotation = libs.androidx.room.compiler)
    implementation(dependencyNotation = libs.androidx.room.ktx)
    implementation(dependencyNotation = libs.androidx.room.runtime)
    implementation("androidx.room:room-paging:2.6.1") //  TODO: Delete? Maybe!?

    // QR Code Scanner
    implementation(dependencyNotation = libs.code.scanner)

    // trash that should be removed
    //implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    // implementation("com.mayank:simplecropview:1.0.0")
    // implementation("com.github.florent37:singledateandtimepicker:2.2.8")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("com.airbnb.android:lottie:6.5.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("commons-codec:commons-codec:1.17.1")
    implementation("dev.turingcomplete:kotlin-onetimepassword:2.4.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("androidx.paging:paging-runtime-ktx:3.3.2")
}