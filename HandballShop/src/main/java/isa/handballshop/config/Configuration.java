package isa.handballshop.config;

import java.util.Calendar;
import java.util.logging.Level;

import isa.handballshop.model.dao.JDBCDAOFactory;

public class Configuration {
    
    /*DATABASE CONFIGURATION*/
    /*Costante che mi permette di scegliere il DB da usare. Tale stringa è contenuta
    nel DAOFactory corrispondente al DB che voglio usare*/
    
    public static final String DAO_IMPL=JDBCDAOFactory.DB2JDBCIMPL;

    /*Scelta del Driver*/
    public static final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
    public static final String DB2_DRIVER="com.ibm.db2.jcc.DB2Driver";
    /*Scelta Timezone*/
    public static final String SERVER_TIMEZONE=Calendar.getInstance().getTimeZone().getID();
    /*Configurazione URL database*/
    public static final String MYSQL_URL="jdbc:mysql://localhost/hsDB?user=root&password=&serverTimezone="+SERVER_TIMEZONE;
    public static final String DB2_URL="jdbc:db2://localhost:50000/testdb";
    public static final String DB2_USER="DB2INST1";
    public static final String DB2_PASSWORD="passw0rd";
    
    /*LOGGER FILES CONFIGURATION*/
    public static final String LOG_NAME="HandallShop_log.%g.%u.txt";
    public static final String DIR_LOG_WINDOWS="C:\\Users\\Riccardo\\Documents\\Progetto\\logs\\" + LOG_NAME;
    public static final String DIR_LOG_LINUX="/home/alessandro/Documenti/HandballShop/HandballShop/Logs/" + LOG_NAME;
    public static final Level GLOBAL_LOGGER_LEVEL=Level.ALL;
    
}