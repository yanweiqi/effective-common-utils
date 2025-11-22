# effective-flux 响应式编程演示模块

本模块基于 Project Reactor 和 Spring WebFlux，系统演示了响应式编程的核心用法，包括：

- 基础流式处理
- 背压（Backpressure）
- 错误处理
- 组合操作符（merge、zip、concat、combineLatest 等）
- 与 WebFlux 集成，响应式 REST API
- 单元测试与注释

## 主要文件说明

- `SamplesFluxTest.java`：响应式流核心案例，涵盖背压、错误处理、组合操作符等
- `FluxDemoController.java`：WebFlux 控制器，演示响应式 REST API
- `FluxDemoApplication.java`：WebFlux 启动类
- `FluxDemoControllerTest.java`：WebFlux 控制器单元测试

## 运行与测试

1. 运行所有测试：
   ```bash
   mvn -pl effective-flux test
   ```
2. 启动 WebFlux 服务：
   ```bash
   mvn -pl effective-flux spring-boot:run
   ```
   访问接口：`GET /demo/flux`

## 依赖说明

- Project Reactor
- Spring Boot WebFlux
- JUnit 5

## 适用场景

适合响应式编程入门、教学、功能演示及单元测试。

---
如需补充更多案例或文档，请随时联系！
