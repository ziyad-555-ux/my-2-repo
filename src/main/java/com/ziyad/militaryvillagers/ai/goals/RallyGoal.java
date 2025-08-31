package com.ziyad.militaryvillagers.ai.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;

import java.util.EnumSet;

public class RallyGoal extends Goal {
    private final Villager v;

    public RallyGoal(Villager v) {
        this.v = v;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override public boolean canUse() { return true; }

    @Override public void tick() {
        // rally عند الجرس الأقرب (تبسيط: نرجع لموضع البيت/القرية)
        var poi = v.getVillage();
        if (poi == null) return;
        var center = v.blockPosition(); // TODO: بدّلها لموضع الجرس الحقيقي
        v.getNavigation().moveTo(center.getX(), center.getY(), center.getZ(), 1.1D);
    }
}
