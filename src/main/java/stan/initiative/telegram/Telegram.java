package stan.initiative.telegram;

public class Telegram
{
    static private Telegram instance;
    static public Telegram getInstance()
    {
        if(instance == null)
        {
            instance = new Telegram();
        }
        return instance;
    }
	
	private CudgelBot bot;
	
    private Telegram()
    {
	}
	
    public void setBot(int i, String t, int c)
    {
		this.bot = new CudgelBot(i, t);
		this.bot.setChatIdMe(c);
	}
    public CudgelBot getBot()
    {
        return this.bot;
    }
}