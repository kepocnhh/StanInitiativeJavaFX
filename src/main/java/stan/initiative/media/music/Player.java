package stan.initiative.media.music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player
{
    static private Player instance;
    static public Player getInstance()
    {
        if(instance == null)
        {
            instance = new Player();
        }
        return instance;
    }

    public interface IMusicPlayerListener
    {
        void stop();
        void pause();
        void play();
    }

    private MediaPlayer mediaPlayer;
    private List<String> listAudio;
    private List<IMusicPlayerListener> listeners;
    private int audio;
    private String folder;
    private boolean random;
    private boolean repeat;
    private boolean paused;
    private boolean mute;
    private double volume;

    private Player()
    {
        this.listAudio = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.setDefault();
    }

    public Player addListener(IMusicPlayerListener l)
    {
        this.listeners.add(l);
        return this;
    }
    public Player setDefault()
    {
        this.folder = null;
        this.random = false;
        this.repeat = false;
        this.paused = false;
        this.audio = -1;
        this.setVolume(1);
        this.mute(false);
        return this;
    }
    public Player setVolume(double v)
    {
        this.volume = v;
        if(mediaPlayer != null)
        {
            mediaPlayer.setVolume(this.volume);
        }
        return this;
    }
    public Player mute(boolean m)
    {
        this.mute = m;
        return this;
    }
    public Player fromFolder(String path)
    {
        System.out.println("Player fromFolder - " + path);
        this.folder = path;
        this.listAudio.clear();
        this.setListFiles(new File(path), listAudio);
        return this;
    }
    public Player fromFile(String file)
    {
        System.out.println("Player fromFile - " + file);
        this.listAudio.clear();
        this.listAudio.add(file);
        return this;
    }
    public Player randomOn()
    {
        this.random = true;
        return this;
    }
    public Player randomOff()
    {
        this.random = false;
        return this;
    }
    public Player repeat()
    {
        this.repeat = true;
        return this;
    }

    public boolean play()
    {
        if(listAudio.size() == 0)
        {
            return false;
        }
        if(isStoped())
        {
            nextAudio();
            if(isStoped())
            {
                return false;
            }
            setMedia();
        }
        this.paused = false;
        mediaPlayer.play();
        for(IMusicPlayerListener l : listeners)
        {
            l.play();
        }
        return true;
    }
    public void next()
    {
        nextAudio();
        if(isStoped())
        {
            return;
        }
        setMedia();
        play();
    }
    public void setMedia()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(new Media(listAudio.get(audio)));
        mediaPlayer.setVolume(this.volume);
        mediaPlayer.setMute(this.mute);
        mediaPlayer.setOnEndOfMedia(new Runnable()
        {
            public void run()
            {
                Player.getInstance().next();
            }
        });
    }
    private void nextAudio()
    {
        if(this.random)
        {
            audio = new Random().nextInt(listAudio.size());
        }
        else
        {
            audio++;
            if(audio >= listAudio.size())
            {
                if(repeat)
                {
                    audio = 0;
                }
                else
                {
                    this.stop();
                }
            }
        }
    }
    public void pause()
    {
        System.out.println("Player pause");
        paused = true;
        mediaPlayer.pause();
        for(IMusicPlayerListener l : listeners)
        {
            l.pause();
        }
    }
    public void stop()
    {
        this.audio = -1;
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
        for(IMusicPlayerListener l : listeners)
        {
            l.stop();
        }
    }

    public boolean isStoped()
    {
        return audio < 0;
    }
    public boolean isPaused()
    {
        return paused;
    }

    private void setListFiles(File parent, List<String> listFiles)
    {
        File[] files = parent.listFiles();
        for(File file : files)
        {
            if(file.isFile())
            {
                if(!file.getName().toLowerCase().endsWith(".mp3"))
                {
                    continue;
                }
                String path = file.toURI().toASCIIString();
                listFiles.add(path);
                //System.out.println("Player setListFiles add - " + path);
            }
            else
            {
                setListFiles(file, listFiles);
            }
        }
    }
}