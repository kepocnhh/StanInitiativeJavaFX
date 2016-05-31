package stan.initiative.telegram;

public class Bot
{
	public int id;
	public String token;
	public int chatIdMe;
	
	public Bot(int i, String t)
	{
		id = i;
		token = t;
	}
	
	public void setChatIdMe(int c)
	{
		chatIdMe = c;
	}
}