package uz.pdp.apphrmanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagment.entity.InOut;
import uz.pdp.apphrmanagment.entity.enums.InOutStatus;
import uz.pdp.apphrmanagment.payload.InOutDto;
import uz.pdp.apphrmanagment.repository.InOutRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class InOutService {
    @Autowired
    InOutRepository inOutRepository;

    public void inOutWriter(InOutDto inOutDto) {
        Timestamp now =Timestamp.valueOf(LocalDateTime.now());

        InOut inOut = new InOut();
        inOut.setStatus(InOutStatus.valueOf(inOutDto.getInOutStatus()));
        inOut.setTime(now);

        inOutRepository.save(inOut);
    }

}
