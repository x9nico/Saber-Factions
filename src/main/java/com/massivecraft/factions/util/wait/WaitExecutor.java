package com.massivecraft.factions.util.wait;


import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Factions - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 4/4/2020
 */
public class WaitExecutor {
    private static boolean enabled = false;
    public static ConcurrentHashMap<Player, WaitTask> taskMap = new ConcurrentHashMap<>();

    public static void startTask() {
        if (enabled) return;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FactionsPlugin.instance, () ->
        {
            for (WaitTask task : taskMap.values()) {
                int i = task.getWait() - 1;
                if (i > 0) {
                    if (i != 1) {
                        task.getPlayer().sendMessage(task.getMessage().format((i + " Seconds")));
                    } else {
                        task.getPlayer().sendMessage(task.getMessage().format((i + " Second")));
                    }
                    task.setWait(i);
                } else {
                    task.success();
                    taskMap.remove(task.getPlayer());
                }
            }
        }, 0L, 20L);
        enabled = true;
    }

    public static void handleAction(Player player) {
        if (!taskMap.containsKey(player)) return;
        taskMap.get(player).fail();
        taskMap.remove(player);
    }
}