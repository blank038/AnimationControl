package com.wheatstudio.animationcontrol.internal.listen

import com.wheatstudio.animationcontrol.internal.cache.AnimationCache
import com.wheatstudio.animationcontrol.internal.cache.PlayerCache
import com.wheatstudio.animationcontrol.internal.manager.CacheManager
import eos.moe.dragoncore.network.PacketSender
import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class PlayerListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        CacheManager.playerCaches[event.player.name] = PlayerCache()
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        CacheManager.playerCaches.remove(event.player.name)
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_AIR && event.action != Action.LEFT_CLICK_BLOCK) {
            return
        }
        val player: Player = event.player
        // 判断是否为切换交互冷却中
        if (System.currentTimeMillis() <= CacheManager.heldCooldown.getOrDefault(player.name, 0L)) {
            event.isCancelled = true
            return
        }
        // 判断是否为动画子节点冷却中
        val cache: PlayerCache? = CacheManager.playerCaches[event.player.name]
        cache?.currentAnimation?.let {
            if (System.currentTimeMillis() <= cache.animatCooldown) {
                event.isCancelled = true
            } else {
                // 更新玩家动作冷却值, 并播放动作
                val animationcache: AnimationCache? = CacheManager.animationCaches[it]
                animationcache?.let { ac ->
                    event.isCancelled = true
                    val diff: Long = System.currentTimeMillis() - cache.animatCooldown
                    val animationNodes: List<AnimationCache.AnimationNode> = ac.getAnimationList()
                    if (cache.index >= animationNodes.size || diff > animationNodes[cache.index].connectDuration) {
                        cache.index = 0
                    }
                    val animationNode: AnimationCache.AnimationNode = animationNodes[cache.index]
                    cache.animatCooldown = System.currentTimeMillis() + animationNode.millisecond
                    // 播放动作
                    PacketSender.setPlayerAnimation(player, animationNode.animat)
                    // 调用 MythicMobs 技能
                    MythicMobs.inst().apiHelper.castSkill(player, animationNode.skill)
                    cache.index++
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onHeldItem(event: PlayerItemHeldEvent) {
        val cache: PlayerCache? = CacheManager.playerCaches[event.player.name]
        val animation: String? = cache?.currentAnimation
        val heldItem: ItemStack? = event.player.inventory.getItem(event.newSlot)
        animation?.let {
            val animationCache: AnimationCache? = CacheManager.animationCaches[animation]
            val cooldown: Int = animationCache?.cooldown ?: return
            CacheManager.heldCooldown[event.player.name] = System.currentTimeMillis() + (1000L * cooldown)
            cache.currentAnimation = null
        } ?: run {
            // 判断当前切换的是否为物品
            if (heldItem?.type == Material.AIR) {
                return
            }
            val meta: ItemMeta = heldItem?.itemMeta ?: return
            val displayName: String = meta.displayName ?: return
            // 检测物品并更新玩家动作
            CacheManager.animationCaches.forEach { (k, v) ->
                if (v.checkItem == displayName) {
                    cache?.currentAnimation = k
                    return
                }
            }
        }
    }
}