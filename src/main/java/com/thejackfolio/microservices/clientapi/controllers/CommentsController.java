package com.thejackfolio.microservices.clientapi.controllers;

import com.thejackfolio.microservices.clientapi.exceptions.DataBaseOperationException;
import com.thejackfolio.microservices.clientapi.exceptions.MapperException;
import com.thejackfolio.microservices.clientapi.exceptions.ValidationException;
import com.thejackfolio.microservices.clientapi.models.ClientComments;
import com.thejackfolio.microservices.clientapi.services.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Comment", description = "Comment management APIs")
@RestController
@RequestMapping("/comments")
public class CommentsController {

    private CommentsService service;

    @Operation(
            summary = "Save comments",
            description = "Save comments and gives the same comments response with a message which defines whether the request is successful or not.",
            tags = { "comments", "post" }
    )
    @PostMapping("/save-comments")
    public ResponseEntity<ClientComments> saveComment(@RequestBody ClientComments comments){
        ClientComments response = null;
        try{
            response = service.saveComment(comments);
        } catch (ValidationException | MapperException | DataBaseOperationException exception){
            if(comments == null){
                comments = new ClientComments();
            }
            comments.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(comments);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get comments",
            description = "It gives the list of comments as response which are not replied yet with a message which defines whether the request is successful or not.",
            tags = { "comments", "get" }
    )
    @GetMapping("/get-comments")
    public ResponseEntity<List<ClientComments>> getComments(){
        List<ClientComments> comments = null;
        try{
            comments = service.getComments();
        } catch (ValidationException | MapperException | DataBaseOperationException exception){
            comments = new ArrayList<>();
            ClientComments comment = new ClientComments();
            comment.setMessage(exception.getMessage());
            comments.add(comment);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(comments);
        }
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }
}
