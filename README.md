# StrangeAttractorSearcher

https://github.com/user-attachments/assets/4374549b-cbba-493d-9c77-977a5ddc0bf4

A visualizer and generator for strange attractors, written in Java with [libGDX](https://libgdx.com/).

This is project was generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

## Features

- Visualize strange attractors in 3D.
- Find new randomly generated attractors using polynomial maps.
- Save and load attractors to/from files.

## Usage

- Select from a set of predefined attractors, or generate a new one.
- Fine tune the particle visualization.

### Controls

- `R`: Reset the simulation.
- `H`: Toggle the GUI.
- `Left and Right Arrows`: Change the attractor.

### Attractor Search

- Randomizes coefficients of the polynomial map.
- Tune the divergence, convergence, and Lyapunov exponent thresholds.
- Options for Quadratic, Cubic, and Quartic maps.

## Project

### Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

### Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `:tasks --all`: displays the list of all available tasks.
- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.


### Distribution

LibGDX uses [Construo](https://github.com/fourlastor-alexandria/construo) to package releases.
To create a zip archive with a release build, run the following Gradle task:

`lwjgl3:packageWinX64`

This builds application's runnable Windows 64-bit executable with JRE bundled in, which can be found at `lwjgl3/construo/dist`. Other platforms are available as well (but not tested):

- `lwjgl3:packageLinuxX64`
- `lwjgl3:packageMacM1`
- `lwjgl3:packageMacX64`
- `lwjgl3:packageWinX64`

## References

- Formulas and methodology are largely based on the work of Julien C. Sprott in his book ["Strange Attractors: Creating Patterns in Chaos"](https://sprott.physics.wisc.edu/fractals/booktext/SABOOK.PDF).
- ["Random Attractors Found using Lyapunov Exponents"](https://paulbourke.net/fractals/lyapunov/) by Paul Bourke.
- Attractor formulas from Letzte Änderung's [site](http://www.3d-meier.de/tut19/Seite0.html).
