package com.github.spacemex.skills.Skill;

import com.github.spacemex.skillApi.Skills.AbstractSkill;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class MiningSkill extends AbstractSkill {
    private final Map<OfflinePlayer, Double> playerExp;
    private final Map<OfflinePlayer, Integer> playerLevels;
    public MiningSkill() {
        super("mining");
        this.playerExp = new HashMap<>();
        this.playerLevels = new HashMap<>();
    }

    @Override
    public boolean hasExp(OfflinePlayer player, int exp) {
        return getExp(player) >= exp;
    }

    @Override
    public boolean hasLevel(OfflinePlayer player, int level) {
        return getLevel(player) >= level;
    }

    @Override
    public double getExp(OfflinePlayer player) {
        return playerExp.getOrDefault(player, 0.0);
    }

    @Override
    public int getLevel(OfflinePlayer player) {
        return playerLevels.getOrDefault(player, 0);
    }

    @Override
    protected void setExp(OfflinePlayer player, double exp) {
        playerExp.put(player, exp);
    }

    @Override
    protected void setLevel(OfflinePlayer player, int level) {
        playerLevels.put(player, level);
    }

    @Override
    public double getExpToLevel(int level) {
        return level * 100.0; // Example: each level requires 100 exp points
    }
}
