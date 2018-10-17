package bcntec.learning.bigdata.hbase.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;

import java.io.IOException;

@Slf4j
public class HBaseCreateTable {

    public static void main(String[] args) throws IOException {
        Configuration config = HBaseConfiguration.create();

        //Add any necessary configuration files (hbase-site.xml, core-site.xml)
        config.addResource(new Path(System.getenv("HBASE_CONF_DIR"), "hbase-site.xml"));
        config.addResource(new Path(System.getenv("HADOOP_CONF_DIR"), "core-site.xml"));
        createSchemaTables(config,"MY_FIRST_TABLE","DEFAULT_FAMILY");
        modifySchema(config, "MY_FIRST_TABLE","DEFAULT_FAMILY","NEW_FAMILY");

    }


    public static void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
    }

    public static void createSchemaTables(Configuration config, String tableName, String familyName) throws IOException {
        try (Connection connection = ConnectionFactory.createConnection(config);
             Admin admin = connection.getAdmin()) {

            HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableName));
            table.addFamily(new HColumnDescriptor(familyName).setCompressionType(Algorithm.NONE));

            log.info("Creating table. ");
            createOrOverwrite(admin, table);
            log.info(" Done.");
        }
    }

    public static void modifySchema(Configuration config, String tableName, String familyName, String newFamilyName) throws IOException {
        try (Connection connection = ConnectionFactory.createConnection(config);
             Admin admin = connection.getAdmin()) {

            TableName tableName2 = TableName.valueOf(tableName);
            if (!admin.tableExists(tableName2)) {
                log.info("Table does not exist.");
                System.exit(-1);
            }

            HTableDescriptor table = admin.getTableDescriptor(tableName2);

            // Update existing table
            HColumnDescriptor newColumn = new HColumnDescriptor(newFamilyName);
            newColumn.setCompactionCompressionType(Algorithm.GZ);
            newColumn.setMaxVersions(HConstants.ALL_VERSIONS);
            admin.addColumn(tableName2, newColumn);

            // Update existing column family
            HColumnDescriptor existingColumn = new HColumnDescriptor(familyName);
            existingColumn.setCompactionCompressionType(Algorithm.GZ);
            existingColumn.setMaxVersions(HConstants.ALL_VERSIONS);
            table.modifyFamily(existingColumn);
            admin.modifyTable(tableName2, table);

            // Disable an existing table
            admin.disableTable(tableName2);

            // Delete an existing column family
            admin.deleteColumn(tableName2, familyName.getBytes("UTF-8"));

            // Delete a table (Need to be disabled first)
            admin.deleteTable(tableName2);
        }
    }

}
