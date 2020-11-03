package de.fyreum.dreships.sign;

import de.fyreum.dreships.DREShips;
import de.fyreum.dreships.data.ConfigLocalization;
import de.fyreum.dreships.data.LangLocalization;
import de.fyreum.dreships.function.PriceCalculation;
import de.fyreum.dreships.persistentdata.ShipDataTypes;
import de.fyreum.litelibrary.util.BlockUtil;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SignManager {


    private final Map<UUID, List<CacheSign>> playerCache;
    private final PriceCalculation priceCalculation;

    public SignManager() {
        this.playerCache = new HashMap<>();
        this.priceCalculation = new PriceCalculation();
    }

    // ---------CacheSign----------

    public void saveSignInCache(@NonNull UUID uuid, @NonNull Sign sign, String name) {
        if (!playerCache.containsKey(uuid)) {
            playerCache.put(uuid, new ArrayList<>());
        }
        if (playerCache.get(uuid).size() > 1) {
            playerCache.get(uuid).set(0, new CacheSign(sign, name));
        } else {
            playerCache.get(uuid).add(new CacheSign(sign, name));
        }
    }

    // ----------TravelSign---------

    public int createFromCache(@NonNull UUID uuid, int price) throws CacheSignException {
        if (playerCache.get(uuid).size() != 2) {
            throw new CacheSignException("Couldn't create a TravelSign. Player cache is empty.");
        }
        Sign sign = playerCache.get(uuid).get(0).getSign();
        Sign destination = playerCache.get(uuid).get(1).getSign();
        String name = playerCache.get(uuid).get(0).getName();
        String destinationName = playerCache.get(uuid).get(1).getName();

        create(sign, destination, name, destinationName, price);
        return price;
    }

    public int calculateAndCreateFromCache(@NonNull UUID uuid, double multipliedDistance) throws CacheSignException {
        if (playerCache.get(uuid).size() != 2) {
            throw new CacheSignException("Couldn't create a TravelSign. Player cache is empty.");
        }
        Sign sign = playerCache.get(uuid).get(0).getSign();
        Sign destination = playerCache.get(uuid).get(1).getSign();
        String name = playerCache.get(uuid).get(0).getName();
        String destinationName = playerCache.get(uuid).get(1).getName();

        int price = this.priceCalculation.calculate(sign.getLocation(), destination.getLocation(), multipliedDistance);
        create(sign, destination, name, destinationName, price);
        return price;
    }

    public void create(@NonNull Sign sign, @NonNull Sign destination, String name, String destinationName, int price) {
        loadAndCreate(sign, destination, name, destinationName, price);
        loadAndCreate(destination, sign, name, destinationName, price);
    }

    private void loadAndCreate(Sign sign, @NonNull Sign destination, String name, String destinationName, int price) {
        BukkitRunnable runnable = new BukkitRunnable() {
            final CompletableFuture<Chunk> completableFuture = sign.getWorld().getChunkAtAsync(sign.getLocation());
            @Override
            public void run() {
                if (completableFuture.isDone()) {
                    try {
                        savePersistentData(sign, destination.getLocation(), name, destinationName, price);
                        visualizeData(sign, name, destinationName, price);
                        DREShips.broadcast(LangLocalization.commands.DS_CREATE_SUCCESS.getTranslation(true, simplify(sign.getLocation()),
                                completableFuture.get() == null), ConfigLocalization.permissions.DS_SAVE.asString());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        cancel();
                    }
                    cancel();
                }
            }
        };
        runnable.runTaskTimerAsynchronously(DREShips.getInstance(), 20,20);
    }

    private void savePersistentData(@NonNull Sign sign, @NonNull Location destination, String name, String destName, int price) {
        sign.getPersistentDataContainer().set(TravelSign.getNameKey(), PersistentDataType.STRING, name);
        sign.getPersistentDataContainer().set(TravelSign.getDestinationNameKey(), PersistentDataType.STRING, destName);
        sign.getPersistentDataContainer().set(TravelSign.getDestinationKey(), ShipDataTypes.LOCATION, destination);
        sign.getPersistentDataContainer().set(TravelSign.getPriceKey(), PersistentDataType.INTEGER, price);
        sign.update(true);
    }

    private void visualizeData(@NonNull Sign sign, String name, String destName, int price) {
        sign.setLine(0, LangLocalization.sign.SIGN_LINE_ONE.getTranslation(false));
        sign.setLine(1, LangLocalization.sign.SIGN_LINE_TWO.getTranslation(false, name));
        sign.setLine(2, LangLocalization.sign.SIGN_LINE_THREE.getTranslation(false, destName));
        sign.setLine(3, LangLocalization.sign.SIGN_LINE_FOUR.getTranslation(false, price));
        sign.update(true);
    }

    // returns the amount of deleted signs
    public int delete(@NonNull Sign sign) {
        if (!TravelSign.travelSign(sign)) {
            return 0;
        }
        Block destinationBlock = new TravelSign(sign).getDestination().getBlock();
        Sign destination = BlockUtil.isSign(destinationBlock) ? (Sign) new TravelSign(sign).getDestination().getBlock().getState() : null;
        return loadAndDelete(sign) + loadAndDelete(destination);
    }

    private int loadAndDelete(@Nullable Sign sign) {
        if (sign == null) {
            return 0;
        }
        BukkitRunnable runnable = new BukkitRunnable() {
            final CompletableFuture<Chunk> completableFuture = sign.getWorld().getChunkAtAsync(sign.getLocation());
            @Override
            public void run() {
                if (completableFuture.isDone()) {
                    try {
                        deletePersistentData(sign);
                        clearLines(sign);
                        DREShips.broadcast(LangLocalization.commands.DS_DELETE_SUCCESS.getTranslation(true, simplify(sign.getLocation()),
                                completableFuture.get() == null), ConfigLocalization.permissions.DS_DELETE.asString());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        cancel();
                    }
                    cancel();
                }
            }
        };
        runnable.runTaskTimerAsynchronously(DREShips.getInstance(), 20,20);
        return 1;
    }

    private void deletePersistentData(@Nullable Sign sign) {
        if (sign == null) {
            return;
        }
        sign.getPersistentDataContainer().remove(TravelSign.getNameKey());
        sign.getPersistentDataContainer().remove(TravelSign.getDestinationNameKey());
        sign.getPersistentDataContainer().remove(TravelSign.getDestinationKey());
        sign.getPersistentDataContainer().remove(TravelSign.getPriceKey());
        sign.update(true);
    }

    private void clearLines(@Nullable Sign sign) {
        if (sign == null) {
            return;
        }
        sign.setLine(0, "");
        sign.setLine(1, "");
        sign.setLine(2, "");
        sign.setLine(3, "");
        sign.update(true);
    }

    /* getter */

    private String simplify(@NonNull Location location) {
        return "[x=" + location.getX() + ", y=" + location.getY() + ", z=" + location.getZ();
    }

    public Map<UUID, List<CacheSign>> getPlayerCache() {
        return playerCache;
    }

    public PriceCalculation getPriceCalculation() {
        return priceCalculation;
    }
}
