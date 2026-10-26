package com.griefprevention.protection;

import com.griefprevention.platform.PlatformDetection;
import com.griefprevention.platform.PlatformListener;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Registers {@link CushionProtectionHandler} on servers that have cushions and Paper's entity break events.
 */
public class CushionProtectionListener implements PlatformListener
{

    @Override
    public boolean isSupported()
    {
        return PlatformDetection.classExists("org.bukkit.entity.Cushion")
                && PlatformDetection.classExists("io.papermc.paper.event.entity.EntityBreakByEntityEvent");
    }

    @Override
    public @NotNull Listener create()
    {
        return new CushionProtectionHandler();
    }

}
