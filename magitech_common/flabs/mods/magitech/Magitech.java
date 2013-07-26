package flabs.mods.magitech;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.*;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import flabs.mods.magitech.client.*;
import flabs.mods.magitech.item.*;
import flabs.mods.magitech.api.*;
import flabs.mods.magitech.aura.*;
import flabs.mods.magitech.block.*;

@Mod(modid = "magitech", name = "Magitech", version = Version.version)
@NetworkMod
public class Magitech {
    
    @Instance
    public static Magitech instance;
    @SidedProxy(clientSide = "flabs.mods.magitech.client.ProxyClient", serverSide = "flabs.mods.magitech.ProxyCommon")
    public static ProxyCommon proxy;
    public Wand wand;
    public Item magibucket, magibottle;
    public Block magi;
    public BlockMagiCrucible magicrucible;
    public Fluid fluidmagi = new Fluid("magi") {
        public int getColor() {
            return MagiColor.getColor(0);
        }
        
        public int getColor(World world, int x, int y, int z) {
            TileEntity te = world.getBlockTileEntity(x, y, z);
            if (te instanceof TileEntityMagi) {
                return MagiColor.getColor(((TileEntityMagi) te).percentage);
            }
            return getColor();
        }
        
        public int getColor(FluidStack stack) {
            return MagiColor.getColor(stack.tag.getByte("perc"));
        }
    };
    
    public static AuraWatcher aura = new AuraWatcher();
    
    public static CreativeTabs creativeTab = new CreativeTabs("magitech") {
        @Override
        public int getTabIconItemIndex() {
            return Magitech.instance.wand.itemID;
        }
    };
    
    @EventHandler
    public void init(FMLInitializationEvent evt) {
        
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        Configuration cfg = new Configuration(new File(evt.getModConfigurationDirectory(), "Magitech.cfg"));
        int wandid, magibucketid, magibottleid, magiid, crucibleid;
        
        try {
            cfg.load();
            
            wandid = cfg.getItem("BasicWand", 15000).getInt();
            magibucketid = cfg.getItem("MagiBucket", 15001).getInt();
            magibottleid = cfg.getItem("MagiBottle", 15002).getInt();
            
            magiid = cfg.getBlock("Magi", 812).getInt();
            crucibleid = cfg.getBlock("Magi Crucible", 925).getInt();
        } finally {
            cfg.save();
        }
        NetworkRegistry.instance().registerGuiHandler(this, proxy);

        FluidRegistry.registerFluid(fluidmagi);
        
        wand = new Wand(wandid);
        wand.setUnlocalizedName(Version.unlocalizedWand).setCreativeTab(creativeTab);
        magi = new BlockMagi(magiid, fluidmagi).setUnlocalizedName(Version.unlocalizedMagi);
        magibucket = new ItemBucket(magibucketid, magi.blockID){
            @Override
            public void registerIcons(IconRegister ir) {
                this.itemIcon=ir.registerIcon(Version.unlocalizedBucketMagi);
            }
        }.setUnlocalizedName(Version.unlocalizedBucketMagi).setCreativeTab(creativeTab);
        magibottle = new Item(magibottleid){
            @Override
            public void registerIcons(IconRegister ir) {
                this.itemIcon=ir.registerIcon(Version.unlocalizedBottleMagi);
            }
        }.setUnlocalizedName(Version.unlocalizedBottleMagi).setCreativeTab(creativeTab);
        magicrucible = new BlockMagiCrucible(crucibleid, proxy.getRenderId());
        magicrucible.setHardness(2.0F).setUnlocalizedName(Version.unlocalizedMagiCrucible).setCreativeTab(creativeTab);
        
        FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidmagi, 64),new ItemStack(magibottle), new ItemStack(Item.glassBottle));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidmagi, 128), new ItemStack(magibucket), new ItemStack(Item.bucketEmpty));
        
        TileEntity.addMapping(TileEntityMagiCrucible.class, "magicruc");
        TileEntity.addMapping(TileEntityMagi.class, "magi");

        GameRegistry.registerBlock(magi, Version.unlocalizedMagi);
        GameRegistry.registerBlock(magicrucible, Version.unlocalizedMagiCrucible);

        fluidmagi.setBlockID(magi).setLuminosity(4);

        MinecraftForge.EVENT_BUS.register(new GuiListenchanger());
        MinecraftForge.EVENT_BUS.register(magi);
        MinecraftForge.EVENT_BUS.register(aura);

        LanguageRegistry.instance().addNameForObject(new ItemStack(magi, 1, 0), "en_US", EnumChatFormatting.AQUA + "Magi");
        LanguageRegistry.instance().addNameForObject(new ItemStack(magi, 1, 1), "en_US", EnumChatFormatting.DARK_PURPLE + "Ender");
        LanguageRegistry.instance().addNameForObject(magicrucible, "en_US", "Magi Crucible");
        LanguageRegistry.instance().addNameForObject(magibucket, "en_US", EnumChatFormatting.AQUA + "Magi Bucket");
        LanguageRegistry.instance().addNameForObject(magibottle, "en_US", EnumChatFormatting.AQUA + "Magi Bottle");
        LanguageRegistry.instance().addNameForObject(wand, "en_US", EnumChatFormatting.GREEN + "Wand");
        LanguageRegistry.instance().addStringLocalization(Version.unlocalizedCreativeTab, "en_US", "Magitech");

        MagiRecipes.setMagi(new ItemStack(Item.ingotIron), 8);
        MagiRecipes.setMagi(new ItemStack(Item.ingotGold), 64);
        MagiRecipes.setMagi(new ItemStack(Item.enderPearl), 128);
        MagiRecipes.setMagi(new ItemStack(Item.diamond), 256);
        
        GameRegistry.addRecipe(new ItemStack(wand), "AAE", "ABA", "BAA", 'E', Item.emerald, 'B', Item.bone);
        GameRegistry.addRecipe(new ItemStack(magicrucible), "ACA", "GEG", 'C', Item.cauldron, 'G', Item.goldNugget, 'E', Item.emerald);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        proxy.registerRendering(magicrucible.getRenderType());
    }
    
    public static void log(Object o) {
        System.out.println("[Magitech]: " + o);
    }
}
