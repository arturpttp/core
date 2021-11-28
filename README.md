# Core
<img align="right" src="https://media.discordapp.net/attachments/517334221660880907/908178316572377168/ezgif-2-8ced1ea5b417.gif" height="200" width="200">

A core made in kotlin that provides a lot of additional functionalities 
to you minecraft plugin.

## Summary

_Please see the [SpigotAPI Docs](https://www.spigotmc.org/wiki/how-to-learn-the-spigot-api/) for more information about minecraft plugins._

1. [Introduction](#creating-the-main-class)
2. [Storage](#creating-configuration-files)
3. [Auto Registrable](#auto-registrable)
4. [Items](#creating-items)
5. [Commands and Menus](#command-and-menus)

## Creating the main class

The main class is done by extending the class `PluginCore` in your class, there you could do anything that you normally do in `JavaPlugin` class.

```kotlin
class Plugin : PluginCore("Plugin's Name", Plugin::class.java) {

    override fun onEnable() {
        // This method send a console message,
        // it accepts string arrays and multiple strings 
        console(
            "Plugin initialized",
            "Version: 0.0.1",
            "Author: ArturDev"
        )
    }
}
```

## Creating configuration files

To create configuration files normally you should use multiple lines in code
now you could easily create multiple files with one line.

**Create a YML configuration file**

```kotlin
val bukkitConfig: Config = BukkitConfig("messages.yml", this)
```

**Create a JSON configuration file**

```kotlin
val jsonConfig: Config = JsonConfig("messages.json", this)
```

**Examples of uses**

```kotlin
//You could set something in config if not contains easily
config.setIfNotContains("Minigame.name", "Skywars")
//Getting a value from config or returns the default
val minigameName: String = config.getOrDefault("Minigame.name", "Skywars") as String;
//Getting a specific value type 
val minigameName: String = config.getString("Minigame.name");
//Saving the config, you have to do this when you change something in config
config.save()
```

## Auto Registrable

In Core you have the interface `AutoRegistrable`, you just need to implement this to the parent class
and then in your ``main class`` you should do:
````kotlin
registries.add(ExampleClass::class.java)
````

**Example of class**

```kotlin
class ExampleClass : AutoRegistrable{

    override val loadMessage: String = "On load, this will print on console"
    //This is your parent class
    override val type: Class<*> = ExampleClass::class.java
    //Here you should place parent classes, or classes that shouldn't be register
    override val notMatches: MutableList<Class<*>> = mutableListOf(type)

    //This will be called when register
    override fun register(plugin: PluginCore) {
        //
    }
}
```

## Creating items

The Core also provides an easy way to create items, it uses the classic class `ItemBuilder`, you can choose the material, name, lore, data, etc.
### Examples

**Item creating**

```kotlin
val item: ItemStack = ItemBuilder(Material.PAPER)
    .name("§aJournal")
    .glow() //this will make your item glows
    .unbreakable(true) //Make item unbreakable, default false
    .lore(
        "This belongs to an old adventurer that pass here"
    )
```
**Head creation**

```kotlin
val head: ItemStack = ItemBuilder()
    .head("player name") //Here you put the player's name, the head skin will belong to it player 
    .name("&aplayer name".color()) //for all strings you can call function String::color() that replaces & to §
    .lore(
        "&eThis player is &aOnline",
        "&eClick to send a message"
    )
```

## Command and Menus

to easily create commands access [**CommandFramework**](https://github.com/arturpttp/command-framework), it uses the [Auto Registrable](#auto-registrable) system
<br/> <br/>
to easily create Menus and GUIs access [**MenuFramework**](https://github.com/arturpttp/menu-framework).