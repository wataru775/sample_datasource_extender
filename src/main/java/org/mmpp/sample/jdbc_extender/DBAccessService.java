package org.mmpp.sample.jdbc_extender;

import org.springframework.stereotype.Service;

/**
 * DBアクセスするサービス
 */
@Service
public class DBAccessService extends AbstractJDBCExtender{
    public DBAccessService() {
        super("sql/service1.properties");
    }
}
