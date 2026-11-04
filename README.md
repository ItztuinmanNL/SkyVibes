# SkyVibes — Fabric 26.2 (starter project)

This is the first project scaffold for the client-side SkyVibes mod.

## Target versions
- Minecraft Java: 26.2
- Fabric Loader: 0.19.3
- Fabric API: 0.154.2+26.2
- Fabric Loom: 1.17.13
- Gradle: 9.5.1
- Java: 25

## Current status
This is **not yet a finished playable mod or compiled JAR**. It is the initial source project: Fabric client entrypoint, mod metadata, build configuration, and a default config file created on first launch.

## Planned implementation order
1. Sky theme renderer (Galaxy, Sunset, Sakura, Ocean, Rainy, Custom).
2. Floating text renderer with Circle, Spiral, and Dynamic animation modes.
3. Client-side target visuals for PvP (visual-only; no combat automation or gameplay advantage).
4. Font selection and text styling.
5. Lyrics input/synchronization integration.
6. Settings screen and keybind.

## Lyrics note
Spotify does not provide a general public API endpoint for full song lyrics. The lyrics feature will need a lawful lyrics source that permits display, or user-provided timed lyric files. Spotify playback detection and lyric text are separate integrations; this starter does not access Spotify or fetch lyrics.

## Build
Install JDK 25 and Gradle 9.5.1, then run this from the project folder:

```bash
gradle build
```

The resulting mod JAR will be in `build/libs/` after Gradle successfully resolves dependencies and compiles the project.
