package com.tweakcraft.server.nbt;

import sun.beans.editors.IntEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: guntherdw
 * Date: 27/11/10
 * Time: 00:05
 * To change this template use File | Settings | File Templates.
 */
public class Nbt {
    public Tag getPlayerData(String name)
    {
        Tag playerData = null;
        if(name.trim().length() != 0)
        {

            try {
                playerData = Tag.readFrom(new FileInputStream("players/" + name + ".dat"));
            }
            catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        return playerData;
    }

    public Tag getChunk(Integer x,Integer y, boolean Nether)
    {
        /*
            * The first folder name is base36(-13 % 64). This is base36(51) which is "1f".
            * The second folder name is base36(44 % 64). This is base36(44) which is "18".
            * The chunk file's name is "c." + base36(-13) + "." + base36(44) + ".dat". This evaluates to "c.-d.18.dat"
            * Thus, the chunk at (-13, 44) is stored in "1f/18/c.-d.18.dat"
         */
        Tag chunk = null;

        Integer cx, cy;

        cx = (int)Math.floor(x / 16);
        cy = (int)Math.floor(y / 16);



        try{
            chunk = Tag.readFrom(new FileInputStream((Nether ? "" : "DIM-1/") + Integer.toString(cx % 64, 36) + "/"
                                                   + Integer.toString(cy % 64, 36) + "/"
                                                   + "c." + Integer.toString(cx, 36) + "."
                                                   + Integer.toString(cy, 36) + ".dat"));
        }
        catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return chunk;
    }
}
