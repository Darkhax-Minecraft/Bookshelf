package net.darkhax.bookshelf.command;

import net.darkhax.bookshelf.lib.VanillaColor;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.lib.util.MathsUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class CommandItemColor extends CommandBase {
    
    @Override
    public String getName () {
        
        return "color";
    }
    
    @Override
    public String getCommandUsage (ICommandSender sender) {
        
        return "command.bookshelf.color.usage";
    }
    
    @Override
    public void execute (ICommandSender sender, String[] params) throws WrongUsageException, PlayerNotFoundException {
        
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        
        int colorToApply = -1;
        
        if (params.length == 1)
            colorToApply = (StringUtils.isNumeric(params[0])) ? Integer.parseInt(params[0]) : (params[0].startsWith("#") ? Color.decode(params[0]).getRGB() : (params[0].equalsIgnoreCase("remove") || params[0].equalsIgnoreCase("r")) ? -666 : (params[0].equalsIgnoreCase("random")) ? MathsUtils.getRandomColor() : -1);
            
        if (params.length == 1 && colorToApply == -1) {
            
            VanillaColor namedColor = VanillaColor.getColorByName(params[0]);
            
            if (namedColor != null)
                colorToApply = namedColor.color.getRGB();
        }
        
        else if (params.length == 3 && areParamsRGB(params))
            colorToApply = new Color(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2])).getRGB();
            
        if (colorToApply == -1 || player == null || player.getHeldItem() == null)
            throw new WrongUsageException("command.bookshelf.color.error", new Object[0]);
            
        else if (colorToApply == -666) {
            
            ItemStackUtils.removeItemColor(player.getHeldItem());
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + StatCollector.translateToLocal("command.bookshelf.color.remove")));
        }
        
        else {
            
            ItemStackUtils.setItemColor(player.getHeldItem(), colorToApply);
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + StatCollector.translateToLocal("command.bookshelf.color.success")));
        }
    }
    
    @Override
    public int getRequiredPermissionLevel () {
        
        return 2;
    }
    
    /**
     * A very simple method to ensure that all of the parameters provided by a method fall into
     * a proper RGB integer format. A proper integer format is three integers which are less
     * then 256 and greater than -1.
     * 
     * @param params: The parameters to check against.
     * @return boolean: True if the params are good, false if they're not.
     */
    private boolean areParamsRGB (String[] params) {
        
        for (String param : params)
            if (!StringUtils.isNumeric(param) && Integer.parseInt(param) < 256)
                return false;
                
        return true;
    }
}