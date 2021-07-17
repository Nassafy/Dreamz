plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}
kapt {
    javacOptions {
        // These options are normally set automatically via the Hilt Gradle plugin, but we
        // set them manually to workaround a bug in the Kotlin 1.5.20
        option("-Adagger.fastInit=ENABLED")
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}
android {
    signingConfigs {
        create("release") {
            storeFile = file("/home/matthias/keystore")
            storePassword = "Matthi1*"
            keyAlias = "key0"
            keyPassword = "Matthi1*"
        }
    }
    compileSdk = 31

    defaultConfig {
        applicationId = "com.matthias.dreamz"
        minSdk = 31
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
    }
}

dependencies {
    val nav_compose_version = "2.4.0-alpha10"
    val room_version = "2.3.0"
    val hilt_version = "2.39.1"
    val hilt_navigation_compose_version = "1.0.0-alpha03"
    val retrofit_version = "2.9.0"
    val accompanist_version = "0.20.0"
    val work_version = "2.7.0"

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.navigation:navigation-compose:$nav_compose_version")
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    implementation("com.google.dagger:hilt-android:$hilt_version")
    implementation("androidx.hilt:hilt-navigation-compose:$hilt_navigation_compose_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")

    implementation("com.squareup.retrofit2:retrofit:${retrofit_version}")
    implementation("com.squareup.retrofit2:converter-gson:${retrofit_version}")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")

    implementation("com.google.accompanist:accompanist-insets:$accompanist_version")
    // If using insets-ui
    implementation("com.google.accompanist:accompanist-insets-ui:$accompanist_version")
    implementation("com.google.accompanist:accompanist-flowlayout:$accompanist_version")
    implementation("com.google.accompanist:accompanist-swiperefresh:$accompanist_version")
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanist_version")

    implementation("androidx.compose.material:material-icons-extended:${rootProject.extra["compose_version"]}")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation("com.google.accompanist:accompanist-pager:$accompanist_version")

    // If using indicators, also depend on
    implementation("com.google.accompanist:accompanist-pager-indicators:$accompanist_version")

    // (Java only)
    implementation("androidx.work:work-runtime:$work_version")

    // Kotlin + coroutines
    implementation("androidx.work:work-runtime-ktx:$work_version")

    // optional - RxJava2 support
    implementation("androidx.work:work-rxjava2:$work_version")

    // optional - GCMNetworkManager support
    implementation("androidx.work:work-gcm:$work_version")

    // optional - Test helpers
    androidTestImplementation("androidx.work:work-testing:$work_version")

    // optional - Multiprocess support
    implementation("androidx.work:work-multiprocess:$work_version")

    implementation("androidx.hilt:hilt-work:1.0.0")
    // When using Kotlin.
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    //app start up
    implementation("androidx.startup:startup-runtime:1.1.0")

    implementation("androidx.compose.runtime:runtime-livedata:${rootProject.extra["compose_version"]}")
}