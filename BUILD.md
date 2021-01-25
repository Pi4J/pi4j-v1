 Pi4J :: Java I/O Library for Raspberry Pi
==========================================================================

## Pi4J BUILD PREREQUISITES

The Pi4J project may require the following tools/build environments to successfully compile
the project:

 - Java 11 (_Any open JDK 11.0.7 or newer distribution.  NOTE:  The latest Oracle JDK11 fail to compile the project._)
 - System environment variable `JAVA_HOME` should be defined   
 - Apache Maven (_3.6.x or newer_)
 - Docker
 - ARM Cross-Compiler GCC Toolchains (`ARMHF` & `AARCH64`)

---

## BUILD Pi4J PROJECT USING DOCKER

If you wish to build the entire Pi4J project without setting up your local build
environment (_JDK, Apache Maven, ARM cross-compiler, etc._), you can use a Docker
container to perform all the required build steps.  You will need to have Docker
installed locally and then execute the following commands to pull down the latest
Pi4J build contain and then perform the build from the root project directory.

```
docker pull pi4j/pi4j-builder:1.4
docker run --user "$(id -u):$(id -g)" --rm --volume $(pwd):/build pi4j/pi4j-builder:1.4
```
(**Note**: _the included bash script file `./build-docker.sh` will also perform the steps listed above._)

---


## BUILD Pi4J PROJECT USING MAVEN

The Pi4J project can be compiled locally using Apache Maven and Java (JDK).  You must have the 
required JDK and Apache Maven installed and configured locally.     


### BUILD Pi4J NATIVE AND JAVA LIBRARIES (Entire Project)

Use the following command to build the Pi4J Java libraries (JARs) the 'libpi4j.so' native
libraries.

```
mvn clean install -P native,docker
-or-
mvn clean install -P native,cross-compile
```
 
The following native libraries will be included for the supported architectures.

- `libpi4j-armhf.so`    (32-bit ARM hard-float)
- `libpi4j-aarch64.so`  (64-bit ARM hard-float)

**Note:** Building the Pi4J native libraries requires a cross-compiler environment to compile the
native libraries for both 32-bit and 64-bit ARM architectures.  Setting up the proper ARM cross-compiler
environments can be complicated and may not be something you wish to install on your local system.  Thus,
by default, the Pi4J native library build will make use of Docker containers to perform the native builds
with the appropriate cross-container toolchains.  This will require Docker be installed on your local
system.  Using these custom Docker containers for building greatly simplifies the build process, eliminates
build issues and provides a consistent and stable environment for building the native libraries.  

Including the `native` profile will instruct Maven to build the native libraries.  Further, including the 
profile `docker` or `cross-compile` will instruct Maven on which method/environment to use to perform 
native library builds.

| Native Build Profile | Description |
| -----------          | ----------- |
| `docker`             | instructs Maven build to use Docker container to build native libraries |
| `cross-compile`      | instructs Maven build to use local cross-compiler toolchains to build native libraries |


### BUILD Pi4J NATIVE LIBRARIES ONLY

Use the following command to build only the Pi4J native libraries.  This build command not build the Pi4J 
Java Libraries (JARs).  

```
mvn clean install --projects pi4j-native -P native,docker
-or-
mvn clean install --projects pi4j-native -P native,cross-compile
```

The following native libraries will be included for the supported architectures.

- `libpi4j-armhf.so`    (32-bit ARM hard-float)
- `libpi4j-aarch64.so`  (64-bit ARM hard-float)

**Note:** Building the Pi4J native libraries requires a cross-compiler environment to compile the
native libraries for both 32-bit and 64-bit ARM architectures.  Setting up the proper ARM cross-compiler
environments can be complicated and may not be something you wish to install on your local system.  Thus,
by default, the Pi4J native library build will make use of Docker containers to perform the native builds
with the appropriate cross-container toolchains.  This will require Docker be installed on your local
system.  Using these custom Docker containers for building greatly simplifies the build process, eliminates
build issues and provides a consistent and stable environment for building the native libraries.

Including the `native` profile will instruct Maven to build the native libraries.  Further, including the
profile `docker` or `cross-compile` will instruct Maven on which method/environment to use to perform
native library builds.

| Native Build Profile | Description |
| -----------          | ----------- |
| `docker`             | instructs Maven build to use Docker container to build native libraries |
| `cross-compile`      | instructs Maven build to use local cross-compiler toolchains to build native libraries |


### BUILD Pi4J JAVA LIBRARIES ONLY

Use the following command to build only the Pi4J Java libraries (JARs).  This build command will not build 
the Pi4J native libraries. Instead, it will use locally cached compiled native libraries or if not found, it  
will download pre-compiled copies from the Maven repository.

> `mvn clean install`

---

