/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.facefreakedstudios.app.lq;

import java.io.IOException;

/**
 *
 * @author gavin17
 */
abstract class Movement
{
    protected String current_blk, current_blk_dat, symbol;
    protected String[][] orig_map, cur_map, map_data;
    protected int posit_x = 0, posit_y = 0, last_posit_x = 0, last_posit_y = 0;
    
    protected void setMap(Lucas lucas, String map) throws IOException // without extensions
    {
        this.orig_map = LQCLI.fetchMap(map + ".map");
        this.cur_map = this.orig_map;
        this.map_data = LQCLI.fetchMapData(lucas, map + ".dat");
    }
        
    protected void updateMapPosit()
    {
        this.current_blk = orig_map[posit_x][posit_y];
        this.current_blk_dat = map_data[posit_x][posit_y];
    }
    
    protected boolean canMove(int x, int y)
    {
        switch(orig_map[this.posit_x + x][this.posit_y + y])
        {
            case "#": LQOS.outError("Cannot walk on walls"); return false;
            case "~": LQOS.outError("Cannot walk on water"); return false;
            case "-": LQOS.outError("Cannot walk on lava"); return false;
            default: break;
        }
        return true;
    }
    
    protected String move(String symbol, int x, int y) throws IOException
    {
        if(canMove(x, y))
        {
            this.posit_x += x;
            this.posit_y += y;
        }
        updateMapPosit(); // Map position updates with every movement
        this.cur_map = LQCLI.updateMap(this.cur_map, this.orig_map, 
            this.posit_x, this.posit_y, this.last_posit_x, 
            this.last_posit_y, this.symbol); // updates the current map
        return LQCLI.stringMap(cur_map);
    }
}