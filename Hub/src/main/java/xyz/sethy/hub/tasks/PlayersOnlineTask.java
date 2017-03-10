package xyz.sethy.hub.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.sethy.hub.Hub;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by sethm on 24/12/2016.
 */
public class PlayersOnlineTask extends BukkitRunnable
{
    @Override
    public void run()
    {
        try
        {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 25565), 1000);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.write(0xFE);

            StringBuilder str = new StringBuilder();

            int b;
            while ((b = in.read()) != -1)
            {
                if (b > 16)
                {
                    if (b != 0 && b != 255 && b != 23 && b != 24)
                    {
                        str.append((char) b);
                    }
                }
            }

            String[] data = str.toString().split("§");
            int online = Integer.parseInt(data[1]);
            Hub.getInstance().getHcfOnline().set(online);
            socket.close();
        }
        catch (IOException e)
        {
            //
        }
    }
}
