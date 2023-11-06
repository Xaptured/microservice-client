package com.thejackfolio.microservices.clientapi.clients;

import com.thejackfolio.microservices.clientapi.models.ClientComments;
import com.thejackfolio.microservices.clientapi.models.ClientCredential;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "DATABASE-SERVICE")
public interface TheJackFolioDBClient {

    @PostMapping("/clients/save-comments")
    public ResponseEntity<ClientComments> saveComments(@RequestBody ClientComments comments);

    @GetMapping("/clients/get-comments")
    public ResponseEntity<List<ClientComments>> getComments();

    @GetMapping("/clients/get-comments/{commentId}")
    public ResponseEntity<ClientComments> getCommentById(@PathVariable Integer commentId);

    @PostMapping("/clients/update-comments/{commentId}")
    public ResponseEntity<ClientComments> updateComments(@RequestBody ClientComments comments, @PathVariable Integer commentId);

    @PostMapping("/clients/save-credentials")
    public ResponseEntity<ClientCredential> saveClientCredential(@RequestBody  ClientCredential credential);

    @GetMapping("/clients/get-credentials/{email}")
    public ResponseEntity<ClientCredential> findClientCredential(@PathVariable String email);
}
