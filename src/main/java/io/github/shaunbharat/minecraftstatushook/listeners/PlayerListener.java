package io.github.shaunbharat.minecraftstatushook.listeners;

import io.github.shaunbharat.minecraftstatushook.MinecraftStatusHook;
import io.github.shaunbharat.minecraftstatushook.utils.Color;
import io.github.shaunbharat.minecraftstatushook.utils.Potd;
import io.github.shaunbharat.minecraftstatushook.utils.Webhook;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerListener implements Listener {
    private final MinecraftStatusHook plugin;
    private final Webhook webhook = MinecraftStatusHook.webhook;
    private final Potd potd = MinecraftStatusHook.potd;

    private final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

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
    public void onPlayerChat(AsyncChatEvent event) {
        if (playersWaiting.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            String messageContent = serializer.serialize(event.message());
            if (messageContent.equals(potd.getPotd())) {
                playersWaiting.remove(event.getPlayer().getUniqueId());
            } else {
                // Cannot kick a player from an async event. Must be passed to the main server thread to be executed.
                Bukkit.getScheduler().runTask(plugin, () -> event.getPlayer().kick(Component.text("Incorrect password")));
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        String message = player.getName() + " died (unknown cause).";
        Component deathMessage = event.deathMessage();
        if (deathMessage != null) {
            message = serializer.serialize(deathMessage);
        }

        String xp = event.getDroppedExp() + " XP lost.";
        StringBuilder inventory = new StringBuilder();
        for (ItemStack item : event.getDrops()) {
            String itemType = item.getType().toString();
            int itemAmount = item.getAmount();
            String itemEnchants = item.getEnchantments().toString();
            inventory.append(itemType).append(" x").append(itemAmount).append(" ").append(itemEnchants).append("\n");
        }
        webhook.sendEmbed(null, message + "\n\n" + xp + "\n\nItems:\n\n" + inventory, Color.RUBY);
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
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (playersWaiting.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && playersWaiting.contains(entity.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (playersWaiting.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && playersWaiting.contains(entity.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
