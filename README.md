# track-shooter
A retro shooter style game made with libgdx
## About this game
In this game, you go around a track and rotate to shoot enemies. Each level you go up, the more enemies there are.
After 10 seconds in each level (except level 1 and levels divisible by 4), a Snake enemy comes in that has crazy and unique movement. The only point of this game is to
get the highest score.

Level 10, and every 8 levels after that are "easy" levels.

## Example:
[Low Quality Video](https://www.youtube.com/watch?v=qpaIXSVZYBI "Track Shooter Demo Video")

![alt text](demo-screenshot.png?raw=true "Demo Screenshot")

## Running the Game
You must have at least Java 8 installed. You must create a local.properties with ```sdk.dir="your android installation"```

Windows: ```gradlew.bat desktop:run```

*nix: ```./gradlew desktop:run```

If you are using logcat, this may be useful if the console is being spammed: `(?=^((?!Skip ramp).)*$)(?=^((?!This process).)*$)`


## Conventions
* Use conventions found in `.editorconfig`
* If you come across a file that has mixed spaces and tabs, change the spaces to tabs IF you make other changes in the file
* If you come across a file with `Objects.requireNonNull()` IF you make other changes, change it to use a static import

## Contributing
If you want to get involved, feel free to create an issue about an idea that you have. Pull requests are welcome, but
if you create an issue I can get back to you if I think the pull request will be accepted.

## Why I made this
I've always loved "retro" arcade games. Some of my favorites are Galaga, Pacman, and Xevious. I wanted to create a game
with the "feel" of these games: 3 lives each game and you try to get the high score each time you play it. December 2016 I had
a lot of fun creating a Pokemon based game (in my repos). After that I moved onto Unity for a while (2017), then made a text adventure (late 2017)
and then worked code for a robot (early 2018). After robotics season I knew I needed a new project to work on. While I created this
I wanted to have the same feeling of designing my game from the ground up like I did with the Pokemon game. (Slick2D was great, but outdated) I didn't want
the learning curve of a huge game engine like Unity. I wanted to start programming it right away. That's why I chose LibGDX.
It allowed me to have the freedom of designing the structure of my code, unlike game engines where the code is just a way to script. 
LibGDX is object oriented but it never felt like it forced me to design my code a certain way like other game engines do. 
For instance, with Unity, it forces you to have a very modular scripting approach.
I didn't want to feel like I was scripting my game so that's why I went with LibGDX.
