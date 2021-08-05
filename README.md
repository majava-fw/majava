# Majava
_[Majava Framework](//github.com/majava-fw)_

<p>
    <a href="//github.com/majava-fw/majava/releases"><img src="https://img.shields.io/github/v/release/majava-fw/majava"></a>
    <a href="https://jitpack.io/#majava-fw/majava"><img src="https://img.shields.io/jitpack/v/majava-fw/majava"></a>
    <a href="//github.com/majava-fw/majava/commits/main"><img src="https://img.shields.io/github/last-commit/majava-fw/majava"></a>
    <a href="//github.com/majava-fw/majava/releases"><img src="https://img.shields.io/github/downloads/majava-fw/majava/total"></a>
    <a href="//github.com/majava-fw/majava/blob/main/LICENSE.md"><img src="https://img.shields.io/github/license/majava-fw/majava"></a>
    <a href="//github.com/majava-fw/majava"><img src="https://img.shields.io/github/languages/code-size/majava-fw/majava"></a>
    <a href="//github.com/majava-fw/majava/issues"><img src="https://img.shields.io/github/issues-raw/majava-fw/majava"></a>
    <a href="//java.com"><img src="https://img.shields.io/badge/java-8-orange"></a>
</p>

Java framework improving your experience developing any project in java.

## Summary
1. [Installation](#installation)
    1. [Gradle](#gradle)
    2. [Maven](#maven)  
2. [How to use](#how-to-use)
   1. [Starting the framework](#starting-the-framework)
   2. [Configuration](#configuration)
   3. [Modules](#modules)
      1. [Listeners](#listeners)
      2. [Logging](#logging)
3. [Built With](#built-with)
4. [Authors](#authors)
5. [License](#license)

## Installation
Make sure to replace `%version%` with the latest version number, or a commit hash, e.g. `1.0.0`.
You can find this library [HERE](https://jitpack.io/#majava-fw/majava)

###  Maven
Register the repository
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
Now add the dependency itself
```xml
<dependency>
    <groupId>com.github.majava-fw</groupId>
    <artifactId>majava</artifactId>
    <version>%version%</version>
</dependency>
```
###  Gradle
Register the repository
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
Now add the dependency itself
```gradle
dependencies {
    implementation 'com.github.majava-fw:majava:%version%'
}
```

## How to use
_This part of the README is not finished yet, but we are working hard on finishing it. Sorry for the inconvenience._
### Starting the framework
To start the framework, you just need to boot it up and run with arguments.
Here is an example class:
```java
import Bootstrap;

public class Example {

    public static void main(String[] args) {
        Bootstrap.boot().run(args);
    }

}
```

### Configuration
Everything can be configured using throughout a yaml configuration file.
The default file is `majava.yml` and it is located in resources.<br>
Now let's go through the configuration process step by step.

- **name**: this is the name of your application. it's default value is `Majava`
- **include**: a list of configuration files that will be loaded and merged with the current configuration
- **di**: the method that provides the application with Object database
- **modules**: a map of modules that will be loaded by the application
- ...**module-config**: configuration for each module. the structure depends on the module itself

Here is an example configuration structure:<br>
**majava.yml**
```yaml
name: Majksa's application
di: cz.majksa.Example::di
include:
    - local.yml
modules:
    simple: cz.majksa.SimpleModule
simple: HELLO
logging:
    name: logger name
```
**local.yml**
```yaml
logging:
    debug: true
```

### Modules
The application is easily extendable. You can create your custom modules, register them and even set up its dependencies required for the module to run.

#### Listeners
```yaml
listeners:
    - cz.majksa.handlers.SimpleHandler
```

#### Logging
```yaml
logging:
    debug: true
    name: logger name
```

## Built With

* [Java 8](https://java.com)

## Authors
* [Majksa (@maxa-ondrej)](https://github.com/maxa-ondrej)

## License

This project is licensed under the GPL-3.0 License - see the [LICENSE](LICENSE) file for details