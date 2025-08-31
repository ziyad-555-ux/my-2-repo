package com.ziyad.militaryvillagers.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class ProfessionUtil {
    // خريطة مبسطة: بلوك محطة العمل -> المهنة
    private static boolean matches(BlockState s, VillagerProfession p) {
        return switch (p) {
            case FARMER -> s.is(Blocks.COMPOSTER);
            case ARMORER -> s.is(Blocks.BLAST_FURNACE);
            case TOOLSMITH -> s.is(Blocks.SMITHING_TABLE);
            case LIBRARIAN -> s.is(Blocks.LECTERN);
            case MASON -> s.is(Blocks.STONECUTTER);
            case BUTCHER -> s.is(Blocks.SMOKER);
            case CLERIC -> s.is(Blocks.BREWING_STAND);
            case FLETCHER -> s.is(Blocks.FLETCHING_TABLE);
            case SHEPHERD -> s.is(Blocks.LOOM);
            case FISHERMAN -> s.is(Blocks.BARREL);
            case LEATHERWORKER -> s.is(Blocks.CAULDRON);
            case CARTOGRAPHER -> s.is(Blocks.CARTOGRAPHY_TABLE);
            case NITWIT, NONE -> false;
            case WEAPONSMITH -> s.is(Blocks.GRINDSTONE);
        };
    }

    public static boolean isProfessionAllowedByNearbyPOI(Villager v, VillagerProfession p, int radius) {
        Level lvl = v.level();
        BlockPos center = v.blockPosition();
        BlockPos.MutableBlockPos cur = new BlockPos.MutableBlockPos();
        int r = Math.max(1, radius);
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    cur.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    if (matches(lvl.getBlockState(cur), p)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
