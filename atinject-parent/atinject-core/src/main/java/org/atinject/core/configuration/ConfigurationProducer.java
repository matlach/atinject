package org.atinject.core.configuration;

import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

public class ConfigurationProducer
{

    @Inject
    private ConfigurationExtension configurationExtension;

    @Produces
    @Configuration
    public BigDecimal bigDecimalConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getBigDecimal(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public BigInteger bigIntegerConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getBigInteger(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public boolean booleanConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getBoolean(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public byte byteConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getByte(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public double doubleConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getDouble(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public float floatConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getFloat(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public int integerConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getInt(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public List<Object> listConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getList(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public long longConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getLong(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public short shortConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getShort(getKey(injectionPoint));
    }

    @Produces
    @Configuration
    public String stringConfiguration(InjectionPoint injectionPoint)
    {
        return configurationExtension.getConfiguration().getString(getKey(injectionPoint));
    }

    private String getKey(InjectionPoint injectionPoint)
    {
        return getKey(injectionPoint.getBean().getBeanClass().getPackage(), injectionPoint.getBean().getBeanClass(),
                injectionPoint.getMember());
    }

    private String getKey(Package p, Class<?> c, Member m)
    {
        return new StringBuilder()
            .append(applyPackageNamingConvention(p))
            .append(".")
            .append(applyBeanClassNamingConvention(c))
            .append(".")
            .append(applyMemberNamingConvention(m))
            .toString();
    }

    public String applyPackageNamingConvention(Package p)
    {
        return p.getName().toLowerCase();
    }

    public String applyBeanClassNamingConvention(Class<?> c)
    {
        return applyNamingConvention(c.getSimpleName());
    }

    public String applyMemberNamingConvention(Member m)
    {
        return applyNamingConvention(m.getName());
    }

    private String applyNamingConvention(String s)
    {
        StringBuilder name = new StringBuilder();
        for (char ch : s.toCharArray())
        {
            if (Character.isUpperCase(ch))
            {
                if (name.length() > 0)
                {
                    name.append(".");
                }
                name.append(Character.toLowerCase(ch));
            }
            else
            {
                name.append(ch);
            }
        }
        return name.toString();
    }
}
