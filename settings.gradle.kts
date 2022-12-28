@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SoMovie"

include(":app")
include(":test:shared:kotlin")
include(":test:shared:android")
include(":test:testfixtures:android")
include(":test:testfixtures:kotlin")

//region core

include(":common")

include(":data:network")
include(":data:database")
include(":data:datastore")
include(":data:mapper")
include(":data:repository")

include(":domain:model")
include(":domain:exception")
include(":domain:usecase")
include(":domain:repository")
include(":domain:util")

include(":ui:common")
include(":ui:component")
include(":ui:theme")

//endregion

//region feature

include(":feature:navigation")
include(":feature:home:common")
include(":feature:home:container")
include(":feature:home:explore")
include(":feature:home:discover")
include(":feature:home:account")
include(":feature:home:watchlist")
include(":feature:moviedetails")
include(":feature:movielist")
include(":feature:login")

//endregion
