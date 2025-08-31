package com.ziyad.militaryvillagers.systems;

import com.ziyad.militaryvillagers.data.SoldierData;
import com.ziyad.militaryvillagers.registry.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;

import java.util.List;

public final class FoodSystem {
    public static final int SOLDIERS_PER_FARMER = 3;
    public static final int STARVATION_DAYS_BEFORE_DEATH = 2;

    // يُستدعى نهاية كل يوم (انظر TickEvents)
    public static void endOfDay(ServerLevel lvl) {
        // احسب كل مجموعة "قرية" حول الجرس/المركز (MVP: نتعامل مع العالم كله ببساطة)
        List<Villager> villagers = lvl.getEntitiesOfClass(Villager.class, new net.minecraft.world.phys.AABB(lvl.getWorldBorder().getCenter()).inflate(256));
        int farmers = (int) villagers.stream().filter(v -> v.getVillagerData().getProfession()==VillagerProfession.FARMER).count();

        int feedable = farmers * SOLDIERS_PER_FARMER;

        // جمع الجنود
        var soldiers = villagers.stream().filter(v -> v.getData(ModAttachments.SOLDIER.get()).isSoldier).toList();

        // أطعم بالترتيب حتى تخلص الحصص
        int fed = 0;
        for (Villager s : soldiers) {
            if (fed >= feedable) break;
            if (tryFeedFromNearby(lvl, s)) {
                SoldierData d = s.getData(ModAttachments.SOLDIER.get());
                d.hunger = Math.min(100, d.hunger + 30); // تجديد
                s.syncData(ModAttachments.SOLDIER.get());
                fed++;
            }
        }

        // اللي ما انطعم ينقص جوعه؛ موت بعد X أيام جوع
        for (Villager s : soldiers) {
            SoldierData d = s.getData(ModAttachments.SOLDIER.get());
            if (d.hunger <= 0) {
                // موت مبسّط
                s.hurt(lvl.damageSources().starve(), 1000f);
            } else {
                d.hunger -= 40; // يوم بدون كفاية
                s.syncData(ModAttachments.SOLDIER.get());
            }
        }
    }

    private static boolean tryFeedFromNearby(ServerLevel lvl, Villager s) {
        // نعتبر حصة اليوم: (1 خبزة) أو (3 جزر) أو (3 بطاطس) من صناديق قريبة
        return pullFood(lvl, s.blockPosition(), 12, Items.BREAD, 1)
                || pullFood(lvl, s.blockPosition(), 12, Items.CARROT, 3)
                || pullFood(lvl, s.blockPosition(), 12, Items.POTATO, 3);
    }

    private static boolean pullFood(ServerLevel lvl, BlockPos center, int r, net.minecraft.world.item.Item item, int count) {
        for (var be : lvl.blockEntityList) {
            if (be.getBlockPos().distManhattan(center) <= r && be instanceof net.minecraft.world.level.block.entity.ChestBlockEntity chest) {
                var inv = net.minecraft.world.Containers.asSimpleContainer(chest);
                int need = count;
                for (int i=0;i<inv.getContainerSize();i++) {
                    var st = inv.getItem(i);
                    if (!st.isEmpty() && st.is(item)) {
                        int use = Math.min(need, st.getCount());
                        st.shrink(use);
                        need -= use;
                        if (need == 0) return true;
                    }
                }
            }
        }
        return false;
    }
}
