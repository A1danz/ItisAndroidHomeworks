plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ru.kpfu.itis.galeev.android.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.kpfu.itis.galeev.android.myapplication"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    val coreKtxVersion = "1.12.0"
    implementation("androidx.core:core-ktx:$coreKtxVersion")

    val appCompatVersion = "1.6.1"
    implementation("androidx.appcompat:appcompat:$appCompatVersion")

    val materialVersion = "1.11.0"
    implementation("com.google.android.material:material:$materialVersion")

    val viewBindingDelegateVersion = "1.5.9"
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:$viewBindingDelegateVersion")

    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    val navVersion = "2.7.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")


    

    // Test section
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}