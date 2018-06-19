/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.facefreakedstudios.app.lq;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gavin17
 */

// This class initializes various game equipment
class Item_Map
{
    wooden_helm helm0 = new wooden_helm();
    bulk_ring ring0 = new bulk_ring();
    mortifer weap0 = new mortifer();
    dull_thrw_knife throw0 = new dull_thrw_knife();
    pot_of_healing potion0 = new pot_of_healing();
    
    final Map<String, Equipment> imap = new HashMap<>();
    Item_Map()
    {
        imap.put(helm0.getName(), helm0);
        imap.put(ring0.getName(), ring0);
        imap.put(weap0.getName(), weap0);
    }
}
