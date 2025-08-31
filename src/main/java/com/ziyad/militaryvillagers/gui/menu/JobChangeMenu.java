package com.ziyad.militaryvillagers.gui.menu;

import com.ziyad.militaryvillagers.registry.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class JobChangeMenu extends AbstractContainerMenu {
    public final int villagerId;

    // يُستدعى على الكلينت: يقرأ الـvillagerId من البفر
    public JobChangeMenu(int containerId, Inventory inv, FriendlyByteBuf extra) {
        this(containerId, inv, extra.readVarInt());
    }

    // يُستدعى على السيرفر
    public JobChangeMenu(int containerId, Inventory inv, int villagerId) {
        super(ModMenus.JOB_CHANGE.get(), containerId);
        this.villagerId = villagerId;
    }

    @Override public boolean stillValid(net.minecraft.world.entity.player.Player player) { return true; }
    @Override public net.minecraft.world.item.ItemStack quickMoveStack(net.minecraft.world.entity.player.Player p, int idx) {
        return net.minecraft.world.item.ItemStack.EMPTY;
    }
}
