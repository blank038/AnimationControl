package com.wheatstudio.animationcontrol.internal.manager

import com.wheatstudio.animationcontrol.internal.cache.AnimationCache
import com.wheatstudio.animationcontrol.internal.cache.PlayerCache

object CacheManager {
    val playerCaches: MutableMap<String, PlayerCache> = mutableMapOf()
    val animationCaches: MutableMap<String, AnimationCache> = mutableMapOf()
    val heldCooldown: MutableMap<String, Long> = mutableMapOf()
}