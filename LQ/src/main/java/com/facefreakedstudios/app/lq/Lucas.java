/*
 * Copyright 2018 gavin17.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facefreakedstudios.app.lq;

import com.facefreakedstudios.app.lq_engine.LQCLI;
import com.facefreakedstudios.app.lq_engine.LQOS;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/*
 *
 * @author gavin17
 */
public class Lucas extends Map_Object
{
    final public static String SYMBOL = "@";
    final public static String NAME = "Lucas";
    private long hp, ap, xp;
    private long cash = 5; // Starting value
    private long xp_point = 0; // used for upgrading skills
    private long lvl_cap = 10; // the amount of xp needed for an upgrade
    private long hp_cap; // the starting HP limit
    private long atk_pow = 0; // the dmg bonus, strength
    private long inv_weight = 20; // inventory weight, strength
    private long[] dmg_apdrain;
    private final Weapon weap = new mortifer(); // temp field
    private Enemy targ;
    public final Map<String, Weighted_Object> inventory = new HashMap<>();
    public final Map<String, Weighted_Object> equipped = new HashMap<>();
    public final Map<String, Long> skills = new HashMap<>(); // Skill tree
    
    Lucas(long hp, long ap) throws IOException
    { // Skills also have in game special effects (like lifting boulders)
        skills.put("Strength", 0L); // Attack power, Inventory weight
        skills.put("Persuasion", 0L); // Barter, Dialogue 
        skills.put("Ingenuity", 0L); // Repair, Crafting, BrewingNAME
        skills.put("Luck", 0L); // XP drops, Item pickups, Gambling
        skills.put("Vitality", 0L); // HP, HP regen
        skills.put("Wisdom", 0L); // Magic use, Magic strength, Potion effect
        skills.put("Endurance", 0L); // AP, Walk dist, Run dist
        skills.put("Immunity", 0L); // Poi, Fre, Drk, Frst, Phy and Bld damage
        skills.put("Stamina", 0L); // AP, AP regen, movement speed, attack first
        equipped.put("Ring", null);
        equipped.put("Helm", null);
        equipped.put("Legs", null);
        equipped.put("Gaunts", null);
        equipped.put("Chest", null);
        equipped.put("Weapon", null);
        equipped.put("Sheild", null);
        equipped.put("QI0", null); // QI stands for Quick Item
        equipped.put("QI1", null); // QIs are mapped to the keyboard
        equipped.put("QI2", null);
        equipped.put("QI3", null);
        equipped.put("QI4", null);
        this.hp = hp; this.hp_cap = hp;
        this.ap = ap;
    }
    
    public boolean isDead()
    {
        return hp <= 0;
    }
    private boolean noAP()
    {
        if(ap <= 0)
        {
            ap = 0; // So you don't have negative AP
            return true;
        }
        return false;
    }
    private boolean canEnter()
    {
        switch(current_blk)
        {
            case "o": return true;
            case "P": return true;
            case "L": return true;
            case "^": return true;
            case "$": return true;
            default: return false;
        }
    }
    private boolean canRead()
    {
        return current_blk.equals("=");
    }
    
    void addHP(long hp)
    {
        this.hp += hp;
        LQOS.outStat(NAME, this.hp, "HP");
    }
    void addXP(long xp)
    {
        LQOS.outStat(NAME, xp, "XP");
        this.xp += xp;
        if(this.xp > lvl_cap)
        {
            ++xp_point; // gains one xp point
            this.xp -= lvl_cap; // removes xp from lvl_cap
            lvl_cap += 10; // revist
            LQOS.outStat(NAME, xp_point, "XP point");
        }
    }
    void addAP(long ap)
    {
        LQOS.outStat(NAME, ap, "AP");
        this.ap += ap;
    }
    void addCash(long cash)
    {
        this.cash += cash;
    }
    
    public void setTarg(Enemy ene)
    {
        this.targ = ene;
    }
    void addInventory(Weighted_Object obj)
    {
        if(findInventoryWeight() < inv_weight)
        {
            inventory.put(obj.getName(),obj);
        }
        else
        {
            LQOS.outError("Reached max inventory weight");
        }
    }
  
