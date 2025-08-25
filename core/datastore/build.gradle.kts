plugins {
    alias(libs.plugins.android.core)
}

android {
    namespace = "com.jg.voicenote.core.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)
}
