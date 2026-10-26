package com.griefprevention.protection;

import io.papermc.paper.event.entity.EntityBreakByEntityEvent;
import io.papermc.paper.event.entity.EntityBreakEvent;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.entity.Cushion;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Event handler for cushions.
 * <p>
 * Cushions are attached to a block like paintings, but they are not {@link org.bukkit.entity.Hanging},
 * so they fire no hanging events. Placing one only fires {@link EntityPlaceEvent}, and a player breaks
 * one before any damage event, so Paper's {@link EntityBreakEvent} is the only place to stop that.
 */
public class CushionProtectionHandler implements Listener
{

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCushionPlace(@NotNull EntityPlaceEvent event)
    {
        if (!(event.getEntity() instanceof Cushion cushion)) return;

        Player player = event.getPlayer();
        if (player == null) return;

        Supplier<String> noBuildReason = ProtectionHelper.checkPermission(player, cushion.getLocation(), ClaimPermission.Build, event);
        if (noBuildReason != null)
        {
            event.setCancelled(true);
            GriefPrevention.sendMessage(player, TextMode.Err, noBuildReason.get());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCushionBreak(@NotNull EntityBreakEvent event)
    {
        if (!(event.getEntity() instanceof Cushion cushion)) return;
        if (!GriefPrevention.instance.claimsEnabledForWorld(cushion.getWorld())) return;

        // Explosions don't destroy cushions, same as paintings and item frames.
        if (event.getCause() == EntityBreakEvent.RemoveCause.EXPLOSION)
        {
            event.setCancelled(true);
            return;
        }

        // Losing support, being obstructed and burning work as in vanilla.
        if (!(event instanceof EntityBreakByEntityEvent entityEvent)) return;

        Entity remover = entityEvent.getRemover();
        if (remover instanceof Projectile projectile && projectile.getShooter() instanceof Player shooter)
        {
            remover = shooter;
        }

        // Only players can break cushions, and only where they could build.
        if (!(remover instanceof Player player))
        {
            event.setCancelled(true);
            return;
        }

        Supplier<String> noBuildReason = ProtectionHelper.checkPermission(player, cushion.getLocation(), ClaimPermission.Build, event);
        if (noBuildReason != null)
        {
            event.setCancelled(true);
            GriefPrevention.sendMessage(player, TextMode.Err, noBuildReason.get());
        }
    }

}
