package com.lnquy.kafka.connect.source;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class MySourceConfig extends AbstractConfig  {

    private static final String SOME_CONF = "source_config.some_conf";
    private static final String SOME_CONF_DOC = "A dummy config which do nothing";
    private static final String SOME_CONF_DEFAULT = "Something";

    public MySourceConfig(ConfigDef config, Map<String, String> parsedConfig) {
        super(config, parsedConfig);
    }

    public MySourceConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }

    public static ConfigDef conf() {
        return new ConfigDef()
                .define(SOME_CONF, ConfigDef.Type.STRING, SOME_CONF_DEFAULT, ConfigDef.Importance.HIGH, SOME_CONF_DOC);
    }

    public String getSomeConf() {
        return this.getString(SOME_CONF);
    }
}
