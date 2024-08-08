// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

dependencies {
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.extensions)
}

