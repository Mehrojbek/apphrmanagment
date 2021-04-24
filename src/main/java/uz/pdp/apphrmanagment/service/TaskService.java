package uz.pdp.apphrmanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagment.component.AllMethod;
import uz.pdp.apphrmanagment.entity.Task;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.entity.enums.TaskStatus;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.TaskDto;
import uz.pdp.apphrmanagment.repository.TaskRepository;
import uz.pdp.apphrmanagment.repository.UserRepository;

import java.util.HashSet;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AllMethod allMethod;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailService mailService;


    //ADD TASK
    public ApiResponse add(TaskDto taskDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails))
            return new ApiResponse("Xatolik",false);
        User user = (User) principal;

        //VAZIFA BERUVCHINING ROLI
        byte taskCreater = allMethod.getRoleNumber(user.getAuthorities());

        List<User> userList = userRepository.findAllById(taskDto.getUsers());

        //BAJARUVCHI KIMLIGINI ANIQLASH YO WORKER YO MANAGER LAR GURUHI YOKI BIRLIGI BO'LISHI MUMKIN
        byte performersRole = allMethod.getPerformers(userList);

        //MANAGER VA WORKER BIRGALIKDA KELMASLIGI KERAK WORKERLARGA VAZIFA BIRIKTIRILGANDA MANAGERNING VAZIFASI ULARNI NAZORAT QILISH
        if (performersRole==-1)
            return new ApiResponse("bir vazifani ham manager ham workerga topshirish mumkin emas",false);

        //VAZIFA BERUVCHI DIRECTOR BO'LSA
        if (taskCreater == 2)
           return saveTask(taskDto,userList);

        //VAZIFA BERUVCHI MANAGER BO'LSA
        if (taskCreater == 1){
            //MANAGER FAQAT WORKER LARGA VAZIFA BERISHI MUMKIN
            if (performersRole==0)
                return saveTask(taskDto,userList);
        }

        //BOSHQA HAR QANDAY HOLATDA XATOLIK YUZ BERADI
        return new ApiResponse("Xatolik",false);
    }






    public ApiResponse saveTask(TaskDto taskDto, List<User> userList){
        Task task = new Task();

        task.setName(taskDto.getName());
        task.setStatus(TaskStatus.STATUS_NEW);
        task.setDescription(taskDto.getDescription());
        task.setUsers(new HashSet<>(userList));
        task.setDeadline(taskDto.getDeadline());

        taskRepository.save(task);

        String message = "Sizga "+task.getName()+" nomli vazifa biriktirildi";
        String subject = "Yangi vazifa";
        for (User user : userList) {
            mailService.sendEmail(user.getEmail(), message, false, subject);
        }
        return new ApiResponse("Muvaffaqiyatli yaratildi",true);
    }
}
