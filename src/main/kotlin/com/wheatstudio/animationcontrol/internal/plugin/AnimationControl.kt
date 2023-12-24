package com.wheatstudio.animationcontrol.internal.plugin

import com.aystudio.core.bukkit.plugin.AyPlugin
import com.wheatstudio.animationcontrol.internal.cache.AnimationCache
import com.wheatstudio.animationcontrol.internal.command.AnimationControlCommand
import com.wheatstudio.animationcontrol.internal.listen.PlayerListener
import com.wheatstudio.animationcontrol.internal.manager.CacheManager
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class AnimationControl : AyPlugin() {

    companion object {
        lateinit var instance: AnimationControl
            private set
    }

    override fun onEnable() {
        super.onEnable()
        instance = this
        // loading config
        this.loadConfig()
        // register command
        this.getCommand("animationcontrol")?.executor = AnimationControlCommand()
        // register listeners
        Bukkit.getPluginManager().registerEvents(PlayerListener(), this)
    }

    fun loadConfig() {
        this.saveDefaultConfig()
        this.reloadConfig()

        CacheManager.animationCaches.clear()
        val anmiations = File(this.dataFolder, "animations")
        if (!anmiations.exists()) {
            this.saveResource("animations/example.yml", "animations/example.yml")
        }
        anmiations.listFiles()?.let {
            it.forEach { i ->
                val configuration: FileConfiguration = YamlConfiguration.loadConfiguration(i)
                CacheManager.animationCaches[i.name.substring(0, i.name.length - 4)] = AnimationCache(configuration)
            }
        }
    }
}