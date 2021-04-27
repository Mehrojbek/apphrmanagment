package uz.pdp.apphrmanagment.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.GetSalaryDto;
import uz.pdp.apphrmanagment.payload.WorkerDto;
import uz.pdp.apphrmanagment.service.LeadershipService;
import uz.pdp.apphrmanagment.service.MonthlySalaryService;
import uz.pdp.apphrmanagment.service.TaskService;

import java.util.*;

@RestController
@RequestMapping("/api/leadership")
public class LeadershipController {
    @Autowired
    LeadershipService leadershipService;
    @Autowired
    MonthlySalaryService monthlySalaryService;
    @Autowired
    TaskService taskService;


    //GET ALL WORKER
    @GetMapping
    public HttpEntity<?> getAllWorker(){
        ApiResponse apiResponse = leadershipService.getAllWorker();
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }

    //GET ONE WORKER
    @GetMapping("/worker/{id}")
    public HttpEntity<?> getOneWorker(@PathVariable UUID id){
        ApiResponse apiResponse = leadershipService.getOneWorker(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }



    //COMPLETE BO'LGAN BARCHA VAZIFALAR
    @GetMapping("/completeTasks")
    public HttpEntity<?> getCompleteInDeadline(){
        ApiResponse apiResponse = taskService.getCompleteTasks();
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }


    /**
     * BITTA YOKI BARCHA ISHCHILARGA BELGILANGAN OY BO'YICHA BERILGAN OYLIKLAR RO'YXATI
     * @param getSalaryDto
     * @return
     */
    @PostMapping("/salary")
    public HttpEntity<?> getSalaryOfWorker(@RequestBody GetSalaryDto getSalaryDto){
        ApiResponse apiResponse = monthlySalaryService.getSalaryOfWorker(getSalaryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }


    /**
     * BITTA ISHCHINI VAQT ORALIG'IDAGI BAJARGAN VAZIFALARI VA KELIB KETISHI
     * @param id
     * @param workerDto
     * @return ApiResponse
     */
    @PostMapping("/worker/{id}")
    public HttpEntity<?> getInfoOfWorker(@PathVariable UUID id,@RequestBody WorkerDto workerDto){
        ApiResponse apiResponse = leadershipService.getInfoOfWorker(id, workerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }



}
