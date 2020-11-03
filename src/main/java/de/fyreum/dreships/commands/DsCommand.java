package de.fyreum.dreships.commands;

import de.fyreum.dreships.data.ConfigLocalization;
import de.fyreum.dreships.data.LangLocalization;
import de.fyreum.dreships.sign.CacheSignException;
import de.fyreum.dreships.sign.SignManager;
import de.fyreum.dreships.sign.TravelSign;
import de.fyreum.litelibrary.util.BlockUtil;
import de.gleyder.umbrella.core.command.annotations.ExecutorMethod;
import de.gleyder.umbrella.core.command.builder.MutableArgumentBuilder;
import de.gleyder.umbrella.core.command.conditions.PermissionCondition;
import de.gleyder.umbrella.core.command.interpreters.Interpreters;
import de.gleyder.umbrella.core.command.logic.CommandAdapter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DsCommand extends CommandAdapter {

    private final SignManager signManager;

    public DsCommand() {
        super("dreships", LangLocalization.descriptions.DS.getTranslation(false),
                LangLocalization.commands.DS_HELP.getTranslation(true), "ds");
        this.signManager = new SignManager();
    }

    @Override
    public void define() {
        // ds delete
        getBuilder()
                .addArgument("delete")
                .setCondition(new PermissionCondition(ConfigLocalization.permissions.DS_DELETE.asString(), ConfigLocalization.permissions.ALL.asString()))
                .buildAndAdd();

        // ds save <name>
        getBuilder()
                .addArgument("save")
                .addArgument(new MutableArgumentBuilder().setInterpreter(Interpreters.STRING).setCompletion("name", true))
                .setCondition(new PermissionCondition(ConfigLocalization.permissions.DS_SAVE.asString(), ConfigLocalization.permissions.ALL.asString()))
                .buildAndAdd();

        // ds create <price/condition>
        getBuilder()
                .addArgument("create")
                .addArgument(new MutableArgumentBuilder().setInterpreter(Interpreters.STRING).setCompletion("price | condition", true))
                .setCondition(new PermissionCondition(ConfigLocalization.permissions.DS_CREATE.asString(), ConfigLocalization.permissions.ALL.asString()))
                .buildAndAdd();

        // ds help
        getBuilder()
                .addArgument("help")
                .setCondition(new PermissionCondition(ConfigLocalization.permissions.DS_HELP.asString(), ConfigLocalization.permissions.ALL.asString()))
                .buildAndAdd();

        // ds info
        getBuilder()
                .addArgument("info")
                .setCondition(new PermissionCondition(ConfigLocalization.permissions.DS_INFO.asString(), ConfigLocalization.permissions.ALL.asString()))
                .buildAndAdd();

        // ds teleport <world> <x> <y> <z>    --- for the use of the suffocation warned teleportation ---
        getBuilder()
                .addArgument("teleport")
                .addArgument(new MutableArgumentBuilder().setInterpreter(Interpreters.WORLD))
                .addArgument(new MutableArgumentBuilder().setInterpreter(Interpreters.DOUBLE))
                .addArgument(new MutableArgumentBuilder().setInterpreter(Interpreters.DOUBLE))
                .addArgument(new MutableArgumentBuilder().setInterpreter(Interpreters.DOUBLE))
                .setCondition(new PermissionCondition(ConfigLocalization.permissions.DS_TELEPORT.asString(), ConfigLocalization.permissions.ALL.asString()))
                .buildAndAdd();
    }

    @ExecutorMethod
    public void delete(Player player) {
        Block target = player.getTargetBlock(8);
        if (target == null || !TravelSign.travelSign(target)) {
            LangLocalization.commands.TARGET_BLOCK_IS_NO_TRAVEL_SIGN.sendMessage(player, "</n>", true);
            return;
        }
        LangLocalization.commands.DS_DELETE_SUCCESS.sendMessage(player, "</n>", true, signManager.delete((Sign) target.getState()));
    }

    @ExecutorMethod
    public void save(Player player, String name) {
        Sign sign;
        Block target = player.getTargetBlock(8);
        if (BlockUtil.isSign(target)) {
            sign = (Sign) target.getState();
            if (TravelSign.travelSign(sign)) {
                LangLocalization.commands.DS_SAVE_ALREADY_TRAVEL_SIGN.sendMessage(player, "</n>", true);
                return;
            }
        } else {
            LangLocalization.commands.TARGET_BLOCK_IS_NO_SIGN.sendMessage(player, "</n>", true);
            return;
        }
        signManager.saveSignInCache(player.getUniqueId(), sign, name);
        LangLocalization.commands.DS_SAVE_SUCCESS.sendMessage(player, "</n>", true, signManager.getPlayerCache().get(player.getUniqueId()).size());
    }

    @ExecutorMethod
    public void create(Player player, String arg) {
        try {
            double multipliedDistance = signManager.getPriceCalculation().getDistanceMultiplier(arg);
            if (multipliedDistance < 0) {
                int price;
                try {
                    price = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    LangLocalization.commands.PRICE_NULL_OR_NEGATIVE.sendMessage(player, "</n>", true);
                    return;
                }
                LangLocalization.commands.DS_CREATE_START.sendMessage(player, "</n>", true);
                signManager.createFromCache(player.getUniqueId(), price);
            } else {
                LangLocalization.commands.DS_CREATE_START.sendMessage(player, "</n>", true);
                signManager.calculateAndCreateFromCache(player.getUniqueId(), multipliedDistance);
            }
        } catch (CacheSignException c) {
            LangLocalization.commands.CACHE_IS_EMPTY.sendMessage(player, "</n>", true);
        }
    }

    @ExecutorMethod
    public void help(Player player) {

    }

    @ExecutorMethod
    public void info(Player player) {
        Block target = player.getTargetBlock(8);
        if (target == null || !TravelSign.travelSign(target)) {
            LangLocalization.commands.DS_INFO_NO_TRAVEL_SIGN.sendMessage(player, true);
            return;
        }
        TravelSign sign = new TravelSign((Sign) target.getState());
        LangLocalization.commands.DS_INFO_IS_TRAVEL_SIGN.sendMessage(player, "</n>",false,
                sign.getName(), sign.getDestinationName(), sign.getDestination().toString(), sign.getPrice());

    }

    @ExecutorMethod
    public void teleport(Player player, World world, double x, double y, double z) {
        Location location = new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
        player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

}
