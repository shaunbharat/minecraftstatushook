package io.github.shaunbharat.minecraftstatushook.listeners;

import io.github.shaunbharat.minecraftstatushook.MinecraftStatusHook;
import io.github.shaunbharat.minecraftstatushook.utils.Color;
import io.github.shaunbharat.minecraftstatushook.utils.Potd;
import io.github.shaunbharat.minecraftstatushook.utils.Webhook;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerListener implements Listener {
    private final MinecraftStatusHook plugin;
    private final Webhook webhook = MinecraftStatusHook.webhook;
    private final Potd potd = MinecraftStatusHook.potd;

    private final Set<UUID> playersWaiting = new HashSet<>();

    public PlayerListener(MinecraftStatusHook plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // No title, to prevent a lot of clutter (no title takes up less space). Allows players to easily scroll and find the latest password of the day.
        // No title in onPlayerJoin and onPlayerLeave.
        webhook.sendEmbed(null, event.getPlayer().getName() + " has joined the server.", Color.SKY_BLUE);

        playersWaiting.add(event.getPlayer().getUniqueId());
        event.getPlayer().sendMessage("Welcome to the server! Enter the password of the day to continue.");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        webhook.sendEmbed(null, event.getPlayer().getName() + " has left the server.", Color.ENDEAVOUR);

        playersWaiting.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (playersWaiting.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (playersWaiting.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (playersWaiting.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            String messageContent = PlainTextComponentSerializer.plainText().serialize(event.message());
            if (messageContent.equals(potd.getPotd())) {
                playersWaiting.remove(event.getPlayer().getUniqueId());
            } else {
                // Cannot kick a player from an async event. Must be passed to the main server thread to be executed.
                Bukkit.getScheduler().runTask(plugin, () -> event.getPlayer().kick(Component.text("Incorrect password")));
            }
        }
    }
}
