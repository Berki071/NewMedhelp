buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jcenter.bintray.com") }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.android.tools.build:gradle:7.2.1")

        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.6.21")

      //  classpath("io.realm.kotlin:gradle-plugin:0.11.1")
        //classpath("io.realm.kotlin:library-base-android-debug:0.11.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url = uri("https://maven.google.com")}
        maven { url = uri("https://jitpack.io")}
        maven { url = uri("https://maven.fabric.io/public")}
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}