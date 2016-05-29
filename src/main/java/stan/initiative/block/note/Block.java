package stan.initiative.block.note;

import java.util.List;

public class Block
	extends Unit
{
    public List<Block> blocks;

    public Block(String i, String n, Dates d)
    {
    	super(i, n, d.whenCreate());
    	dates.update = d.update;
    	dates.sync = d.sync;
    }

    public void setBlocks(List<Block> b)
    {
    	blocks = b;
    }
}