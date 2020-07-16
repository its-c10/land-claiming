package net.dohaw.play.landclaiming.files;

import me.c10coding.coreapi.files.ConfigManager;
import net.dohaw.play.landclaiming.Message;
import org.bukkit.plugin.java.JavaPlugin;

public class MessagesConfig extends ConfigManager {

    public MessagesConfig(JavaPlugin plugin) {
        super(plugin, "messages.yml");
    }

    public String getMessage(Message msg){
        return config.getString(msg.getConfigKey());
    }

}
