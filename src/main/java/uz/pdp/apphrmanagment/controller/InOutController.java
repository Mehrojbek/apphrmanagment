package uz.pdp.apphrmanagment.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.apphrmanagment.payload.InOutDto;
import uz.pdp.apphrmanagment.service.InOutService;

@RestController
@RequestMapping("/api/inOut") //BU YO'LGA FAQAT QURILMA TOMONIDAN MUROJAAT QILINADI
public class InOutController {
    @Autowired
    InOutService inOutService;

    @PostMapping
    public void inOutWriter(@RequestBody InOutDto inOutDto){
        inOutService.inOutWriter(inOutDto);
    }
}
