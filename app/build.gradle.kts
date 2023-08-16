import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.devtools.ksp")
}
android {
    compileSdk = 34
    namespace = "com.d4rk.qrcodescanner.plus"
    defaultConfig {
        applicationId = "com.d4rk.qrcodescanner.plus"
        minSdk = 26
        targetSdk = 34
        versionCode = 30
        versionName = "3.1_r1"
        archivesName = "${applicationId}-v${versionName}"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resourceConfigurations += listOf("en", "de", "es", "fr", "hi", "hu", "in", "it", "ja", "ro", "ru", "tr", "sv", "bg", "pl", "uk")
    }
    buildTypes {
        getByName("release") {
            multiDexEnabled = true
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            multiDexEnabled = true
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    bundle {
        storeArchive {
            enable = true
        }
    }
}
dependencies {
    implementation("com.google.android.play:review-ktx:2.0.1")
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.4.0")
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-perf:20.4.0")
    implementation("com.google.android.play:review-ktx:2.0.1")
    implementation("com.googlecode.ez-vcard:ez-vcard:0.12.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.1")
    implementation("com.google.android.gms:play-services-ads:22.2.0")
    implementation("com.android.billingclient:billing:6.0.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-process:2.6.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-rxjava2:2.5.2")
    implementation("android.arch.paging:runtime:1.0.1")
    implementation("android.arch.paging:rxjava2:1.0.1")
    implementation("com.github.yuriy-budiyev:code-scanner:2.3.2")
    implementation("com.github.florent37:singledateandtimepicker:2.2.8")
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.jakewharton.rxbinding2:rxbinding-appcompat-v7-kotlin:2.2.0")
    implementation("com.mayank:simplecropview:1.0.0")
    implementation("commons-codec:commons-codec:1.16.0")
    implementation("dev.turingcomplete:kotlin-onetimepassword:2.4.0")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("me.zhanghai.android.fastscroll:library:1.3.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
    testImplementation("junit:junit:4.13.2")
    ksp("androidx.room:room-compiler:2.5.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}