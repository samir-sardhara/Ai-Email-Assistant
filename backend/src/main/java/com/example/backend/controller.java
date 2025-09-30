package com.example.backend;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class controller {
    public EmailGeneratorServices emailGeneratorServices;



    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){
       String response =  emailGeneratorServices.genereteEmailReplay(emailRequest);
        return ResponseEntity.ok(response);
    }
}
