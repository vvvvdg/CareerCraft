package com.xiangqin.careercraft.listener;

import com.xiangqin.careercraft.CareerCraftPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

/**
 * 进度监听（骨架）。
 *
 * <p>P1 会在这里把事件翻译成“成就条件命中”，目前只在 debug=true 时打印，
 * 用来确认服务器上这些事件确实在触发、字段拿得到。
 */
public final class ProgressListener implements Listener {

    private final CareerCraftPlugin plugin;

    public ProgressListener(CareerCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        if (!plugin.debug()) {
            return;
        }
        plugin.getLogger().info("[debug] advancement "
                + event.getPlayer().getName() + " -> " + event.getAdvancement().getKey());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStatistic(PlayerStatisticIncrementEvent event) {
        if (!plugin.debug()) {
            return;
        }
        plugin.getLogger().info("[debug] statistic "
                + event.getPlayer().getName() + " "
                + event.getStatistic() + " " + event.getEntityType()
                + " " + (event.getNewValue() - event.getPreviousValue()));
    }
}
