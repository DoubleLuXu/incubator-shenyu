/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.plugin.logging.elasticsearch.client;

import org.apache.shenyu.common.dto.PluginData;
import org.apache.shenyu.common.utils.GsonUtils;
import org.apache.shenyu.plugin.logging.common.entity.ShenyuRequestLog;
import org.apache.shenyu.plugin.logging.elasticsearch.config.LogCollectConfig;
import org.apache.shenyu.plugin.logging.elasticsearch.constant.LoggingConstant;
import org.apache.shenyu.plugin.logging.elasticsearch.utils.ElasticSearchLogCollectConfigUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * test cases for ElasticSearchLog.
 */
public class ElasticSearchLogCollectClientTest {

    private ElasticSearchLogCollectClient elasticSearchLogCollectClient;

    private PluginData pluginData = new PluginData();

    private Properties properties = new Properties();

    private LogCollectConfig.GlobalLogConfig globalLogConfig;

    private List<ShenyuRequestLog> logs = new ArrayList<>();

    private ShenyuRequestLog shenyuRequestLog = new ShenyuRequestLog();

    @BeforeEach
    public void setUp() {
        this.elasticSearchLogCollectClient = new ElasticSearchLogCollectClient();
        pluginData.setEnabled(true);
        pluginData.setConfig("{\"host\":\"localhost\", \"port\":\"9200\"}");
        globalLogConfig = GsonUtils.getInstance().fromJson(pluginData.getConfig(),
                LogCollectConfig.GlobalLogConfig.class);
        properties.setProperty(LoggingConstant.HOST, globalLogConfig.getHost());
        properties.setProperty(LoggingConstant.PORT, globalLogConfig.getPort());

        shenyuRequestLog.setClientIp("0.0.0.0");
        shenyuRequestLog.setPath("org/apache/shenyu/plugin/logging");
        logs.add(shenyuRequestLog);
    }

    @Test
    public void testConsume() {
        String msg = "";
        ElasticSearchLogCollectConfigUtils.setGlobalConfig(globalLogConfig);
        elasticSearchLogCollectClient.initClient(properties);
        try {
            elasticSearchLogCollectClient.consume(logs);
        } catch (Exception e) {
            msg = "false";
        }
        Assertions.assertEquals(msg, "");
        elasticSearchLogCollectClient.close();
    }

    @Test
    public void testCreateIndex() {
        ElasticSearchLogCollectConfigUtils.setGlobalConfig(globalLogConfig);
        elasticSearchLogCollectClient.initClient(properties);
        elasticSearchLogCollectClient.createIndex("test");
    }
}
