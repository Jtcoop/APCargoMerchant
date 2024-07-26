plugins {
    id("java")
    id("io.github.0ffz.github-packages") version "1.2.1" // This plugin will allow us to authenticate to GitHub Packages automatically.
}

group = "io.github.ccm5"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    /*
     As Spigot-API depends on the BungeeCord ChatComponent-API,
    we need to add the Sonatype OSS repository, as Gradle,
    in comparison to maven, doesn't want to understand the ~/.m2
    directory unless added using mavenLocal(). Maven usually just gets
    it from there, as most people have run the BuildTools at least once.
    This is therefore not needed if you're using the full Spigot/CraftBukkit,
    or if you're using the Bukkit API.
    */
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    // mavenLocal() // This is needed for CraftBukkit and Spigot.
    maven { githubPackage("apdevteam/movecraft")(this) } // This line will use the above plugin to automatically authenticate and add our GitHub Packages Repository.
    maven {
        name = "citizens-repo"
        url = uri("https://maven.citizensnpcs.co/repo")
    }
        maven { url = uri("https://jitpack.io") }

}

dependencies {
    // Pick only one of these and read the comment in the repositories block.
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo.
    compileOnly("net.countercraft:movecraft:+") // Automatically depend on the latest Movecraft
    compileOnly("net.citizensnpcs:citizens-main:2.0.33-SNAPSHOT") {
        exclude( group= "*", module= "*")
    }
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    // compileOnly("com.degitise.minevid:dtlTraders:LATEST")
    compileOnly(fileTree("libs") { include("*.jar") })
}