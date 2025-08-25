plugins {
    alias(libs.plugins.android.core)
    alias(libs.plugins.android.room)
}

android {
    namespace = "com.jg.voicenote.core.database"
}

dependencies {
    implementation(project(":core:model"))
}
