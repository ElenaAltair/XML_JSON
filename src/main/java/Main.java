import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        //Для получения списка сотрудников из XML документа используйте метод parseXML()
        List<Employee> list = parseXML("data.xml");

        //Полученный список преобразуйте в строчку в формате JSON
        String json = listToJson(list);

        //запишите полученный JSON в файл с помощью метода writeString()
        String fileNameJson = "data.json";
        writeString(json, fileNameJson);
    }

    //
    public static List<Employee> parseXML(String filexmlName) throws ParserConfigurationException, IOException, SAXException {
        // получить экземпляр класса Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filexmlName));

        //получите из объекта Document корневой узел Node с помощью метода getDocumentElement()
        Node root = doc.getDocumentElement();

        /*
        Из корневого узла извлеките список узлов NodeList с помощью метода getChildNodes().
        Пройдитесь по списку узлов и получите из каждого из них Element.
        У элементов получите значения, с помощью которых создайте экземпляр класса Employee.
        Так как элементов может быть несколько, организуйте всю работу в цикле.
        Метод parseXML() должен возвращать список сотрудников
         */
        NodeList nodeList = root.getChildNodes();
        List<Employee> list = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element employee = (Element) node;
                int id = Integer.parseInt(employee.getElementsByTagName("id").item(0).getTextContent());
                String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
                String country = employee.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent());
                Employee empl = new Employee(id, firstName, lastName, country, age);

                list.add(empl);


            }

        }

        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);

        return json;
    }

    public static void writeString(String json, String fileNameJson) {
        //поможет FileWriter и его метод write()
        try (FileWriter file = new FileWriter(fileNameJson)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
