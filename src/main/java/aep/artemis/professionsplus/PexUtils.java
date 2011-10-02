package aep.artemis.professionsplus;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: art3m1s
 * Date: 9/30/11
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class PexUtils {
    public static ProfessionsPlus pp;
    public PexUtils(ProfessionsPlus plugin){
        pp = plugin;
    }

    public void fixGroups(List<World> worlds) {
        PermissionManager pm = PermissionsEx.getPermissionManager();
        PermissionGroup[] aG = pm.getGroups();
        for(World w: worlds){
            String wName = w.getName();
            if(!wName.endsWith("_nether")){
                Configuration wG;
                wG = new Configuration(new File(pp.getDataFolder().getPath()+"/PEX/groups", wName+".yml"));
                wG.load();
                String[] gR = {"<groupName2>", "<groupName1>&<groupName3>"};
                List<String> req = Arrays.asList(gR);
                wG.setProperty("<groupName>.profession-type", "<someProfession>");
                wG.setProperty("<groupName>.group-requirements", req);
                wG.save();
                for(PermissionGroup g: aG){
                    if(!wG.getKeys().contains(g.getName())&&!wG.getKeys().contains("<groupName>")){
                        wG.setProperty(g.getName()+".profession-type", null);
                        wG.setProperty(g.getName()+".group-requirements", null);
                        wG.save();
                    }
                }
                for(String gIy: wG.getKeys()){
                    boolean gIyIp = false;
                    for(PermissionGroup gIp: aG){
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
