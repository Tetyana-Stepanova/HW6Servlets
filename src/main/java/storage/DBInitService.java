package storage;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.flywaydb.core.Flyway;

public class DBInitService {
    public void initDb() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(StorageConstants.CONNECTION_URL);
        dataSource.setUser(StorageConstants.USERNAME);
        dataSource.setPassword(StorageConstants.PASSWORD);

        Flyway flyway = Flyway
                .configure()
                .dataSource(dataSource)
                .load();

        flyway.migrate();
    }
}
