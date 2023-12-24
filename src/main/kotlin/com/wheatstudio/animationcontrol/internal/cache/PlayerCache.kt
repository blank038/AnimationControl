package com.wheatstudio.animationcontrol.internal.cache

class PlayerCache {
    var currentAnimation: String? = null  // 存储玩家当前动画
    var index: Int = 0  // 存储动画内子节点索引
    var animatCooldown: Long = 0L  // 存储当前动画子节点冷却结束时间
}