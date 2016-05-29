package stan.initiative.block.note;

public class Unit
{
    public String id;
    public String name;
    public Dates dates;

    public Unit(String i, String n, long c)
    {
        id = i;
        name = n;
        dates = new Dates(c);
    }
}