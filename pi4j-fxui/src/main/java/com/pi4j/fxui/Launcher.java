package com.pi4j.fxui;

/**
 * An additional Launcher class is needed to be able to start the JavaFX application from a JAR file.
 * https://stackoverflow.com/questions/53533486/how-to-open-javafx-jar-file-with-jdk-11
 * https://stackoverflow.com/questions/52653836/maven-shade-javafx-runtime-components-are-missing/52654791#52654791
 */
public class Launcher {

    public static void main(String[] args) {
        Main.main(args);
    }
}
