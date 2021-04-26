package uz.pdp.apphrmanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagment.component.AllNeedfullMethod;
import uz.pdp.apphrmanagment.entity.InOut;
import uz.pdp.apphrmanagment.entity.MonthlySalary;
import uz.pdp.apphrmanagment.entity.Task;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.entity.enums.MonthName;
import uz.pdp.apphrmanagment.entity.enums.TaskStatus;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.GetSalaryDto;
import uz.pdp.apphrmanagment.payload.WorkerDto;
import uz.pdp.apphrmanagment.repository.InOutRepository;
import uz.pdp.apphrmanagment.repository.MonthlySalaryRepository;
import uz.pdp.apphrmanagment.repository.TaskRepository;
import uz.pdp.apphrmanagment.repository.UserRepository;

import java.util.*;

@Service
public class LidershipService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AllNeedfullMethod allNeedfullMethod;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    InOutRepository inOutRepository;
    @Autowired
    MonthlySalaryRepository monthlySalaryRepository;

    public ApiResponse getAllWorker() {


        User user = allNeedfullMethod.getUserFromPrincipal();
        if (user == null)
            return new ApiResponse("Xatolik",false);

        byte roleNumber = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        //DIRECTOR BARCHA XODIMLARNI KO'RA OLADI
        if (roleNumber == 2)
            return new ApiResponse("Muvaffaqiyatli bajarildi",true,userRepository.findAll());

        //KIMNI QO'SHGAN BO'LSA SHU XODIMLAR RO'YXTINI QAYTARAMIZ
        if (roleNumber == 1)
            return new ApiResponse("Muvaffaqiyatli bajarildi",true,userRepository.findAllByCreatedBy(user.getId()));

        return new ApiResponse("Xatolik",false);
    }


    public ApiResponse getOneWorker(UUID id) {
        //TIZIMGA KIRGAN USER NI ANIQLASH
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails))
            return new ApiResponse("Xatolik", false);

        User user = (User) principal;

        //USER ROLINI ANIQLASH
        byte roleNumber = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        if (roleNumber == 1 || roleNumber == 2) {

            Optional<User> optionalWorker = userRepository.findById(id);
            if (!optionalWorker.isPresent())
                return new ApiResponse("Ishchi topilmadi", false);

            User worker = optionalWorker.get();

            //DIRECTOR XOHLAGAN ISHCHI HAQIDAGI MA'LUMOTNI OLISHI MUMKIN
            if (roleNumber == 2)
                return new ApiResponse("Muvaffaqiyatli bajarildi", true, worker);

            //MANAGER ESA O'ZI QO'SHGAN ISHCHI HAQIDA MA'LUMOTNI OLISHI MUMKIN
            if (worker.getCreatedBy().equals(user.getId()))
                return new ApiResponse("Muvaffaqiyatli bajarildi", true, worker);
        }
        //BOSHQA HAR QANDAY HOLATDA XATOLIK
        return new ApiResponse("Xatolik", false);
    }


    public ApiResponse getInfoOfWorker(UUID id, WorkerDto workerDto) {

        User user = allNeedfullMethod.getUserFromPrincipal();
        if (user == null)
            return new ApiResponse("Xatolik",false);

        Optional<User> optionalWorker = userRepository.findById(id);
        if (!optionalWorker.isPresent())
            return new ApiResponse("ishchi topilmadi",false);
        User worker = optionalWorker.get();

        byte roleNumber = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        if (roleNumber == 2 || worker.getCreatedBy().equals(user.getId())){
            List<Task> tasks = taskRepository.getAllTasksByWorkerIdAndTime(user.getId(), workerDto.getFrom(), workerDto.getTo());
            List<InOut> inOuts = inOutRepository.getAllInOutsByWorkerIdAndTime(user.getId(), workerDto.getFrom(), workerDto.getTo());
            List<Object> objectArrayList = new ArrayList<>(tasks);
            objectArrayList.addAll(inOuts);
            return new ApiResponse("Muvaffaqiyatli bajarildi",true,objectArrayList);
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
                salaryList = monthlySalaryRepository.findAllByWorkerIdAndMonthName(worker.getId(), MonthName.valueOf(getSalaryDto.getMonthName()));
            }
            //AGAR WORKER_ID BERILMAGAN BO'LSA BARCHA XODIMLARGA BERILGAN OYLIKLAR RO'YXATI KETADI
            salaryList = monthlySalaryRepository.findAllByMonthName(MonthName.valueOf(getSalaryDto.getMonthName()));
            return new ApiResponse("Muvaffaqiyatli bajarildi",true,salaryList);
        }
        return new ApiResponse("Xatolik",false);
    }


    public ApiResponse getCompleteTasks(){
        User user = allNeedfullMethod.getUserFromPrincipal();
        if (user == null)
            return new ApiResponse("Xatolik",false);

        byte roleNumber = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        //DIRECTOR BARCHA BAJARILGAN VAZIFALARNI KO'RA OLADI
        if (roleNumber == 2){
            List<Task> taskList = taskRepository.findAllByStatus(TaskStatus.STATUS_DONE);
            return new ApiResponse("Muvaffaqyatli bajarildi",true,taskList);
        }

        //MANAGER O'ZI YARATGAN VA BAJARILGAN VAZIFALARNI KO'RA OLADI
        if (roleNumber == 1){
            List<Task> taskList = taskRepository.findAllByCreatedByAndStatus(user.getId(), TaskStatus.STATUS_DONE);
            return new ApiResponse("Muvaffaqiyatli bajarildi",true,taskList);
        }
        return new ApiResponse("Xatolik",false);
    }
}
