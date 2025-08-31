package com.ziyad.militaryvillagers.systems;

import com.ziyad.militaryvillagers.data.SoldierData;
import com.ziyad.militaryvillagers.registry.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public final class LoadoutSystem {
    public static void tryEquipSoldier(ServerLevel lvl, Villager v) {
        SoldierData d = v.getData(ModAttachments.SOLDIER.get());
        if (!d.isSoldier || d.hasLoadout) return;

        // ابحث عن موارد من صناديق قريبة (تبسيط)
        Container armory = gatherNearbyItems(lvl, v.blockPosition(), 12);

        // تجهيز بسيط حسب الرتبة (MVP)
        ItemStack sword = pull(armory, Items.IRON_SWORD);
        if (!sword.isEmpty()) v.setItemSlot(EquipmentSlot.MAINHAND, sword);

        ItemStack helmet = pull(armory, Items.IRON_HELMET);
        if (!helmet.isEmpty()) v.setItemSlot(EquipmentSlot.HEAD, helmet);

        ItemStack chest = pull(armory, Items.IRON_CHESTPLATE);
        if (!chest.isEmpty()) v.setItemSlot(EquipmentSlot.CHEST, chest);

        d.hasLoadout = true; // اعتبرنا اكتفى
        v.syncData(ModAttachments.SOLDIER.get());
    }

    private static Container gatherNearbyItems(ServerLevel lvl, BlockPos center, int r) {
        SimpleContainer bag = new SimpleContainer(54);
        for (var be : lvl.blockEntityList) {
            if (be.getBlockPos().distManhattan(center) <= r && be instanceof net.minecraft.world.level.block.entity.ChestBlockEntity chest) {
                var inv = net.minecraft.world.Containers.asSimpleContainer(chest);
                for (int i=0;i<inv.getContainerSize();i++) {
                    var st = inv.getItem(i);
                    if (!st.isEmpty()) bag.addItem(st.copy());
                }
            }
        }
        return bag;
    }
    private static ItemStack pull(Container c, net.minecraft.world.item.Item item) {
        for (int i=0;i<c.getContainerSize();i++) {
            ItemStack s = c.getItem(i);
            if (!s.isEmpty() && s.is(item)) {
                ItemStack one = s.copy(); one.setCount(1);
                s.shrink(1);
                return one;
            }
        }
        return ItemStack.EMPTY;
    }

    public static void massEquip(ServerLevel lvl, List<Villager> soldiers) {
        for (Villager v : soldiers) tryEquipSoldier(lvl, v);
    }
}
