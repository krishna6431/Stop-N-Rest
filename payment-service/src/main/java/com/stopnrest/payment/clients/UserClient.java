package com.stopnrest.payment.clients;

import com.stopnrest.payment.dto.CreditCardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8084")
public interface UserClient {
    @GetMapping("/api/v1/users/{username}/credit-cards")
    List<CreditCardDTO> getCreditCards(@PathVariable("username") String username, @RequestHeader("Authorization")String authHeader);
}