## Pi4J PROJECT TEAM MEMBER BUILD INFORMATION (for Pi4J Developers Only)

The following build instructions are for the Pi4J development team members.  These build steps require additional
access permissions, digital signatures and account credentials to sign artifacts, upload artifacts to the Maven 
repositories and upload builds and site content to Pi4J servers.     


### BUILD AND DEPLOY Pi4J SNAPSHOTS

Use the following command to deploy a snapshot build to the OSS Maven SNAPSHOT repository.
(Note, you must have a "sonatype-oss-snapshot-repo" site credentials defined in your private Maven settings.xml file.)

```
mvn clean deploy                                                 (Java libraries only)
mvn clean deploy --projects pi4j-native -P native,docker         (JNI native libraries build using Docker container)
mvn clean deploy --projects pi4j-native -P native,cross-compile  (JNI native libraries build using cross-compiler toolchains)
mvn clean deploy -P native,docker                                (Java libraries and JNI native libraries build using Docker container)
mvn clean deploy -P native,cross-compile                         (Java libraries and JNI native libraries build using cross-compiler toolchains)
```


### REGENERATE NATIVE JNI HEADER FILES

If changes or additions are made to the Java JNI classes, then you may need to regenerate the native JNI .H header files.
Use the following Maven command to regenerate the .H header source file.
(**NOTE:**  _This command must be executed inside the pi4j-native project, not at the parent project level._)

> `mvn generate-sources --projects pi4j-native -P generate-jni-headers`


### DEPLOY Pi4J PACKAGES (Downloadable Artifacts) TO Pi4J CONTENT SERVERS

Use the following Maven command to deploy the downloadable artifacts to the Pi4J downloads page.

> `mvn deploy -Drelease-build --projects pi4j-distribution`


### DEPLOY Pi4J WEBSITE CONTENT TO Pi4J CONTENT SERVERS

Use the following Maven command to locally generate project site documentation:

> `mvn -N clean site`

Use the following Maven command to generate and deploy the project site documentation to Pi4J content servers.
(Note, you must have a `pi4j.aws.access.key` and `pi4j.aws.access.secret` credentials defined in your private 
Maven settings.xml file.

> `mvn -N clean site -Pdeploy-site`


During an official release, include the `publish-site` profile to not only deploy the new site content but to also 
replace the root site `index.html` file to default to the new release version website content.

> `mvn -N clean site -Pdeploy-site,publish-site`


---

## PUBLIC SNAPSHOT DEPLOYMENT NOTES

Use the following series of instructions to publish a public deployment of a **SNAPSHOT** build.  This build command 
will perform the following:

 - compile Pi4J native libraries (_using Docker_) 
 - compile Pi4J java libraries
 - create `ZIP`, `TAR.GZ` and `DEB` packages 
 - upload the packages to Maven SNAPSHOT repository
 - upload the packages to Pi4J download servers
 - generate updated website content for this version/distribution
 - upload generated website content to Pi4J content servers

```
mvn clean deploy -P native,docker,deploy-downloads
mvn -N clean site -Pdeploy-site
```

> **NOTE:** 
>  - In order to upload artifacts and site content, the `pi4j.aws.access.key` and `pi4j.aws.secret.key` must be
>    configured in your maven `settings.xml` file or must be included as arguments on the build command line.


## PUBLIC RELEASE DEPLOYMENT NOTES

Use the following series of instructions to publish a public deployment of a **RELEASE** build.  This build command
will perform the following:

 - compile Pi4J native libraries (_using Docker_)
 - compile Pi4J java libraries
 - create `ZIP`, `TAR.GZ` and `DEB` packages
 - digitally sign DEB package
 - upload the packages to Maven STAGING repository
 - upload the packages to Pi4J download servers
 - create and upload APT package to Pi4J APT repository 
 - generate updated website content for this version/distribution
 - upload generated website content to Pi4J content servers
 - upload Pi4J root site index.html to default to this version/distribution


1.  First, remove "-SNAPSHOT" from all project pom.xml files prior to official release.
2.  Execute the following build commands:

    ```
    mvn clean deploy -Drelease-build
    mvn -N clean site -Pdeploy-site,publish-site
    ```

3.  create git repo tag (`release/x.x`) for release version
4.  send release notification via @Pi4J on Twitter, post release to blog and RaspberryPi forums
5.  Update sources in `master` branch for next SNAPSHOT version

> **NOTE:**
>  - In order to sign the artifacts, the `{pi4j.gpg.key}` and `{pi4j.gpg.passphrase}` properties must be 
>    configured in your maven `settings.xml` file or must be included as arguments on the build command line.
>  - In order to upload artifacts and site content, the `pi4j.aws.access.key` and `pi4j.aws.secret.key` must be 
>    configured in your maven `settings.xml` file or must be included as arguments on the build command line.
