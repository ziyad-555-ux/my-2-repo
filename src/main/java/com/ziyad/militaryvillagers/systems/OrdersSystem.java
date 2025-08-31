package com.ziyad.militaryvillagers.systems;

import com.ziyad.militaryvillagers.data.SoldierData;
import com.ziyad.militaryvillagers.registry.ModAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import java.util.List;

public final class OrdersSystem {
    public enum OrderType { RALLY, FOLLOW, DEFEND, PATROL, SCOUT }

    // يطبّق الأمر على مجموعة جنود
    public static void applyOrder(ServerLevel level, List<Villager> soldiers, OrderType order, Object param) {
        for (Villager v : soldiers) {
            SoldierData d = v.getData(ModAttachments.SOLDIER.get());
            if (!d.isSoldier) continue;

            switch (order) {
                case RALLY -> setRallyGoal(level, v, param);
                case FOLLOW -> setFollowGoal(level, v, param);   // param = UUID اللاعب القائد
                case DEFEND -> setDefendGoal(level, v, param);   // param = BlockPos/قرية
                case PATROL -> setPatrolGoal(level, v, param);   // TODO: نقاط
                case SCOUT -> setScoutGoal(level, v, param);     // TODO: نصف قطر
            }
        }
    }

    private static void setRallyGoal(ServerLevel lvl, Villager v, Object param) {
        // إزالة الأهداف القديمة ثم إضافة RallyGoal
        v.goalSelector.removeAllGoals(g -> true); // تبسيط للمثال
        v.goalSelector.addGoal(2, new com.ziyad.militaryvillagers.ai.goals.RallyGoal(v));
    }
    private static void setFollowGoal(ServerLevel lvl, Villager v, Object param) {
        java.util.UUID leader = (java.util.UUID) param;
        v.goalSelector.removeAllGoals(g -> true);
        v.goalSelector.addGoal(2, new com.ziyad.militaryvillagers.ai.goals.FollowLeaderGoal(v, leader, 1.2D, 2.5F));
    }
    private static void setDefendGoal(ServerLevel lvl, Villager v, Object param) {
        net.minecraft.core.BlockPos center = (net.minecraft.core.BlockPos) param;
        v.goalSelector.removeAllGoals(g -> true);
        v.goalSelector.addGoal(2, new com.ziyad.militaryvillagers.ai.goals.DefendVillageGoal(v, center, 16));
    }
    private static void setPatrolGoal(ServerLevel lvl, Villager v, Object param) { /* TODO */ }
    private static void setScoutGoal(ServerLevel lvl, Villager v, Object param)  { /* TODO */ }
}
