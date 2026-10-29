package com.xiangqin.careercraft;

import com.xiangqin.careercraft.achievement.AchievementRegistry;
import com.xiangqin.careercraft.command.CareerCraftCommand;
import com.xiangqin.careercraft.listener.ProgressListener;
import java.util.Objects;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * CareerCraft · 生涯之书
 *
 * <p>骨架阶段职责：加载配置、读取成就表、注册命令与事件监听。
 * 真正的“条件判定 + 发奖”在 P1 实现。
 */
public final class CareerCraftPlugin extends JavaPlugin {

    private AchievementRegistry registry;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.registry = new AchievementRegistry(this);
        this.registry.reload();

        PluginCommand command = Objects.requireNonNull(getCommand("cc"), "plugin.yml 里缺少 cc 命令");
        CareerCraftCommand executor = new CareerCraftCommand(this);
        command.setExecutor(executor);
        command.setTabCompleter(executor);

        getServer().getPluginManager().registerEvents(new ProgressListener(this), this);

        getLogger().info("CareerCraft 已加载，成就数：" + registry.size());
    }

    @Override
    public void onDisable() {
        getLogger().info("CareerCraft 已卸载");
    }

    /** 成就表 */
    public AchievementRegistry registry() {
        return registry;
    }

    /** 重载配置与成就表（不重启服务器） */
    public void reloadAll() {
        reloadConfig();
        registry.reload();
    }

    /** 是否开启调试输出 */
    public boolean debug() {
        return getConfig().getBoolean("debug", false);
    }
}
