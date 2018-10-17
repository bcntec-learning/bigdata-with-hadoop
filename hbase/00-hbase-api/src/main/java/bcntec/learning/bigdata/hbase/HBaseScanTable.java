package bcntec.learning.bigdata.hbase;

import bcntec.learning.bigdata.hbase.HBaseUtils;
import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class HBaseScanTable {


    public static void main(String[] args) throws IOException, ServiceException {
        Configuration config = HBaseUtils.createConfiguration();
        try (Connection connection = ConnectionFactory.createConnection(config)) {
            //todo en clase
            //todo en clase
            //todo en clase

        }

    }
}
