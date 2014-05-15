package jo.sm.logic.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;

public class MapUtils {

    private static final Logger logger = Logger.getLogger(MapUtils.class.getPackage().getName());

    public static boolean isPropertyEqual(Properties property1, Properties property2) {
        if (property1 == null && property2 == null) {
            return true;
        }
        if ((property1 == null && property2 != null) || (property2 == null && property1 != null)) {
            return false;
        }
        if (!property1.isEmpty() || !property2.isEmpty()) {
        } else {
            return true;
        }
        if (property1.size() != property2.size()) {
            logger.log(Level.FINER, "Size1:{0}", property1.size());
            logger.log(Level.FINER, "Size2:{0}", property2.size());
            return false;
        }
        for (Object key : property1.keySet()) {
            Object value1 = property1.get(key);
            Object value2 = property2.get(key);
            if (value2 == null) {
                if (!StringUtils.isTrivial((String) value1)) {
                    logger.log(Level.FINEST,"{0}" + "Key:" + " is not in original property.", key);
                    logger.log(Level.FINEST, "Key:{0}", key);
                    logger.log(Level.FINEST, "Value1={0}", value1);
                    logger.finest("Value2 is null");
                    return false;
                }
            } else if (!value2.equals(value1)) {
                logger.finest("Values are not equal:");
                logger.log(Level.FINEST, "Key:{0}", key.toString());
                logger.log(Level.FINEST, "Value1:{0}", value1.toString());
                logger.log(Level.FINEST, "Value2:{0}", value2.toString());
                return false;
            }
        }
        return true;
    }

    public static void store(Map<Object, Object> metadata, Properties source) {
        store(metadata, source, "");
    }

    public static void store(Map<Object, Object> metadata, Properties source, String prefix) {
        logger.log(Level.FINE, "Storing {0} properties into metadata.", prefix);
        for (Enumeration<Object> e = source.keys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String value = (String) source.getProperty(key);

            if (!key.equals("orig_Text")) {
                logger.log(Level.FINEST, "Storing Key:{0}{1}:: Value:{2}:", new Object[]{prefix, key, value});
            }
            metadata.put(prefix + key, source.getProperty(key));
        }
    }

    public static Properties load(Properties source) {
        return load(source, "");
    }

    public static Properties load(Map<Object, Object> metadata, String prefix) {
        logger.log(Level.FINE,"{0}" + "Loading " + " properties from metadata.", prefix);

        Properties targetProperty = new Properties();
        for (Object key : metadata.keySet()) {
            Object val = metadata.get(key);
            if (!(key instanceof String) || !(val instanceof String)) {
                continue;
            }
            String k = (String) key;
            if (!StringUtils.isTrivial(prefix) && !k.startsWith(prefix)) {
                continue;
            }

            String id = k.substring(prefix.length(), k.length());
            if (val instanceof String) {
                if (!key.equals("orig_Text")) {
                    logger.log(Level.FINEST, "Loading Key:{0}: Value:{1}", new Object[]{key, val});
                }
                targetProperty.put(id, (String) val);
            }
        }
        return targetProperty;
    }

    public static void clear(Map<Object, Object> metadata) {
        clear(metadata, "");
    }

    public static void clear(Map<Object, Object> metadata, String prefix) {
        for (Object key : metadata.keySet().toArray()) {
            if (key instanceof String) {
                if (((String) key).startsWith(prefix)) {
                    metadata.remove(key);
                }
            }
        }
    }

    public static void dump(Map<?, ?> m, String prefix) {
        dump(m, prefix, System.out);
    }

