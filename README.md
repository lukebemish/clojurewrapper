# ClojureWrapper

A language loader/provider and wrapper API for Clojure for Forge and Fabric.

## How to use:

Check out the example mod for a minimal working example: [Clojure Example Mod](https://github.com/lukebemish/clojure_example_mod)

The artifacts for the api and loader are available through Jitpack:
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}
```

### Fabric

```gradle
dependencies {
  modApi("com.github.lukebemish.clojurewrapper:clojurewrapper:${project.clojurewrapper_version}") {transitive=false}
  modApi("com.github.lukebemish.clojurewrapper:clojurewrapper-fabric:${project.clojurewrapper_version}") {transitive=false}
  modApi "org.clojure:clojure:${rootProject.clojure_version}"
}
```

Then, in `fabric.mod.json`, set the adapter for the `main` entrypoint:
```json
"main": [
  {
    "adapter": "clojureloader",
    "value": "name.space/function"
  }
]
```

### Forge

With Forge Loom:

```gradle
dependencies {
  modApi("com.github.lukebemish.clojurewrapper:clojurewrapper:${project.clojurewrapper_version}") {transitive=false}
  modApi("com.github.lukebemish.clojurewrapper:clojurewrapper-forgeapi:${project.clojurewrapper_version}:remap") {transitive=false}
  modApi("com.github.lukebemish.clojurewrapper:clojurewrapper-forgeloader:${project.clojurewrapper_version}") {transitive=false}
  forgeRuntimeLibrary "org.clojure:clojure:${rootProject.clojure_version}"
  api "org.clojure:clojure:${rootProject.clojure_version}"
}
```

With Forgegradle:

Much the same way as with Loom, simply using `fg.deobf` instead. You may need to take extra steps to get the clojure standard library to show up on the classpath during runtime in a dev environment.

Then, in `mods.toml`, set:
```
modLoader = "clojureloader"

[[mods]]
modId = "modid"
clojure = "name.space/function"
```
