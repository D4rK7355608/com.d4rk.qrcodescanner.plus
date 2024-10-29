plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googlePlayServices)
    alias(libs.plugins.googleOssServices)
    alias(libs.plugins.googleFirebase)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.devToolsKsp)

}

android {
    compileSdk = 34
    namespace = "com.d4rk.qrcodescanner.plus"
    defaultConfig {
        applicationId = "com.d4rk.qrcodescanner.plus"
        minSdk = 26
        targetSdk = 34
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
            multiDexEnabled = true
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt") , "proguard-rules.pro"
            )
        }

        debug {
            multiDexEnabled = true
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt") , "proguard-rules.pro"
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
    implementation("com.googlecode.ez-vcard:ez-vcard:0.12.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation(libs.androidx.navigation.ui.ktx)
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("com.github.yuriy-budiyev:code-scanner:2.3.2")
    // implementation("com.github.florent37:singledateandtimepicker:2.2.8")
    implementation("com.airbnb.android:lottie:6.5.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.jakewharton.rxbinding2:rxbinding-appcompat-v7-kotlin:2.2.0")
    // implementation("com.mayank:simplecropview:1.0.0")
    implementation("commons-codec:commons-codec:1.17.1")
    implementation("dev.turingcomplete:kotlin-onetimepassword:2.4.1")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("me.zhanghai.android.fastscroll:library:1.3.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("androidx.paging:paging-runtime-ktx:3.3.2")
    implementation("androidx.room:room-rxjava2:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
    implementation (libs.coil.compose)

    implementation ("com.google.zxing:core:3.5.1")

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.perf)

    // Google
    implementation(libs.play.services.ads)
    implementation(libs.billing)
    implementation(libs.material)
    implementation(libs.play.services.oss.licenses)
    implementation(libs.review.ktx)
    implementation(libs.app.update.ktx)
    implementation(libs.volley)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.animation.core)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.ui.tooling)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.navigation.compose)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.work.runtime.ktx)

    // KSP
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)

    // Kotlin
    implementation(libs.kotlinx.coroutines.android)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}