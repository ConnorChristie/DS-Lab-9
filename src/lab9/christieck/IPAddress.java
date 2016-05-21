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

    /**
     * Validates the IP address
     *
     * @param address The IP address to validate
     */
    private static void validateIpAddress(String address)
    {
        if (!IPAddressUtil.isIPv4LiteralAddress(address))
        {
            throw new IllegalArgumentException("The IP address is not valid");
        }
    }

    /**
     * Checks whether the specified IP address is equal
     *
     * @param address The IP address to check
     * @return Whether they are equal
     */
    @Override
    public boolean equals(Object address)
    {
        if (address instanceof IPAddress)
        {
            return toString().equalsIgnoreCase(address.toString());
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return address.hashCode();
    }

    @Override
    public String toString()
    {
        return address;
    }
}
