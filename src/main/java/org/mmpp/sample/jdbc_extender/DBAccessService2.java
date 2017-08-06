package org.mmpp.sample.jdbc_extender;

import org.springframework.stereotype.Service;

/**
 * DBアクセスするサービス 2
 */
@Service
public class DBAccessService2 extends AbstractJDBCExtender{
    public DBAccessService2() {
        super("sql/service2.properties");
    }

}
