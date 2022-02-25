package eu.zzagro.manaapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class ManaApi extends JavaPlugin {

    private static ManaApi plugin;

    //public File configFile;
    //public FileConfiguration config;

    public static ArrayList<Player> manaRecoverList = new ArrayList<>();
    public static HashMap<Player, Integer> manaMap = new HashMap<>();
    public static HashMap<Player, Integer> maxManaMap = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ManaAPI by zZagro successfully enabled!");
        //createCustomConfig();
    }

    @Override
    public void onDisable() {

    }

    public static ManaApi getInstance() {
        return plugin;
    }

    public static void setMaxMana(Player player, int amount) {
        maxManaMap.put(player, amount);
    }

    public static void addMaxMana(Player player, int amount) {
        int mana = (getMaxMana(player)) + amount;
        if (mana >= 0) {
            maxManaMap.put(player, mana);
        } else {
            maxManaMap.put(player, 0);
        }
    }

    public static void removeMaxMana(Player player, int amount) {
        int mana = (getMaxMana(player)) - amount;
        if (mana >= 0) {
            maxManaMap.put(player, mana);
        } else {
            maxManaMap.put(player, 0);
        }
    }

    public static int getMaxMana(Player player) {
        return maxManaMap.get(player);
    }

    public static void setCurrentMana(Player player, int amount) {
        manaMap.put(player, amount);
    }

    public static void addCurrentMana(Player player, int amount) {
        int mana = (getCurrentMana(player)) + amount;
        if (mana >= 0 && mana <= getMaxMana(player)) {
            manaMap.put(player, mana);
        } else if (mana > getMaxMana(player)){
            manaMap.put(player, getMaxMana(player));
        } else {
            manaMap.put(player, 0);
        }
    }

    public static void removeCurrentMana(Player player, int amount) {
        int mana = (getCurrentMana(player)) - amount;
        if (mana >= 0 && mana <= getMaxMana(player)) {
            manaMap.put(player, mana);
        } else if (mana > getMaxMana(player)){
            manaMap.put(player, getMaxMana(player));
        } else {
            manaMap.put(player, 0);
        }
    }

    public static int getCurrentMana(Player player) {
        return manaMap.get(player);
    }

    public static void recoverMana(Player player, float recoveryPercent) {
        int maxMana = getMaxMana(player);
        final int[] currentMana = {getCurrentMana(player)};
        int recover = (int) (getMaxMana(player) * (recoveryPercent/100.0f));
        manaRecoverList.add(player);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (currentMana[0] < maxMana) {
                currentMana[0] = getCurrentMana(player) + recover;
                setCurrentMana(player, currentMana[0]);
                if (currentMana[0] > maxMana) {
                    setCurrentMana(player, maxMana);
                    manaRecoverList.remove(player);
                } else if (currentMana[0] == maxMana) {
                    manaRecoverList.remove(player);
                }
            }
        }, 20, 20);

        if (currentMana[0] > maxMana) {
            setCurrentMana(player, maxMana);
        }
    }

    /*public FileConfiguration getCustomConfig() {
        return this.config;
    }

    private void createCustomConfig() {
        configFile = new File(getDataFolder(), "custom.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("custom.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }*/
}
