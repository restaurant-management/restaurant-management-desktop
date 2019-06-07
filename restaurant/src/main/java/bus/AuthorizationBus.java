package bus;

import dao.Repository;
import exceptions.DontHavePermissionException;
import model.enums.Permission;

import java.util.ArrayList;

public class AuthorizationBus {
    public void authorization() throws DontHavePermissionException {
        ArrayList<Permission> permissions = Repository.getInstance().get_currentUserPermissions();
        if (permissions == null) return;
        for (Permission per : permissions) {
            if (per == Permission.BILL_MANAGEMENT
                    || per == Permission.DAILY_DISH_MANAGEMENT
                    || per == Permission.DISH_MANAGEMENT
                    || per == Permission.ROLE_MANAGEMENT
                    || per == Permission.USER_MANAGEMENT)
                return;
        }
        throw new DontHavePermissionException();
    }

    public boolean checkPermission(Permission permission) {
        return Repository.getInstance().get_currentUserPermissions().contains(permission);
    }
}
