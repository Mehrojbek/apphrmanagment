package uz.pdp.apphrmanagment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagment.component.AllNeedfullMethod;
import uz.pdp.apphrmanagment.entity.User;
import uz.pdp.apphrmanagment.payload.ApiResponse;
import uz.pdp.apphrmanagment.repository.UserRepository;

import java.util.*;

@Service
public class LidershipService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AllNeedfullMethod allNeedfullMethod;

    public List<User> getAllWorker(){


        User user = allNeedfullMethod.getUserFromPrincipal();
        if (user == null)
            return new ArrayList<>();

        byte roleNumber = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        //DIRECTOR BARCHA XODIMLARNI KO'RA OLADI
        if (roleNumber == 2)
            return userRepository.findAll();

        //KIMNI QO'SHGAN BO'LSA SHU XODIMLAR RO'YXTINI QAYTARAMIZ
        if (roleNumber == 1)
            return userRepository.findAllByCreatedBy(user.getId());

        return new ArrayList<>();
    }


    public ApiResponse getOneWorker(UUID id){
        //TIZIMGA KIRGAN USER NI ANIQLASH
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails))
            return new ApiResponse("Xatolik",false);

        User user = (User) principal;

        //USER ROLINI ANIQLASH
        byte roleNumber = allNeedfullMethod.getRoleNumber(user.getAuthorities());

        if (roleNumber == 1 || roleNumber == 2) {

            Optional<User> optionalWorker = userRepository.findById(id);
            if (!optionalWorker.isPresent())
                return new ApiResponse("Ishchi topilmadi",false);

            User worker = optionalWorker.get();

            //DIRECTOR XOHLAGAN ISHCHI HAQIDAGI MA'LUMOTNI OLISHI MUMKIN
            if (roleNumber == 2)
                return new ApiResponse("Muvaffaqiyatli bajarildi",true,worker);

            //MANAGER ESA O'ZI QO'SHGAN ISHCHI HAQIDA MA'LUMOTNI OLISHI MUMKIN
            if (worker.getCreatedBy().equals(user.getId()))
                return new ApiResponse("Muvaffaqiyatli bajarildi",true,worker);
        }
        //BOSHQA HAR QANDAY HOLATDA XATOLIK
        return new ApiResponse("Xatolik",false);
    }
}
