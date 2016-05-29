package stan.initiative.block.note;

public class Dates
{
	private long create;
	public long update;
	public long sync;

    public Dates(long c)
    {
    	create = c;
    	update = c;
    	sync = -1;
    }

    public long whenCreate()
    {
    	return create;
    }
}