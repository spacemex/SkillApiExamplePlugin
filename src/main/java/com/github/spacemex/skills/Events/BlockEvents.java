package com.github.spacemex.skills.Events;

import com.github.spacemex.skills.ServerUtil;
import com.github.spacemex.skillApi.Skills.Skill;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockEvents implements Listener {

    private static final List<Material> STONE_TIERS;
    private static final List<Material> LOW_TIERS;
    private static final List<Material> MEDIUM_TIERS;
    private static final List<Material> HIGH_TIERS;

    private ServerUtil utils;

    public BlockEvents(ServerUtil utils) {
        this.utils = utils;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var block = e.getBlock();
        var player = e.getPlayer();
        var skillManager = utils.getPlugin().getSkillUtils().getSkillManager();
        var miningSkill = skillManager.getSkill("mining");

        if (miningSkill == null) {
            player.sendMessage("Mining skill not found!");
            return;
        }

        double expGain = 0;

        // Check STONE_TIERS
        for (Material type : STONE_TIERS) {
            if (type.equals(block.getType())) {
                expGain = 0.05;
                utils.logInfo("Found stone");
                break;
            }
        }

        // Check LOW_TIERS
        for (Material type : LOW_TIERS) {
            if (type.equals(block.getType())) {
                utils.logInfo("Found low tier");
                expGain = 0.5;
                break;
            }
        }

        // Check MEDIUM_TIERS
        for (Material type : MEDIUM_TIERS) {
            if (type.equals(block.getType())) {
                expGain = 1.0;
                utils.logInfo("Found medium tier");
                break;
            }
        }

        // Check HIGH_TIERS
        for (Material type : HIGH_TIERS) {
            if (block.getType().equals(type)) {
                expGain = 2.0;
                utils.logInfo("Found high tier");
                break;
            }
        }

        if (Material.GOLD_BLOCK.equals(block.getType())) {
            expGain = 50.0;
            utils.logInfo("Found gold block");
        }
        // Add experience and check for level up
        if (expGain > 0) {
            utils.logInfo("Exp Gain: " + expGain);
            miningSkill.addExp(player, expGain);
            levelUp(player, miningSkill);
        }
    }

    private void levelUp(Player player, Skill skill) {
        int currentLevel = skill.getLevel(player); // Retrieve current level
        double currentExp = skill.getExp(player);  // Retrieve current experience
        double expToNextLevel = skill.getExpToLevel(currentLevel + 1); // Experience needed to level up

        if (currentExp >= expToNextLevel) {
            // Increment the player's level
            skill.addLevel(player, 1);
            // Deduct the experience needed for the level up (or reset to 0)
            double remainingExp = currentExp - expToNextLevel;
            skill.addExp(player, -expToNextLevel + remainingExp); // Adjust the experience

            // Optionally, notify the player
            player.sendMessage("Congratulations! You leveled up to level " + (currentLevel + 1) + " in " + skill.getName() + " skill!");

            // Recursively check if further leveling up is possible with remaining experience
            levelUp(player, skill);
        }
    }

    static {
        STONE_TIERS = List.of(Material.STONE, Material.COBBLESTONE, Material.GRANITE, Material.DIORITE, Material.ANDESITE);
        LOW_TIERS = List.of(Material.NETHER_QUARTZ_ORE, Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.COPPER_ORE, Material.NETHER_GOLD_ORE);
        MEDIUM_TIERS = List.of(Material.DIAMOND_ORE, Material.OBSIDIAN);
        HIGH_TIERS = List.of(Material.EMERALD_ORE, Material.ANCIENT_DEBRIS);
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e){
        Player player = e.getPlayer();
        Skill miningSkill = utils.getPlugin().getSkillUtils().getSkillManager().getSkill("mining");
        if (miningSkill != null) {
            player.sendMessage("Current Level: " + miningSkill.getLevel(player));
            player.sendMessage("Current Experience: " + miningSkill.getExp(player));

            if (miningSkill.getLevel(player) <= 0){
                miningSkill.addLevel(player, 1);
            }
        }
    }
}