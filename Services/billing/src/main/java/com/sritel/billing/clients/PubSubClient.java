package com.sritel.billing.clients;

import com.sritel.billing.dto.PublishRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pub-sub")
public interface PubSubClient {

    @PostMapping("api/billing/publish")
    void publish(@RequestBody PublishRequest publishRequest);

}
