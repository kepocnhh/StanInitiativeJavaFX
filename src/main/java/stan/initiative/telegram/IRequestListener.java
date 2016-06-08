package stan.initiative.telegram;

import java.io.IOException;

public interface IRequestListener
{
    void answer(String answer);
    void error(IOException error);
}