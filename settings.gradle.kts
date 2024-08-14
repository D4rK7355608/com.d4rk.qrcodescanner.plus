pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        maven(url = "https://jitpack.io")
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.google.android.gms.oss-licenses-plugin") {
                useModule("com.google.android.gms:oss-licenses-plugin:${requested.version}")
            }
        }
    }
}

@Suppress("UnstableApiUsage") dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        maven(url = "https://jitpack.io")
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

rootProject.name = "QR & Bar Code Scanner Plus"
include(":app")