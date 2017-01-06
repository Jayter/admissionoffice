package com.jayton.admissionoffice.util.di;

import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class InjectionResolver {

    private static final String BEANS = "beans";
    private static final String BEAN = "bean";
    private static final String ID = "id";
    private static final String CLASS = "class";
    private static final String TYPE = "type";
    private static final String CONSTRUCTOR = "constructor";
    private static final String SINGLETONE = "singletone";
    private static final String GET_INSTANCE = "getInstance";
    private static final String FIELD = "field";
    private static final String NAME = "name";
    private static final String REF = "ref";

    private Document doc;
    private Map<String, Object> beansMap = new HashMap<>();

    public InjectionResolver() {
    }

    public void init() throws InjectionException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            URL path = getClass().getClassLoader().getResource("di/dependencies.xml");
            if(path == null) {
                throw new InjectionException("Dependencies configuration is not found.");
            }
            doc = builder.parse(path.getPath());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InjectionException("Initialization exception.", e);
        }
    }

    public void parse() throws InjectionException {
        NodeList rootElems = doc.getElementsByTagName(BEANS);
        Node root = rootElems.item(0);

        if(root == null) {
            throw new InjectionException("Parsing exception. Can not find <beans> tag");
        }

        NodeList beans = root.getChildNodes();
        for(int i = 0; i < beans.getLength(); i++) {
            Node bean = beans.item(i);
            if(BEAN.equals(bean.getNodeName())) {
                instantiateBean(bean);
            }
        }
    }

    private void instantiateBean(Node bean) throws InjectionException {
        NamedNodeMap attributes = bean.getAttributes();

        String id = attributes.getNamedItem(ID).getNodeValue();
        String clazz = attributes.getNamedItem(CLASS).getNodeValue();
        Node type = attributes.getNamedItem(TYPE);

        String instantiationType;
        if (type == null) {
            instantiationType = CONSTRUCTOR;
        } else {
            instantiationType = type.getNodeValue();
        }

        Object instance = createInstance(clazz, instantiationType);

        NodeList fields = bean.getChildNodes();
        for(int i = 0; i < fields.getLength(); i++) {
            Node current = fields.item(i);
            if(FIELD.equals(current.getNodeName())) {
                instantiateField(instance, current);
            }
        }

        beansMap.put(id, instance);
    }

    private Object createInstance(String clazz, String type) throws InjectionException {
        Object bean = null;
        try {
            Class<?> cls = Class.forName(clazz);

            if(CONSTRUCTOR.equals(type)) {
                bean = cls.newInstance();
            } else if(SINGLETONE.equals(type)) {
                Method getInstance = cls.getDeclaredMethod(GET_INSTANCE);
                bean = getInstance.invoke(cls);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                NoSuchMethodException | InvocationTargetException e) {
            throw new InjectionException("Could not instantiate bean. Please check descriptor and bean`s signature.", e);
        }
        return bean;
    }

    private void instantiateField(Object bean, Node node) throws InjectionException {
        NamedNodeMap attributes = node.getAttributes();
        Node nameAttribute = attributes.getNamedItem(NAME);
        Node referenceAttribute = attributes.getNamedItem(REF);

        if(nameAttribute == null || referenceAttribute == null) {
            throw new InjectionException(String.format("Instantiation error. Fields %s, %s must not be empty.", NAME, REF));
        }
        String name = nameAttribute.getNodeValue();
        String ref = referenceAttribute.getNodeValue();

        Class<?> cls = bean.getClass();
        try {
            Field field = cls.getDeclaredField(name);
            Method setter = cls.getDeclaredMethod(getSetterName(ref), field.getType());
            Object fieldValue = beansMap.get(name);
            setter.invoke(bean, fieldValue);
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new InjectionException("Could not instantiate field. Please check descriptor and bean`s signature.", e);
        }
    }

    private String getSetterName(String field) {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1, field.length());
    }
}