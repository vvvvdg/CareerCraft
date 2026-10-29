package com.xiangqin.careercraft.achievement;

import com.xiangqin.careercraft.CareerCraftPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * 成就表：从 achievements.yml 读取，保存在内存里。
 *
 * <p>骨架阶段只解析 id / name / desc / trigger / enabled，并做最小校验：
 * 缺 id 或 trigger 的条目会被跳过并在控制台报出行号级别的提示。
 */
public final class AchievementRegistry {

    private final CareerCraftPlugin plugin;
    private final Map<String, AchievementDefinition> byId = new LinkedHashMap<>();

    public AchievementRegistry(CareerCraftPlugin plugin) {
        this.plugin = plugin;
    }

    /** 重新读取成就表 */
    public void reload() {
        byId.clear();

        String fileName = plugin.getConfig().getString("achievements-file", "achievements.yml");
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        List<Map<?, ?>> raw = yaml.getMapList("achievements");
        int skipped = 0;

        for (int i = 0; i < raw.size(); i++) {
            Map<?, ?> entry = raw.get(i);
            String id = str(entry.get("id"));
            String trigger = str(entry.get("trigger"));

            if (id.isBlank() || trigger.isBlank()) {
                skipped++;
                plugin.getLogger().warning("achievements.yml 第 " + (i + 1) + " 条缺少 id 或 trigger，已跳过");
                continue;
            }
            if (byId.containsKey(id)) {
                skipped++;
                plugin.getLogger().warning("achievements.yml 里 id 重复：" + id + "，后一条已跳过");
                continue;
            }

            byId.put(id, new AchievementDefinition(
                    id,
                    str(entry.get("name")).isBlank() ? id : str(entry.get("name")),
                    str(entry.get("desc")),
                    trigger,
                    entry.get("enabled") == null || Boolean.parseBoolean(str(entry.get("enabled")))
            ));
        }

        plugin.getLogger().info("成就表已加载：" + byId.size() + " 条"
                + (skipped > 0 ? "（跳过 " + skipped + " 条）" : ""));
    }

    public int size() {
        return byId.size();
    }

    public Collection<AchievementDefinition> all() {
        return List.copyOf(byId.values());
    }

    public List<AchievementDefinition> byTrigger(String trigger) {
        List<AchievementDefinition> out = new ArrayList<>();
        for (AchievementDefinition def : byId.values()) {
            if (def.enabled() && def.trigger().equals(trigger)) {
                out.add(def);
            }
        }
        return out;
    }

    private static String str(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }
}
