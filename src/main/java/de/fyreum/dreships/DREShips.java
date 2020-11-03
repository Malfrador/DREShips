package de.fyreum.dreships;

import de.fyreum.dreships.data.ConfigLocalization;
import de.fyreum.dreships.data.LangLocalization;
import de.gleyder.umbrella.core.module.UmbrellaModule;
import de.gleyder.umbrella.core.module.UmbrellaPlugin;
import lombok.NonNull;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class DREShips extends UmbrellaPlugin {

    private static DREShips plugin;
    private Economy economy = null;

    public DREShips() {
        super(DREShipsModule.class, false);
    }

    @Override
    protected void onInit(UmbrellaModule module) {
        plugin = this;
    }

    @Override
    protected void onPluginEnable(UmbrellaModule module) {
        if (!setupEconomy() ) {
            System.out.println(LangLocalization.warns.VAULT_IS_MISSING.getTranslation(false));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupEconomy() {
        if (economy != null) {
            return true;
        }
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            return false;
        }
        this.economy = registeredServiceProvider.getProvider();
        // TODO: maybe add a translation... maybe.
        System.out.println("[DREShips] Vault was successfully hooked.");
        return true;
    }

    public static boolean hasPermission(Player player, String... permissions) {
        if (player.hasPermission(ConfigLocalization.permissions.ALL.asString())) {
            return true;
        }
        for (String permission : permissions) {
            if (!player.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    public static void broadcast(String message, String... permission) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (hasPermission(onlinePlayer, permission)) {
                onlinePlayer.sendMessage(message);
            }
        }
    }

    /* getter */

    public static DREShips getInstance() {
        return plugin;
    }

    public static NamespacedKey getNamespace(String key) {
        return new NamespacedKey(plugin, key);
    }

    public Economy getEconomy() {
        return economy;
    }
}
