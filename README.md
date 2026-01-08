# Particle-Based-Fluid-Simulation

## Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [Setup](#setup)
- [Technical Overview](#technical-overview)

## Overview

This project is a Java particle-based fluid simulation that uses simple physics rules to create the appearance of fluid-like motion. Each particle is affected by forces such as gravity, collisions with walls, and interactions with nearby particles. The simulation runs in real time with rendering in a Swing window, providing smooth and visually appealing motion. Users can interact with the system using mouse input to attract or repel particles dynamically.

## Requirements

Before running this project, ensure that you meet the following requirements:

### Software Dependencies
1. **Java 19.0.0 (or later)**: Be sure to use Java 19 or newer.
2. **Java Swing Libary**: This libary included need features like JFrame, JPanel, and SwingUtilities.

## Setup

### Step 1: Clone the Repository
First, clone or download this repository to your local machine.
```
git clone https://github.com/DaveMorra/Particle-Based-Fluid-Simulation.git
```
Then, navigate to the source directory:
```
cd Particle-Based-Fluid-Simulation/src
```
### Step 2: Compile and Run
Quickly build and run the project in any IDE that supports java (e.g., IntelliJ IDEA, Eclipse, or javac).
Alternatively, you can compile and run it directly from the command line on any system with a built-in Java compiler.

Compile the files below:
```
javac Models/Particle.java Models/UserMouseEvent.java JFrameWindow.java ParticleEnv.java
```

Then, Run the main program:
```
java FluidSimulator.java
```

## Technical Overview
The simulation uses a particle-based physics engine to model fluid-like behavior by updating forces, velocities, and positions for each particle. Particles are organized in a spatial grid to efficiently calculate interactions only with nearby neighbors. Real-time rendering is handled with Swing, and users can interact with the simulation using mouse events to attract or repel particles. Below are three images that demonstrate the simulation in action, showing particle movement, fluid-like interactions, and user-driven effects through mouse input.

 - Rendering: Swing (JFrame + JPanel)
 - Simulation Loop: Updates particles in real time at (Default: 120FPS)
 - Physics: Position, velocity, and force calculations
 - Interaction: UserMouseEvent to pass mouse input into the environment

![alt text](https://github.com/DaveMorra/Particle-Based-Fluid-Simulation/blob/main/img/image1.png)
![alt text](https://github.com/DaveMorra/Particle-Based-Fluid-Simulation/blob/main/img/image2.png)
![alt text](https://github.com/DaveMorra/Particle-Based-Fluid-Simulation/blob/main/img/image3.png)
