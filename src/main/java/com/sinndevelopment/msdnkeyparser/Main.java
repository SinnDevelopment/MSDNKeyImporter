package com.sinndevelopment.msdnkeyparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        SQLHandler sqlHandler = new SQLHandler("database.sinn.lan", 3306, "product_keys", "product_keys", "password");
        if(sqlHandler.getConnection() == null)
            return;
        List<ProductKey> keys =  parseFile("KeysExport (1).xml");
        for(ProductKey key : keys)
            sqlHandler.insertProductKey(key);

    }

    private static List<ProductKey> parseFile(String file) throws Exception
    {
        List<ProductKey> output = new ArrayList<>();
        File inputFile = new File(file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Product_Key");

        for (int i = 0; i < nList.getLength(); i++)
        {
            Node nNode = nList.item(i);
            Element product = (Element) nNode;
            String product_name = product.getAttribute("Name");
            NodeList keys = product.getElementsByTagName("Key");
            for (int j = 0; j < keys.getLength(); j++)
            {
                ProductKey productKey = new ProductKey();
                Node keyNode = keys.item(j);
                Element key = (Element) keyNode;
                String key_type = key.getAttribute("Type");
                String key_claimedDate = key.getAttribute("ClaimedDate");
                String key_key = key.getTextContent();
                productKey.setKey(key_key);
                productKey.setClaimedDate(key_claimedDate);
                productKey.setKeyType(ProductKey.Type.valueOf(key_type.replaceAll(" ", "")));
                productKey.setProduct(product_name);
                output.add(productKey);
                //System.out.println(productKey.toString());
            }
        }
        return output;
    }
}
