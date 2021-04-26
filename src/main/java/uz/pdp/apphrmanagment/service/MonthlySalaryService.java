package uz.pdp.apphrmanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagment.component.AllNeedfullMethod;
import uz.pdp.apphrmanagment.entity.MonthlySalary;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.entity.enums.MonthName;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.MonthlySalaryDto;
import uz.pdp.apphrmanagment.repository.MonthlySalaryRepository;
import uz.pdp.apphrmanagment.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class MonthlySalaryService {
    @Autowired
    AllNeedfullMethod allNeedfullMethod;
    @Autowired
    UserRepository userRepository;

    @Autowired
    MonthlySalaryRepository monthlySalaryRepository;

    public ApiResponse add(MonthlySalaryDto monthlySalaryDto) {

        User user = allNeedfullMethod.getUserFromPrincipal();
        if (user == null)
            return new ApiResponse("Xatolik", false);

        byte roleNumber = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        Optional<User> optionalWorker = userRepository.findById(monthlySalaryDto.getUserId());
        if (!optionalWorker.isPresent())
            return new ApiResponse("Ishchi topilmadi", false);
        User worker = optionalWorker.get();

        //OYLIK BELGILAYOTGAN USER DIRECTOR YOKI SHU ISHCHINI QO'SHGAN USER BO'LISHI MUMKIN
        if (roleNumber == 2 || worker.getCreatedBy().equals(user.getId())) {
            MonthlySalary monthlySalary = new MonthlySalary();
            monthlySalary.setAmount(monthlySalaryDto.getAmount());
            monthlySalary.setUser(worker);
            monthlySalary.setMonthName(MonthName.valueOf(monthlySalaryDto.getMonthName()));
            monthlySalaryRepository.save(monthlySalary);

            String message = "Sizga "+monthlySalaryDto.getAmount()+" miqdorida maosh, "+monthlySalaryDto.getMonthName()+"oyi uchun belgilandi";
            String subject = "Oylik belgilandi";
            allNeedfullMethod.sendEmail(worker.getEmail(), message,false,subject);
            return new ApiResponse("Muvaffaqiyatli bajarildi",true);
        }

        //BOSHQA HAR QANDAY HOLATDA XATOLIK
        return new ApiResponse("Xatolik",false);
    }


    public ApiResponse edit(UUID id, MonthlySalaryDto monthlySalaryDto){
        User user = allNeedfullMethod.getUserFromPrincipal();
        if (user == null)
            return new ApiResponse("Xatolik",false);

        byte roleNumber = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        Optional<MonthlySalary> optionalMonthlySalary = monthlySalaryRepository.findById(id);
        if (!optionalMonthlySalary.isPresent())
            return new ApiResponse("Oylik maoshi topilmadi",false);
        MonthlySalary monthlySalary = optionalMonthlySalary.get();

        if (roleNumber == 2 || user.getId().equals(monthlySalary.getCreatedBy())){

        }

        return null;
    }
}
