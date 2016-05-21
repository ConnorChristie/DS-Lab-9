package lab9.christieck;

import java.io.File;

public class DNS
{
    private File storageFile;

    public DNS(String fileName)
    {
        storageFile = new File(fileName);
    }

    public boolean start()
    {
        return false;
    }

    public boolean stop()
    {
        return false;
    }

    public IPAddress lookup(DomainName domain)
    {
        return null;
    }

    public IPAddress update(String command)
    {
        return null;
    }

    public void add(DomainName domain, IPAddress ipAddress)
    {
        // Do work
        // Call update with the command
    }

    private void delete(DomainName domain)
    {

    }
}
