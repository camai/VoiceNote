plugins {
    alias(libs.plugins.android.core)
}

android {
    namespace = "com.jg.voicenote.core.data"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
}