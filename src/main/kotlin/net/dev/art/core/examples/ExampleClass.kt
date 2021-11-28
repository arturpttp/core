package net.dev.art.core.examples

import net.dev.art.core.libs.PluginCore
import net.dev.art.core.libs.interfaces.AutoRegistrable

/*Created in 07:07 at 25/11/2021 in Programming dreams By artur*/
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