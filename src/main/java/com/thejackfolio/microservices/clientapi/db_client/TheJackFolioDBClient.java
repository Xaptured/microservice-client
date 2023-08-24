package com.thejackfolio.microservices.clientapi.db_client;

import com.thejackfolio.microservices.clientapi.models.ClientComments;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "DATABASE-SERVICE")
public interface TheJackFolioDBClient {

    @PostMapping("/clients/save-comments")
    public ResponseEntity<ClientComments> saveComments(@RequestBody ClientComments comments);

    @GetMapping("/clients/get-comments")
    public ResponseEntity<List<ClientComments>> getComments();
}
