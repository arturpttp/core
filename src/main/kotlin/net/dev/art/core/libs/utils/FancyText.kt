package net.dev.art.core.libs.utils

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent

class FancyText(private val message: String) {

    private val textComponent: TextComponent = TextComponent(message)

    fun hover(action: HoverEvent.Action, vararg messages: String): FancyText {
        val message: String = messages.joinToString("\n")
        textComponent.hoverEvent = HoverEvent(
            action,
            ComponentBuilder(message).create()
        )
        return this
    }

    fun hover(action: HoverEvent.Action, message: String): FancyText {
        textComponent.hoverEvent = HoverEvent(
            action,
            ComponentBuilder(message).create()
        )
        return this
    }

    fun click(action: ClickEvent.Action, message: String): FancyText {
        textComponent.clickEvent = ClickEvent(
            action,
            message
        )
        return this
    }

}