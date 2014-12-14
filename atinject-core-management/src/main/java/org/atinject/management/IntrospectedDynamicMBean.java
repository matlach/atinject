package org.atinject.management;

import static java.lang.String.format;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

import org.atinject.management.annotation.Description;
import org.atinject.management.annotation.MBean;
import org.atinject.management.annotation.ManagedAttribute;
import org.atinject.management.annotation.ManagedOperation;
import org.atinject.management.annotation.ManagedOperation.Impact;
import org.atinject.management.annotation.Parameter;
import org.atinject.management.exception.ManagementException;

/**
 * A DynamicMBean that can introspect an annotated POJO bean and expose it as a DynamicMBean
 *
 * @author morten.hattesen@gmail.com
 *
 */
public class IntrospectedDynamicMBean implements DynamicMBean {
    private final Object mbean;
    private final Class<?> mbeanClass;
    private final Map<String, PropertyDescriptor> propertyDescriptors;
    private final Map<String, Method> operationMethods;
    private final MBeanInfo mbeanInfo;

    /** Constructs a Dynamic MBean by introspecting an annotated POJO MBean {@code annotatedMBean}
     * @param mbean the POJO MBean that should be exposed as a {@link DynamicMBean}
     * @throws ManagementException if an exception occurs during the introspection of {@code mbean}
     */
    public IntrospectedDynamicMBean(Object mbean) throws ManagementException {
        this.mbean = mbean;
        this.mbeanClass = mbean.getClass();
        if (!mbeanClass.isAnnotationPresent(MBean.class)) {
            throw new ManagementException(
                    format("MBean %s is not annotated with @%s", mbeanClass, MBean.class.getName()));
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(mbeanClass);
            propertyDescriptors = createPropertyDescriptors(beanInfo);
            operationMethods = createOperationMethods(beanInfo);
            mbeanInfo = createMbeanInfo();
        } catch (IntrospectionException e) {
            throw new ManagementException(e);
        } catch (java.beans.IntrospectionException e) {
            throw new ManagementException(e);
        }
    }


    @Override
    public Object getAttribute(String attribute) throws AttributeNotFoundException,
            MBeanException, ReflectionException {
        PropertyDescriptor propertyDescriptor = propertyDescriptors.get(attribute);
        if (propertyDescriptor == null) {
            throw new AttributeNotFoundException(attribute);
        }
        Method getter = propertyDescriptor.getReadMethod();
        if (getter == null) {
            throw new AttributeNotFoundException(
                    format("Getter method for attribute %s of %s", attribute, mbeanClass));
        }
        try {
            if (!getter.isAccessible()) {
                getter.setAccessible(true);
            }
            return getter.invoke(mbean);
        } catch (Exception e) {
            throw new RuntimeException(
                    format("Unable to obtain value of attribute %s of %s", attribute, mbeanClass));
        }
    }

