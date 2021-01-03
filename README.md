<p><img width=12.5% alt="" src="https://gitlab.com/minecraftforge/springbootstrap/-/raw/master/src/main/resources/logo.png"></p>

# Spring bootstrap for MinecraftForge

One of the main features of the Spring framework is the IoC(Inversion of Control) container.
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
2. Include our dependencies in your mod(see [Gradle](#Gradle))

##### Now you can go directly to your mod

All we need to do is extend our mod class as SpringMod.

```java
@Mod("mymod")
public class MyMod extends SpringMod {
}
```

If for some reason the structure of your mod is not up to standards,
you need to manually specify the parent package for class scanning,
then the main class of your mod will look like this

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
        if(context == null) context = ctx;
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
[gitlab](https://gitlab.com/minecraftforge/springbootstrap),
to use these assemblies in your project use the following
code in `build.gradle`

```gradle
repositories {
    mavenCentral()
    maven { url 'https://gitlab.com/api/v4/projects/23209488/packages/maven' }
}

dependencies {
    compileOnly 'ru.ckateptb: springbootstrap: [VERSION]'
}
```

Just replace `[VERSION]` with [latest version](https://gitlab.com/minecraftforge/springbootstrap/-/blob/master/gradle.properties#L2)

## What it is

First, let's take a quick look at what Spring is and what it is eaten with.

Scary acronyms like IoC and DI that you may have come across are all about him, about Spring!

Let's start from the very beginning of `org.springframework.context.ApplicationContext`, then just a container.
It creates and stores instances of your classes. Many of you didn't even understand what that means,
but that's not a reason to stop reading right now! You will understand this along the way, with basic examples.

In order for Spring to create a container with our instances, it needs to know from which(classes / objects)
your application will be composed, how they are created and what dependencies they have.

### What are the containers and how to create them

The ApplicationContext interface has many implementations:
   * `ClassPathXmlApplicationContext`
   * `FileSystemXmlApplicationContext`
   * `GenericGroovyApplicationContext`
   * `AnnotationConfigApplicationContext`
   * even `StaticApplicationContext`
   * as well as some others.
   
The modern way of configuring are annotations(`AnnotationConfigApplicationContext`), and that's what we use.

With this mod, you do not need to create a container, because it is already created in it.
If you really want to know how to create one, the internet is full of information on this.

### Container in a nutshell

Spring is usually just an IoC container that helps structure Java applications.
You should know that the word "Spring" really hides the whole world.

The IoC container is a great way to piece together an application from different components.
Spring provides convenient ways to both write chunks of data and combine them into a single application.

For example, we have two classes:

Service:

```java
public class MyService {
    private ServiceDependency dependency;

    public MyService(ServiceDependency dependency) {
        this.dependency = dependency;
    }

    public void setDependency(ServiceDependency dependency) {
        this.dependency = dependency;
    }

    public ServiceDependency getDependency() {
        return this.dependency;
    }

    public void usefulWork() {
        this.dependency.dependentWork();
    }
}
```

Addiction:

```java
public class ServiceDependency {
    // fields

    public ServiceDependency() {
    }

    public void dependentWork() {
        // any actions
    }
}
```

The easiest way to combine these components into a single application is to write something like:

```java
@Mod("mymod")
public class MyMod {
    public MyMod() {
        ServiceDependency dep = new ServiceDependency();
        MyService service = new MyService(dep);
        service.usefulWork();
    }
}
```

Despite its simplicity, this code has serious flaws that are critical for large projects.
Indeed, in this example, it is quite obvious that an instance of the ServiceDependency class must be created earlier,
than an instance of the MyService object. And in large projects there can be so many such services and dependencies,
that the programmer's enumeration of the order of creating objects would take quite indecent time.

Personally, I would like to save my time and not do what in fact can not be done!
I would like not to even think about creating objects, their order, and so on.

This is where Spring comes to the rescue, or to be more precise, Spring Context.
Together with Spring, Lombok comes to the rescue in this task.

If your project still doesn't have Lombok for some reason, then let's add it right now!

```gradle
// Add Lombok to our project
dependencies {
    compileOnly 'org.projectlombok: lombok: 1.18.12'
    annotationProcessor 'org.projectlombok: lombok: 1.18.12'
}
```

Let's see with an example how Spring and Lombok make our life easier!

Let's modify our classes a little by adding so-called stereotype annotations.

```java
@Getter
@Setter
@Service
@AllArgsConstructor
public class MyService {
    private ServiceDependency dependency;

    public void usefulWork() {
        this.dependency.dependentWork();
    }
}
```

Now I will tell you about the annotations that we used in our service.

   * `@Service` - Tells Spring that this is a service class, and that in turn will automatically create and save its object in the container
   * `@AllArgsConstructor` - This is already an annotation from Lombok, which automatically creates a constructor, with all the variables declared in our class
   * `@Getter` - Says that all variables declared in our class must have a` get` method
   * `@Setter` - Similar to` @Getter`, only with the `set` method

   As a result, at the output we will get the same class as indicated above, but writing an order of magnitude fewer lines. Fine!
    
```java
@Component
@NoArgsConstructor
public class ServiceDependency {
    public void dependentWork() {
        // any actions
    }
}
```

Now let's look at these annotations

   * `@Component` - Tells Spring that this is a component for our service and that it will behave the same as with the` @Service` annotation
   * `@NoArgsConstructor` - This is also an annotation from Lombok, but it also creates an empty constructor
   
   Well, the situation repeated itself, we again got the result we needed by writing an order of magnitude fewer lines.
   
Of course, the class of this service should handle, in our case, this is the class of our mod:

```java
@Mod("mymod")
public class MyMod extends SpringMod {
    public MyMod() {
        getContext(). getBean(MyService.class) .usefulWork();
    }
}
```

And that's it! Note that no new is written here for our objects.

Also, I want to add that the annotations are `@Component`,` @Repository`, `@Controller`,` @Configuration`
In practice, they will have the same effect for you, so it makes no difference how you annotate your class.
This is done so that you understand what's what.

Logically:
   * `@Service` - that this class is a service for something
   * `@Repository` - reads / writes information(for example, from / to a file)
   * `@Controler` - controls requests
   * `@Configuration` - configuring our application
   * `@Component` - well ... If all else fails, then use this annotation

But in fact, you implement all this yourself, so you can mark the class as you like.

All of the listed annotations are inherited from the `@Component` annotation, and the annotated
classes are usually called components

## How to add an existing object that was created from outside to the container?

There is nothing complicated here either, and there are two whole solutions!

The first and I would say the correct solution sounds like this:

We need to create a configuration helper class for our application with the `@Configuration` annotation, if not already there.
Then in it we create a method that returns the object we need and annotate it as `@Bean`
And that's all, our need is satisfied!

Let's look at an example, let's say we want to save an object of another mod into a container in order to further automate work with it.

```java
@Configuration
@NoArgsConstructor
public class MyConfiguration {
    @Bean
    public OtherMod getOtherMod() {
        return OtherMod.getInstance();
    }
}
```

That's it, now we can use this in a container to automate the coding routine.

It is worth noting that the moment these annotations are processed is triggered during `FMLCommonSetupEvent` and
it may be that at this moment the target object has not yet been created, here the second option will help us.

Let's set ourselves the task of adding a server to our container, here's how we do it:

```java
@Mod("mymod")
public class MyMod extends SpringMod {
    public MyMod() {
        FMLJavaModLoadingContext.get(). GetModEventBus(). AddListener(this :: onServerStartingEvent);
    }

    public void onServerStartingEvent(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        getContext(). getBeanFactory(). registerResolvableDependency(server.getClass(), server);
    }
}
```

By the way, I will say that you do not need to add the server to the container, this mod has already done it!
And also, any mod inherited from SpringMod is also in the container.

## How to access the container object from outside?

There are situations like this, first we need to get the container itself, for this
Let's create a static instance of our mod.

```java
@Mod("mymod")
class MyMod extends SpringMod {
    @Getter
    private static MyMod instance;
    
    public MyMod() {
        instance = this;
    }
}
```

Now we can use `MyMod.getInstance(). GetContext(). GetBean(OtherMod.class)`,
where `OtherMod.class` is the class of the object we would like to receive.

## How to distinguish several objects of the same class

We will also consider this issue with an example.
By default, there are 3 worlds in Minecraft, these are normal, nether and end, let's say we want to add each of them to the container,
but all worlds are World objects. Here we need the already familiar configuration helper class

```java
@Configuration
@NoArgsConstructor
public class MyConfiguration {
    @Bean("normal")
    public World getNormalWorld() {
        return normal;
    }

    @Bean("nether")
    public World getNetherWorld() {
        return nether;
    }

    @Bean("end")
    public World getEndWorld() {
        return end;
    }
}
```

Now we need to indicate what kind of world we need. Let's say we want to get a nether.

```java
@Getter
@Setter
@Component
public class NetherWorldHolder {
    private final World nether;
    public NetherWorldHolder(@Qualifier("nether") World nether) {
        this.nether = nether;
    }
}
```

I don't think it's worth explaining how the @Qualifier annotation works, because everything is intuitively clear with an example!

## How to register an event in a component

Besides the fact that you can do this as stated in Forge, you can do this
extending your component with the Listener interface. This will enable you to quickly
registering events is not static, as Forge suggests.

```java
@Service
@NoArgsConstructor
public class MyService implements Listener {
    public void tick() {
        // ...
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(TickEvent.ServerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            this.tick();
        }
    }
}
```

## How to use the Scheduler mentioned in the features

It is a classic to execute some algorithm with a certain interval or with a delay!
We have a convenient solution for this, sharpened for Minecraft ticks
To do this, you just need to annotate the method that needs to be repeated,
like `@Schedule` and specify a delay in ticks before` initialDelay` execution
or the `fixedRate` interval. You can specify both, then `initialDelay`
will only work before the first execution.

```java
@Service
@NoArgsConstructor
public class MyService {
    // (20 ticks = 1 second)
    // This method after 2 seconds
    // will be executed every second
    @Schedule(initialDelay = 40, fixedRate = 20)
    public void processEverySecondWithTwoSecondInitialDelay() {
        // ...
    }
}
```

the end
=

You've just scratched the surface of the basics of Spring, and this is just the basics, as mentioned earlier,
the whole world hidden under the word "Spring"!
If you have a desire to explore this world, then the Internet is full of material on one of the most popular java frameworks.

Spring is so big! It's impossible to describe it here, but it's enough to get you started!

###### SpringBootstrap will speed up your mods writing and take over the automation of routine processes