package flabs.mods.magitech;

import java.io.File;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "magitech", name = "Magitech", version = Version.version)
@NetworkMod
public class Magitech {
    @EventHandler
    public void init(FMLInitializationEvent evt) {
        
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        Configuration cfg = new Configuration(new File(evt.getModConfigurationDirectory(), "Magitech.cfg"));
        
        try {
            cfg.load();
            cfg.getBlock("Magi", 812).getInt();
        } finally {
            cfg.save();
        }
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        
    }
}
