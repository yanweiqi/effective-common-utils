# Copilot Instructions for effective-common-utils

## 项目概述 (Project Overview)

这是一个 Java 工具和示例代码的多模块 Maven 集合项目，用于学习和演示各种技术栈的实践用法。项目使用中文注释，主要面向教学和技术积累。

**关键特点:**
- 多模块 Maven 项目，每个模块独立演示特定技术
- Java 版本: Java 21 (根模块配置)，但各子模块可能使用 Java 8/11
- 中文注释和文档为主
- 以教学演示为目的，注重代码示例的完整性和可运行性

## 项目结构

### 活跃模块 (根 pom.xml 中启用)
- `effective-netty/` - Netty 网络编程示例 (BIO/NIO/AIO, HTTP/2, 集群, 心跳, Protobuf)
- `effective-flux/` - Spring WebFlux 响应式编程示例
- `effective-disruptor/` - LMAX Disruptor 高性能队列示例
- `effective-lambda/` - Java Lambda 表达式和函数式编程
- `effective-annotation/` - 注解处理和编译时代码生成
- `effective-etcd/` - etcd 分布式配置
- `effective-flow-control/` - 流量控制示例

### 非活跃模块 (已注释)
大部分模块在根 pom.xml 中被注释掉，包括 ActiveMQ、Akka、Spring Boot、Zookeeper 等。修改代码时注意模块激活状态。

### 特殊目录
- `effective-pom/` - 包含各技术栈的父 POM 定义 (Apache, Google, Netty, Spring Boot, JUnit5 等)
- `effective-activemq/` - 包含 7 个 Spring + ActiveMQ 示例子模块 (springMQ1-7)

## Maven 构建约定

### 编译器配置模式
项目中存在**两种编译器配置模式**，需注意一致性:

```xml
<!-- 模式 1: properties 方式 (推荐用于新模块) -->
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>

<!-- 模式 2: plugin configuration 方式 (遗留代码) -->
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```

**编辑 pom.xml 时:**
- 优先使用模式 1 (properties 方式)
- 如果模块已使用模式 2，保持一致性
- 根模块使用 Java 21，但子模块可能需要 Java 8/11 (特别是旧技术栈如 Spring 3.x)

### 依赖版本管理
- **无统一父 POM**: 各模块独立管理依赖版本
- **版本属性化**: 使用 `<properties>` 定义版本号 (如 `${netty.version}`)
- **常见依赖版本:**
  - Netty: 4.1.96.Final
  - Lombok: 1.18.30
  - Log4j2: 2.9.1
  - Spring Boot 3.x: 3.4.12 (新模块如 effective-flux)
  - Spring 3.x: 3.2.13.RELEASE (旧模块如 effective-activemq)

### 构建命令
```bash
# 仅构建指定模块 (推荐，因为根 pom.xml 只启用了部分模块)
mvn -pl effective-flux clean install
mvn -pl effective-netty clean install

# 运行测试
mvn -pl effective-flux test

# Spring Boot 模块启动
mvn -pl effective-flux spring-boot:run
```

## 代码规范

### 日志规范
- **首选方案**: 使用 Lombok 的 `@Slf4j` 注解 (见 `effective-netty-cluster` 所有类)
- **配置文件**: `log4j2.xml` 在 `src/main/resources/`
- **避免**: 直接使用 `System.out.println()` (除了快速测试)

```java
@Slf4j
public class ClusterBroker {
    public void start() {
        log.info("服务端启动...");
        log.error("启动失败! cause={}", ex.getMessage(), ex);
    }
}
```

### 测试代码模式
项目中存在**三种测试模式**:

1. **JUnit 5 标准测试** (`effective-flux`):
```java
@SpringBootTest
public class FluxDemoControllerTest {
    @Test
    void testFlux() { /* ... */ }
}
```

2. **可执行的测试类** (带 main 方法，用于教学演示):
```java
public class SamplesFluxTest {
    @Test
    public void testBackpressure() { /* ... */ }
    
    public static void main(String[] args) {
        // 可直接运行演示
    }
}
```

3. **混合模式** (`src/main/java` 中包含 `@Test` 注解):
   - 见 `effective-flux/src/main/java/com/effective/common/flux/Samples*.java`
   - 这是为了方便作为示例代码运行，非标准做法

