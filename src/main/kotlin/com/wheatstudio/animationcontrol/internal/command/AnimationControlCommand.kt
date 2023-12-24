package com.wheatstudio.animationcontrol.internal.command

import com.wheatstudio.animationcontrol.internal.plugin.AnimationControl
import com.wheatstudio.animationcontrol.internal.utils.TextUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class AnimationControlCommand : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command?,
        label: String,
        params: Array<out String>
    ): Boolean {
        if (params.isEmpty()) {
            return true
        }
        when (params[0]) {
            "reload" -> reloadConfig(sender)
            else -> {}
        }
        return true
    }

    private fun reloadConfig(sender: CommandSender) {
        if (sender.hasPermission("animationcontrol.admin")) {
            AnimationControl.instance.loadConfig()
            sender.sendMessage(TextUtil.get("message.reload", true))
        }
    }
}