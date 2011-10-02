package aep.artemis.professionsplus;

import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;
import org.bukkit.World;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: art3m1s
 * Date: 10/2/11
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class PBukkitUtils {
    public static ProfessionsPlus pp;
    public PBukkitUtils(ProfessionsPlus instance) {
        pp = instance;
    }

    public void fixGroups(List<World> worlds) {
        PermissionsPlugin PermsPlugin = new PermissionsPlugin();
        List<Group> aG = PermsPlugin.getAllGroups();
        for(World w: worlds){
            String wName = w.getName();
            if(!wName.endsWith("_nether")){
                Configuration wG;
                wG = new Configuration(new File(pp.getDataFolder().getPath()+"/PermsBukkit/groups", wName+".yml"));
                wG.load();
                String[] gR = {"<groupName2>", "<groupName1>&<groupName3>"};
                List<String> req = Arrays.asList(gR);
                wG.setProperty("<groupName>.profession-type", "<someProfession>");
                wG.setProperty("<groupName>.group-requirements", req);
                wG.save();
                for(Group g: aG){
                    if(!wG.getKeys().contains(g.getName())&&!wG.getKeys().contains("<groupName>")){
                        wG.setProperty(g.getName()+".profession-type", null);
                        wG.setProperty(g.getName()+".group-requirements", null);
                        wG.save();
                    }
                }
                for(String gIy: wG.getKeys()){
                    boolean gIyIp = false;
                    for(Group gIp: aG){
                        if(gIy.equals(gIp.getName())){
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
