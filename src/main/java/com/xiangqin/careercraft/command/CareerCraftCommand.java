package com.xiangqin.careercraft.command;

import com.xiangqin.careercraft.CareerCraftPlugin;
import com.xiangqin.careercraft.achievement.AchievementDefinition;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

/** /cc 命令：help / list / reload / version */
public final class CareerCraftCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUB = List.of("help", "list", "reload", "version");

    private final CareerCraftPlugin plugin;

    public CareerCraftCommand(CareerCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, String @NotNull [] args) {
        String sub = args.length == 0 ? "help" : args[0].toLowerCase(Locale.ROOT);

        switch (sub) {
            case "help" -> help(sender, label);
            case "list" -> {
                if (!sender.hasPermission("careercraft.admin")) {
                    sender.sendMessage(Component.text("你没有权限喵", NamedTextColor.RED));
                    return true;
                }
                list(sender);
            }
            case "reload" -> {
                if (!sender.hasPermission("careercraft.admin")) {
                    sender.sendMessage(Component.text("你没有权限喵", NamedTextColor.RED));
                    return true;
                }
                plugin.reloadAll();
                sender.sendMessage(Component.text("已重载，成就数：" + plugin.registry().size(), NamedTextColor.GREEN));
            }
            case "version" -> sender.sendMessage(Component.text(
                    "CareerCraft " + plugin.getPluginMeta().getVersion(), NamedTextColor.GRAY));
            default -> sender.sendMessage(Component.text("未知子命令，试试 /" + label + " help", NamedTextColor.RED));
        }
        return true;
    }

    private void help(CommandSender sender, String label) {
        sender.sendMessage(Component.text("CareerCraft · 生涯之书", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/" + label + " list", NamedTextColor.YELLOW)
                .append(Component.text("   查看已加载的成就", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/" + label + " reload", NamedTextColor.YELLOW)
                .append(Component.text("   重载配置与成就表", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/" + label + " version", NamedTextColor.YELLOW)
                .append(Component.text("   查看版本", NamedTextColor.GRAY)));
    }

    private void list(CommandSender sender) {
        List<AchievementDefinition> all = new ArrayList<>(plugin.registry().all());
        if (all.isEmpty()) {
            sender.sendMessage(Component.text("还没有配置任何成就", NamedTextColor.GRAY));
            return;
        }
        sender.sendMessage(Component.text("共 " + all.size() + " 条成就：", NamedTextColor.GOLD));
        for (AchievementDefinition def : all) {
            sender.sendMessage(Component.text(" - " + def.id(), NamedTextColor.YELLOW)
                    .append(Component.text(" [" + def.trigger() + "]", NamedTextColor.DARK_AQUA))
                    .append(Component.text(" "))
                    .append(LegacyComponentSerializer.legacyAmpersand().deserialize(def.name()))
                    .append(def.enabled() ? Component.empty() : Component.text(" (已停用)", NamedTextColor.DARK_GRAY)));
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, String @NotNull [] args) {
        if (args.length == 1) {
            String prefix = args[0].toLowerCase(Locale.ROOT);
            return SUB.stream().filter(s -> s.startsWith(prefix)).toList();
        }
        return List.of();
    }
}
