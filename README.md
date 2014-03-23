# rpg

A multiplayer cyberpunk action RPG.

# Contributing

*If you want to contribute, you **MUST** follow this process:*

## Forks & Pull Requests

All contributions must be made via forks and pull requests: So first, you must fork the repository.

When making a change, checkout a new branch based off the most recent version of develop, do your work, then rebase your
changes off the upstream repository before making a pull request. **If you do not make a pull request from a separate
branch, I will close your PR immediately.**

## Commit Messages

Commits must conform to the following format:

```
ComponentName: Short description no longer than 70 characters.

A longer description, if necessary.

Closes #1
```

Should there be any issues associated with the commit you are making, they must be explicitly called out with the
`closes ###` format so that the issue you are working on is automatically closed.

## Reporting Issues

If you are reporting a new issue, add a short description and be sure to add the proper labels to it. It should not be
assigned to anyone unless they are going to start working on it immediately.

# Development

## Setup a JVM env

You should download and install the most recent version of JDK7.

## Setup GVM

This project uses Gradle, which you can get via GVM: http://gvmtool.net/

Once GVM is installed, run:

```
gvm install gradle 1.9
```

## Command-line Usage

On Linux or Mac OS X you invoke gradle like this:

    ./gradlew clean

Note the leading dot slash on Unix like systems. In both cases, the clean task will remove all
build files, e.g. class files previously generated.

#### Running the desktop project
To run the desktop project issue this gradle command:

    ./gradlew desktop:run

#### Packaging the desktop project
To create a ZIP distribution including shell scripts to start your app, issue this gradle command:

    ./gradlew desktop:distZip

This will create a ZIP file in the folder desktop/build/distributions, ready to be send to testers.

### Eclipse Usage
You can let Gradle generate Eclipse projects for your application easily:

    ./gradlew eclipse
    
Once Gradle is done, **delete the .project file in the root directory of the project**. This is a 
little glitch that will get resolved shorty.

Now you can import the projects into Eclipse

  1. File -> Import ... -> General -> Existing projects into Workspace
  2. Click 'Browse', select the root folder of the project. You should see the core, android and desktop project.
  3. Make sure the three projects are checked, then click 'OK'

#### Running/Debugging in Eclipse
To run/debug the desktop project: 
  1. Right click the desktop project in the package viewer
  2. Select either 'Run As' or 'Debug As'
  3. Select 'Java Application'
  
To run/debug the Android project:
  1. Right click the Android project in the package viewer
  2. Select either 'Run As' or 'Debug As'
  3. Select 'Android Application'
  
### Intellij Idea Usage
You can let Gradle generate Intellij Idea project for your application easily:

    ./gradlew idea
    
Once Gradle is done, you can open the projects in Intellij Idea:

  1. File -> Open
  2. Select the .ipr file in the root of the project, Click 'OK'
  
You'll need to set the Android SDK on the Android module before continuing:

  1. File -> Project Structure ...
  2. Select 'modules' in the left side panel
  3. Select the 'android' module in the modules panel
  4. Set 'Module SDK' to the Android SDK you configured for IntelliJ Idea
  
#### Running/Debugging in Intellij Idea
To run/debug the desktop project, first create a run configuration:

  1. Run -> Edit Configurations
  2. Click the plus ('+') in the top left corner, select 'Application'
  3. Set the Name of the configuration, e.g. 'Desktop'
  4. Set the Main Class to 'DesktopLauncher', by clicking on the button on the left of the field
  5. Set the Working Directory to $root/android/assets, by clicking on the button on the left of the field
  6. Set 'Use classpath of module' to 'desktop'
  7. Click 'OK'
  
To run/debug the desktop project, just select the run configuration you just created
and then either click the green play button, or the green play button with the bug.

To run/debug the Android project, first create a run configuration:

  1. Run -> Edit Configurations
  2. Click the plus ('+') in the top left corner, select 'Android Application'
  3. Set the Name of the configuration, e.g. 'Android'
  4. Set 'Module' to 'Android'
  7. Click 'OK'
  
To run/debug the desktop project, just select the run configuration you just created
and then either click the green play button, or the green play button with the bug.
