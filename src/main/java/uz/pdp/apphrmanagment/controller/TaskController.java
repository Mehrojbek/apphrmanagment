package uz.pdp.apphrmanagment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.EditingTaskDto;
import uz.pdp.apphrmanagment.payload.ReseiveTaskDto;
import uz.pdp.apphrmanagment.payload.TaskDto;
import uz.pdp.apphrmanagment.service.TaskService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/add")
    public HttpEntity<?> add(@RequestBody @Valid TaskDto taskDto){
        ApiResponse apiResponse=taskService.add(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }


    @PostMapping("/reseive")
    public HttpEntity<?> reseiveTask(@RequestBody @Valid ReseiveTaskDto reseiveTaskDto){
        ApiResponse apiResponse = taskService.reseiveTask(reseiveTaskDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody EditingTaskDto editingTaskDto){
        ApiResponse apiResponse = taskService.edit(id, editingTaskDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    //VAZIFANI BAJARUVCHILAR UCHUN
    @PutMapping("/worker/{id}")
    public HttpEntity<?> complete(@PathVariable UUID id){
        ApiResponse apiResponse = taskService.complete(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


    //BARCHA TASKLARNI KO'RISH
    @GetMapping
    public HttpEntity<?> getAll(){
        List<Object> objectList = taskService.getAll();
        return ResponseEntity.ok(objectList);
    }

}
