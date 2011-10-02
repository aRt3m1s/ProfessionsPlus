package aep.artemis.professionsplus;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: art3m1s
 * Date: 9/29/11
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfessionsPlus extends JavaPlugin{
    Logger log = Logger.getLogger("Minecraft");
    final String ppPrefix = "[PROF] ";
    private final PexUtils pexUtils = new PexUtils(this);
    private final BPermsUtils bPermsUtils = new BPermsUtils(this);
    private final PBukkitUtils pBukkitUtils = new PBukkitUtils(this);
    String whatPermissions;
    Configuration professions;
    Configuration history;
    @Override
    public void onDisable() {
        log.info(ppPrefix+getDescription().getName()+" version v"+getDescription().getVersion()+" has been Disabled!");
    }

    @Override
    public void onEnable() {
        log.info(ppPrefix+getDescription().getName()+" version v"+getDescription().getVersion()+" has been Enabled!");
        PluginManager pm = getServer().getPluginManager();
        if(pm.isPluginEnabled("PermissionsEx")){
            whatPermissions = "PermissionsEx";
            log.info(ppPrefix+"hooked into PermissionsEx");
        }else if(pm.isPluginEnabled("bPermissions")){
            whatPermissions = "bPermissions";
            log.info(ppPrefix+"hooked into bPermissions");
        }else if(pm.isPluginEnabled("PermissionsBukkit")){
            whatPermissions = "PermissionsBukkit";
            log.info(ppPrefix+"hooked into PermissionsBukkit");
        }else{
            whatPermissions = "none";
            log.info(ppPrefix+"no supported Permissions found, disabling plugin");
            pm.disablePlugin(this);
        }
        if(isEnabled()){
            List<World> worlds = getServer().getWorlds();
            professions = new Configuration(new File(getDataFolder().getPath()+"/professions", "/professions.yml"));
            professions.load();fixProfs(professions, worlds);professions.load();
            history = new Configuration(new File(getDataFolder().getPath()+"/professions", "/history.yml"));
            history.load();makeHistory(history);history.load();
            fixGroups(worlds, whatPermissions);
        }
    }

    void fixGroups(List<World> worlds, String whatPermissions) {
        if(whatPermissions.equalsIgnoreCase("PermissionsEx")){
            pexUtils.fixGroups(worlds);
        }else if(whatPermissions.equalsIgnoreCase("bPermissions")){
            bPermsUtils.fixGroups(worlds);
        }else if(whatPermissions.equalsIgnoreCase("PermissionsBukkit")){
            pBukkitUtils.fixGroups(worlds);
        }
    }

    void makeHistory(Configuration history) {
        if(history.getKeys("<worldName>").isEmpty()){
            history.setProperty("<worldName>.<userName>.<groupName>.last-join", 0);
            history.save();
        }
    }

    void fixProfs(Configuration professions, List<World> worlds) {
        for(World w: worlds){
            if(!w.getName().endsWith("_nether")){
                String wName = w.getName();
                if(professions.getKeys(wName).isEmpty()){
                    professions.setProperty(wName+".professions.faction.group-limit", (1));
                    professions.setProperty(wName+".professions.faction.time-limit", (192));
                    professions.setProperty(wName+".professions.faction.join-cost", (double) (600));
                    professions.setProperty(wName+".professions.faction.leave-cost", (double) (300));
                    professions.save();
                    professions.setProperty(wName+".professions.job.group-limit", (1));
                    professions.setProperty(wName+".professions.job.time-limit", (192));
                    professions.setProperty(wName+".professions.job.join-cost", (double) (600));
                    professions.setProperty(wName+".professions.job.leave-cost", (double) (300));
                    professions.save();
                    professions.setProperty(wName+".professions.sub-job.group-limit", (1));
                    professions.setProperty(wName+".professions.sub-job.time-limit", (192));
                    professions.setProperty(wName+".professions.sub-job.join-cost", (double) 600);
                    professions.setProperty(wName+".professions.sub-job.leave-cost", (double) (300));
                    professions.save();
                    professions.setProperty(wName+".professions.skill.group-limit", (1));
                    professions.setProperty(wName+".professions.skill.time-limit", (192));
                    professions.setProperty(wName+".professions.skill.join-cost", (double) (600));
                    professions.setProperty(wName+".professions.skill.leave-cost", (double) (300));
                    professions.save();
                }
            }
        }
    }
}
