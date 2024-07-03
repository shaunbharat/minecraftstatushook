package io.github.shaunbharat.minecraftstatushook.commands;

import io.github.shaunbharat.minecraftstatushook.MinecraftStatusHook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class ToggleRefreshPotd implements CommandExecutor {
    private final io.github.shaunbharat.minecraftstatushook.utils.Potd potd = MinecraftStatusHook.potd;

    public ToggleRefreshPotd(MinecraftStatusHook plugin) {
        plugin.getCommand("togglerefreshpotd").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        potd.setRefreshPotd(!potd.getRefreshPotd());
        return true;
    }
}