### 服务器/客户端模式
Netty 示例遵循固定模式 (见 `effective-netty-base`):

```java
// 服务端
public class TimeServer {
    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChildChannelHandler());
            // ...
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) { /* 启动入口 */ }
}

// 客户端
public class TimeClient {
    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() { /* ... */ });
            // ...
        } finally {
            group.shutdownGracefully();
        }
    }
}
```

### 命名约定
- **包名**: `com.effective.common.<module>` (如 `com.effective.common.netty.cluster`)
- **类名**: 
  - Server/Client 后缀用于网络程序
  - Handler 后缀用于 Netty ChannelHandler
  - Test 后缀用于测试类
  - Application/App 后缀用于 Spring Boot 启动类
- **注释**: 使用中文注释解释业务逻辑和技术要点

## 响应式编程 (effective-flux)

### WebFlux REST API 模式
```java
@RestController
public class FluxDemoController {
    @GetMapping("/demo/flux")
    public Flux<String> getFluxDemo() {
        return Flux.just("Hello", "Reactive", "World");
    }
}
```

### 测试响应式流
使用 `StepVerifier` 进行测试 (见 `SamplesFluxTest.java`):
```java
@Test
public void testFlux() {
    Flux<Integer> flux = Flux.range(1, 5);
    StepVerifier.create(flux)
        .expectNext(1, 2, 3, 4, 5)
        .verifyComplete();
}
```

## Netty 架构模式

### 模块分层 (effective-netty)
- `effective-netty-common/` - 通用工具和基础类
- `effective-netty-base/` - BIO/NIO/AIO 基础示例
- `effective-netty-cluster/` - 集群通信框架 (Broker, 序列化, 压缩, 协议)
- `effective-netty-heartbeat/` - 心跳和连接管理
- `effective-netty-protobuf/` - Protocol Buffers 集成
- `effective-netty-http2/` - HTTP/2 示例

### Cluster 模块核心组件
- **Broker 抽象**: `ClusterBroker` (服务端), `BrokerClient` (客户端)
- **序列化策略**: `SerializationSelector` 支持 Protostuff, JSON
- **压缩策略**: `CompressionSelector` 支持 Gzip, LZ4
- **协议层**: `CommandProtocol`, `Header`, `RequestCode/ResponseCode`
- **编解码器**: `CommandEncoder`, `CommandDecoder`

## 常见工作流

### 添加新的技术示例模块
1. 在根目录创建 `effective-<tech>/` 目录
2. 创建 pom.xml (参考 `effective-flux/pom.xml` 结构)
3. 使用 Java 21 和最新版本的依赖
4. 在根 pom.xml 的 `<modules>` 中取消注释或添加
5. 创建 README.md 说明模块用途和运行方式 (中文)
6. 创建 `src/main/java` 和 `src/test/java` 标准结构

### 更新依赖版本
1. 检查 `effective-pom/` 中是否有对应的父 POM
2. 更新子模块 pom.xml 中的版本属性
3. 运行 `mvn -pl <module> clean test` 验证兼容性
4. 注意 Spring 3.x 等旧版本模块需保持在 Java 8

### 调试 Netty 程序
1. 先启动 Server (运行 `*Server.main()`)
2. 再启动 Client (运行 `*Client.main()`)
3. 检查 log4j2.xml 日志级别 (默认 INFO)
4. 常见端口: 8080, 8090 (集群示例)

## 关键文件位置

- Maven 配置: `/pom.xml` (根), `<module>/pom.xml` (子模块)
- 日志配置: `<module>/src/main/resources/log4j2.xml`
- Spring 配置: `<module>/src/main/resources/application.properties`
- 响应式示例: `effective-flux/src/test/java/com/effective/common/flux/SamplesFluxTest.java`
- Netty 集群示例: `effective-netty/effective-netty-cluster/src/main/java/com/effective/common/netty/cluster/broker/`

## 注意事项

- **中文优先**: 注释、文档、日志消息使用中文
- **可运行性**: 示例代码应包含 main 方法，能独立运行演示
- **模块隔离**: 不同模块之间无依赖关系，独立构建
- **版本兼容**: 注意 Java 版本和框架版本的匹配 (Spring 3.x 需 Java 8)
- **测试位置**: 有些测试代码在 `src/main/java` 中 (教学需要)，非标准但符合项目风格
