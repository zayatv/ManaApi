package eu.zzagro.manaapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class ManaApi extends JavaPlugin {

    private static ManaApi plugin;

    public static ArrayList<Player> manaRecoverList = new ArrayList<>();
    public static HashMap<Player, Integer> manaMap = new HashMap<>();
    public static HashMap<Player, Integer> maxManaMap = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "ManaAPI by zZagro successfully enabled!");
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

    public static int getMaxMana(Player player) {
        return maxManaMap.get(player);
    }

    public static void setCurrentMana(Player player, int amount) {
        manaMap.put(player, amount);
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
}
