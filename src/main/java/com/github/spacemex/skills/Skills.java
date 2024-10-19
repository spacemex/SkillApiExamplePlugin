package com.github.spacemex.skills;

import com.github.spacemex.skills.Events.BlockEvents;
import com.github.spacemex.skills.Skill.MiningSkill;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Skills extends JavaPlugin implements TabExecutor {
     private ServerUtil utils;
     private SkillUtils skillUtils;

    @Override
    public void onEnable() {
        this.utils = new ServerUtil(this);
        if (!utils.isPluginInstalled("SkillAPI")) {
            utils.logError("SkillApi Is Not Installed");
            utils.logError("Disabling Plugin");
            utils.disablePlugin(this);
        }
        skillUtils = new SkillUtils(this);

        skillUtils.addSkill(new MiningSkill());

        utils.registerEvent(BlockEvents.class);

    }

    public ServerUtil getUtils() {
        return utils;
    }

    public SkillUtils getSkillUtils() {
        return skillUtils;
    }
}