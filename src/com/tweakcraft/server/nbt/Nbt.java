package com.tweakcraft.server.nbt;

import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.Config;
import com.tweakcraft.server.model.Inventory;
import com.tweakcraft.server.model.Item;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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
        ArrayList<Item> items;
        Inventory crafting = null;
        Inventory armor = null;
        Inventory inv = null;

        if(name.trim().length() != 0)
        {

            try {
                playerData = Tag.readFrom(new FileInputStream(Config.WORLD_FOLDER + "/players/" + name + ".dat"));
                invData = playerData.findTagByName("Inventory");
                items = getInventory(invData);
                inv = new Inventory(36);
                crafting = new Inventory(4);
                armor = new Inventory(4);
                for(Item i : items)
                {
                    if(i.getSlot() < 80)
                        inv.setSlot(i.getSlot(), i);
                    else if(i.getSlot() >= 80 && i.getSlot() < 100)
                        crafting.setSlot(i.getSlot()-80, i);
                    else if(i.getSlot() >= 100)
                        armor.setSlot(i.getSlot()-100, i);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        return _player;
    }

    public ArrayList<Item> getInventory(Tag tag)
    {
        ArrayList<Item> tagMap = null;

        Tag[] subtags = (Tag[]) tag.getValue();
		for (Tag st : subtags) {
            Item it = new Item();
            String name = st.getName();
            Tag[] subsubtags = (Tag[]) st.getValue();
            for (Tag sst : subsubtags) {
                it.setAttribute(sst.getName(), sst.getType(), sst.getValue());
            }
            tagMap.add(it);
		}
        return tagMap;
    }

    public static Tag getChunk(int x,int y, boolean Nether)
    {
        /*
            * The first folder name is base36(-13 % 64). This is base36(51) which is "1f".
            * The second folder name is base36(44 % 64). This is base36(44) which is "18".
            * The chunk file's name is "c." + base36(-13) + "." + base36(44) + ".dat". This evaluates to "c.-d.18.dat"
            * Thus, the chunk at (-13, 44) is stored in "1f/18/c.-d.18.dat"
         */
        Tag chunk = null;

        int cx, cy, bx, by;

//        cx = (int)Math.floor(x / 16);
//        cy = (int)Math.floor(y / 16);


	//System.out.println(y % 64 + " " + y);
	//System.out.println(x % 64 + " " + x);

	bx = cx = x >> 4;
	by = cy = y >> 4;


	while(bx < 0)
	    bx += 64;
	while(by < 0)
	    by += 64;


	//System.out.println(cx % 64 + " " + cy % 64);
        try{

            chunk = Tag.readFrom(new FileInputStream(Config.WORLD_FOLDER + "/" + (Nether ? "DIM-1/" : "") + Integer.toString((bx % 64), 36) + "/"
		    + Integer.toString((by % 64), 36) + "/"
		    + "c." + Integer.toString(cx, 36) + "."
		    + Integer.toString(cy, 36) + ".dat"));
        }
        catch (IOException e) {
                e.printStackTrace();  
        }
        return chunk;
    }
}
