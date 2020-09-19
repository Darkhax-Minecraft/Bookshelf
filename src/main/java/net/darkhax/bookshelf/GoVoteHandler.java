/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package net.darkhax.bookshelf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * This class is released for public use via the Waive Clause of the Botania License. You
 * are encouraged to copy and use it. Keep the marker file path the same so multiple mods don't
 * show the screen at once. If you are uncomfortable with the network access to ip-api,
 * feel free to remove it. The fallback is to examine the computer's current locale.
 * 
 * Note: This is a modified version of the original class. These are the main changes.
 * - Automatically registers itself, no need for calling init.
 * - Better setup handling that minimizes usage when the screen is disabled.
 * - Removed ip-api code for privacy and request volume concerns.
 * - Slightly decreased the time each message takes to display.
 */
@EventBusSubscriber(modid = Bookshelf.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class GoVoteHandler {
    
    private static final String BRAND = "Darkhax's Mods";
    private static final String MARKER_PATH = ".vote2020_marker";
    private static final LocalDate ELECTION_DAY = LocalDate.of(2020, Month.NOVEMBER, 3);
    private static final String LINK = "https://vote.gov/";
    private static boolean shownThisSession = false;
    
    private static volatile boolean markerAlreadyExists = false;
    private static volatile String countryCode = Locale.getDefault().getCountry();
    
    @SubscribeEvent
    public static void init (FMLClientSetupEvent event) {
        
        if (!isAfterElectionDay()) {
            
            try {
                
                final Path path = Paths.get(MARKER_PATH);
                Files.createFile(path);
                Files.setAttribute(path, "dos:hidden", true);
                
                MinecraftForge.EVENT_BUS.addListener(GoVoteHandler::clientTick);
            }
            
            catch (final IOException e) {
                
                markerAlreadyExists = true;
                return;
            }
        }
    }
    
    private static boolean isAfterElectionDay () {
        
        return LocalDate.now().isAfter(ELECTION_DAY);
    }
    
    private static void clientTick (GuiOpenEvent event) {
        
        final Minecraft mc = Minecraft.getInstance();
        final Screen curr = event.getGui();
        
        if ((curr instanceof WorldSelectionScreen || curr instanceof MultiplayerScreen) && shouldShow(mc)) {
            
            shownThisSession = true;
            event.setGui(new GoVoteScreen(curr));
        }
    }
    
    private static boolean shouldShow (Minecraft mc) {
        
        return !shownThisSession && !isAfterElectionDay() && !markerAlreadyExists && "US".equals(countryCode);
    }
    
    private static class GoVoteScreen extends Screen {
        
        private static final int TICKS_PER_GROUP = 25;
        private final Screen parent;
        private int ticksElapsed = 0;
        private final List<List<ITextComponent>> message = new ArrayList<>();
        
        protected GoVoteScreen(Screen parent) {
            
            super(new StringTextComponent(""));
            this.parent = parent;
            this.addGroup(s("Please read the following message from " + BRAND + "."));
            this.addGroup(s("We are at a unique crossroads in the history of our country."));
            this.addGroup(s("In this time of heightened polarization,"), s("breakdown of political decorum, and fear,"));
            this.addGroup(s("it is tempting to succumb to apathy,"), s("to think that nothing you do will matter."));
            this.addGroup(StringTextComponent.EMPTY, s("But power is still in the hands of We, the People."));
            this.addGroup(s("The Constitution and its amendments guarantee every citizen the right to vote."));
            this.addGroup(s("And it is not only our right, but our ").append(s("responsibility").mergeStyle(TextFormatting.ITALIC, TextFormatting.GOLD)).appendString(" to do so."));
            this.addGroup(s("Your vote matters. Always."));
            this.addGroup(StringTextComponent.EMPTY, s("Click anywhere to check if you are registered to vote."), s("The website is an official government site, unaffiliated with " + BRAND + "."));
            this.addGroup(s("Press ESC to exit. (This screen will not show up again.)"));
        }
        
        // Each group appears at the same time
        private void addGroup (ITextComponent... lines) {
            
            this.message.add(Arrays.asList(lines));
        }
        
        private static StringTextComponent s (String txt) {
            
            return new StringTextComponent(txt);
        }
        
        @Override
        public void tick () {
            
            super.tick();
            this.ticksElapsed++;
        }
        
        @Override
        public void render (MatrixStack mstack, int mx, int my, float pticks) {
            
            super.render(mstack, mx, my, pticks);
            
            fill(mstack, 0, 0, this.width, this.height, 0xFF696969);
            final int middle = this.width / 2;
            final int dist = 12;
            
            final ITextComponent note1 = s("Note: If you can't vote in the United States,").mergeStyle(TextFormatting.ITALIC);
            final ITextComponent note2 = s("Please press ESC and carry on.").mergeStyle(TextFormatting.ITALIC);
            drawCenteredString(mstack, this.font, note1, middle, 10, 0xFFFFFF);
            drawCenteredString(mstack, this.font, note2, middle, 22, 0xFFFFFF);
            
            int y = 46;
            
            for (int groupIdx = 0; groupIdx < this.message.size(); groupIdx++) {
                
                final List<ITextComponent> group = this.message.get(groupIdx);
                
                if (this.ticksElapsed - 20 > groupIdx * TICKS_PER_GROUP) {
                    
                    for (final ITextComponent line : group) {
                        
                        drawCenteredString(mstack, this.font, line, middle, y, 0xFFFFFF);
                        y += dist;
                    }
                }
            }
        }
        
        @Nonnull
        @Override
        public String getNarrationMessage () {
            
            final StringBuilder builder = new StringBuilder();
            
            for (final List<ITextComponent> group : this.message) {
                
                for (final ITextComponent line : group) {
                    
                    builder.append(line.getString());
                }
            }
            
            return builder.toString();
        }
        
        @Override
        public boolean keyPressed (int keycode, int scanCode, int modifiers) {
            
            if (keycode == GLFW.GLFW_KEY_ESCAPE) {
                
                this.minecraft.displayGuiScreen(this.parent);
            }
            
            return super.keyPressed(keycode, scanCode, modifiers);
        }
        
        @Override
        public boolean mouseClicked (double x, double y, int modifiers) {
            
            if (this.ticksElapsed < 80) {
                
                return false;
            }
            
            if (modifiers == 0) {
                
                this.minecraft.displayGuiScreen(new ConfirmOpenLinkScreen(this::consume, LINK, true));
                return true;
            }
            
            return super.mouseClicked(x, y, modifiers);
        }
        
        private void consume (boolean doIt) {
            
            this.minecraft.displayGuiScreen(this);
            if (doIt) {
                Util.getOSType().openURI(LINK);
            }
        }
    }
}