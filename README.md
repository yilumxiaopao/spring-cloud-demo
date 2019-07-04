# spring-cloud-demo
简单的包含eureka service 网关 config的例子，学习记录用


1 cloud-eureka
@EnableEurekaServer

server.port=8761
eureka.instance.hostname=127.0.0.1
eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
spring.application.name=eurka-server
#不向注册中心注册自己
eureka.client.registerWithEureka=true
#禁止检索服务
eureka.client.fetchRegistry=false

http://localhost:8761/ 访问地址


2 CLOUD-SERVICE   注册自己为cloud-service
server.port: 8085
spring.application.name: cloud-service
eureka.client.serviceUrl.defaultZone: http://localhost:8761/eureka/

http://localhost:8085/hello



3 cloud-consumer   负载调用cloud-service，和熔断
@EnableDiscoveryClient  用于发现eureka服务
@EnableEurekaClient   自己也是eureka的服务
@EnableHystrix 开启熔断

localhost:8083/hello?name=test   会不断轮询机调用cloud-server

restTemplate.getForObject("http://CLOUD-SERVICE/hello?name="+name, String.class)
RestTemplate是 spring提供的用于访问Rest服务的客户端，只有restTemplate可以调用server名方式调用程序


负载策略
    ️RandomRule：随机策略会通过一个随机数从可用服务实例中选择一个实例。实现是用一个while循环，如果实例为空则执行随机选择，直到获取到。该策略有不严谨的地方，如果一直获取不到，很可能出现死循环的情况。
    ️RoundRobinRule：按照线性轮训的方式选择可用的实例。该策略定义了一个计数器的变量，每次循环后计数器累加    ，如果10次选择不到服务实例，则提示无可用服务。
    ️RetryRule：该策略增加了重试的机制，默认使用RoundRobinRule策略，如果超过时间阙值则返回null。
    ️WeightedResponseTimeRule：是在RoundRobinRule的基础上增加权重的策略，会根据实例的响应以及运行情况分配负载的权重，以便更好的利用资源，达到最优化。
    ️BestAvailableRule：遍历所有可用实例，过滤到故障实例，选择出请求并发数最小的一个。也就是说会优先选择空闲实例。




4  cloud-consumer-feign   声明性REST客户端Feign
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients   集成了Hystrix ，要在配置文件开启一下
feign.hystrix.enabled = true

//  value 是客户端名字 ，可以先发往网关cloud-zuul，再由网关找服务，或者直接写调用服务名cloud-service
@FeignClient(value = "cloud-zuul", fallback = HelloServiceHystric.class)
public interface IHelloService {

    //  如果直接调用，配合上面的cloud-service， 这里是hello
    @RequestMapping(value = "api-a/hello", method = RequestMethod.GET)
    String hello(@RequestParam(value = "name") String name);
}
localhost:8087/hello?name=111 首次测试，也是轮询机制的


5 cloud-zuul      网关 geteway 网关有更多断言
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableDiscoveryClient

localhost:8007/api-a/hello?name=test



6 config-server 可以取git得配置，当自身配置文件



7 config-client 
@Value("${foo}")
String foo;

在git上配置文件得指，被注入到foo中


