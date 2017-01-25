package com.jayton.admissionoffice.util.di;

import com.jayton.admissionoffice.util.di.exception.InjectionException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Initializes classes from an xml descriptor.
 * <p>
 * This class is responsible for initialization of components,
 * described in {@link #initPath}. The component can be an
 * instance of any class. It must neither have a public default
 * constructor or be a singletone and use a <code>getInstance()</code>
 * method for instantiation.
 * <p>
 * The bean can have fields that needs to be initialized by other beans.
 * In this case a tag that describes a certain field must contain name
 * of the field and reference to the bean that needs to be injected.
 */
public class XmlBeanContext implements BeanContext {

    private static final String BEANS = "beans";
    private static final String BEAN = "bean";
    private static final String ID = "id";
    private static final String CLASS = "class";
    private static final String TYPE = "instantiationType";
    private static final String CONSTRUCTOR = "constructor";
    private static final String SINGLETONE = "singletone";
    private static final String GET_INSTANCE = "getInstance";
    private static final String FIELD = "field";
    private static final String NAME = "name";
    private static final String REFERENCE = "reference";
    private static final String VALUE = "value";

    /**
     * Path to the context descriptor.
     */
    private String initPath;

    /**
     * Represents an xml file that stores description of components of this application.
     */
    private Document doc;

    /**
     * Stores initialized beans. A key to bean is it`s name within the scope.
     * It is declared as value of the "id" attribute of the "bean" tag.
     */
    private Map<String, Object> beansMap = new HashMap<>();

    /**
     * Initializes an {@link #initPath} - path to the context descriptor.
     *
     * @param path - context descriptor location
     */
    public XmlBeanContext(String path) {
        this.initPath = path;
    }

    /**
     * Initializes a {@link #doc} - document that is used for information extraction.
     * <p>
     * The document is searched at the {@link #initPath}.
     *
     * @throws InjectionException if descriptor file was not found or any
     *                            other exception was thrown during the file parsing
     */
    public void init() throws InjectionException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            URL path = getClass().getClassLoader().getResource(initPath);
            if(path == null) {
                throw new InjectionException("Dependencies configuration is not found.");
            }
            doc = builder.parse(path.getPath());
            parse();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InjectionException("Initialization exception.", e);
        }
    }

    /**
     * Parses a {@link #doc} file and creates objects of declared classes.
     * <p>
     * The bean is described using <code>bean</code> tag. Its attributes:
     * <ul>
     *     <i>compulsory:</i>
     *     <li>
     *         <code>id</code> - bean`s name within context
     *     </li>
     *     <li>
     *         <code>class</code> - class of the bean
     *     </li>
     *     <i>optional:</i>
     *     <li>
     *         <code>instantiationType</code> - how to instantiate a bean.
     *          Two values are supported:
     *          <ul>
     *              <li>
     *                  <code>constructor</code> - bean is instantiated by
     *                  the default constructor
     *              </li>
     *              <li>
     *                  <code>singletone</code> - bean is instantiated by
     *                  a <code>getInstance</code> method
     *              </li>
     *          </ul>
     *          If the type is not specified, the <code>constructor</code> type is used
     *     </li>
     * </ul>
     * <p>
     * The bean can contain fields that are instantiated by other beans or values.
     * It is implemented via nested tag called <code>field</code>. Its attributes:
     * <ul>
     *     <li>
     *         <code>name</code> - name of the field. This value is used by
     *         reflection, so it must be a real name of the field that is
     *         declared in the class of the bean
     *     </li>
     *     <li>
     *         <code>reference</code> - name of the bean that must be injected.
     *     It is specified by <code>id</code> attribute
     *     </li>
     *     <li>
     *         <code>value</code> - a string representation of the field. In this case
     *         the class of the field must have a constructor that takes a string as
     *         an argument. It is used for instantiation of the field.
     *     </li>
     * </ul>
     *
     * @throws InjectionException if the root tag <code>beans</code> was not found
     *                            or any exception was thrown during parsing
     */
    private void parse() throws InjectionException {
        NodeList rootElems = doc.getElementsByTagName(BEANS);
        Node root = rootElems.item(0);

        if(root == null) {
            throw new InjectionException("Parsing exception. Can not find <beans> tag");
        }

        Queue<FieldInfo> beanFields = new LinkedList<>();

        NodeList beans = root.getChildNodes();
        for(int i = 0; i < beans.getLength(); i++) {
            Node bean = beans.item(i);
            if(BEAN.equals(bean.getNodeName())) {
                String name = instantiateBean(bean);

                //adds fields into queue
                NodeList fields = bean.getChildNodes();
                for(int j = 0; j < fields.getLength(); j++) {
                    Node current = fields.item(j);
                    if(FIELD.equals(current.getNodeName())) {
                        beanFields.add(new FieldInfo(name, current));
                    }
                }
            }
        }

        while (!beanFields.isEmpty()) {
            instantiateField(beanFields.poll());
        }
    }

    /**
     * Instantiates a bean from {@link org.w3c.dom.Node}.
     *
     * @param bean stores info about bean
     * @return name of the bean within context
     * @throws InjectionException if any exception was thrown during parsing
     */
    private String instantiateBean(Node bean) throws InjectionException {
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
        beansMap.put(id, instance);

        return id;
    }

    /**
     * Creates an instance of the class via reflection.
     * <p>
     * Two ways of instantiation are supported: via default constructor
     * and via static method <code>getInstance</code>. These ways are
     * specified by the <code>type</code> parameter.
     * @param clazz - class of the bean
     * @param type - instantiation type: either <code>constructor</code>
     *             or <code>singleton</code>
     * @return instance of the specified class
     * @throws InjectionException if any exception was thrown during instantiation
     */
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
            throw new InjectionException("Could not instantiate a bean. Please check descriptor and bean`s signature.", e);
        }
        return bean;
    }

    /**
     * Instantiates a field of the bean.
     * <p>
     * @param info - stores all necessary information about the bean,
     *             the field that should be instantiated and either
     *             name of the bean that will instantiate a field
     *             or value of the specified field
     * @throws InjectionException if any exception was thrown during the field instantiation
     */
    private void instantiateField(FieldInfo info) throws InjectionException {
        Node node = info.getNode();
        Object bean = beansMap.get(info.getDeclaringBeanName());

        NamedNodeMap attributes = node.getAttributes();
        Node nameAttribute = attributes.getNamedItem(NAME);
        Node referenceAttribute = attributes.getNamedItem(REFERENCE);
        Node valueAttribute = attributes.getNamedItem(VALUE);

        if(nameAttribute == null || referenceAttribute == null && valueAttribute == null) {
            throw new InjectionException(String.format("Instantiation error. Fields %s, %s or %s must not be empty.",
                    NAME, REFERENCE, VALUE));
        }

        String name = nameAttribute.getNodeValue();
        Class<?> cls = bean.getClass();
        try {
            Field field;
            //if fields was not found in the current class, search it in a superclass
            try {
                field = cls.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                Class<?> superClass = cls.getSuperclass();
                field = superClass.getDeclaredField(name);
            }
            field.setAccessible(true);

            Object fieldValue;
            if(referenceAttribute != null) {
                String ref = referenceAttribute.getNodeValue();
                fieldValue = beansMap.get(ref);
            } else {
                String val = valueAttribute.getNodeValue();
                fieldValue = getFieldValue(field, val);
            }
            field.set(bean, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new InjectionException("Could not instantiate field. Please check descriptor and bean`s signature.", e);
        }
    }

    /**
     * Creates an instance of the class specified in field.
     * <p/>
     * The class of the field`s type must have a constructor that takes a string
     * as an argument and creates corresponding object.
     *
     * @param field contains meta info about the field
     * @param val - a string representation of the field`s value
     * @return instance of the class described in <code>field</code>
     * @throws InjectionException - if any exception was thrown during lookup and
     *                            and instantiation
     */
    private Object getFieldValue(Field field, String val) throws InjectionException {
        Class<?> type = field.getType();
        Class<String> stringType = String.class;

        if(type == stringType) {
            return val;
        } else {
            try {
                Constructor<?> constructor = type.getConstructor(stringType);
                return constructor.newInstance(val);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new InjectionException("Could not instantiate field. Please check descriptor and bean`s signature.", e);
            }
        }
    }

    /**
     * Stores information about fields of the bean that needs to be instantiated.
     */
    private static class FieldInfo {
        /**
         * Name of the bean whose field should be instantiated.
         */
        String declaringBeanName;
        /**
         * Contains information about the field`s value and name of the
         * bean that will be injected.
         */
        Node node;

        FieldInfo(String declaringBeanName, Node node) {
            this.declaringBeanName = declaringBeanName;
            this.node = node;
        }

        String getDeclaringBeanName() {
            return declaringBeanName;
        }

        Node getNode() {
            return node;
        }
    }

    @Override
    public Object getBean(String name) {
        return beansMap.get(name);
    }
}