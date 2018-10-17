package bcntec.learning.bigdata.hbase;

import bcntec.learning.bigdata.hbase.HBaseUtils;
import com.google.protobuf.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.io.compress.Compression;

import java.io.IOException;

@Slf4j
public class HBaseAlterTable {

    public static void main(String[] args) throws IOException, ServiceException {
        Configuration config = HBaseUtils.createConfiguration();
        try (Connection connection = ConnectionFactory.createConnection(config)) {
            modifySchema(config, "MY_FIRST_TABLE","DEFAULT_FAMILY","NEW_FAMILY");
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
            newColumn.setCompactionCompressionType(Compression.Algorithm.GZ);
            newColumn.setMaxVersions(HConstants.ALL_VERSIONS);
            admin.addColumn(tableName2, newColumn);

            // Update existing column family
            HColumnDescriptor existingColumn = new HColumnDescriptor(familyName);
            existingColumn.setCompactionCompressionType(Compression.Algorithm.GZ);
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
