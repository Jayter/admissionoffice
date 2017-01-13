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
import java.util.*;

/**
 * Initializes classes described in "di/dependencies.xml" descriptor.
 */
public class InjectionResolver {

    private static final String DESCRIPTOR_PATH = "di/dependencies.xml";

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
     * Creates an object.
     * <p>
     * The object must be initialized by {@link #init()} method.
     */
    public InjectionResolver() {
    }

    /**
     * Initializes a {@link #doc} - document that is used for information extraction.
     * <p>
     * Descriptor file must have name "dependencies.xml" and has to be
     * situated in package <code>di</code>. In any other case an instance
     * of {@link InjectionException} is thrown.
     *
     * @throws InjectionException if "di/dependencies.xml" was not found or any
     *                            other exception was thrown during the file parsing
     */
    public void init() throws InjectionException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            URL path = getClass().getClassLoader().getResource(DESCRIPTOR_PATH);
            if(path == null) {
                throw new InjectionException("Dependencies configuration is not found.");
            }
            doc = builder.parse(path.getPath());
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
     * The bean can contain fields that are instantiated by other beans.
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
     * </ul>
     *
     * @throws InjectionException if the root tag <code>beans</code> was not found
     *                            or any exception was thrown during parsing
     */
    public void parse() throws InjectionException {
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

                //add fields into queue
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
     * @return name of the bean that will be used to instantiate fields of other beans
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
     *             the field that should be instantiated and name of
     *             the bean that will instantiate a field
     * @throws InjectionException if any exception was thrown during the field instantiation
     */
    private void instantiateField(FieldInfo info) throws InjectionException {
        Node node = info.getNode();
        Object bean = beansMap.get(info.getDeclaringBeanName());

        NamedNodeMap attributes = node.getAttributes();
        Node nameAttribute = attributes.getNamedItem(NAME);
        Node referenceAttribute = attributes.getNamedItem(REFERENCE);

        if(nameAttribute == null || referenceAttribute == null) {
            throw new InjectionException(String.format("Instantiation error. Fields %s, %s must not be empty.", NAME, REFERENCE));
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

    /**
     * Counts and returns name of the setter of specified field.
     * <p>
     * @param field - name of the field
     * @return name of the field`s setter
     */
    private String getSetterName(String field) {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1, field.length());
    }

    /**
     * Used to store information about fields of the bean that needs to be instantiated.
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

        public FieldInfo(String declaringBeanName, Node node) {
            this.declaringBeanName = declaringBeanName;
            this.node = node;
        }

        public String getDeclaringBeanName() {
            return declaringBeanName;
        }

        public Node getNode() {
            return node;
        }
    }
}