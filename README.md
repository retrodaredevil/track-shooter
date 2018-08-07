# track-shooter
A retro shooter style game made with libgdx
## About this game
In this game, you go around a track and rotate to shoot enemies. Each level you go up, the more enemies there are.
After 10 seconds in each level, a Snake enemy comes in that has crazy and unique movement. The only point of this game is to
get the highest score.

## Example:
[Low Quality Video](https://www.youtube.com/watch?v=qpaIXSVZYBI "Track Shooter Demo Video")

![alt text](demo-screenshot.png?raw=true "Demo Screenshot")

## Running the Game
You must have a java 8 jdk installed or $JAVA_HOME must point to a valid java 8 jdk installation. The current
gradle version that is being used does not support java 9 or 10. You must create a local.properties
with ```sdk.dir="your android installation"```

Windows: ```gradlew.bat desktop:run```

*nix: ```./gradlew desktop:run```