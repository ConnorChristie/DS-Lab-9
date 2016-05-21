package lab9.christieck;

import java.io.*;
import java.util.*;

public class DNS
{
    private File storageFile;

    private Stack<DNSEvent> dnsEvents;
    private Map<DomainName, IPAddress> dnsMap;

    private boolean isStarted = false;

    public DNS(String fileName)
    {
        storageFile = new File(fileName);

        dnsEvents = new Stack<>();
        dnsMap = new HashMap<>();
    }

    /**
     * Starts the DNS server and reads the list of domains and IP addresses from the file
     *
     * @return Whether the DNS server was started successfully
     */
    public boolean start()
    {
        if (isStarted)
        {
            return true;
        }

        try (Scanner in = new Scanner(storageFile))
        {
            while (in.hasNext() && in.hasNext())
            {
                String ipAddress = in.next();
                String domainName = in.next();

                try
                {
                    IPAddress address = new IPAddress(ipAddress);
                    DomainName domain = new DomainName(domainName);

                    dnsMap.put(domain, address);
                } catch (IllegalArgumentException ex)
                {
                    System.err.println("Skipping (" + domainName + ", " + ipAddress + "): " + ex.getMessage());
                }
            }

            return isStarted = true;
        } catch (FileNotFoundException e) { }

        return false;
    }

    /**
     * Stops the DNS server
     *
     * @return Whether the DNS server was stopped successfully
     */
    public boolean stop()
    {
        if (!isStarted)
        {
            return true;
        }

        try (PrintWriter printer = new PrintWriter(storageFile))
        {
            for (Map.Entry<DomainName, IPAddress> entry : dnsMap.entrySet())
            {
                printer.println(entry.getValue() + "\t\t" + entry.getKey());
            }

            isStarted = false;
            return true;
        } catch (IOException e) { }

        return false;
    }

    /**
     * Gets the IP address for the specified domain name
     *
     * @param domain The domain to lookup the IP address for
     * @return The IP address for the domain name
     */
    public IPAddress lookup(DomainName domain)
    {
        return dnsMap.get(domain);
    }

    /**
     * Updates a domain and IP address
     *
     * @param command The command to perform
     * @return The IP address of the record
     */
    public IPAddress update(String command)
    {
        String[] vars = command.split("\\s+");

        if (vars.length == 3)
        {
            DNSEventType eventType = DNSEventType.fromCode(vars[0]);

            if (eventType == null)
            {
                throw new IllegalArgumentException("Invalid action type specified. Only ADD or DEL");
            }

            IPAddress address = new IPAddress(vars[1]);
            DomainName domain = new DomainName(vars[2]);

            return update(eventType.getEvent(domain, address));
        }

        return null;
    }

    /**
     * Updates a domain and IP address
     *
     * @param event The event to perform
     * @return The IP address of the record
     */
    public IPAddress update(DNSEvent event)
    {
        DomainName domain = event.getDomain();
        IPAddress address = event.getAddress();

        if (event.getType() == DNSEventType.ADD)
        {
            dnsEvents.push(event);

            return dnsMap.put(domain, address);
        }

        if (event.getType() == DNSEventType.DELETE)
        {
            IPAddress foundAddress = dnsMap.get(domain);

            if (foundAddress != null)
            {
                if (address.equals(foundAddress))
                {
                    dnsEvents.push(event);

                    return dnsMap.remove(domain);
                }

                throw new InputMismatchException("The specified domain name and IP address do not match any entries in the DNS");
            }
        }

        return null;
    }

    /**
     * Adds a domain and IP address to the DNS lookup table
     *
     * @param domain The domain to add
     * @param ipAddress The IP address to add
     */
    public void add(DomainName domain, IPAddress ipAddress)
    {
        update(DNSEventType.ADD.getEvent(domain, ipAddress));
    }

    /**
     * Deletes a domain name from the DNS records
     *
     * @param domain The domain to delete the record for
     * @param address The IP address to delete the record for
     */
    public boolean delete(DomainName domain, IPAddress address)
    {
        return update(DNSEventType.DELETE.getEvent(domain, address)) != null;
    }

    public void undo()
    {
        DNSEvent inverse = dnsEvents.pop().getInverse();

        update(inverse);
    }

    public void redo()
    {

    }

    /**
     * A DNS update event
     */
    private static class DNSEvent
    {
        private DNSEventType type;

        private DomainName domain;
        private IPAddress address;

        public DNSEvent(DNSEventType type, DomainName domain, IPAddress address)
        {
            this.type = type;

            this.domain = domain;
            this.address = address;
        }

        /**
         * Gets the inverse event
         *
         * @return The inverse event
         */
        public DNSEvent getInverse()
        {
            DNSEventType newType = null;

            switch (type)
            {
                case ADD:
                    newType = DNSEventType.DELETE;

                    break;
                case DELETE:
                    newType = DNSEventType.ADD;

                    break;
            }

            return new DNSEvent(newType, domain, address);
        }

        public DomainName getDomain()
        {
            return domain;
        }

        public IPAddress getAddress()
        {
            return address;
        }

        public DNSEventType getType()
        {
            return type;
        }
    }

    /**
     * The event type for a DNS update
     */
    private enum DNSEventType
    {
        ADD("ADD"), DELETE("DEL");

        private String code;

        DNSEventType(String code)
        {
            this.code = code;
        }

        public String getCode()
        {
            return code;
        }

        /**
         * Gets the DNS event for this event type
         *
         * @param domain The domain name
         * @param address The IP address
         * @return The DNS event
         */
        public DNSEvent getEvent(DomainName domain, IPAddress address)
        {
            return new DNSEvent(this, domain, address);
        }

        /**
         * Gets the DNS event from the specified code
         *
         * @param code The code to get the event for
         * @return The event type, null if not found
         */
        public static DNSEventType fromCode(String code)
        {
            for (DNSEventType type : values())
            {
                if (type.getCode().equalsIgnoreCase(code))
                {
                    return type;
                }
            }

            return null;
        }
    }
}
