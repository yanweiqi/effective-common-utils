package com.effective.common.netty.cluster.constants;

import com.effective.common.netty.cluster.utils.UUIDUtil;
import com.effective.common.netty.cluster.utils.url.URLOption;

public class GatewayConstants {

    public static final String APP_NAME="Jdt-Iot-Broker";

    public static final String SYSTEM_NAME = "JDT-IOT : ";

    public static final String SYSTEM_JSF = "JSF : ";

    public static final String SYSTEM_ETCD = "ETCD : ";

    public static final String SYSTEM_PUBLISH = "Publish : ";

    public static final String SYSTEM_MQTT = "MQTT : ";

    public static final String SYSTEM_MQTT_START = "MQTT : START ";

    public static final String SYSTEM_MQTT_STOP = "MQTT : STOP ";

    public static final String SYSTEM_CORE = "CORE : ";

    public static final String CHANNEL_CLOSE_LISTENER = "ChannelCloseListener : ";

    public static final String CLIENT_REQUEST = "Client Request  : ";

    public static final String CLIENT_CONNECTION = "Client Conn : ";

    public static final String ADMIN_CONSOLE = "ADMIN CONSOLE :";

    public static final String CLIENT_DISCONNECTION = "Client DisConn : ";

    public static final String CLIENT_PING = "Client Ping : ";

    public static final String CONN_CHANNEL_IN_ACTIVE = "Client ChannelInActive : ";

    public static final String  BROADCAST_RECEIPT_MQ = "Broadcast Receipt MQ : ";

    public static final String CONN_USER_EVENT_TRIGGERED = "Client UserEventTriggered : ";

    public static final String CONN_EXCEPTION_CAUGHT = "Client ExceptionCaught : ";

    public static final String CLIENT_SUBSCRIPTION = "Client Sub : ";

    public static final String CLIENT_RECEIVE = "Client Rec : ";

    public static final String CLIENT_UN_SUB = "Client UnSub : ";

    public static final String RECEIVE_CLIENT_MSG = "Handler Forward  : ";

    public static final String CUSTOM_FILTER_KEY = "customFilters";

    public static final String CLASS_INFO = "classInfo";

    public static final String METHOD_INFO = "methodInfo";

    public static final String METHOD_CONFIG_INFO = "methodConfigInfo";

    public static final String SHORT_LINE = "-";

    public static final String VERSION = "version";

    public static final URLOption<String> ADDRESS_OPTION = new URLOption<>("address", "");

    public static final String REGISTRY_NAME_KEY = "registryName";

    public static final String USERNAME = "username";

    public static final String DEVICE = "device";

    public static final String PASSWORD = "password";

    /**
     * 当前所在文件夹地址
     */
    public final static String KEY_APPPATH = "appPath";

    public static final String JAVA_VERSION_KEY = "javaVersion";

    public static final String BUILD_VERSION_KEY = "buildVersion";

    /**
     * token
     */
    public final static String TOKEN = "token";

    /**
     * 上下文资源
     */
    public static final String CONTEXT_RESOURCE = "context.resource";

    public static final String SERVICE_SWITCHER = "serviceSwitcher";

    public static final String PROTOCOL_KEY = "protocol";

    public static final String ROOT = "root";

    public static final String REGION = "region";

    public static final String TOPIC = "topic";

    public static final String TOPIC_CONFIG_ID = "topicConfigId";

    public static final String CLIENT_ID = "clientId";

    public static final String MESSAGE_ID = "messageId";

    public static final String TOPIC_MESSAGE_QUEUE_ID = "topicMessageQueueId";

    public static final String MQTT = "mqtt";

    public static final String TCP = "tcp";

    public static final String LOCAL_ID = UUIDUtil.createUUID();

    public static final String LOCAL_IP = "localIp";

    public static final String LOCAL_URL = "localUrl";

    public static final String BROKERID = "brokerId";

    public static final String BROKER_META_MAP = "brokerMetaMap";

    public static final String LOCAL_BROKERID = "localBrokerId";

    public static final String APP = "app";

    public static final String APP_TOKEN = "appToken";

    public static final String APP_CODE = "appCode";

    public static final String APP_MANAGER = "appManager";

    public static final String CODE_TYPE = "codeType";

    public static final String CODE = "code";

    public static final String SUBSCRIBE = "subscribe";

    public static final String PRODUCE = "produce";

    public static final String APP_REGIONS = "appRegions";

    public static final String CLIENT = "client";

    public static final String SESSIONS = "sessions";

    public static final String PERSISTENCE = "persistence";

    public static final String CLUSTER_MODE = "clusterMode";

    public static final String COMMAND_NAME = "commandName";

    public static final String CONNECTION_INFO = "connectionInfo";

    public static final String TOPIC_CONFIGURATION_INFO = "topicConfigurationInfo";

    public static final String ES_MESSAGE_INDEX = "gateway-message";

    public static final String ES_CHANNEL_EVENT_INDEX = "gateway-channel-event";

    public static final String ES_CHANNEL_REALTIME_INDEX = "gateway-channel-realtime";

    public static final String TOPIC_CONFIG_MQEXPIRATIONTIME= "mqExpirationTime";

    public static final String TOPIC_CONFIG_MQFIXEDLENGTH= "mqFixedLength";

    public static final String OFF_LINE = "offline";

    public static final String ON_LINE = "online";

    public static final String DEVELOP = "develop";

    public static final String PRE = "pre";

    public static final String PRODUCTION = "production";
}
