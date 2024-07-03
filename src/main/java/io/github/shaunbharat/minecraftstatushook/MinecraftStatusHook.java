package io.github.shaunbharat.minecraftstatushook;

import io.github.shaunbharat.minecraftstatushook.commands.Potd;
import io.github.shaunbharat.minecraftstatushook.commands.SetDiscordWebhook;
import io.github.shaunbharat.minecraftstatushook.commands.ToggleRefreshPotd;
import io.github.shaunbharat.minecraftstatushook.listeners.PlayerListener;
import io.github.shaunbharat.minecraftstatushook.utils.Color;
import io.github.shaunbharat.minecraftstatushook.utils.Webhook;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftStatusHook extends JavaPlugin {
    public static Webhook webhook;
    public static io.github.shaunbharat.minecraftstatushook.utils.Potd potd;

    @Override
    public void onEnable() {
        getLogger().info("MinecraftStatusHook has been enabled!");

        saveDefaultConfig();

        webhook = new Webhook(this);
        potd = new io.github.shaunbharat.minecraftstatushook.utils.Potd(this);

        if (webhook.getDiscordWebhook() == null) {
            getLogger().warning("No Discord webhook (discord-webhook) found in config.yml.");
        }

        // Register events
        new PlayerListener(this);

        // Register commands
        new SetDiscordWebhook(this);
        new Potd(this);
        new ToggleRefreshPotd(this);

        webhook.sendEmbed("Minecraft Server Status", "Server has been started", Color.MEDIUM_SEA_GREEN);
    }

    @Override
    public void onDisable() {
        getLogger().info("MinecraftStatusHook has been disabled!");
        webhook.sendEmbed("Minecraft Server Status", "Server is shutting down", Color.RUBY);
    }
}
