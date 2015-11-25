package net.darkhax.bookshelf.client.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.darkhax.bookshelf.lib.util.RenderUtils;

@SideOnly(Side.CLIENT)
public class GuiNotification extends Gui {
    
    /**
     * The ResourceLocation for the background of the effect.
     */
    private static final ResourceLocation BACKGROUND = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    
    /**
     * The client-side Minecraft instance.
     */
    private Minecraft minecraft;
    
    /**
     * The width of the GUI
     */
    private int width;
    
    /**
     * The height of the gui.
     */
    private int height;
    
    /**
     * The title on the notification. This is the top part.
     */
    private String title;
    
    /**
     * The description on the notification. This is the bottom part.
     */
    private String description;
    
    /**
     * A timer to track how long the notification has been displayed.
     */
    private long notificationTime;
    
    /**
     * An instance of the ItemRenderer.
     */
    private RenderItem itemRenderer;
    
    /**
     * An ItemStack to render on the notification. If null, the iconLocation will be used.
     */
    private ItemStack iconStack;
    
    /**
     * A 16x16 texture file to render onto the notification GUI.
     */
    private ResourceLocation iconLocation;
    
    /**
     * Constructs a new instance of GuiNotification. Each mod should have their own instance
     * which they can use, although it is not required.
     * 
     * @param minecraft: The instance of Minecraft.
     */
    public GuiNotification(Minecraft minecraft) {
        
        this.minecraft = minecraft;
        this.itemRenderer = new RenderItem();
    }
    
    /**
     * Updates the notification with a new title and description. This also sets the time to
     * the current system time.
     * 
     * @param title: The title for the notification.
     * @param description: The description for the notification.
     */
    public void updateNotification (String title, String description) {
        
        this.title = title;
        this.description = description;
        this.notificationTime = Minecraft.getSystemTime();
    }
    
    /**
     * Updates the icon for the notification to a ResourceLocation. A 16x16 image is expected.
     * 
     * @param iconLocation: The ResourceLocation to load for the effect.
     */
    public void updateNotificationIcon (ResourceLocation iconLocation) {
        
        this.iconLocation = iconLocation;
    }
    
    /**
     * Updates the icon for the notification to an ItemStack.
     * 
     * @param stack: The ItemStack to render on the notification.
     */
    public void updateNotificationIcon (ItemStack stack) {
        
        this.iconStack = stack;
    }
    
    /**
     * Updates the scaling of the notification GUI. This is automatically called by the
     * renderNotification method.
     */
    private void updateNotificationScale () {
        
        GL11.glViewport(0, 0, this.minecraft.displayWidth, this.minecraft.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.width = this.minecraft.displayWidth;
        this.height = this.minecraft.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.minecraft, this.minecraft.displayWidth, this.minecraft.displayHeight);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double) this.width, (double) this.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }
    
    /**
     * Renders the notification onto the screen. It is recommenced to call this every tick in a
     * TickEvent to ensure that the effect will render correctly.
     */
    public void renderNotification () {
        
        if (this.notificationTime != 0L && Minecraft.getMinecraft().thePlayer != null) {
            
            double progression = (double) (Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;
            
            if (progression < 0.0D || progression > 1.0D) {
                
                this.notificationTime = 0L;
                return;
            }
            
            if (progression > 0.5D)
                progression = 0.5D;
                
            this.updateNotificationScale();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            double slideDistance = progression * 2.0D;
            
            if (slideDistance > 1.0D)
                slideDistance = 2.0D - slideDistance;
                
            slideDistance *= 4.0D;
            slideDistance = 1.0D - slideDistance;
            
            if (slideDistance < 0.0D)
                slideDistance = 0.0D;
                
            slideDistance *= slideDistance;
            slideDistance *= slideDistance;
            int posX = this.width - 160;
            int posY = 0 - (int) (slideDistance * 36.0D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            this.minecraft.getTextureManager().bindTexture(BACKGROUND);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.drawTexturedModalRect(posX, posY, 96, 202, 160, 32);
            
            this.minecraft.fontRenderer.drawString(this.title, posX + 30, posY + 7, -256);
            this.minecraft.fontRenderer.drawString(this.description, posX + 30, posY + 18, -1);
            
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            
            if (this.iconStack != null)
                this.itemRenderer.renderItemAndEffectIntoGUI(this.minecraft.fontRenderer, this.minecraft.getTextureManager(), this.iconStack, posX + 8, posY + 8);
                
            else if (this.iconLocation != null) {
                
                minecraft.renderEngine.bindTexture(this.iconLocation);
                RenderUtils.drawTextureModalRectSize(posX + 8, posY + 8, 0, 0, 16, 16, 16, 1f);
            }
            
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }
    
    /**
     * Ends the notification, and resets all of the data.
     */
    public void stopNotification () {
        
        this.notificationTime = 0L;
        this.title = null;
        this.description = null;
        this.iconLocation = null;
        this.iconStack = null;
    }
}