    void equip(Weighted_Object equipment)
    {
        equipped.put(equipment.getType(), equipment);
        LQOS.outAny("%s equipped".format(equipment.getName()));
    }

    public void upgradeSkill(String skill, long xp_points)
    {
        if(xp_points < xp_point)
        {
            LQOS.outError("Invalid number of XP points");
        }
        long upgraded = skills.get(skill);
        skills.put(skill, upgraded);
        LQOS.outStat("Lucas", skills.get(skill), skill);
        updateSkills();
    }
    
    private void updateSkills()
    {
        
    }
  
    public void enter() throws IOException
    {
        if(canEnter())
        {
            String[] map_location = {current_blk_dat, null};
            if(current_blk_dat.contains("@"))
            {
                 map_location = current_blk_dat.split("@");
            }
            setMap(this, map_location[0], map_location[1]);
        }
        else
        {
            LQOS.outError("Not an enterable block");
        }
    }
    
    public void read()
    {
        if(canRead())
        {
            LQOS.outSign(current_blk_dat);
        }
        else
        {
            LQOS.outError("Not a readable block");
        }
    }
    
    public long getHP()
    {
        return hp;
    }
    public long getAP()
    {
        return ap;
    }
    public long getXP()
    {
        return xp;
    }
    public long getCash()
    {
        return cash;
    }
    
    
    @Override
    String getCurrentBlk()
    {
        return this.current_blk;
    }
    @Override
    String getCurrentBlkDat()
    {
        return this.current_blk_dat;
    }
    @Override
    public int[] getPosit()
    {
        int[] posit = {this.positx, this.posity};
        return posit;
    }
    @Override
    int[] getLastPosit()
    {
        int[] last_posit = {this.last_positx, this.last_posity};
        return last_posit;
    }
    
    private long findInventoryWeight()
    {
        long total_weight = 0;
        for(Map.Entry<String, Weighted_Object> entry : inventory.entrySet()) 
        {
            Weighted_Object obj = entry.getValue();
            total_weight += obj.getWeight();
        }
        return total_weight;
    }
    
    Enemy getTarg()
    {
        return targ;
    }
    
    public void regenHP()
    {
        if(this.hp >= this.hp_cap)
        {
            this.hp = this.hp_cap; // so HP doesn't exceed cap when regenerating
        }
        else
        {
            addHP(skills.get("Vitality") + 1);
        }
    }
    
    public void attack(int atk)
    {
        ap -= 8;
        if(noAP()) // return no damage if no AP
        {
            targ.addHP(targ.getHP() - 0);
        }
        switch(atk)
        {
            case 0:
                dmg_apdrain = weap.move0(this);
                addAP(-dmg_apdrain[1]); // 1 is the ap_drain
                targ.addHP(targ.getHP() - 
                    (dmg_apdrain[0] + atk_pow)); // 0 is the weap's damage
            case 1:
                dmg_apdrain = weap.move1(this); 
                addAP(-dmg_apdrain[1]);
                targ.addHP(targ.getHP() - (dmg_apdrain[0] + atk_pow));
            case 2:
                dmg_apdrain = weap.move2(this); 
                addAP(dmg_apdrain[1]);
                targ.addHP(targ.getHP() - (dmg_apdrain[0] + atk_pow));
            case 3:
            default: targ.addHP(0);
        }
    }
    // Overloads move from Movement.java
    public String move(String symbol, int x, int y) throws IOException
    {
        if(canMove(this, x, -y))
        {
            this.last_positx = positx;
            this.last_posity = posity;
            this.positx += x;
            this.posity += -y;
        }
        updateMapPosit(); // Map position updates with every movement
        this.cur_map = LQCLI.updateMap(this.cur_map, this.orig_map, 
            this.positx, this.posity, this.last_positx, 
            this.last_posity, symbol); // updates the current map
        return LQCLI.stringMap(cur_map);
    }
}
