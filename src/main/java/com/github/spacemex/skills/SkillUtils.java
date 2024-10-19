package com.github.spacemex.skills;

import com.github.spacemex.skillApi.SkillManager;
import com.github.spacemex.skillApi.Skills.Skill;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class SkillUtils {
    private final boolean isProvider;
    private final SkillManager skillManager;
    private final ServerUtil utils;

    public SkillUtils(Skills plugin){
        this.utils = plugin.getUtils();

        RegisteredServiceProvider<SkillManager> rsp = utils.getPlugin().getServer().getServicesManager().getRegistration(SkillManager.class);
        if (rsp != null) {
            this.skillManager = rsp.getProvider();
            this.isProvider = false;
            utils.logWarning("Another Skill Provider Detected,Some Features May Not Work: -> " + rsp.getPlugin().getName());
        } else {
            this.isProvider = true;
            this.skillManager = new SkillManager();
            utils.getServer().getServicesManager().register(SkillManager.class, skillManager, utils.getPlugin(), ServicePriority.Normal);
            utils.logInfo("No Skill Providers Detected, Registering New Provider");
        }
    }
    public SkillManager getSkillManager() {
        return skillManager;
    }
    public boolean isProvider() {
        return isProvider;
    }

    public boolean doesSkillExist(String skillName){
        return skillManager.getSkill(skillName) != null;
    }

    public void addSkill(Skill skill){
        if (!isProvider) {
            utils.logWarning("Plugin is Not The Registered Skill Provider, Some Features May Not Work");
            utils.logWarning("Attempting To Register Skill");
        }
        if (!doesSkillExist(skill.getName())) {
            skillManager.registerSkill(skill);
        }
        else {
            utils.logWarning("Skill Already Exists, Ignoring Skill");
        }
    }
    public void removeSkill(String skillName){
        if (!isProvider) {
            utils.logWarning("Plugin is Not The Registered Skill Provider, Some Features May Not Work");
            utils.logWarning("Attempting To Remove Skill");
        }
        if (doesSkillExist(skillName)) {
            skillManager.unregisterSkill(skillName);
        }
        else {
            utils.logWarning("Skill Does Not Exist, Ignoring Skill");
        }
    }
}
