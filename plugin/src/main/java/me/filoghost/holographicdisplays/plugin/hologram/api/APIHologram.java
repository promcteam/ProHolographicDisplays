/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologram;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramLines;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class APIHologram extends BaseHologram implements Hologram {

    private final BaseHologramLines<APIHologramLine> lines;
    private final Plugin plugin;
    private final APIHologramManager apiHologramManager;
    private final DefaultVisibilitySettings visibilitySettings;

    private boolean allowPlaceholders;

    protected APIHologram(
            BaseHologramPosition position,
            Plugin plugin,
            APIHologramManager apiHologramManager,
            LineTrackerManager lineTrackerManager) {
        super(position, lineTrackerManager);
        Preconditions.notNull(plugin, "plugin");
        this.lines = new BaseHologramLines<>(this);
        this.plugin = plugin;
        this.apiHologramManager = apiHologramManager;
        this.visibilitySettings = new DefaultVisibilitySettings();
    }

    @Override
    public BaseHologramLines<APIHologramLine> getLines() {
        return lines;
    }

    @Override
    public Plugin getCreatorPlugin() {
        return plugin;
    }

    @Override
    public @NotNull APIHologramLine getLine(int index) {
        return lines.get(index);
    }

    @Override
    public @NotNull APITextLine appendTextLine(@Nullable String text) {
        checkNotDeleted();

        APITextLine line = new APITextLine(this, text);
        lines.add(line);
        return line;
    }

    @Override
    public @NotNull APIItemLine appendItemLine(@NotNull ItemStack itemStack) {
        Preconditions.notNull(itemStack, "itemStack");
        checkNotDeleted();

        APIItemLine line = new APIItemLine(this, itemStack);
        lines.add(line);
        return line;
    }

    @Override
    public @NotNull APITextLine insertTextLine(int index, @Nullable String text) {
        checkNotDeleted();

        APITextLine line = new APITextLine(this, text);
        lines.add(line);
        return line;
    }

    @Override
    public @NotNull APIItemLine insertItemLine(int index, @NotNull ItemStack itemStack) {
        Preconditions.notNull(itemStack, "itemStack");
        checkNotDeleted();

        APIItemLine line = new APIItemLine(this, itemStack);
        lines.add(line);
        return line;
    }

    @Override
    public void removeLine(int index) {
        checkNotDeleted();

        lines.remove(index);
    }

    @Override
    public boolean removeLine(@NotNull HologramLine line) {
        checkNotDeleted();

        if (line instanceof APIHologramLine) {
            return lines.remove((APIHologramLine) line);
        } else {
            return false;
        }
    }

    @Override
    public void clearLines() {
        checkNotDeleted();

        lines.clear();
    }

    @Override
    public int getLineCount() {
        return getLines().size();
    }

    @Override
    public void setAllowPlaceholders(boolean allowPlaceholders) {
        checkNotDeleted();

        if (this.allowPlaceholders == allowPlaceholders) {
            return;
        }

        this.allowPlaceholders = allowPlaceholders;
        for (APIHologramLine line : lines) {
            line.setChanged();
        }
    }

    @Override
    public boolean isAllowPlaceholders() {
        return allowPlaceholders;
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return visibilitySettings.isVisibleTo(player);
    }

    @Override
    public double getHeight() {
        return lines.getHeight();
    }

    @Override
    public @NotNull DefaultVisibilitySettings getVisibilitySettings() {
        return visibilitySettings;
    }

    @Override
    public void delete() {
        apiHologramManager.deleteHologram(this);
    }

}
