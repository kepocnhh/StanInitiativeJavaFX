package stan.initiative.telegram;

public abstract class Sender
{
	private String url;

	public Sender(String u)
	{
		url = u;
	}
	
	public String sendMessage(int chat_id, String message)
	{
		return Connection.sendMessage(url, chat_id, message);
	}
	public String sendSticker(int chat_id, String stickerId)
	{
		return Connection.sendSticker(url, chat_id, stickerId);
	}
	public String sendPhoto(int chat_id, byte[] photo)
	{
		return Connection.sendPhoto(url, chat_id, photo);
	}
	public String getUpdates()
	{
		return Connection.getUpdates(url);
	}
}