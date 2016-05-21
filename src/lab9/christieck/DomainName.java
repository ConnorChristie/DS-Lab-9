package lab9.christieck;

public class DomainName
{
    private String domain;

    private static final String alphaNumericRegex = "^[a-zA-Z0-9\\.\\-]*$";

    public DomainName(String domain)
    {
        validateDomainName(domain);

        this.domain = domain.toLowerCase();
    }

    /**
     * Validates the domain name
     *
     * @param domain The domain name to validate
     */
    private static void validateDomainName(String domain)
    {
        if (domain.length() > 253)
        {
            throw new IllegalArgumentException("Domain name length must be less than 253 characters");
        }

        if (domain.startsWith(".") || domain.startsWith("-") || domain.endsWith(".") || domain.endsWith("-"))
        {
            throw new IllegalArgumentException("The domain name must not start or end with a dash or period");
        }

        if (domain.contains(".."))
        {
            throw new IllegalArgumentException("The domain name must not contain two periods in series");
        }

        if (!domain.matches(alphaNumericRegex))
        {
            throw new IllegalArgumentException("The domain name contains illegal characters");
        }
    }

    /**
     * Checks whether the specified domain name is equal
     *
     * @param domain The domain to check
     * @return Whether they are equal
     */
    @Override
    public boolean equals(Object domain)
    {
        if (domain instanceof DomainName)
        {
            return toString().equalsIgnoreCase(domain.toString());
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return domain.hashCode();
    }

    @Override
    public String toString()
    {
        return domain;
    }
}
