plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.medhelp.medhelp"
        minSdk = 21
        targetSdk = 32
        versionCode = 72
        versionName = "1.2.1"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

}

dependencies {
    implementation(project(":shared"))
    implementation (fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation (fileTree(mapOf("dir" to "libs", "include" to listOf ("*.aar"))))

    implementation("com.google.android.material:material:1.6.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    implementation ("androidx.core:core-ktx:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0-native-mt")

    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.material:material:1.6.0")
    implementation ("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation ("androidx.exifinterface:exifinterface:1.3.3")
    implementation ("androidx.media:media:1.6.0")

    //    RxJava
    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.code.gson:gson:2.9.0")

    implementation ("com.thoughtbot:expandablerecyclerview:1.4")
    implementation ("com.prolificinteractive:material-calendarview:1.4.3")
    implementation ("com.amitshekhar.android:rx2-android-networking:1.0.2")
    implementation ("io.github.luizgrp.sectionedrecyclerviewadapter:sectionedrecyclerviewadapter:1.1.3")

    //    Logger
    implementation ("com.jakewharton.timber:timber:5.0.1")

    //Firebase
    implementation (platform("com.google.firebase:firebase-bom:30.0.0"))
    implementation ("com.google.firebase:firebase-crashlytics")
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-messaging")
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth:21.0.4")

    //просмотр pdf
    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation ("com.mindorks.android:prdownloader:0.6.0")
    //всплывающие подсказки
    implementation ("it.sephiroth.android.library.targettooltip:target-tooltip-library:2.0.3")
    //изображения с зумом
    implementation ("com.davemorrissey.labs:subsampling-scale-image-view:3.10.0")
    //RapidFloatingActionButton
    implementation ("com.github.wangjiegulu:rfab:2.0.0")
    //для оплаты
    implementation ("ru.yoomoney.sdk.kassa.payments:yookassa-android-sdk:6.5.2")
    implementation ("com.google.android.gms:play-services-location:19.0.1")
    implementation ("com.google.android.gms:play-services-wallet:19.1.0")
    implementation ("com.google.android.play:core:1.10.3")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")

    //карусель для отображения списка пользователей
    implementation ("com.github.crosswall:Android-Coverflow:release-v1.0.5")
    implementation ("ru.egslava:MaskedEditText:1.0.5")

    //для реси, что бы сдвигать элементы
    implementation ("com.chauthai.swipereveallayout:swipe-reveal-layout:1.4.1")
    implementation("androidx.work:work-runtime:2.7.1")

   // compileOnly("io.realm.kotlin:library:0.11.1")

}