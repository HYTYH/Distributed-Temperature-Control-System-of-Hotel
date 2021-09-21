package team.bupt.central_air.dao;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.ListOrderedMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.*;


/**
 * @author wanz
 */
public class DBConnectionUtil {
    static final String USER = "root";
    static final String PASS = "root";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = ("jdbc:mysql://localhost:3306/" +
            "HotelAir?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    static Connection conn = null;

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取数据库连接
     *
     * @return Connection
     */
    public static synchronized Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (Exception ignored) {
            }
        }
        return conn;
    }

    /**
     * 查询一个sql,将结果转为hashmap形式
     *
     * @param sql 制定查询的sql
     * @return List<Map < String, Object>>
     */
    public static List<Map<String, Object>> query(String sql) {
        Connection connection = getConnection();
        ResultSet resultSet;
        List<Map<String, Object>> res = null;
        try {
            resultSet = connection.createStatement().executeQuery(sql);

            res = convertList(resultSet);

        } catch (SQLException exp) {
            exp.printStackTrace();
        }
        return res;
    }

    /**
     * 查询一个sql,将结果转为对象形式
     *
     * @param sql   制定查询的sql
     * @param clazz 目标对象的.class
     * @return List<Object>
     */
    public static List<Object> query(String sql, Class<?> clazz) throws Exception {
        List<Map<String, Object>> query = query(sql);
        List<Object> res = new LinkedList<>();
        for (Map<String, Object> stringObjectMap : query) {
            Object mapToObject = getMapToObject(stringObjectMap, clazz);
            res.add(mapToObject);
        }
        return res;
    }

    /**
     * 直接查询对应class
     *
     * @param clazz 目标对象的.class
     * @return List<Object>
     */
    public static List<Object> query(Class<?> clazz) throws Exception {
        String sql = "select * from " + clazz.getName();
        return query(sql, clazz);
    }

    /**
     * 转换ResultSet为Hashmap
     *
     * @param rs 转换ResultSet为Hashmap
     * @return List<Map < String, Object>>
     * @throws SQLException .
     */
    private static List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }

    /**
     * 将对象转换成hashmap的形式
     *
     * @param obj 对象
     * @return hashmap
     * @throws IllegalAccessException .
     */
    private static Map<String, Object> getObjectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String keyName = field.getName();
            Object value = field.get(obj);
            if (value == null)
                value = "";
            map.put(keyName, value);
        }
        return map;
    }

    /**
     * 通过反射将map转换成对象
     * 目标类必须要有无参构造函数以及get setter方法
     *
     * @param map   map
     * @param clazz 目标对象的class文件
     * @return Object
     * @throws Exception .
     */
    private static Object getMapToObject(Map<String, Object> map, Class<?> clazz) throws Exception {
        if (map == null) {
            return null;
        }
        Object obj = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(obj, map.get(field.getName()));
            }
        }
        return obj;
    }

    /**
     * 将对象插入表中
     *
     * @param o     对象
     * @param table 表名称
     * @return 成功的条数
     * @throws IllegalAccessException .
     */
    public static int insertToTable(Object o, String table) throws IllegalAccessException {
        Map<String, Object> objectToMap = getObjectToMap(o);
        Connection connection = getConnection();
        OrderedMap map = ListOrderedMap.decorate(objectToMap);
        String[] result = table.split("\\.");
        table = result[result.length-1];
        StringBuilder sql = new StringBuilder("insert into " + table +  " (");

        for (Object key : map.keySet()) {
            sql.append(key).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") VALUES (");
        for (Object value : map.values()) {
            sql.append("'").append(value).append("'").append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");

        // System.out.println(sql);

        try {
            return connection.createStatement().executeUpdate(String.valueOf(sql));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将对象插入表中,表名称为o.getClass().getName()
     *
     * @param o 对象
     * @return 成功的条数
     * @throws IllegalAccessException .
     */
    public static int insertToTable(Object o) throws IllegalAccessException {
        return insertToTable(o, o.getClass().getName());
    }

    public static int delete(String sql) {
        Connection connection = getConnection();
        try {
            return connection.createStatement().executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public static int delete(Object obj) throws IllegalAccessException {
        Connection connection = getConnection();
        String tableName = obj.getClass().getName();
        String[] result = tableName.split("\\.");
        tableName = result[result.length-1];
        StringBuilder sql = new StringBuilder("delete from " + tableName + " where ");
        Map<String, Object> map = getObjectToMap(obj);
        map.forEach((k, v) -> sql.append(" " + k + "='" + v + "' and "));
        sql.deleteCharAt(sql.length() - 1);
        sql.deleteCharAt(sql.length() - 1);
        sql.deleteCharAt(sql.length() - 1);
        sql.deleteCharAt(sql.length() - 1);
        try {
            System.out.println(sql);
            return connection.createStatement().executeUpdate(String.valueOf(sql));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public static int update(String sql) {
        return delete(sql);
    }
}