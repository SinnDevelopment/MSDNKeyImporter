package com.sinndevelopment.msdnkeyimporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductKey
{

    private String product;
    private String key;
    private Date claimedDate;
    private Type keyType;

    public ProductKey()
    {

    }

    public ProductKey(String product, String key, Date claimedDate, Type keyType)
    {
        this.product = product;
        this.key = key;
        this.claimedDate = claimedDate;
        this.keyType = keyType;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public Date getClaimedDate()
    {
        return claimedDate;
    }

    public void setClaimedDate(Date claimedDate)
    {
        this.claimedDate = claimedDate;
    }

    public void setClaimedDate(String claimedDate) throws ParseException
    {
        if (claimedDate.isEmpty())
            return;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/DD/YYYY");
        this.claimedDate = dateFormat.parse(claimedDate);
    }

    public Type getKeyType()
    {
        return keyType;
    }

    public void setKeyType(Type keyType)
    {
        this.keyType = keyType;
    }

    @Override
    public String toString()
    {
        return product + ":\n\t" + key + "\n\t" + keyType + "\n\t" + claimedDate;
    }

    enum Type
    {
        Retail,
        MultipleActivation,
        StaticActivationKey,
        CustomKey
    }
}
