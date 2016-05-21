package lab9.christieck;

import sun.net.util.IPAddressUtil;

public class IPAddress
{
    private String address;

    public IPAddress(String address)
    {
        validateIpAddress(address);

        this.address = address;
    }

    private static void validateIpAddress(String address)
    {
        if (!IPAddressUtil.isIPv4LiteralAddress(address))
        {
            throw new IllegalArgumentException("The IP address is not valid");
        }
    }

    public String toString()
    {
        return address;
    }
}
