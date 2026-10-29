package com.xiangqin.careercraft.achievement;

/**
 * 一条成就的定义。
 *
 * <p>骨架阶段只保留最小字段；P1 会在此基础上加条件参数（目标生物/物品/数量、模式 first/累计）
 * 和奖励列表（物品 / 经验 / 命令 / 动态物品）。
 *
 * @param id          唯一标识，英文小写
 * @param name        显示名（可带 &amp; 颜色符）
 * @param description 描述，可为空
 * @param trigger     事件类型，如 kill_mob、death_by_entity
 * @param enabled     是否启用
 */
public record AchievementDefinition(
        String id,
        String name,
        String description,
        String trigger,
        boolean enabled
) {
}
