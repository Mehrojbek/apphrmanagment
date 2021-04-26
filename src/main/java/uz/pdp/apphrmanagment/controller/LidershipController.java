package uz.pdp.apphrmanagment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.GetSalaryDto;
import uz.pdp.apphrmanagment.payload.WorkerDto;
import uz.pdp.apphrmanagment.service.LidershipService;

import java.util.*;

@RestController
@RequestMapping("/api/leadership")
public class LidershipController {
    @Autowired
    LidershipService lidershipService;

    //GET ALL WORKER
    @GetMapping
    public HttpEntity<?> getAllWorker(){
        ApiResponse apiResponse = lidershipService.getAllWorker();
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }

    //GET ONE WORKER
    @GetMapping("/worker/{id}")
    public HttpEntity<?> getOneWorker(@PathVariable UUID id){
        ApiResponse apiResponse = lidershipService.getOneWorker(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }



    //COMPLETE BO'LGAN BARCHA VAZIFALAR
    @GetMapping("/completeTasks")
    public HttpEntity<?> getCompleteInDeadline(){
        ApiResponse apiResponse = lidershipService.getCompleteTasks();
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }


    /**
     * BITTA YOKI BARCHA ISHCHILARGA BELGILANGAN OY BO'YICHA BERILGAN OYLIKLAR RO'YXATI
     * @param getSalaryDto
     * @return
     */
    @PostMapping("/salary")
    public HttpEntity<?> getSalaryOfWorker(@RequestBody GetSalaryDto getSalaryDto){
        ApiResponse apiResponse = lidershipService.getSalaryOfWorker(getSalaryDto);
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
        ApiResponse apiResponse = lidershipService.getInfoOfWorker(id, workerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }



}
