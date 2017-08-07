package org.mmpp.sample.jdbc_extender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;


/**
 * DBにアクセスする抽象サービス
 */
abstract public class AbstractJDBCExtender {
    private Logger logger = LoggerFactory.getLogger(DBAccessService.class);

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 設定ファイル
     */
    private final String configFile;

    /**
     * 設定
     */
    private Properties configProperties = null;


    public AbstractJDBCExtender(String filepath) {
        this.configFile = filepath;
    }

    /**
     * コンフィグを読み取ります
     * @return コンフィグ
     */
    private Properties getConfig(){
        if(configProperties != null){
            return configProperties;
        }
        configProperties = new Properties();
        Resource resource = resourceLoader.getResource("classpath:" + this.configFile);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

            configProperties.load(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return configProperties;
    }

    /**
     * サービス開始
     */
    public void start() {
        logger.info(getConfig().getProperty("name"));

        // 実行するSQLの格納しているファイルを抽出します
        String sqlFilePath;
        try {
            sqlFilePath = getConfig().getProperty("query");
        } catch (NullPointerException e) {
            NullPointerException nullPointerException = new NullPointerException("Query Fileが取得できません");
            nullPointerException.addSuppressed(e);
            throw nullPointerException;
        }

        // SQLファイルからSQLを読み取ります
        try {
            String query = readFile(sqlFilePath);
            logger.info("  query : " + query);

            // 実行します
            List<Customer> results = jdbcTemplate.query(query,new BeanPropertyRowMapper<>(Customer.class));

            // 繰り返します
            for(Customer customer : results){
                logger.info("  result : " + customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 指定ファイルを読み取ります
     * @param filePath 読み取りファイル
     * @return ファイル内容
     */
    private String readFile(String filePath)throws IOException{
        if (filePath == null) {
            throw new NullPointerException("Fileが取得できません");
        }

        StringBuffer fileData ;
        BufferedReader bufferedReader = null;
        try {
            // ClassPathのリソースを読み取ります
            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            this.getClass().getClassLoader().getResourceAsStream(filePath)));
            fileData = new StringBuffer();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if(fileData.length() != 0){
                    fileData.append(System.getProperty("line.separator"));
                }
                fileData.append(line);
            }

            return fileData.toString();
        } finally {
            if (bufferedReader != null) {
                try {  bufferedReader.close();  } catch (IOException e) { }
            }
        }
    }
}
