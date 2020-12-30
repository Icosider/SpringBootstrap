<p><img width=12.5% alt="" src="https://gitlab.com/minecraftforge/springbootstrap/-/raw/master/src/main/resources/logo.png"></p>

# Spring bootstrap for MinecraftForge

One of the main features of the Spring framework is the IoC (Inversion of Control) container.
The IoC container is responsible for managing application objects.

This makes our development easier, faster and more beautiful, why don't we use
is it when developing mods?

# [Download](https://gitlab.com/minecraftforge/springbootstrap/-/pipelines)
> Implemented for 1.16.4, should work on 1.14 and higher

## Contents

- [Features](#Features)
- [Introduction](#Introduction)
- [Instructions](#Instructions)
- [License](#License)
- [Preparation](#Preparation)
- [Build](#Build)
- [Gradle](#Gradle)

## Features

* Full SpringContext support
* Automatic registration of events in components implemented as Listener
* Spring @Scheduler works with initialDelay and fixedRate based on Minecraft ticks
* Incredibly easy to use

## Introduction

SpringBootstrap will give absolutely nothing to a simple user,
but if you are a developer, then all the possibilities of [AnnotationConfigApplicationContext](https://www.baeldung.com/spring-application-context) will be open to you

## Instructions

##### Make sure you have everything you need before starting.

1. To work with this, you need minimal knowledge of
   * Java 8
   * [MinecraftForge](https://mcforge.readthedocs.io/en/1.16.x/)
   * [Spring 5](https://spring.io/quickstart/) (optional, you can learn in practice)
2. Include our dependencies in your mod (see [Gradle](#Gradle))

##### Now you can go directly to your mod

All we need to do is extend our mod class as SpringMod.

```java
@Mod ("mymod")
public class MyMod extends SpringMod {
}
```

If for some reason the structure of your mod is not up to standards,
you need to manually specify the parent package for class scanning,
then the main class of your mod will look like this

```java
public class MyMod implements SpringContextHolder {
    private AnnotationConfigApplicationContext context;

    public MyMod () {
        MinecraftForgeSpringContextInitializer.register (this, "com.example.mymodpackage");
    }

    @Override
    public AnnotationConfigApplicationContext getContext () {
        return context;
    }

    @Override
    public void setContext (AnnotationConfigApplicationContext ctx) {
        if (context == null) context = ctx;
    }
}
```

I hope the main class of your mod is already in the parent package.

An example of a correct structure:

```ignorelang
    > com.example.mymod       << PARENT PACKAGE
        > firstawesomepacket  << JUST PACKAGE
            AwesomeClass      << JUST CLASS
        > secondawesomepacket << JUST PACKAGE
        MyMod                 << MOD CLASS
```

An example of an incorrect structure:

```ignorelang
    > com.example.mymod       << PARENT PACKAGE
        > firstawesomepacket  << JUST PACKAGE
            AwesomeClass      << JUST CLASS
        > secondawesomepacket << JUST PACKAGE
            MyMod             << MOD CLASS
```

Everything, nothing complicated here. Now you can use Spring in your mod.

##### For more detailed instructions see below

## License

Distributed under license WTFPL2. See the [LICENSE](https://gitlab.com/minecraftforge/springbootstrap/-/blob/master/LICENSE) file for more information.

## Preparation

To prepare your SpringBootstrap development environment, you must clone the repository.

```bash
git clone https://gitlab.com/minecraftforge/springbootstrap.git
```

After cloning and initializing the project, you can directly import it into the IDE of your choice.

## Build

Building your project is as easy as running the Gradle command! Just run:

```bash
gradlew build
```

the resulting `.jar` files will be placed in` build/libs/`.

## Gradle

We keep our maven repository at
[github](https://github.com/CKATEPTb/repo/tree/minecraftforge),
to use these assemblies in your project use the following
code in `build.gradle`

```gradle
repositories {
    mavenCentral ()
    maven {url 'http://raw.github.com/CKATEPTb/repo/minecraftforge'}
}

dependencies {
    compileOnly 'ru.ckateptb: springbootstrap: [VERSION]'
}
```

Just replace `[VERSION]` with [latest version](https://github.com/CKATEPTb/repo/tree/minecraftforge/en/ckateptb/springbootstrap)

## What it is

First, let's take a quick look at what Spring is and what it is eaten with.

Scary acronyms like IoC and DI that you may have come across are all about him, about Spring!

Let's start from the very beginning of `org.springframework.context.ApplicationContext`, then just a container.
It creates and stores instances of your classes. Many of you didn't even understand what that means,
but that's not a reason to stop reading right now! You will understand this along the way, with basic examples.

In order for Spring to create a container with our copies