package com.ziyad.militaryvillagers.gui.screen;

import com.ziyad.militaryvillagers.gui.menu.JobChangeMenu;
import com.ziyad.militaryvillagers.net.payload.SetProfessionC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.network.PacketDistributor;

public class JobChangeScreen extends AbstractContainerScreen<JobChangeMenu> {

    public JobChangeScreen(JobChangeMenu menu, net.minecraft.world.entity.player.Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 100;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.leftPos + 10, y = this.topPos + 20, w = 70, h = 20;

        // أزرار مهن شائعة + زر "عسكري"
        addRenderableWidget(Button.builder(Component.literal("Farmer"), b -> sendSet(VillagerProfession.FARMER)).bounds(x, y, w, h).build());
        addRenderableWidget(Button.builder(Component.literal("Armorer"), b -> sendSet(VillagerProfession.ARMORER)).bounds(x+80, y, w, h).build());
        addRenderableWidget(Button.builder(Component.literal("Librarian"), b -> sendSet(VillagerProfession.LIBRARIAN)).bounds(x, y+24, w, h).build());
        addRenderableWidget(Button.builder(Component.literal("Fletcher"), b -> sendSet(VillagerProfession.FLETCHER)).bounds(x+80, y+24, w, h).build());
        addRenderableWidget(Button.builder(Component.literal("MILITARY"), b -> sendSetMilitary()).bounds(x, y+48, 150, h).build());
    }

    private void sendSet(VillagerProfession prof) {
        PacketDistributor.sendToServer(new SetProfessionC2S(this.menu.villagerId, prof.name())); // يمرّر enum name
        this.onClose();
    }

    private void sendSetMilitary() {
        PacketDistributor.sendToServer(new SetProfessionC2S(this.menu.villagerId, "MILITARY"));
        this.onClose();
    }

    @Override
    protected void renderBg(GuiGraphics g, float partial, int mouseX, int mouseY) {
        // خلفية بسيطة أو اتركها فاضية
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partial) {
        this.renderBackground(g);
        super.render(g, mouseX, mouseY, partial);
        g.drawString(this.font, this.title, this.leftPos + 10, this.topPos + 6, 0xFFFFFF);
    }
}
