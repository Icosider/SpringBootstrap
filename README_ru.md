<p><img width=12.5% src="https://gitlab.com/minecraftforge/springbootstrap/-/raw/master/src/main/resources/logo.png" alt=""></p>

# Spring bootstrap для MinecraftForge

Одна из основных особенностей среды Spring - это контейнер IoC (Inversion of Control).
IoC-контейнер отвечает за управление объектами приложения.

Это делает нашу разработку проще, быстрее и красивее, почему бы нам не использовать
это при разработке модов?

# [Скачать](https://gitlab.com/minecraftforge/springbootstrap/-/pipelines)
> Реализовано для 1.16.4, должно работать на 1.14 и выше

## Содержание

- [Особенности](#Особенности)
- [Введение](#Введение)
- [Инструкция](#Инструкция)
- [Лицензия](#Лицензия)
- [Подготовка](#Подготовка)
- [Сборка](#Сборка)
- [Gradle](#Gradle)

## Особенности

* Полная поддержка контейнера Spring
* Автоматическая регистрация событий в компонентах реализованных как Listener
* Spring @Scheduler работает с initialDelay и fixedRate на основе тиков Minecraft
* Невероятно прост в использовании

## Введение

SpringBootstrap абсолютно ничего не даст простому пользователю,
но если вы разработчик, то для вас будут открыты все возможности [AnnotationConfigApplicationContext](https://www.baeldung.com/spring-application-context)

## Инструкция

##### Прежде чем начать убедитесь, что у вас есть все необходимое.

1. Чтобы работать с этим, вам нужны минимальные знания в
   * Java 8
   * [MinecraftForge](https://mcforge.readthedocs.io/en/1.16.x/)  
   * [Spring 5](https://spring.io/quickstart/) (по желанию, можно узнать на практике)
2. Включите наши зависимости в свой мод (см. [Gradle](#Gradle))

##### Теперь вы можете перейти непосредственно к вашему моду

Все, что нам нужно сделать, это расширить наш класс мода как SpringMod.

```java
@Mod("mymod")
public class MyMod extends SpringMod {
}
```

Если по какой-то причине структура вашего мода не соответствует стандартам,
вам необходимо вручную указать родительский пакет для сканирования классов,
тогда основной класс вашего мода будет выглядеть так

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

Но я надеюсь, что основной класс вашего мода уже находится в родительском пакете.

Пример правильной структуры:

```ignorelang
    > com.example.mymod         << PARENT PACKAGE
        > firstawesomepacket    << JUST PACKAGE
            AwesomeClass        << JUST CLASS
        > secondawesomepacket   << JUST PACKAGE
        MyMod                   << MOD CLASS
```

Пример неправильной структуры:

```ignorelang
    > com.example.mymod         << PARENT PACKAGE
        > firstawesomepacket    << JUST PACKAGE
            AwesomeClass        << JUST CLASS
        > secondawesomepacket   << JUST PACKAGE
            MyMod               << MOD CLASS
```

Все, ничего сложного здесь нет. Теперь вы можете использовать Spring в своем моде.

##### Более подробную инструкцию смотрите ниже

## Лицензия

Распространяется по лицензии WTFPL2. См. Файл [LICENSE](https://gitlab.com/minecraftforge/springbootstrap/-/blob/master/LICENSE) для получения дополнительной информации.

## Подготовка

Чтобы подготовить среду разработки SpringBootstrap, вы должны клонировать репозиторий.

```bash
git clone https://gitlab.com/minecraftforge/springbootstrap.git
```

После клонирования и инициализации проекта вы можете напрямую импортировать его в выбранную вами среду IDE.

## Сборка

Собрать проект так же просто, как запустить команду Gradle! Просто запустите:

```bash
gradlew build
```

а полученные файлы `.jar` будут помещены в` build/libs/`.

## Gradle

Мы храним наш maven репозиторий на
[gitlab](https://gitlab.com/minecraftforge/springbootstrap),
чтобы использовать эти сборки в своем проекте, используйте следующий
код в build.gradle

```gradle
repositories {
    mavenCentral()
    maven { url 'https://gitlab.com/api/v4/projects/23209488/packages/maven' }
}

dependencies {
    compileOnly 'ru.ckateptb:springbootstrap:[VERSION]'
}
```

Просто замените `[VERSION]` на [последнюю версию](https://gitlab.com/minecraftforge/springbootstrap/-/blob/master/gradle.properties#L2)

## Что это такое

Для начала, давайте поверхностно разберемся, что такое Spring и с чем его едят.

Страшные аббревиатуры, такие как IoC и DI, которые вы возможно встречали - это все про него, про Spring!

Начнем с самых истоков `org.springframework.context.ApplicationContext`, далее просто контейнер.
Он создает и хранит экземпляры ваших классов. Многие из вас даже не поняли, что это значит,
но это не повод переставать читать прямо сейчас! Вы поймете это по ходу, на базовых примерах.

Для того, чтобы Spring создал контейнер с нашими экземплярами, ему нужно знать из каких (классов/объектов)
будет состоять ваше приложение, как они создаются и какие у них есть зависимости.

### Какие бывают контейнер и как их создать

У интерфейса ApplicationContext есть большое количество реализаций:
   * `ClassPathXmlApplicationContext`
   * `FileSystemXmlApplicationContext`
   * `GenericGroovyApplicationContext`
   * `AnnotationConfigApplicationContext`
   * и даже `StaticApplicationContext`
   * а также некоторые другие.
   
Современным способом конфигурирования считаются аннотации (`AnnotationConfigApplicationContext`), и мы используем именно их.

С данным модом, у вас нет необходимости создавать контейнер, ведь он уже создан в нем.
Если вы действительно хотите знать, как его создать, в интернете полно информации по этому поводу.

### В двух словах про контейнер

Под словом «Spring» обычно подразумевают просто IoC-контейнер, помогающий структурировать Java-приложения.
Но вы должны знать, что в действительности под словом «Spring» скрывается целый мир.

IoC-контейнер, это замечательный способ собрать приложение «по кусочкам» из разных компонентов.
Spring предоставляет удобные способы как написания данных «кусочков», так и объединения их в единое приложение.

Например, у нас есть два класса:

Сервис:

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

И его зависимость:

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

Самый простой способ объединить эти компоненты в единое приложение – это написать что-то вроде:

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

Несмотря на простоту, данный код обладает серьезными недостатками, которые являются критическими для больших проектов.
Действительно, в данном примере вполне очевидно, что экземпляр класса ServiceDependency необходимо создавать раньше,
чем экземпляр объекта MyService. А в больших проектах таких сервисов и зависимостей может быть столько,
что перебор программистом порядка создания объектов занимал бы совсем неприличное время.

Лично мне хотелось бы экономить свое время и не делать то, что по факту можно не делать!
Хотелось бы даже не задумываться о создании объектов, их порядке и прочем. 

Здесь и приходит на помощь Spring, а если быть точнее, то Spring Context.
Вместе со Spring, нам в этой задаче приходит на помощь Lombok.

Если в вашем проекте, по какой-то причине все еще нет Lombok, то давайте добавим его прямо сейчас!

```gradle
// Добавляем Lombok в наш проект
dependencies {
    compileOnly 'org.projectlombok:lombok:1.18.12'
    annotationProcessor 'org.projectlombok:lombok:1.18.12'
}
```

Давайте посмотрим на примере, как Spring и Lombok делают нашу жизнь проще!

Модифицируем немного наши классы, добавив так называемые аннотации стереотипов.

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

Сейчас я расскажу вам об аннотациях, которые мы использовали в нашем сервисе.

   * `@Service` - Сообщает в Spring, что это класс сервис, а тот в свою очередь автоматически создаст и сохранит его объект в контейнере
   * `@AllArgsConstructor` - Это уже аннотация из Lombok, которая автоматически создает конструктор, со всеми переменными объявленными в нашем классе
   * `@Getter` - Говорит что у всех, объявленных в нашем классе, переменных должен быть метод `get`
   * `@Setter` - Аналогично `@Getter`, только с методом `set`

   По итогу на выходе мы получим такой же класс, как указали выше, но написав на порядок меньше строк. Отлично!
    
```java
@Component
@NoArgsConstructor
public class ServiceDependency {
    public void dependentWork() {
        // any actions
    }
}
```

Теперь рассмотрим эти аннотации

   * `@Component` - Сообщает в Spring, что это компонент для нашего сервиса и тот поведет себя так же, как и с аннотацией `@Service`
   * `@NoArgsConstructor` - Это тоже аннотация из Lombok, но она же, создает пустой конструктор
   
   Ну вот, ситуация повторилась, мы опять получили необходимый нам результат, написав на порядок меньше строк.
   
Ну и конечно, класс, который должен обработать этот сервис, в нашем случае, это класс нашего мода:

```java
@Mod("mymod")
public class MyMod extends SpringMod {
    public MyMod() {
        getContext().getBean(MyService.class).usefulWork();
    }
}
```

И все! Обратите внимание, что здесь не написано ни одно new для наших объектов.

Также, хочу добавить, что аннотации `@Component`, `@Repository`, `@Controller`, `@Configuration`
На практике для вас будут иметь одинаковый эффект, по этому нет разницы как вы аннотируете ваш класс.
Это сделано, чтобы вы понимали, что к чему.

По логике:
   * `@Service` - что этот класс - сервис для чего-то
   * `@Repository` - читает/записывает информацию (например из/в файл)
   * `@Controler` - контролирует запросы 
   * `@Configuration` - настройка нашего приложение
   * `@Component` - ну... Если все остальное не подошло, то используйте эту аннотацию

Но на деле, все это вы реализуете сами, по этому можете пометить класс как вашей душе угодно.

Все перечисленные аннотации унаследованы от аннотации `@Component`, а аннотированные 
ими классы принято называть компонентами  

## Как добавить в контейнер уже существующий объект, который был создан из вне?

Тут тоже нет ничего сложного и есть целых два решения!

Первое и я бы сказал правильное решение звучит так:

Необходимо создать вспомогательный класс конфигурацию нашего приложения с аннотацией `@Configuration`, если такого еще нет.
Затем в нем мы создаем метод, который возвращает необходимый нам объект и аннотируем его как `@Bean`
И все, наша потребность удовлетворена!

Давайте посмотрим на примере, допустим мы хотим сохранить объект другого мода в контейнер,чтобы в дальнейшем автоматизировать работу с ним.

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

Вот и все, теперь мы может использовать это в контейнере, чтобы автоматизировать рутинное написание кода

Стоит отметить, что момент обработки этих аннотаций срабатывает во время `FMLCommonSetupEvent` и
может быть так, что в этот момент целевой объект еще не создан, тут нам поможет второй вариант.

Давайте поставим перед собой задачу добавить сервер в наш контейнер, вот как мы это сделаем:

```java
@Mod("mymod")
public class MyMod extends SpringMod {
    public MyMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onServerStartingEvent);
    }

    public void onServerStartingEvent(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        getContext().getBeanFactory().registerResolvableDependency(server.getClass(), server);
    }
}
```

К слову скажу, что вам не нужно добавлять сервер в контейнер, этот мод уже сделал это!
А еще, любой мод, унаследованный от SpringMod, также есть в контейнере.

## Как получить доступ к объекту контейнера из вне?

Бывают и такие ситуации, для начала нам необходимо получить сам контейнер, для этого
создадим статичный instance нашего мода.

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

Теперь мы можем использовать `MyMod.getInstance().getContext().getBean(OtherMod.class)`,
где `OtherMod.class` - это класс объекта, который мы хотели бы получить.

## Как отличать несколько объектов одного класса

Этот вопрос мы так-же рассмотрим на примере.
По-умолчанию в Minecraft есть 3 мира, это normal, nether и end, допустим мы каждый из них хотим добавить в контейнер,
но все миры это объекты World. Тут нам понадобиться уже знакомый нам вспомогательный класс конфигурации

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

Теперь нам нужно указать, какой именно мир нам необходим. Допустим мы хотим получить nether.

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

Я думаю не стоит объяснять, как работает аннотация `@Qualifier`, ведь тут все интуитивно понятно на примере!

## Как зарегистрировать событие в компоненте

Помимо того, что вы можете сделать это, как заявлено в Forge, вы можете сделать это
расширив ваш компонент интерфейсом Listener. Это даст вам возможность быстро
зарегистрировать события не статично, как это предлагает Forge.

```java
@Service
@NoArgsConstructor
public class MyService implements Listener {
    public void tick() {
        //...        
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.tick();
        }
    }
}
```

## Как использовать Scheduler о котором говорилось в особенностях

Выполнять какой-то алгоритм с определенным интервалом или с задержкой - классика!
У нас есть удобное решение для этого, заточенное под тики Minecraft
Для этого, нужно просто аннотировать метод, который необходимо повторять,
как `@Schedule` и в тиках указать задержку перед выполнением `initialDelay`
или интервал `fixedRate`. Вы можете указать оба параметра, тогда `initialDelay`
сработает только перед первым выполнением

```java
@Service
@NoArgsConstructor
public class MyService {
    // (20 тиков = 1 секунда)
    // Данный метод спустя 2 секунды 
    // будет выполнен каждую секунду
    @Schedule(initialDelay = 40, fixedRate = 20)
    public void processEverySecondWithTwoSecondInitialDelay() {
        //...
    }
}
```

Конец
=

Вы поверхностно ознакомились с основами Spring, а это только база, как уже упоминалось ранее,
под словом «Spring» скрывается целый мир!
Если у вас есть желание изучить этот мир, то в интернете полно материала по одному из самых популярных java framework'ов

Spring настолько большой, что его попросту нереально описать тут, но этого хватит, чтобы вы могли начать!

###### SpringBootstrap ускорит написание ваших модов и возьмет на себя автоматизацию рутинных процессов