package io.github.shaunbharat.minecraftstatushook.utils;

import io.github.shaunbharat.minecraftstatushook.MinecraftStatusHook;

import java.awt.Color;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Potd {
    private final MinecraftStatusHook plugin;

    public Potd(MinecraftStatusHook plugin) {
        this.plugin = plugin;
        generatePotd();
        // Generate a new password of the day every 24 hours
        plugin.getServer().getScheduler().runTaskLater(plugin, this::generatePotd, TimeUnit.HOURS.toSeconds(24) * 20);
    }

    public String getPotd() {
        return plugin.getConfig().getString("potd");
    }

    public void setPotd(String potd) {
        plugin.getConfig().set("potd", potd);
        plugin.saveConfig();
    }

    public String generatePotd() {
        String potd = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        setPotd(potd);
        MinecraftStatusHook.webhook.sendEmbed("** Password of the Day **", "The password has been set to: \"" + potd + "\"", Color.ORANGE);
        return potd;
    }
}
