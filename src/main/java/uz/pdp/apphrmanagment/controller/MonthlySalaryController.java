package uz.pdp.apphrmanagment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.MonthlySalaryDto;
import uz.pdp.apphrmanagment.service.MonthlySalaryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/monthlySalary")
public class MonthlySalaryController {
    @Autowired
    MonthlySalaryService monthlySalaryService;


    @PostMapping("/add")
    public HttpEntity<?> add(@RequestBody MonthlySalaryDto monthlySalaryDto){
        ApiResponse apiResponse = monthlySalaryService.add(monthlySalaryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody MonthlySalaryDto monthlySalaryDto){
        ApiResponse apiResponse = monthlySalaryService.edit(id, monthlySalaryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
