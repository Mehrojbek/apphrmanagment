package uz.pdp.apphrmanagment.entity.enums;

public enum RoleName {
    ROLE_DIRECTOR("director"),
    ROLE_MANAGER("meneger"),
    ROLE_WORKER("ishchi");

    public String simpleName;

    RoleName(String simpleName) {
        this.simpleName = simpleName;
    }
}