    public static void dump(Map<?, ?> m, String prefix, OutputStream os) {
        try {
            Object[] keys = m.keySet().toArray();
            Arrays.sort(keys);
            for (Object key : keys) {
                Object v = m.get(key);
                if (v == null) {
                    os.write((prefix + key + "=<null>").getBytes("utf8"));
                    os.write("\r\n".getBytes("utf8"));
                } else {
                    String val = v.toString();
                    if (val.length() > 60) {
                        val = val.substring(0, 60) + "...";
                    }
                    int o = val.indexOf('\n');
                    if (o >= 0) {
                        val = val.substring(0, o) + "...";
                    }
                    os.write((prefix + key + "='" + val + "'").getBytes("utf8"));
                    os.write("\r\n".getBytes("utf8"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void copy(Map dest, Map src) {
        for (Object k : src.keySet()) {
            Object v = src.get(k);
            dest.put(k, v);
        }
    }

    public static void toXML(Map<?, ?> p, String nodeName, Node parent) {
        Node params = XMLEditUtils.addElement(parent, nodeName);
        for (Object k : p.keySet()) {
            Object v = p.get(k);
            if (v == null) {
                continue;
            }
            if (!(k instanceof String)) {
                logger.log(Level.WARNING, "Attempt to write map to XML with key of type ''{0}''", k.getClass().getName());
            }
            Node param = XMLEditUtils.addElement(params, "param");
            XMLEditUtils.addAttribute(param, "name", k.toString());
            XMLEditUtils.addAttribute(param, "type", v.getClass().getName());
            if (v instanceof String[]) {
                for (String item : ((String[]) v)) {
                    XMLEditUtils.addTextTag(param, "element", escapeString(item));
                }
            } else if (v instanceof Properties) {
                for (Object pName : ((Properties) v).keySet()) {
                    String val = ((Properties) v).getProperty((String) pName);
                    if (val != null) {
                        Node prop = XMLEditUtils.addElement(param, "property");
                        XMLEditUtils.addAttribute(prop, "key", (String) pName);
                        XMLEditUtils.addAttribute(prop, "value", escapeString(val));
                    }
                }
            } else if (v instanceof String) {
                XMLEditUtils.addText(param, escapeString((String) v));
            } else if (v instanceof Boolean) {
                XMLEditUtils.addText(param, escapeString(v.toString()));
            } else if (v instanceof Long) {
                XMLEditUtils.addText(param, escapeString(v.toString()));
            } else if (v instanceof Integer) {
                XMLEditUtils.addText(param, escapeString(v.toString()));
            } else if (v instanceof Map<?, ?>) {
                toXML((Map<?, ?>) v, "map", param);
            } else {
                logger.log(Level.WARNING, "Attempt to write map to XML with value of type ''{0}'' (key={1})", new Object[]{v.getClass().getName(), k});
                XMLEditUtils.addText(param, v.toString());
            }
        }
    }

    public static void fromXML(Map<Object, Object> p, Node params) {
        if (params == null) {
            return;
        }
        for (Node param : XMLUtils.findNodes(params, "param")) {
            String name = XMLUtils.getAttribute(param, "name");
            String type = XMLUtils.getAttribute(param, "type");
            switch (type) {
                case "[Ljava.lang.String;":
                    List<String> strings = new ArrayList<>();
                    for (Node element : XMLUtils.findNodes(param, "element")) {
                        strings.add(unescapeString(XMLUtils.getText(element)));
                    }   p.put(name, strings.toArray(new String[0]));
                    break;
                case "java.util.Properties":
                    Properties props = new Properties();
                    for (Node prop : XMLUtils.findNodes(param, "property")) {
                        String key = XMLUtils.getAttribute(prop, "key");
                        String val = unescapeString(XMLUtils.getAttribute(prop, "value"));
                        props.put(key, val);
                    }   p.put(name, props);
                    break;
                case "java.util.HashMap":
                    Map<Object, Object> map = new HashMap<>();
                    Node m = XMLUtils.findFirstNode(param, "map");
                    fromXML(map, m);
                    p.put(name, map);
                    break;
                case "java.lang.String":
                    p.put(name, unescapeString(XMLUtils.getText(param)));
                    break;
                case "java.lang.Boolean":
                    p.put(name, BooleanUtils.parseBoolean(XMLUtils.getText(param)));
                    break;
                case "java.lang.Integer":
                    p.put(name, Integer.parseInt(XMLUtils.getText(param)));
                    break;
                case "java.lang.Long":
                    p.put(name, Long.parseLong(XMLUtils.getText(param)));
                    break;
                default:
                    throw new IllegalArgumentException("I don't know how to read a " + type);
            }
        }
    }

    private static String escapeString(String txt) {
        txt = txt.replace("\r", "&xd;");
        txt = txt.replace("\n", "&xa;");
        return txt;
    }

    private static String unescapeString(String txt) {
        txt = txt.replace("&xd;", "\r");
        txt = txt.replace("&xa;", "\n");
        return txt;
    }

    public static Object getKey(Map<?, ?> map, Object val) {
        for (Object key : map.keySet()) {
            Object v = map.get(key);
            if (v == val) {
                return key;
            }
        }
        return null;
    }
}
