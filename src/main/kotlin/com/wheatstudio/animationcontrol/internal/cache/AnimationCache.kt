package com.wheatstudio.animationcontrol.internal.cache

import org.bukkit.ChatColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

class AnimationCache(config: FileConfiguration) {
    private val animationList: MutableList<AnimationNode> = mutableListOf()
    val checkItem: String = ChatColor.translateAlternateColorCodes('&', config.getString("check-item"))
    val cooldown: Int = config.getInt("cooldown")

    init {
        config.getList("animations", mutableListOf<Map<String, Any>>())
            .mapNotNull {
                @Suppress("UNCHECKED_CAST")
                it as? Map<String, Any>
            }
            .map {
                val section: FileConfiguration = YamlConfiguration()
                section.addDefaults(it)
                animationList.add(AnimationNode(section))
            }
    }

    fun getAnimationList(): List<AnimationNode> = animationList

    class AnimationNode(section: ConfigurationSection) {
        val animat: String = section.getString("animat")
        val millisecond: Long = section.getLong("millisecond")
        val skill: String = section.getString("skill")
        val connectDuration: Long = section.getLong("connect-duration")
    }
}