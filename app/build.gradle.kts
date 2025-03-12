import java.util.Properties
import java.io.FileInputStream
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}


android {
    namespace = "lang.app.llearning"
    compileSdk = 35

    defaultConfig {
        applicationId = "lang.app.llearning"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        val baseUrl = localProperties.getProperty("BASE_URL") ?: "http://default-url.com"
        val serverClientId = localProperties.getProperty("SERVER_CLIENT_KEY")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        buildConfigField("String","SERVER_CLIENT_KEY","\"$serverClientId\"")


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
        buildConfig=true
    }
}

dependencies {
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation("androidx.compose.material3.adaptive:adaptive:1.1.0")
    implementation ("androidx.compose.material3.adaptive:adaptive-layout:1.1.0")
    implementation ("androidx.compose.material3.adaptive:adaptive-navigation:1.1.0")
    implementation("androidx.compose.animation:animation:1.7.8")
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation (libs.converter.gson)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.adaptive.android)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.identity.credentials)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.espresso.core)

    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.credentials)
    implementation (libs.androidx.credentials.play.services.auth)
    implementation(libs.androidx.security.state)



    implementation(libs.googleid)




}