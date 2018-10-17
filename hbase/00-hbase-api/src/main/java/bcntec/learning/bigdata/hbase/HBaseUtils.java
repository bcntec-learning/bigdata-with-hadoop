package bcntec.learning.bigdata.hbase;

import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/**
 * @author fphilip@houseware.es
 */
public class HBaseUtils {

    public static Configuration createConfiguration() throws IOException, ServiceException {
        Configuration config = HBaseConfiguration.create();

        String path = Thread.currentThread().getContextClassLoader().getResource("hbase-site.xml").getPath();

        config.addResource(new Path(path));

        HBaseAdmin.checkHBaseAvailable(config);
        return config;

    }
}
