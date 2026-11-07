# Bloomer Client — initial development build

Target: Minecraft 1.21.4, Fabric Loader 0.19.5, Java 21.

## Current milestone
- Fabric client entry point
- Right Shift GUI keybind
- Basic dark ClickGUI
- Module toggle framework
- HUD Test module
- GitHub Actions build workflow

## Build
The GitHub Actions workflow builds the mod with Java 21 and Gradle 8.12.1.
After a successful run, download the `bloomer-client-1.21.4` artifact.

The expected mod JAR is produced in `build/libs/`.
