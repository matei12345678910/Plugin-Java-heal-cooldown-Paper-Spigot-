package ro.matei.heal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class HealPlugin extends JavaPlugin {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final int COOLDOWN_SECONDS = 30;

    @Override
    public void onEnable() {
        getCommand("heal").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Doar jucătorii pot folosi această comandă.");
                return true;
            }

            Player p = (Player) sender;

            if (!p.hasPermission("heal.use")) {
                p.sendMessage(ChatColor.RED + "Nu ai permisiunea necesară.");
                return true;
            }

            long now = System.currentTimeMillis();
            long last = cooldown.getOrDefault(p.getUniqueId(), 0L);

            if (now - last < COOLDOWN_SECONDS * 1000L) {
                long remaining = ((last + COOLDOWN_SECONDS * 1000L) - now) / 1000;
                p.sendMessage(ChatColor.RED + "Poți folosi comanda din nou în " + remaining + " secunde.");
                return true;
            }

            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.sendMessage(ChatColor.GREEN + "Ai fost vindecat!");
            cooldown.put(p.getUniqueId(), now);

            return true;
        });

        Bukkit.getLogger().info("HealPlugin by Matei a fost activat.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("HealPlugin a fost dezactivat.");
    }
}
