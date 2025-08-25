plugins {
    alias(libs.plugins.android.core)
}

android {
    namespace = "com.jg.voicenote.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    
    implementation(libs.kotlinx.coroutines.android)
}
