package xyz.sethy.sg.map;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by sethm on 22/12/2016.
 */
public class SchematicHolder
{
    private static Field dataf;
    private String name;
    private CuboidClipboard clipBoard;
    private int yOffset;
    private int percentage;

    static
    {
        dataf = null;
    }

    public SchematicHolder()
    {
        this.percentage = 2500;
    }

    public String getName()
    {
        return this.name;
    }

    public void loadSchmeatic(final File file) throws IOException
    {
        Preconditions.checkState(this.clipBoard == null);
        this.name = file.getName().replaceAll(".schematic", "");
        final SchematicFormat format = SchematicFormat.MCEDIT;
        try
        {
            this.clipBoard = format.load(file);
        }
        catch (DataException e)
        {
            throw new IOException((Throwable) e);
        }
    }

    public void rotateRandomly()
    {
        this.clipBoard.rotate2D(90 * (RandomUtils.nextInt(4) + 1));
    }

    public int getWidth()
    {
        return this.clipBoard.getWidth();
    }

    public int getLength()
    {
        return this.clipBoard.getLength();
    }

    public int getHeight()
    {
        return this.clipBoard.getHeight();
    }

    public int getBlockIDAt(final int x, final int y, final int z)
    {
        final BaseBlock[][][] block = this.getInternalData(this.clipBoard);
        if (block[x][y][z] == null)
        {
            throw new IllegalStateException("null at " + x + "," + y + "," + z);
        }
        return block[x][y][z].getId();
    }

    public int getDataAt(final int x, final int y, final int z)
    {
        final BaseBlock[][][] block = this.getInternalData(this.clipBoard);
        if (block[x][y][z] == null)
        {
            throw new IllegalStateException("null at " + x + "," + y + "," + z);
        }
        return block[x][y][z].getData();
    }

    private BaseBlock[][][] getInternalData(final CuboidClipboard clipboard)
    {
        try
        {
            if (dataf == null)
            {
                dataf = CuboidClipboard.class.getDeclaredField("data");
                dataf.setAccessible(true);
            }
            return (BaseBlock[][][]) dataf.get(this.clipBoard);
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    private EditSession newEditSession(final World world)
    {
        return new EditSession(new BukkitWorld(world), 999999);
    }

    public int getYOffset()
    {
        return this.yOffset;
    }

    public void setYOffset(final int yOffset)
    {
        this.yOffset = yOffset;
    }

    public int getPercentage()
    {
        return this.percentage;
    }

    public void setPercentage(final int percentage)
    {
        this.percentage = percentage;
    }
}
