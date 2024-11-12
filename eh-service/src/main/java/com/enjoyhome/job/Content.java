package com.enjoyhome.job;

import lombok.Data;

import java.util.Map;

/**
 * AMQP消息body
 */
@Data
public class Content {

    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备ID
     */
    private String iotId;
    /**
     * 请求ID
     */
    private long requestId;
    private Map<String, Object> checkFailedData;

    /**
     * 产品key
     */
    private String productKey;
    private long gmtCreate;
    private String deviceName;
    private Map<String, Item> items;

    /**
     * "CurrentHumidity":{
     * "value":75,
     * "time":1699948275447
     * },
     */
    public class Item {
        // 属性值
        private int value;
        // 触发时间
        private long time;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}