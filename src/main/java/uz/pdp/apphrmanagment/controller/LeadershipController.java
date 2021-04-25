package uz.pdp.apphrmanagment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.service.LidershipService;

import java.util.*;

@RestController
@RequestMapping("/api/leadership")
public class LeadershipController {
    @Autowired
    LidershipService lidershipService;

    //GET ALL WORKER
    @GetMapping
    public HttpEntity<?> getAllWorker(){
        List<User> allWorker = lidershipService.getAllWorker();
        return ResponseEntity.ok(allWorker);
    }

    //GET ONE WORKER
    @GetMapping("/{id}")
    public HttpEntity<?> getOneWorker(@PathVariable UUID id){
        ApiResponse apiResponse = lidershipService.getOneWorker(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }

    @PostMapping("/")
}
