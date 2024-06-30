package io.github.shaunbharat.minecraftstatushook.commands;

import io.github.shaunbharat.minecraftstatushook.MinecraftStatusHook;
import io.github.shaunbharat.minecraftstatushook.utils.Webhook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Potd implements CommandExecutor {
    private final Webhook webhook = MinecraftStatusHook.webhook;
    private final io.github.shaunbharat.minecraftstatushook.utils.Potd potd = MinecraftStatusHook.potd;

    public Potd(MinecraftStatusHook plugin) {
        plugin.getCommand("potd").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        sender.sendMessage("Refreshed password of the day: \"" + potd.generatePotd() + "\"");
        return true;
    }
}
