plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.sparkleseditor"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.sparkleseditor"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = file(layout.buildDirectory.dir("../release_key.jks"))
            storePassword = "release_temp"
            keyAlias = "release_temp"
            keyPassword = "release_temp"
        }
        getByName("debug") {
            storeFile = file(layout.buildDirectory.dir("../testkey.keystore"))
            storePassword = "testkey"
            keyAlias = "testkey"
            keyPassword = "testkey"
        }
    }
    buildTypes {
        release{
                isMinifyEnabled = true
                isShrinkResources = false
                proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(platform(libs.editor.bom))
    implementation(libs.editor)
    implementation(libs.language.textmate)
    implementation(libs.language.java)
    implementation(libs.transition)
    implementation(libs.fragment)
    implementation(libs.activity)
    implementation(project(":filetree"))



    //tests
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}