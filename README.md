# sudie-mesh
RPC调用框架

一个集群中心帮你打工，Consumer、Provider都直接注册在上面，集群中心带有内存队列，端注册中心，为服务端缓冲，等结果池产生数据则触发返回。

扩展点:
1. 服务全队列化，可以紊流，限流，熔断
2. 服务状态数据采集、服务流量、响应事件评估服务可靠度、状态注册展示
3. 服务情况全链路跟踪，反馈响应时间等
4. 动态伸缩注册

### 代码结构说明

- sudie-consumer  
RPC框架中的服务调用者、顾客(Request请求发出方)

- sudie-mesh-center  
核心调度模块，自组网，自均衡，接收请求，发送到服务提供侧

- sudie-mesh-common  
通用类库、结构声明

- sudie-mesh-decode  
自定义socket报文解析模块

- sudie-mesh-encode  
自定义socket报文组装模块

- sudie-provider  
RPC框架中的服务提供者，接收Request请求，进行方法调用，返回结果





### 2022-03-29

着手开发了去中心化的集群核心，核心初始化时基于集群声明地址进行通信网构建(Socket)机制，心跳维持通信网。
每个集群中都有一个内存队列，通过client方式推送到任意节点时，节点会均匀发送消息到各个节点中，并递归触发队列消费，构成一个生产、消费机制。

*todo:* 
1. 报文断帧处理(客户端断帧队列，重组)
2. RPC机制尝试实现，先基于SerializationUtils实现传输功能，consumer -> cluster-center -> provider -> cluster-center -> consumer
3. 构建结果池，服务链路追踪标识，REQ-RES模式或异步Task进度模式的请求链路
4. 节点中心添加扩展点机制，外部加载。(构思基于socket指令)

### 2022-04-08

已经基于FTS(Java)序列化机制，实现序列化反序列化的调用远程调用。
已实现基于Socket的通信状态NIO模式、对Provider、Consumer的发送进行动态调控(自适应、自识别)，组成服务网。
已基本实现报文断帧重组的需求。
调用链路已经可以做到单向 consumer ->  cluster-center -> provider 的调用，下一步将结果写入结果池，返回到consumer链路

*todo:*

1. 结果反馈至consumer (包含抛出异常)
2. ack机制设计，consumer -> cluster-center 防止链路中请求丢失。
3. 链路标识设计，方便进行多服务链路跟踪，设计consumer的异步与同步模式。  
……
