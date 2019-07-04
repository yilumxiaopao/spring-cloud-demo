package com.cloud.consumer.feign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wei.li
 * on 2019/2/26
 */
//  value 是客户端名字 ，可以先发往网关cloud-zuul，再由网关找服务，或者直接写调用服务名cloud-service
@FeignClient(value = "cloud-geteWay", fallback = HelloServiceHystric.class)
public interface IHelloService {

    //  如果直接调用，配合上面的cloud-service， 这里是hello
    @RequestMapping(value = "api-a/hello", method = RequestMethod.GET)
    String hello(@RequestParam(value = "name") String name);
}
