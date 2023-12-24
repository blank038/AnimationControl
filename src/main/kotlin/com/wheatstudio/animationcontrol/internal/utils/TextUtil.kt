package com.wheatstudio.animationcontrol.internal.utils

import com.wheatstudio.animationcontrol.internal.plugin.AnimationControl
import org.bukkit.ChatColor

object TextUtil {

    fun get(key: String): String {
        return get(key, false)
    }

    fun get(key: String, boolean: Boolean): String {
        var message: String = AnimationControl.instance.config.getString(key, "")
        if (boolean) {
            AnimationControl.instance.config.getString("message.prefix", "").let { prefix ->
                message = "$prefix$message".takeIf { it.isNotEmpty() } ?: message
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}