// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    compileSdk = 34
    namespace = "tokyo.maigo_name.introduction"

    defaultConfig {
        applicationId = "tokyo.maigo_name.introduction"
        minSdk = 21
        targetSdk = 34
    }
    dataBinding {
        enable = true
    }
    viewBinding {
        enable = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.kotlin.android.extensions)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.recyclerview)
    implementation(libs.kotlin.stdlib)
    implementation(libs.material)
}

