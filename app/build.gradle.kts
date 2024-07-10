plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.playlistmaker"
    compileSdk = 34

    buildFeatures {
        viewBinding=true
    }

    defaultConfig {
        applicationId = "com.example.playlistmaker"
        minSdk = 29
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    val lifecycleVersion = "2.5.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    val activityVersion = "1.6.1"
    implementation ("androidx.activity:activity-ktx:$activityVersion")
    implementation("com.squareup.okhttp3:okhttp:4.7.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.7.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    val koinVersion = "3.3.0"
    implementation ("io.insert-koin:koin-android:$koinVersion")
    val navigationFragmentVersion = "2.5.3"
    implementation ("androidx.navigation:navigation-fragment-ktx:$navigationFragmentVersion")
    val navigationUiVersion = "2.5.3"
    implementation ("androidx.navigation:navigation-ui-ktx:$navigationUiVersion")
}
