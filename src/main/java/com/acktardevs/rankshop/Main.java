package com.acktardevs.rankshop;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import net.lldv.llamaeconomy.LlamaEconomy;

import java.util.Iterator;
import java.util.List;

public class Main extends PluginBase implements Listener {

public Integer tempval;

@Override
public void onEnable() {
	
      this.saveDefaultConfig();
      
     if (this.getServer().getPluginManager().getPlugin("LlamaEconomy") == null) {
         this.getLogger().info("Missing dependency (LlamaEconomy). Disabling plugin");
         this.getServer().getPluginManager().disablePlugin(this);
      } else if (this.getServer().getPluginManager().getPlugin("LuckPerms") == null) {
         this.getLogger().info("Missing dependency (LuckPerms). Disabling plugin");
         this.getServer().getPluginManager().disablePlugin(this);
      } 
      
      this.getServer().getPluginManager().registerEvents(this, this);
   }

@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
   switch (command.getName()) {
        case "ranks":
          if(!(sender instanceof Player)){
                this.getLogger().info("You cannot use this command from console");
           return false;
         }

             FormWindowSimple form = new FormWindowSimple(getConfig().getString("form-name"), "");
	
	         List<String> purchasableRanksList = getConfig().getStringList("identifiers");
             Iterator var2 = purchasableRanksList.iterator();

             while(var2.hasNext()) {
                String rank = (String)var2.next();
                form.addButton(new ElementButton("§8"+getConfig().getString(rank+".name")+"\n§d§l»§r §8Tap to select!"));
             }
          
          Player player = (Player) sender;
          player.showFormWindow(form);
                break;
        }
        return true;
    }

@EventHandler
    public void formRespond(cn.nukkit.event.player.PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        FormWindow window = event.getWindow();
        if (event.getResponse() == null) return;
        if (window instanceof FormWindowSimple) {
            String title = ((FormWindowSimple) event.getWindow()).getTitle();
            int button = ((FormResponseSimple) event.getResponse()).getClickedButtonId();
            if (!event.wasClosed()) {
                if (title.equals(getConfig().getString("form-name"))) {
                    tempval = button;
               	FormWindowSimple form = new FormWindowSimple(getConfig().getString("confirmation-form-name"), "Are you sure that you want to buy the §l§d"+getConfig().getString(button+".name")+" §rrank for §l§7$§e"+getConfig().getDouble(button+".price"));
                         form.addButton(new ElementButton("§8Purchase\n§d§l»§r §8Tap to select!"));
	                     form.addButton(new ElementButton("§l§cBack\n§d§l»§r §8Tap to select!"));
                   player.showFormWindow(form);
                   
                } else if (title.equals(getConfig().getString("confirmation-form-name"))) {
                	if (button == 0) {
                	   if (LlamaEconomy.getAPI().getMoney(player) < getConfig().getDouble(tempval+".price")) {
                	       player.sendMessage(getConfig().getString("no-money"));
               } else {
                	LlamaEconomy.getAPI().reduceMoney(player.getName(), getConfig().getDouble(tempval+".price"));
                
                    this.getServer().dispatchCommand(getServer().getConsoleSender(), "lp user " + player.getName() + " parent set "+getConfig().getString(tempval+".lp-user"));
                    player.sendMessage(getConfig().getString("rank-purchased"));
              } 
                 } 
                  } 
            } 
        } 
    } 
                        
} 
