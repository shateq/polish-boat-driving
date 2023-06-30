package shateq.bukkit.polishboat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PolishBoatDriving extends JavaPlugin implements Listener {

    public Set<Material> ice = Set.of(Material.ICE, Material.PACKED_ICE, Material.BLUE_ICE, Material.FROSTED_ICE);
    public Map<UUID, Integer> timeout = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getLogger().fine("Polish Boat Driving plugin's registered its events.");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHandbrakeUse(final @NotNull PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if (!(p.isInsideVehicle() && p.getVehicle() instanceof Boat boat)) return;
        if (boat.isInsideVehicle()) return;

        if (timeout.containsKey(p.getUniqueId())) return;

        Material m = boat.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if (ice.contains(m)) {
            /*proper way doesn't work
            CraftBoat craftB = (CraftBoat) boat;
            net.minecraft.world.entity.vehicle.Boat b = craftB.getHandle();

            Vec3 vec3 = b.getDeltaMovement();
            b.setDeltaMovement(0D, 0D, 0D);*/
            List<Entity> pass = boat.getPassengers();
            for (Entity entity : pass) boat.removePassenger(entity);

            Location locum = boat.getLocation();
            locum.add(0D, 2D, 0D);
            boat.setFallDistance(2.0f);
            boat.teleport(locum);
            boat.setVelocity(locum.getDirection().normalize().multiply(0.5D));

            timeout.put(p.getUniqueId(), 1);
            Bukkit.getScheduler().runTaskLater(this, () -> {
                for (Entity entity : pass) boat.addPassenger(entity);
                timeout.remove(p.getUniqueId());
            }, 6);
        }
    }
}
