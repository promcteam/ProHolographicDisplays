/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.core.base.BaseHologramManager;
import me.filoghost.holographicdisplays.core.base.ImmutablePosition;
import me.filoghost.holographicdisplays.core.tracking.LineTrackerManager;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class APIHologramManager extends BaseHologramManager<APIHologram> {

    private final LineTrackerManager lineTrackerManager;

    public APIHologramManager(LineTrackerManager lineTrackerManager) {
        this.lineTrackerManager = lineTrackerManager;
    }

    public APIHologram createHologram(ImmutablePosition position, Plugin plugin) {
        APIHologram hologram = new APIHologram(position, plugin, this, lineTrackerManager);
        super.addHologram(hologram);
        return hologram;
    }

    Collection<Hologram> getHologramsByPlugin(Plugin plugin) {
        List<Hologram> ownedHolograms = new ArrayList<>();

        for (APIHologram hologram : getHolograms()) {
            if (hologram.getCreatorPlugin().equals(plugin)) {
                ownedHolograms.add(hologram);
            }
        }

        return Collections.unmodifiableList(ownedHolograms);
    }

}
