package com.thejackfolio.microservices.clientapi.db_client;

import com.thejackfolio.microservices.clientapi.models.ClientComments;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ClientComments-db-client",url = "http://localhost:8080/clients")
public interface TheJackFolioDBClient {

    @PostMapping("/save-comments")
    public ResponseEntity<ClientComments> saveComments(@RequestBody ClientComments comments);

    @GetMapping("/get-comments")
    public ResponseEntity<List<ClientComments>> getComments();
}
