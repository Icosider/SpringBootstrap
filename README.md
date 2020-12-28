<p align="center"><img width=12.5% src="https://gitlab.com/minecraftforge/springbootstrap/-/raw/master/src/main/resources/logo.png"></p>

# Spring bootstrap for MinecraftForge

One of the main features of the Spring framework is the IoC (Inversion of Control) container. The Spring IoC container is responsible for managing the objects of an application. It uses dependency injection to achieve inversion of control.

It makes our development easier, faster and prettier, why don't we use this when developing mods?

###### Implemented for 1.16.4, should work on 1.14 and higher

## Table of Contents

- [Features](#features)
- [Introduction](#introduction)
- [How To Use](#How To Use)
- [License](#license)
- [Setup](#setup)
- [Build](#build)
- [Gradle](#gradle)

## Features

* Inversion of Control container and other Spring Context features
* Automatic registration of events in components implement as Listener
* Spring @Scheduler works with initialDelay and fixedRate based on Minecraft Tick
* It's simple and works like a clock 

## Introduction

SpringBootstrap will give the average user absolutely nothing,
but if you are a developer, then all the possibilities [AnnotationConfigApplicationContext](https://www.baeldung.com/spring-application-context)  will be open to you

## How To Use

##### Before we start, make sure we have everything we need.

1. In order to work with this, you need minimal knowledge in
   * Java 8
   * [MinecraftForge](https://mcforge.readthedocs.io/en/1.16.x/)  
   * [Spring 5](https://spring.io/quickstart/) (optional, you can learn in practice)
2. Include our dependencies in your minecraft forge mod (see [Gradle](#gradle))

##### Now we can proceed directly to our mod

All we need to do is extend our mod class as SpringMod.

```java
@Mod("mymod")
public class MyMod extends SpringMod {
}
```

If for some reason, your mod's structure is not in accordance with java standards, you will need to manually specify the parent package for class scanning, then the main class of your mod will look like this

```java
public class MyMod implements SpringContextHolder {
    private AnnotationConfigApplicationContext context;

    public MyMod() {
        MinecraftForgeSpringContextInitializer.register(this, "com.example.mymodpackage");
    }

    @Override
    public AnnotationConfigApplicationContext getContext() {
        return context;
    }

    @Override
    public void setContext(AnnotationConfigApplicationContext ctx) {
        if (context == null) context = ctx;
    }
}
```

But I hope that the main class of your mod is already in the parent package.

Example of correct structure:

```ignorelang
    > com.example.mymod         << PARENT PACKAGE
        > firstawesomepacket    << JUST PACKAGE
            AwesomeClass        << JUST CLASS
        > secondawesomepacket   << JUST PACKAGE
        MyMod                   << MOD CLASS
```

Example of incorrect structure:

```ignorelang
    > com.example.mymod         << PARENT PACKAGE
        > firstawesomepacket    << JUST PACKAGE
            AwesomeClass        << JUST CLASS
        > secondawesomepacket   << JUST PACKAGE
            MyMod               << MOD CLASS
```

That's all, there is nothing complicated here. Now you can use Spring in your mod.

###### If asked, I will leave a link for an example of working with Spring

## License

Distributed under the WTFPL2. See the [LICENSE](https://gitlab.com/minecraftforge/springbootstrap/-/blob/master/LICENSE) file for more information.

## Setup

To set up the SpringBootstrap development environment you must clone the repo and initialize the submodule. 

```bash
git clone https://gitlab.com/minecraftforge/springbootstrap.git
```

After the project has been cloned and initialized you can directly import it into your IDE of choice.

## Build

Building the project is as easy as running a Gradle command!
Simply run:

```bash
gradlew build
```
and the outputted `.jar` files will be put in `build/libs/`.

## Gradle

We keep our repository on [github](https://github.com/CKATEPTb/repo/tree/minecraftforge) as maven, to use this builds in your project, simply use the following code in your build.gradle

```gradle
repositories {
    mavenCentral()
    maven { url 'http://raw.github.com/CKATEPTb/repo/minecraftforge' }
}

dependencies {
    compileOnly 'ru.ckateptb:springbootstrap:[VERSION]'
}
```

Just replace `[VERSION]` with the [latest released version](https://github.com/CKATEPTb/repo/tree/minecraftforge/ru/ckateptb/springbootstrap)