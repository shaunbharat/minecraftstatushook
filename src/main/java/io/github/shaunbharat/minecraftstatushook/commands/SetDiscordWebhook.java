package io.github.shaunbharat.minecraftstatushook.commands;

import io.github.shaunbharat.minecraftstatushook.MinecraftStatusHook;
import io.github.shaunbharat.minecraftstatushook.utils.Webhook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetDiscordWebhook implements CommandExecutor {
    private final Webhook webhook = MinecraftStatusHook.webhook;

    public SetDiscordWebhook(MinecraftStatusHook plugin) {
        plugin.getCommand("setdiscordwebhook").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        webhook.setDiscordWebhook(args[0]);
        sender.sendMessage("Discord webhook has been set to " + args[0]);
        return true;
    }
}
