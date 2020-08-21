package cn.huanzi.qch.baseadmin.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate code
 */
@Slf4j
public class CodeDOM {


    /**
     * Database connection
     */
    private static final String URL = "jdbc:mysql://localhost:3306/base_admin?serverTimezone=UTC&characterEncoding=utf-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "pwd";
    private static final String DRIVER_CLASSNAME = "com.mysql.jdbc.Driver";
    /**
     * table name
     */
    private String tableName;

    /**
     * path description
     */
    private String basePackage_;
    private String package_;
    private String basePath;


    /**
     * Constructor
     */
    private CodeDOM(String tableName) {
        this.tableName = tableName;
        basePackage_ = "cn\\huanzi\\qch\\baseadmin\\sys\\";
        package_ = basePackage_ + StringUtil.camelCaseName(tableName).toLowerCase() + "\\";
        //System.getProperty("user.dir") retrieves the path of project, apply sub path for sub project
        basePath = System.getProperty("user.dir") + "\\src\\main\\java\\" + package_;
        basePackage_ = "cn\\huanzi\\qch\\baseadmin\\";
    }

    /**
     * Creat pojo
     */
    private void createPojo(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "pojo\\" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + ".java");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(
                "package " + package_.replaceAll("\\\\", ".") + "pojo;\n" +
                        "\n" +
                        "import lombok.Data;\n" +
                        "import javax.persistence.*;\n" +
                        "import java.io.Serializable;\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "@Entity\n" +
                        "@Table(name = \"" + tableName + "\")\n" +
                        "@Data\n" +
                        "public class " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + " implements Serializable {\n"
        );
        // traverse the table and set the field
        for (TableInfo tableInfo : tableInfos) {
            // primary key
            if ("PRI".equals(tableInfo.getColumnKey())) {
                stringBuffer.append("    @Id\n");
            }
            // auto increment
            if ("auto_increment".equals(tableInfo.getExtra())) {
                stringBuffer.append("    @GeneratedValue(strategy= GenerationType.IDENTITY)\n");
            }
            stringBuffer.append("    private " + StringUtil.typeMapping(tableInfo.getDataType()) + " " + StringUtil.camelCaseName(tableInfo.getColumnName()) + ";//" + tableInfo.getColumnComment() + "\n\n");
        }
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);
    }

    /**
     * create vo class
     */
    private void createVo(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "vo\\" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Vo.java");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(
                "package " + package_.replaceAll("\\\\", ".") + "vo;\n" +
                        "\n" +
                        "import "+ basePackage_.replaceAll("\\\\", ".") +" common.pojo.PageCondition;"+
                        "import lombok.Data;\n" +
                        "import java.io.Serializable;\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "@Data\n" +
                        "public class " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Vo extends PageCondition implements Serializable {\n"
        );
        // traverse and set the fields
        for (TableInfo tableInfo : tableInfos) {
            stringBuffer.append("    private " + StringUtil.typeMapping(tableInfo.getDataType()) + " " + StringUtil.camelCaseName(tableInfo.getColumnName()) + ";//" + tableInfo.getColumnComment() + "\n\n");
        }
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);
    }

    /**
     * Create Repository class
     */
    private void createRepository(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "repository\\" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Repository.java");
        StringBuffer stringBuffer = new StringBuffer();
        String t = "String";
        // traverse the table and set the fields
        for (TableInfo tableInfo : tableInfos) {
            // Primary key
            if ("PRI".equals(tableInfo.getColumnKey())) {
                t = StringUtil.typeMapping(tableInfo.getDataType());
            }
        }
        stringBuffer.append(
                "package " + package_.replaceAll("\\\\", ".") + "repository;\n" +
                        "\n" +
                        "import " + basePackage_.replaceAll("\\\\", ".") + "common.repository.*;\n" +
                        "import " + package_.replaceAll("\\\\", ".") + "pojo." + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + ";\n" +
                        "import org.springframework.stereotype.Repository;\n" +
                        "\n" +
                        "@Repository\n" +
                        "public interface " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Repository extends CommonRepository<" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + ", " + t + "> {"
        );
        stringBuffer.append("\n");
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);
    }

    /**
     * Create Service Class
     */
    private void createService(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "service\\" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Service.java");
        StringBuffer stringBuffer = new StringBuffer();
        String t = "String";
        // Traverse the table and set the fileds
        for (TableInfo tableInfo : tableInfos) {
            // Primary key
            if ("PRI".equals(tableInfo.getColumnKey())) {
                t = StringUtil.typeMapping(tableInfo.getDataType());
            }
        }
        stringBuffer.append(
                "package " + package_.replaceAll("\\\\", ".") + "service;\n" +
                        "\n" +
                        "import " + basePackage_.replaceAll("\\\\", ".") + "common.service.*;\n" +
                        "import " + package_.replaceAll("\\\\", ".") + "pojo." + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + ";\n" +
                        "import " + package_.replaceAll("\\\\", ".") + "vo." + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Vo;\n" +
                        "\n" +
                        "public interface " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Service extends CommonService<" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Vo, " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + ", " + t + "> {"
        );
        stringBuffer.append("\n");
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);

        //Impl
        File file1 = FileUtil.createFile(basePath + "service\\" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "ServiceImpl.java");
        StringBuffer stringBuffer1 = new StringBuffer();
        stringBuffer1.append(
                "package " + package_.replaceAll("\\\\", ".") + "service;\n" +
                        "\n" +
                        "import " + basePackage_.replaceAll("\\\\", ".") + "common.service.*;\n" +
                        "import " + package_.replaceAll("\\\\", ".") + "pojo." + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + ";\n" +
                        "import " + package_.replaceAll("\\\\", ".") + "vo." + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Vo;\n" +
                        "import " + package_.replaceAll("\\\\", ".") + "repository." + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Repository;\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.stereotype.Service;\n" +
                        "import org.springframework.transaction.annotation.Transactional;\n" +
                        "import javax.persistence.EntityManager;\n" +
                        "import javax.persistence.PersistenceContext;\n" +
                        "\n" +
                        "@Service\n" +
                        "@Transactional\n" +
                        "public class " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "ServiceImpl extends CommonServiceImpl<" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Vo, " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + ", " + t + "> implements " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Service{"
        );
        stringBuffer1.append("\n\n");
        stringBuffer1.append(
                "    @PersistenceContext\n" +
                        "    private EntityManager em;\n");

        stringBuffer1.append("" +
                "    @Autowired\n" +
                "    private " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Repository " + StringUtil.camelCaseName(tableName) + "Repository;\n");
        stringBuffer1.append("}");
        FileUtil.fileWriter(file1, stringBuffer1);
    }

    /**
     * Create controller class
     */
    private void createController(List<TableInfo> tableInfos) {
        File file = FileUtil.createFile(basePath + "controller\\" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Controller.java");
        StringBuffer stringBuffer = new StringBuffer();
        String t = "String";
        // Traverse the table
        for (TableInfo tableInfo : tableInfos) {
            // Primary key
            if ("PRI".equals(tableInfo.getColumnKey())) {
                t = StringUtil.typeMapping(tableInfo.getDataType());
            }
        }
        stringBuffer.append(
                "package " + package_.replaceAll("\\\\", ".") + "controller;\n" +
                        "\n" +
                        "import " + basePackage_.replaceAll("\\\\", ".") + "common.controller.*;\n" +
                        "import " + package_.replaceAll("\\\\", ".") + "pojo." + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + ";\n" +
                        "import " + package_.replaceAll("\\\\", ".") + "vo." + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Vo;\n" +
                        "import " + package_.replaceAll("\\\\", ".") + "service." + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Service;\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.web.bind.annotation.*;\n" +
                        "\n" +
                        "@RestController\n" +
                        "@RequestMapping(\"/sys/" + StringUtil.camelCaseName(tableName) + "/\")\n" +
                        "public class " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Controller extends CommonController<" + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Vo, " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + ", " + t + "> {"
        );
        stringBuffer.append("\n");
        stringBuffer.append("" +
                "    @Autowired\n" +
                "    private " + StringUtil.captureName(StringUtil.camelCaseName(tableName)) + "Service " + StringUtil.camelCaseName(tableName) + "Service;\n");
        stringBuffer.append("}");
        FileUtil.fileWriter(file, stringBuffer);
    }

    /**
     * get table structural info
     */
    private List<TableInfo> getTableInfo() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<TableInfo> list = new ArrayList<>();
        try {
            conn = DBConnectionUtil.getConnection();
            String sql = "select column_name,data_type,column_comment,column_key,extra from information_schema.columns where table_name=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tableName);
            rs = ps.executeQuery();
            while (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                // set all columns to lowercase
                tableInfo.setColumnName(rs.getString("column_name").toLowerCase());
                // type of columns
                tableInfo.setDataType(rs.getString("data_type"));
                // get the comment
                tableInfo.setColumnComment(rs.getString("column_comment"));
                // primary key
                tableInfo.setColumnKey(rs.getString("column_key"));
                // primary key type
                tableInfo.setExtra(rs.getString("extra"));
                list.add(tableInfo);
            }
        } catch (SQLException e) {
            // output to log file
            log.error(ErrorUtil.errorInfoToString(e));
        } finally {
            assert rs != null;
            DBConnectionUtil.close(conn, ps, rs);
        }
        return list;
    }

    /**
     * file util
     */
    private static class FileUtil {
        /**
         * create file
         *
         * @param pathNameAndFileName
         * @return File object
         */
        private static File createFile(String pathNameAndFileName) {
            File file = new File(pathNameAndFileName);
            try {
                // retrieve parent file
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                // creat file
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception e) {
                file = null;
                System.err.println("Error: create files");
                // output to log file
                log.error(ErrorUtil.errorInfoToString(e));
            }
            return file;
        }

        /**
         * bytestream to file
         *
         * @param file         file object
         * @param stringBuffer data to be written
         */
        private static void fileWriter(File file, StringBuffer stringBuffer) {
            // byte stream
            try {
                FileWriter resultFile = new FileWriter(file, false);//true,then append; false,then cover
                PrintWriter myFile = new PrintWriter(resultFile);
                // write
                myFile.println(stringBuffer.toString());

                myFile.close();
                resultFile.close();
            } catch (Exception e) {
                System.err.println("Error: input");
                // output to log file
                log.error(ErrorUtil.errorInfoToString(e));
            }
        }
    }

    /**
     * String util
     */
    private static class StringUtil {
        /**
         * database type -> Java type
         *
         * @param dbType
         * @return Java type
         */
        private static String typeMapping(String dbType) {
            String javaType;
            if ("int|integer".contains(dbType)) {
                javaType = "Integer";
            } else if ("float|double|decimal|real".contains(dbType)) {
                javaType = "Double";
            } else if ("date|time|datetime|timestamp".contains(dbType)) {
                javaType = "Date";
            } else {
                javaType = "String";
            }
            return javaType;
        }

        /**
         * Camel-case to Underscore
         */
        public static String underscoreName(String camelCaseName) {
            StringBuilder result = new StringBuilder();
            if (camelCaseName != null && camelCaseName.length() > 0) {
                result.append(camelCaseName.substring(0, 1).toLowerCase());
                for (int i = 1; i < camelCaseName.length(); i++) {
                    char ch = camelCaseName.charAt(i);
                    if (Character.isUpperCase(ch)) {
                        result.append("_");
                        result.append(Character.toLowerCase(ch));
                    } else {
                        result.append(ch);
                    }
                }
            }
            return result.toString();
        }

        /**
         * First Character uppercase
         */
        static String captureName(String name) {
            char[] cs = name.toCharArray();
            cs[0] -= 32;
            return String.valueOf(cs);

        }

        /**
         * Underscore to Camel-case
         */
        static String camelCaseName(String underscoreName) {
            StringBuilder result = new StringBuilder();
            if (underscoreName != null && underscoreName.length() > 0) {
                boolean flag = false;
                for (int i = 0; i < underscoreName.length(); i++) {
                    char ch = underscoreName.charAt(i);
                    if ("_".charAt(0) == ch) {
                        flag = true;
                    } else {
                        if (flag) {
                            result.append(Character.toUpperCase(ch));
                            flag = false;
                        } else {
                            result.append(ch);
                        }
                    }
                }
            }
            return result.toString();
        }
    }

    /**
     * JDBC connector
     */
    private static class DBConnectionUtil {

        static {
            // 1、load driver
            try {
                Class.forName(DRIVER_CLASSNAME);
            } catch (ClassNotFoundException e) {
                // output to log file
                log.error(ErrorUtil.errorInfoToString(e));
            }
        }

        /**
         * Return a connection
         */
        static Connection getConnection() {
            Connection conn = null;
            // 2、Connect to database
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                // output to log file
                log.error(ErrorUtil.errorInfoToString(e));
            }
            return conn;
        }

        /**
         *  Close Connection, Statement
         */
        public static void close(Connection conn, Statement stmt) {
            try {
                conn.close();
                stmt.close();
            } catch (SQLException e) {
                // Output to log file
                log.error(ErrorUtil.errorInfoToString(e));
            }
        }

        /**
         * Close Connection，Statement，ResultSet
         */
        public static void close(Connection conn, Statement stmt, ResultSet rs) {
            try {
                close(conn, stmt);
                rs.close();
            } catch (SQLException e) {
                // Output to log file
                log.error(ErrorUtil.errorInfoToString(e));
            }
        }

    }

    /**
     * TableInfor pojo
     */
    private class TableInfo {
        private String columnName;
        private String dataType;
        private String columnComment;
        private String columnKey;
        private String extra;

        TableInfo() {
        }

        String getColumnName() {
            return columnName;
        }

        void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        String getDataType() {
            return dataType;
        }

        void setDataType(String dataType) {
            this.dataType = dataType;
        }

        String getColumnComment() {
            return columnComment;
        }

        void setColumnComment(String columnComment) {
            this.columnComment = columnComment;
        }

        String getColumnKey() {
            return columnKey;
        }

        void setColumnKey(String columnKey) {
            this.columnKey = columnKey;
        }

        String getExtra() {
            return extra;
        }

        void setExtra(String extra) {
            this.extra = extra;
        }
    }

    /**
     * Fast create, can be used externally. Don't forget to set the basic path before calling this method
     */
    private String create() {
        List<TableInfo> tableInfo = getTableInfo();
        createPojo(tableInfo);
        createVo(tableInfo);
        createRepository(tableInfo);
        createService(tableInfo);
        createController(tableInfo);
        System.out.println("basePath:" + basePath);
        return tableName + " Generate backend code: Success!";
    }

//    public static void main(String[] args) {
//        String[] tables = {"sys_user","sys_menu","sys_authority","sys_user_menu","sys_user_authority","sys_shortcut_menu","sys_setting"};
//        for (String table : tables) {
//            String msg = new CodeDOM(table).create();
//            System.out.println(msg);
//        }
//    }
}
