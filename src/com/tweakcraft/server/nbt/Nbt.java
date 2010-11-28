package com.tweakcraft.server.nbt;

import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.Config;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 *  @author GuntherDW
 *
 */
public class Nbt {

    public Player getPlayerData(String name)
    {
        Tag playerData = null;
        Player _player = null;
        Tag invData = null;

        if(name.trim().length() != 0)
        {

            try {
                playerData = Tag.readFrom(new FileInputStream(Config.WORLD_FOLDER + "/players/" + name + ".dat"));
                invData = playerData.findTagByName("Inventory");
                //invData.

            }
            catch (IOException e) {
                e.printStackTrace();  
            }

        }
        return _player;
    }

    public static Tag getChunk(Integer x,Integer y, boolean Nether)
    {
        /*
            * The first folder name is base36(-13 % 64). This is base36(51) which is "1f".
            * The second folder name is base36(44 % 64). This is base36(44) which is "18".
            * The chunk file's name is "c." + base36(-13) + "." + base36(44) + ".dat". This evaluates to "c.-d.18.dat"
            * Thus, the chunk at (-13, 44) is stored in "1f/18/c.-d.18.dat"
         */
        Tag chunk = null;

        int cx, cy;

//        cx = (int)Math.floor(x / 16);
//        cy = (int)Math.floor(y / 16);

	cx = x >> 4;
	cy = y >> 4;

        try{
            chunk = Tag.readFrom(new FileInputStream(Config.WORLD_FOLDER + "/" + (Nether ? "" : "DIM-1/") + Integer.toString(cx % 64, 36) + "/"
		    + Integer.toString(cy % 64, 36) + "/"
		    + "c." + Integer.toString(cx, 36) + "."
		    + Integer.toString(cy, 36) + ".dat"));
        }
        catch (IOException e) {
                e.printStackTrace();  
        }
        return chunk;
    }
}
