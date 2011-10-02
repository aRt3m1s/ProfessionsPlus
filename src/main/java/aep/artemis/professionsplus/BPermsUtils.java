package aep.artemis.professionsplus;

import de.bananaco.permissions.Permissions;
import de.bananaco.permissions.interfaces.PermissionSet;
import de.bananaco.permissions.worlds.WorldPermissionsManager;
import org.bukkit.World;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: art3m1s
 * Date: 10/2/11
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class BPermsUtils {
    public static ProfessionsPlus pp;
    public WorldPermissionsManager wpm = null;
    public BPermsUtils(ProfessionsPlus instance) {
        pp = instance;
    }

    public void fixGroups(List<World> worlds) {
        wpm = Permissions.getWorldPermissionsManager();
        for(World w: worlds){
            String wName = w.getName();
            if(!wName.endsWith("_nether")){
                PermissionSet pSet = wpm.getPermissionSet(w);
                List<String> aG = pSet.getAllCachedGroups();
                Configuration wG;
                wG = new Configuration(new File(pp.getDataFolder().getPath()+"/bPerms/groups", wName+".yml"));
                wG.load();
                String[] gR = {"<groupName2>", "<groupName1>&<groupName3>"};
                List<String> req = Arrays.asList(gR);
                wG.setProperty("<groupName>.profession-type", "<someProfession>");
                wG.setProperty("<groupName>.group-requirements", req);
                wG.save();
                for(String g: aG){
                    if(!wG.getKeys().contains(g)&&!wG.getKeys().contains("<groupName>")){
                        wG.setProperty(g+".profession-type", null);
                        wG.setProperty(g+".group-requirements", null);
                        wG.save();
                    }
                }
                for(String gIy: wG.getKeys()){
                    boolean gIyIp = false;
                    for(String gIp: aG){
                        if(gIy.equals(gIp)){
                            gIyIp = true;
                            break;
                        }
                    }
                    if(!gIyIp&&(!gIy.equalsIgnoreCase("<groupName>"))){
                        wG.removeProperty(gIy);
                    }
                }
            }
        }
    }
}
