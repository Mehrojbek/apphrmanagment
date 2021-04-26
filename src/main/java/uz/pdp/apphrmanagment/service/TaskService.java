package uz.pdp.apphrmanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagment.component.AllNeedfullMethod;
import uz.pdp.apphrmanagment.entity.Task;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.entity.enums.TaskStatus;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.payload.EditingTaskDto;
import uz.pdp.apphrmanagment.payload.TaskDto;
import uz.pdp.apphrmanagment.repository.TaskRepository;
import uz.pdp.apphrmanagment.repository.UserRepository;

import java.util.*;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AllNeedfullMethod allNeedfullMethod;
    @Autowired
    UserRepository userRepository;


    //ADD TASK
    public ApiResponse add(TaskDto taskDto) {
        User user = allNeedfullMethod.getUserFromPrincipal();
        if (user == null)
            return new ApiResponse("Xatolik", false);


        //VAZIFA BERUVCHINING ROLI
        byte taskCreater = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        Optional<User> optionalUser = userRepository.findById(taskDto.getPerformerId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Bajaruvchi toplimadi", false);

        //BAJARUVCHINI ROLINI ANIQLASH
        User performer = optionalUser.get();
        byte performerRole = allNeedfullMethod.getRoleNumber(performer.getAuthorities());

        //MANAGER VA WORKER BIRGALIKDA KELMASLIGI KERAK WORKERLARGA VAZIFA BIRIKTIRILGANDA MANAGERNING VAZIFASI ULARNI NAZORAT QILISH
        if (performerRole == -1)
            return new ApiResponse("Xatolik", false);

        //VAZIFA BERUVCHI DIRECTOR BO'LSA
        if (taskCreater == 2) //saveTask() NOMLI METHOD QOLGAN ISHLARNI BAJARADI
            return saveTask(taskDto, performer);

        //VAZIFA BERUVCHI MANAGER BO'LSA
        if (taskCreater == 1) {
            //MANAGER FAQAT WORKER LARGA VAZIFA BERISHI MUMKIN
            if (performerRole == 0)
                return saveTask(taskDto, performer);
        }

        //BOSHQA HAR QANDAY HOLATDA XATOLIK YUZ BERADI
        return new ApiResponse("Xatolik", false);
    }


    //RESEIVE TASK
    public ApiResponse reseiveTask(boolean accept, UUID taskId) {

        User user = allNeedfullMethod.getUserFromPrincipal();
        if (user == null)
            return new ApiResponse("Xatolik",false);

        Optional<Task> optionalTask = taskRepository.findByIdAndPerformerId(taskId);
        if (!optionalTask.isPresent())
            return new ApiResponse("Xatolik", false);

        Task task = optionalTask.get();
        //VAZIFA BERUVCHINI OLIB, UNGA XODIM VAZIFANI QABUL QILGAN YOKI QILMAGANLIGI HAQIDA XABAR YUBORAMIZ
        Optional<User> optionalCreater = userRepository.findById(task.getCreatedBy());
        if (!optionalCreater.isPresent())
            return new ApiResponse("Xatolik", false);
        User creater = optionalCreater.get();

        String message;
        String subject;
        //TIZIMDAGI USER SHU TASK NI BAJARUVCHISIMI
        if (task.getPerformer().equals(user)) {
            //USER TASKNI QABUL QILDI
            if (accept) {
                task.setAcceptedByPerformer(true);
                task.setStatus(TaskStatus.STATUS_PROGRESS);
                taskRepository.save(task);

                message = task.getPerformer().getFirstName() + " siz biriktirgan " + task.getName() + " vazifasini qabul qildi";
                subject = "Vazifa qabul qilindi";
                allNeedfullMethod.sendEmail(creater.getEmail(), message, false, subject);

                return new ApiResponse("Vazifani qabul qildingiz", true);
            }

            //USER TASKNI ABUL QILMADI
            message = task.getPerformer().getFirstName() + " siz biriktirgan " + task.getName() + " vazifasini qabul qilmadi";
            subject = "Vazifa qabul qilinmadi";
            allNeedfullMethod.sendEmail(creater.getEmail(), message, false, subject);

            return new ApiResponse("Vazifa rad etildi", true);
        }

        return new ApiResponse("Xatolik",false);
    }


    //EDIT TASK
    public ApiResponse edit(UUID id, EditingTaskDto editingTaskDto) {

        //SHU YO'LGA KELGAN VA VAZIFANI EDIT QILMOQCHI BO'LGAN USER
        User editorUser = allNeedfullMethod.getUserFromPrincipal();
        if (editorUser == null)
            return new ApiResponse("Xatolik", false);

        //BU USERNING ROLINI ANIQLASH
        byte roleNumber = allNeedfullMethod.getRoleNumber(editorUser.getAuthorities());

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Vazifa topilmadi", false);

        //EDIT QILINAYOTGAN VAZIFA
        Task editingTask = optionalTask.get();

        //VAZIFANI BAJARUVCHISI
        User performer = editingTask.getPerformer();

        Optional<User> optionalTaskCreater = userRepository.findById(editingTask.getCreatedBy());
        if (!optionalTaskCreater.isPresent())
            return new ApiResponse("Xatolik", false);

        //VAZIFANI YARATUVCHISI
        User taskCreater = optionalTaskCreater.get();

        String message;
        String subject;

            //AGAR EDITOR_USER USHBU VAZIFANI YARATUVCHISI YOKI DIRECTOR BO'LSA U VAZIFADAGI BARCHA FIELDLARNI O'ZGARTIRISHI MUMKIN
            if (editorUser.equals(taskCreater) || roleNumber == 2){
                editingTask.setName(editingTaskDto.getName());
                editingTask.setDescription(editingTaskDto.getDescription());
                editingTask.setDeadline(editingTaskDto.getDeadline());
                editingTask.setStatus(TaskStatus.valueOf(editingTaskDto.getStatus()));

                //AGAR VAZIFANI BAJARUVCHI XODIM ALMASHTIRILAYOTGAN BO'LSA
                if (!editingTaskDto.getPerformerId().equals(performer.getId())) {
                    Optional<User> optionalUser = userRepository.findById(editingTaskDto.getPerformerId());
                    if (!optionalUser.isPresent())
                        return new ApiResponse("Bajaruvchi topilmadi",false);
                    User newPerformer = optionalUser.get();
                    editingTask.setPerformer(newPerformer);
                    taskRepository.save(editingTask);

                    message = "Sizga " + editingTask.getName() + " nomli vazifa biriktirildi";
                    subject = "Yangi vazifa";
                    allNeedfullMethod.sendEmail(newPerformer.getEmail(), message,false,subject);
                    taskRepository.save(editingTask);
                    return new ApiResponse("Vazifa boshqa xodimga topshirildi",true);
                }

                //EDITOR_USER DIRECTOR LEKIN TASK_CREATOR BO'LMASA TASK CREATERNI OGOHLANTIRISH
                if (!editorUser.equals(taskCreater)) {
                    message = "Siz tomoningizdan yaratilgan "+editingTask.getName()+" nomli vazifaga director tomonidan o'zgartirish kiritildi";
                    subject = "Director tomonidan o'zgartirish";
                    allNeedfullMethod.sendEmail(taskCreater.getEmail(), message, false, subject);
                }

                taskRepository.save(editingTask);
                return new ApiResponse("Vazifa muvaffaqiyatli o'zgartirildi",true);
            }

        return new ApiResponse("Xatolik",false);
    }



    //COMPLETE TASK
    public ApiResponse complete(UUID id){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails))
            return new ApiResponse("Xatolik", false);
        //SHU YO'LGA KELGAN USER
        User editorUser = (User) principal;

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Vazifa topilmadi", false);

        //BAJARILGAN VAZIFA
        Task editingTask = optionalTask.get();

        //VAZIFAGA BIRIKTIRLGAN XODIM
        User performer = editingTask.getPerformer();

        //VAZIFANI YARATGAN USER
        Optional<User> optionalTaskCreater = userRepository.findById(editingTask.getCreatedBy());
        if (!optionalTaskCreater.isPresent())
            return new ApiResponse("Vazifani yaratuvchisi topilmadi",false);
        User taskCreater = optionalTaskCreater.get();

        if (editorUser.equals(performer)){
            editingTask.setStatus(TaskStatus.STATUS_DONE);
            taskRepository.save(editingTask);

            String message = editingTask.getName() + " nomli vazifa " + editorUser.getFirstName() + " tomonidan bajarildi";
            String subject = "Vazifa bajarildi";
            allNeedfullMethod.sendEmail(taskCreater.getEmail(), message, false, subject);
            return new ApiResponse("Muvaffaqiyatli saqlandi",true);
        }

        return new ApiResponse("Xatolik",false);
    }






    //GET ALL TASKS
    public List<Object> getAll(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails))
            return new ArrayList<>();

        User user = (User) principal;

        //USHBU USER GA BIRIKTIRLGAN VAZIFALAR
        List<Task> allByPerformerId = taskRepository.findAllByPerformerId(user.getId());

        //USHBU USER YARATGAN VAZIFALAR
        List<Task> allByCreatedBy = taskRepository.findAllByCreatedBy(user.getId());

        List<Object> objectList = new ArrayList<>(allByPerformerId);
        objectList.addAll(allByCreatedBy);
        return objectList;
    }









    //VAZIFANI ADD QILISH UCHUN YORDAMCHI METHOD
    public ApiResponse saveTask(TaskDto taskDto, User performer) {
        Task task = new Task();

        task.setName(taskDto.getName());
        task.setStatus(TaskStatus.STATUS_NEW);
        task.setDescription(taskDto.getDescription());
        task.setPerformer(performer);
        task.setDeadline(taskDto.getDeadline());

        taskRepository.save(task);

        String message = "Sizga " + task.getName() + " nomli vazifa biriktirildi";
        String subject = "Yangi vazifa";

        //BAJARUVCHIGA XABAR YUBORISH
        allNeedfullMethod.sendEmail(performer.getEmail(), message, false, subject);

        return new ApiResponse("Muvaffaqiyatli yaratildi", true);
    }


    //VAZIFANI EDIT QILISH UCHUN YORDAMCHI METHOD

}
