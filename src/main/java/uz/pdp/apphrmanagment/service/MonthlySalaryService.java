package uz.pdp.apphrmanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagment.component.AllNeedfullMethod;
import uz.pdp.apphrmanagment.entity.Month;
import uz.pdp.apphrmanagment.entity.MonthlySalary;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.entity.enums.MonthName;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.GetSalaryDto;
import uz.pdp.apphrmanagment.payload.MonthlySalaryDto;
import uz.pdp.apphrmanagment.repository.MonthRepository;
import uz.pdp.apphrmanagment.repository.MonthlySalaryRepository;
import uz.pdp.apphrmanagment.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MonthlySalaryService {
    @Autowired
    AllNeedfullMethod allNeedfullMethod;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MonthRepository monthRepository;

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
            monthlySalary.setWorker(worker);
            monthlySalary.setMonth(monthRepository.getOne(monthlySalaryDto.getMonthId()));
            monthlySalaryRepository.save(monthlySalary);

            Month month = monthRepository.getOne(monthlySalaryDto.getMonthId());

            String message = "Sizga "+monthlySalaryDto.getAmount()+" miqdorida maosh, "+month.getMonthName()+"oyi uchun belgilandi";
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
            Optional<User> optionalWorker = userRepository.findById(monthlySalaryDto.getUserId());
            if (!optionalWorker.isPresent())
                return new ApiResponse("Ishchi topilmadi",false);

            monthlySalary.setAmount(monthlySalaryDto.getAmount());
            monthlySalary.setMonth(monthRepository.getOne(monthlySalaryDto.getMonthId()));
            monthlySalary.setWorker(optionalWorker.get());

            monthlySalaryRepository.save(monthlySalary);
            return new ApiResponse("Muvaffaqiyatli tahrirlandi",true);
        }

        return new ApiResponse("Xatolik",false);
    }



    public ApiResponse getSalaryOfWorker(GetSalaryDto getSalaryDto){
        User user = allNeedfullMethod.getUserFromPrincipal();
        if (user == null)
            return new ApiResponse("Xatolik",false);

        byte roleNumber = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        Optional<User> optionalWorker = userRepository.findById(getSalaryDto.getWorkerId());
        if (!optionalWorker.isPresent())
            return new ApiResponse("Ishchi toplimadi",false);

        User worker = optionalWorker.get();

        //DIRECTOR YOKI USHBU WORKER NI QO'SHGAN MANAGER BO'LSA
        if (roleNumber == 2 || worker.getCreatedBy().equals(user.getId())){
            List<MonthlySalary> salaryList;
            //AGAR WORKER_ID BERILGAN BO'LSA
            if (getSalaryDto.getWorkerId() != null) {
                salaryList = monthlySalaryRepository.findAllByWorkerIdAndMonth_Id(getSalaryDto.getWorkerId(),getSalaryDto.getMonthId());
            }
            //AGAR WORKER_ID BERILMAGAN BO'LSA BARCHA XODIMLARGA BERILGAN OYLIKLAR RO'YXATI KETADI
            salaryList = monthlySalaryRepository.findAllByMonth_Id(getSalaryDto.getMonthId());
            return new ApiResponse("Muvaffaqiyatli bajarildi",true,salaryList);
        }
        return new ApiResponse("Xatolik",false);
    }

}
