package com.ziyad.militaryvillagers.ai.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;

import java.util.EnumSet;
import java.util.UUID;

public class FollowLeaderGoal extends Goal {
    private final Villager v;
    private final UUID leader;
    private final double speed;
    private final float minDist;

    public FollowLeaderGoal(Villager v, UUID leader, double speed, float minDist) {
        this.v = v; this.leader = leader; this.speed = speed; this.minDist = minDist;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override public boolean canUse() { return true; }
    @Override public void tick() {
        var lvl = (net.minecraft.server.level.ServerLevel)v.level();
        var ent = lvl.getEntity(leader);
        if (ent == null) return;
        if (v.distanceTo(ent) > minDist) {
            v.getNavigation().moveTo(ent.getX(), ent.getY(), ent.getZ(), speed);
        }
    }
}
