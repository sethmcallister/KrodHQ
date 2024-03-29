/*
 *  PacketWrapper - Contains wrappers for each packet in Minecraft.
 *  Copyright (C) 2012 Kristian S. Stangeland
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the 
 *  GNU Lesser General Public License as published by the Free Software Foundation; either version 2 of 
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program; 
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 *  02111-1307 USA
 */

package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.ItemStack;

public class WrapperPlayClientSetCreativeSlot extends AbstractPacket
{
    public static final PacketType TYPE = PacketType.Play.Client.SET_CREATIVE_SLOT;

    public WrapperPlayClientSetCreativeSlot()
    {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientSetCreativeSlot(PacketContainer packet)
    {
        super(packet, TYPE);
    }

    /**
     * Retrieve the inventory slot index.
     *
     * @return The current Slot
     */
    public short getSlot()
    {
        return handle.getIntegers().read(0).shortValue();
    }

    /**
     * Set the inventory slot index.
     *
     * @param value - new value.
     */
    public void setSlot(short value)
    {
        handle.getIntegers().write(0, (int) value);
    }

    /**
     * Retrieve the clicked item stack.
     *
     * @return The current Clicked item
     */
    public ItemStack getClickedItem()
    {
        return handle.getItemModifier().read(0);
    }

    /**
     * Set the clicked item stack.
     *
     * @param value - new value.
     */
    public void setClickedItem(ItemStack value)
    {
        handle.getItemModifier().write(0, value);
    }
}