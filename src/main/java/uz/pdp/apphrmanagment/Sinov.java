package uz.pdp.apphrmanagment;

import uz.pdp.apphrmanagment.entity.enums.RoleName;

public class Sinov {
    public static void main(String[] args) {
        RoleName role_worker = RoleName.valueOf("ROLE_WORKER");
        System.out.println(role_worker);
    }
}
