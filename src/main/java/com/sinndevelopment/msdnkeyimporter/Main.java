package com.sinndevelopment.msdnkeyimporter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        List<ProductKey> keys;
        ConfigReader config = new ConfigReader();
        SQLHandler sqlHandler = new SQLHandler(config.getHost(), config.getPort(), config.getDatabase(), config.getUsername(), config.getPassword());
        if (sqlHandler.getConnection() == null)
            throw new Exception("Database could not be connected to. Please check connection details.");
        if (args.length >= 1)
            keys = parseFile(new File(args[0]));
        else
            keys = parseFile("KeysExport.xml");
        for (ProductKey key : keys)
            sqlHandler.insertProductKey(key);

    }

    private static List<ProductKey> parseFile(String file) throws Exception
    {
        File inputFile = new File(file);
        return parseFile(inputFile);
    }

    private static List<ProductKey> parseFile(File file) throws ParseException, ParserConfigurationException, IOException, SAXException
    {
        List<ProductKey> output = new ArrayList<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
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
