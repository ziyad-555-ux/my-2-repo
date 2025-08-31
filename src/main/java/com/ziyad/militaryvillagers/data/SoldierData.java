package com.ziyad.militaryvillagers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class SoldierData {
    public enum Rank {
        GENERAL,
        DEPUTY_GENERAL,
        BATTALION_LEADER,
        LIEUTENANT,
        CORPORAL,
        PRIVATE,
        SCOUT_SQUAD,
        SERGEANT_MAJOR,
        FIRST_SERGEANT,
        SERGEANT
    }

    public boolean isSoldier;
    public Rank rank;
    public int hunger;   // 0..100 تقديرياً
    public boolean hasLoadout;

    public SoldierData() {
        this(false, Rank.PRIVATE, 100, false);
    }

    public SoldierData(boolean isSoldier, Rank rank, int hunger, boolean hasLoadout) {
        this.isSoldier = isSoldier;
        this.rank = rank;
        this.hunger = hunger;
        this.hasLoadout = hasLoadout;
    }

    // حفظ NBT
    public static final Codec<SoldierData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.BOOL.fieldOf("isSoldier").forGetter(d -> d.isSoldier),
            Codec.STRING.xmap(Rank::valueOf, Rank::name).fieldOf("rank").forGetter(d -> d.rank.name()),
            Codec.INT.fieldOf("hunger").forGetter(d -> d.hunger),
            Codec.BOOL.fieldOf("hasLoadout").forGetter(d -> d.hasLoadout)
    ).apply(i, (isS, rname, h, lo) -> new SoldierData(isS, Rank.valueOf(rname), h, lo)));

    // مزامنة شبكة
    public static final StreamCodec<ByteBuf, SoldierData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, d -> d.isSoldier,
                    ByteBufCodecs.STRING_UTF8, d -> d.rank.name(),
                    ByteBufCodecs.VAR_INT, d -> d.hunger,
                    ByteBufCodecs.BOOL, d -> d.hasLoadout,
                    (isS, rname, h, lo) -> new SoldierData(isS, Rank.valueOf(rname), h, lo)
            );
}