    @Override
    public AttributeList getAttributes(String[] attributeNames) {
        AttributeList attributes = new AttributeList(attributeNames.length);
        for (String attributeName : attributeNames) {
            try {
                Attribute attribute = new Attribute(attributeName, getAttribute(attributeName));
                attributes.add(attribute);
            } catch (Exception e) {
                // Must be a mistake that the signature doesn't allow throwing exceptions
                throw new IllegalArgumentException(e);
            }
        }
        return attributes;
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException,
    InvalidAttributeValueException, MBeanException, ReflectionException {
        String name = attribute.getName();
        PropertyDescriptor propertyDescriptor = propertyDescriptors.get(name);
        if (propertyDescriptor == null) {
            throw new AttributeNotFoundException(name);
        }
        Method setter = propertyDescriptor.getWriteMethod();
        if (setter == null) {
            throw new AttributeNotFoundException(format("setter method for attribute %s of %s", name, mbeanClass));
        }
        Object value = attribute.getValue();
        try {
            if (!setter.isAccessible()) {
                setter.setAccessible(true);
            }
            setter.invoke(mbean, value);
        } catch (IllegalArgumentException e) {
            throw new InvalidAttributeValueException(String.format("attribute %s, value = (%s)%s, expected (%s)",
                    name, value.getClass().getName(), value, setter.getParameterTypes()[0].getName()));
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e, format("attribute %s of %s, value = (%s)%s",
                    name, mbeanClass, value.getClass().getName(), value));
        } catch (InvocationTargetException e) {
            throw new MBeanException(e, format("attribute %s of %s, value = (%s)%s",
                    name, mbeanClass, value.getClass().getName(), value));
        }
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        for (Object object : attributes) {
            Attribute attribute = (Attribute) object;
            try {
                setAttribute(attribute);
            } catch (Exception e) {
                // Must be a mistake that the signature doesn't allow throwing exceptions
                throw new IllegalArgumentException(e);
            }
        }
        // It seems like an API mistake that we have to return the attributes
        return attributes;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return mbeanInfo;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException {
        Method method = operationMethods.get(actionName);
        //TODO verify that the right signature is picked to avoid throwing an IllegalArgumentException
        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method.invoke(mbean, params);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * @param mbean the annotated POJO MBean
     * @return an MBeanInfo created by introspecting the {@code mbean}
     * @throws IntrospectionException
     * @throws javax.management.IntrospectionException
     * @throws ManagementException
     */
    private MBeanInfo createMbeanInfo() throws IntrospectionException, ManagementException {
        String description = description(mbeanClass);
        final MBeanAttributeInfo[] attributeInfo = createAttributeInfo(propertyDescriptors);
        final MBeanConstructorInfo[] constructorInfo = createConstructorInfo();
        final MBeanOperationInfo[] operationInfo = createOperationInfo();
        final MBeanNotificationInfo[] notificationInfo = createNotificationInfo();
        return new MBeanInfo(
                mbeanClass.getName(),
                description,
                attributeInfo,
                constructorInfo,
                operationInfo,
                notificationInfo);
    }

    /**
     * TODO should this be implemented?
     * @return null
     */
    private MBeanNotificationInfo[] createNotificationInfo() {
        return null;
    }

    /**
     * TODO: Consider allowing multiple matches for each (overloaded) method name
     *
     * @return The methods that constitute the operations
     * @throws ManagementException if multiple Operation annotations exist on identically named (overloaded) methods
     */
    private Map<String, Method> createOperationMethods(BeanInfo beanInfo) throws ManagementException {
        Map<String, Method> operationMethods = new HashMap<>();
        for (MethodDescriptor descriptor : beanInfo.getMethodDescriptors()) {
            Method method = descriptor.getMethod();
            ManagedOperation annotation = method.getAnnotation(ManagedOperation.class);
            if (annotation != null) {
                // This method is an operation
                Method old = operationMethods.put(method.getName(), method);
                if (old != null) {
                    throw new ManagementException(format("Multiple Operation annotations for operation %s of %s",
                            method.getName(), mbeanClass));
                }
            }
        }
        return operationMethods;
    }

    /**
     * @return an MBeanOPerationInfo array that describes the {@link ManagedOperation} annotated methods of the operationMethods
     * @throws ManagementException
     */
    private MBeanOperationInfo[] createOperationInfo() throws ManagementException {
        MBeanOperationInfo[] operationInfos = new MBeanOperationInfo[operationMethods.size()];
        int operationIndex = 0;
        // Iterate in method name order
        for (String methodName : sortedKeys(operationMethods)) {
            Method method = operationMethods.get(methodName);
            ManagedOperation annotation = method.getAnnotation(ManagedOperation.class);
            // add description and names to parameters
            MBeanParameterInfo[] signature = createParameterInfo(method);
            // add description and parameter info to operation method
            Impact impact = annotation.value();
            int impactValue = impact.impactValue;
            String description = description(method);
            MBeanOperationInfo opInfo = new MBeanOperationInfo(
                    method.getName(),
                    description,
                    signature,
                    method.getReturnType().getName(),
                    impactValue,
                    null);
            operationInfos[operationIndex++] = opInfo;
        }
        return operationInfos;
    }

    protected MBeanParameterInfo[] createParameterInfo(Method method) {
        MBeanParameterInfo[] parameters = new MBeanParameterInfo[method.getParameterTypes().length];
        for (int parameterIndex = 0; parameterIndex < parameters.length; parameterIndex++) {
            final String pType = method.getParameterTypes()[parameterIndex].getName();
            // locate parameter annotation
            Parameter parameter = getParameterAnnotation(method, parameterIndex, Parameter.class);
            Description description = getParameterAnnotation(method, parameterIndex, Description.class);
            final String pName = (parameter != null) ? parameter.value() : "p" + (parameterIndex + 1); // 1 .. n
            final String pDesc = (description != null) ? description.value() : null;
            parameters[parameterIndex] = new MBeanParameterInfo(pName, pType, pDesc);
        }
        return parameters;
    }

    /**
     * TODO should this be implemented?
     * @return null
     */
    private MBeanConstructorInfo[] createConstructorInfo() {
        return null;
    }

    /**
     * @return all properties where getter or setter is annotated with {@link ManagedAttribute}
     * @throws ManagementException
     */
    private Map<String, PropertyDescriptor> createPropertyDescriptors(BeanInfo beanInfo) throws ManagementException {
        Map<String, PropertyDescriptor> properties = new HashMap<>();
        for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
            ManagedAttribute getterAnnotation = getAnnotation(property.getReadMethod(), ManagedAttribute.class);
            ManagedAttribute setterAnnotation = getAnnotation(property.getWriteMethod(), ManagedAttribute.class);
            if ((getterAnnotation != null || setterAnnotation != null)) {
                properties.put(property.getName(), property);
            }
        }
        return properties;
    }

    /**
     *
     * @param propertyDescriptors property descriptors that are known to have at least one {@link ManagedAttribute}
     * annotation on its getter or setter method
     * @return MBean attributeInfo instances with getter/setter methods and description according to annotations
     * @throws ManagementException
     * @throws IntrospectionException
     */
    private MBeanAttributeInfo[] createAttributeInfo(Map<String, PropertyDescriptor> propertyDescriptors) throws ManagementException, IntrospectionException {
        MBeanAttributeInfo[] infos = new MBeanAttributeInfo[propertyDescriptors.size()];
        int i = 0;
        // iterate over properties that are known to have ManagedAttribute annotations, sorted by name
        for (String propertyName : sortedKeys(propertyDescriptors)) {
            PropertyDescriptor property = propertyDescriptors.get(propertyName);
            Method readMethod = property.getReadMethod();
            Method writeMethod = property.getWriteMethod();
            boolean readable = null != getAnnotation(readMethod, ManagedAttribute.class);
            boolean writable = null != getAnnotation(writeMethod, ManagedAttribute.class);
            Description descriptionAnnotation = getSingleAnnotation(property, Description.class, readMethod, writeMethod);
            String description = (descriptionAnnotation != null) ? descriptionAnnotation.value() : null;
            MBeanAttributeInfo info = new MBeanAttributeInfo(
                    property.getName(),
                    description,
                    readable ? readMethod : null,
                    writable ? writeMethod : null);
            infos[i++] = info;
        }
        return infos;
    }

    /**
     *
     * @param <T>
     * @param property The property to which entities belong
     * @param annotationClass Annotation type
     * @param entities A number of {@code Method}'s or {@code null}'s
     * @return The one (and only) annotation of type {@code annotationClass} that appears on {@code methods},
     * or null if none of the entities are annotated with annotationClass
     * @throws ManagementException if more than one of the entities are annotated with annotationClass
     */
    private <T extends Annotation> T getSingleAnnotation(PropertyDescriptor property, Class<T> annotationClass,
            AccessibleObject... entities) throws ManagementException {
        T result = null;
        for (AccessibleObject entity : entities) {
            if (entity != null) {
                T annotation = entity.getAnnotation(annotationClass);
                if (annotation != null) {
                    if (result != null) {
                        throw new ManagementException(
                                String.format("Multiple %s annotations found for property %s of %s",
                                        annotationClass.getName(), property.getName(), mbeanClass));
                    }
                    result = annotation;
                }
            }
        }
        return result;
    }

    /**
     * Find an annotation for a parameter on a method.
     *
     * @param <A> The annotation.
     * @param method The method.
     * @param index The index (0 .. n-1) of the parameter in the parameters list
     * @param annotationClass The annotation class
     * @return The annotation, or null
     */
    private static <A extends Annotation> A getParameterAnnotation(Method method,
            int index, Class<A> annotationClass) {
        for (Annotation a : method.getParameterAnnotations()[index]) {
            if (annotationClass.isInstance(a)) {
                return annotationClass.cast(a);
            }
        }
        return null;
    }

    /**
     * Null safe annotation checker
     * @param <A>
     * @param element element or null
     * @param annotationClass
     * @return the annotation, if element is not null and the annotation is present. Otherwise null
     */
    private <A extends Annotation> A getAnnotation(AnnotatedElement element, Class<A> annotationClass) {
        return (element != null) ? element.getAnnotation(annotationClass) : null;
    }

    private static String description(AnnotatedElement element) {
        Description annotation = element.getAnnotation(Description.class);
        return (annotation != null) ? annotation.value() : null;
    }

    /**
     * @param map
     * @return a list of the keys in map, sorted
     */
    private List<String> sortedKeys(Map<String, ?> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        return keys;
    }

}
