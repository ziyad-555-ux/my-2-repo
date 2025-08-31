package com.ziyad.militaryvillagers.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;

import java.util.EnumSet;
import java.util.List;

public class DefendVillageGoal extends Goal {
    private final Villager v;
    private final BlockPos center;
    private final int radius;

    public DefendVillageGoal(Villager v, BlockPos center, int radius) {
        this.v = v; this.center = center; this.radius = radius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override public boolean canUse() { return true; }

    @Override public void tick() {
        // دور على هدف عدائي قريب (زومبي/رايدر) وامشِ له
        List<LivingEntity> mobs = v.level().getEntitiesOfClass(LivingEntity.class,
                new net.minecraft.world.phys.AABB(center).inflate(radius),
                e -> e.getType().getCategory().isFriendly() == false && e != v);
        if (!mobs.isEmpty()) {
            var target = mobs.get(0);
            v.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.15D);
        } else {
            // تمركز عند المركز
            v.getNavigation().moveTo(center.getX(), center.getY(), center.getZ(), 1.0D);
        }
    }
}
