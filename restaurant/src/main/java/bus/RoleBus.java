package bus;

import dao.Repository;
import dao.exceptions.roleExceptions.GetRoleFailException;
import model.RoleModel;

import java.util.ArrayList;

public class RoleBus {

    private static RoleBus _instance = new RoleBus();
    private Repository _repository = Repository.getInstance();
    private ArrayList<RoleModel> listRoles;

    private RoleBus() {
    }

    public static RoleBus get_instance() {
        return _instance;
    }

    private ArrayList<RoleModel> getAll() throws GetRoleFailException {
        return _repository.getAllRoles();
    }

    public ArrayList<RoleModel> getListRoles() throws GetRoleFailException {
        if (listRoles == null) listRoles = getAll();
        return listRoles;
    }
}
