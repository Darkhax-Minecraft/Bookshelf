package net.darkhax.bookshelf.command;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;

import net.darkhax.bookshelf.util.Utilities;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class CommandItemColor extends CommandBase {
    
    @Override
    public String getCommandName () {
        
        return "color";
    }
    
    @Override
    public String getCommandUsage (ICommandSender sender) {
        
        return "command.bookshelf.color.usage";
    }
    
    @Override
    public void processCommand (ICommandSender sender, String[] params) {
        
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        
        int colorToApply = -1;
        
        if (params.length == 1)
            colorToApply = (StringUtils.isNumeric(params[0])) ? Integer.parseInt(params[0]) : (params[0].startsWith("#") ? Color.decode(params[0]).getRGB() : (params[0].equalsIgnoreCase("remove") || params[0].equalsIgnoreCase("r")) ? -666 : (params[0].equalsIgnoreCase("random")) ? Utilities.getRandomColor() : -1);
            
        else if (params.length == 3 && areParamsRGB(params))
            colorToApply = new Color(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2])).getRGB();
            
        if (colorToApply == -1 || player == null || player.getHeldItem() == null)
            throw new WrongUsageException("command.bookshelf.color.error", new Object[0]);
            
        else if (colorToApply == -666) {
            
            Utilities.removeItemColor(player.getHeldItem());
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + StatCollector.translateToLocal("command.bookshelf.color.remove")));
        }
        
        else {
            
            Utilities.setItemColor(player.getHeldItem(), colorToApply);
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