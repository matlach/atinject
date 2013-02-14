package org.atinject.tool;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.jgroups.conf.PropertyHelper;
import org.slf4j.Logger;

public class EnumToJavascript
{
    private static Logger logger = LoggingHelper.getLogger(EnumToJavascript.class);

    public static String enumerationPath;
    public static final String phoenixEnumerationPath = "src/apps/globals/enumeration/";

    public static void main(String[] args) throws Exception
    {
        logger.info("running enum generator");

        System.setProperty("running.outside.j2ee", "true");
        String userPropertiesFilename = "build." + System.getProperty("user.name") + ".properties";
        Properties userProperties = PropertyHelper.loadProperties(userPropertiesFilename);

        String phoenixPath = userProperties.getProperty("phoenix.path");

        enumerationPath = phoenixPath + phoenixEnumerationPath;

        logger.info("export path : '{}'", enumerationPath);

        List<Class<?>> mycaClasses = new ArrayList<Class<?>>();
        Enumeration<URL> roots = EnumToJavascript.class.getClassLoader().getResources("");
        while (roots.hasMoreElements())
        {
            File root = new File(roots.nextElement().getPath());
            mycaClasses.addAll(getAllMycaClasses(root));
        }

        List<Class<Enum<?>>> enumerationClasses = new ArrayList<Class<Enum<?>>>();
        for (Class<?> mycaClass : mycaClasses)
        {
            // looks for a com.myca.* enum which implements the ExportableEnumeration interface
            if (mycaClass.isEnum() && ExportableEnumeration.class.isAssignableFrom(mycaClass))
            {
                // we support two kind of enums, ensure it implements String/IntValueEnumeration as well
                if (StringValueEnumeration.class.isAssignableFrom(mycaClass)
                        || IntValueEnumeration.class.isAssignableFrom(mycaClass))
                {
                    logger.info("found exportable enumeration '{}'", mycaClass.getCanonicalName());
                    enumerationClasses.add((Class<Enum<?>>) mycaClass);
                }
            }
        }

        if (!enumerationClasses.isEmpty())
        {
            logger.info("exporting enumeration ...");

            for (Class<Enum<?>> enumeration : enumerationClasses)
            {
                exportEnumeration(enumeration);
            }
        }
    }

    private static List<Class<?>> getAllMycaClasses(File root) throws Exception
    {
        List<Class<?>> files = new ArrayList<Class<?>>();
        for (File file : root.listFiles())
        {
            if (file.isDirectory())
            {
                files.addAll(getAllMycaClasses(file));
            }
            else
            {
                // if a non inlined myca class
                if (file.getPath().contains("com" + File.separator + "myca") && !file.getName().contains("$")
                        && file.getName().endsWith(".class"))
                {
                    String canonicalClassName = file.getPath().replace(File.separator, ".").replace(".class", "");
                    canonicalClassName = canonicalClassName.substring(canonicalClassName.indexOf("com.myca"));

                    Class<?> clazz = Class.forName(canonicalClassName);
                    files.add(clazz);
                }
            }
        }
        return files;
    }

    private static void exportEnumeration(Class<Enum<?>> enumerationClass)
    {
        String enumTemplate = "define([], function(){" + "\n" + "    var constants = {" + "\n"
                + "        ${enumNameFunctions}" + "\n" + "    };" + "\n" + "    " + "\n"
                + "    constants.fromValue = function(id){" + "\n" + "        switch(id){" + "\n"
                + "            ${cases}" + "\n" + "        }" + "\n" + "    };" + "\n" + "    " + "\n"
                + "    return constants;" + "\n" + "});" + "\n";

        String enumNameFunctionTemplate = "        ${enumName}: ${enumValue},";

        String caseTemplate = "            case this.${enumName}: return '${enumName}'; break;";

        Map<String, String> enumNameValues = enumNameValue(enumerationClass);

        String replacedEnumNameFunctions = "";

        for (Entry<String, String> entry : enumNameValues.entrySet())
        {
            replacedEnumNameFunctions = replacedEnumNameFunctions
                    + enumNameFunctionTemplate.replace("${enumName}", entry.getKey()).replace("${enumValue}",
                            entry.getValue()) + "\n";
        }
        // remove trailing ","
        replacedEnumNameFunctions = replacedEnumNameFunctions.substring(0, replacedEnumNameFunctions.lastIndexOf(","))
                + "\n";

        String replacedCases = "";

        for (Entry<String, String> entry : enumNameValues.entrySet())
        {
            replacedCases = replacedCases
                    + caseTemplate.replace("${enumValue}", entry.getValue()).replace("${enumName}", entry.getKey())
                    + "\n";
        }

        String exportedEnumeration = enumTemplate.replace("${enumNameFunctions}", replacedEnumNameFunctions.trim())
                .replace("${cases}", replacedCases.trim());

        logger.info("exported enumeration '{}'", enumerationClass.getCanonicalName());

        saveExportedEnumeration(enumerationClass, exportedEnumeration);
    }

    private static Map<String, String> enumNameValue(Class<Enum<?>> enumerationClass)
    {
        Map<String, String> enumNameValue = new LinkedHashMap<String, String>();

        Enum<?>[] enumerations = enumerationClass.getEnumConstants();
        for (Enum<?> enumeration : enumerations)
        {
            String enumName = enumeration.name();
            String enumValue = "'undefined'";
            if (enumeration instanceof StringValueEnumeration)
            {
                enumValue = "'" + ((StringValueEnumeration) enumeration).getValue() + "'";
            }
            else if (enumeration instanceof IntValueEnumeration)
            {
                enumValue = String.valueOf(((IntValueEnumeration) enumeration).getValue());
            }
            enumNameValue.put(enumName, enumValue);
        }

        return enumNameValue;
    }

    private static void saveExportedEnumeration(Class<Enum<?>> enumerationClass, String exportedEnumeration)
    {
        File enumerationFile = new File(enumerationPath + enumerationClass.getSimpleName() + ".js");
        FileHelper.writeStringToFile(enumerationFile, exportedEnumeration);
    }
}