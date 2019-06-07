package bus;

import dao.Repository;
import dao.exceptions.AddPermissionFailException;
import dao.exceptions.dishExceptions.EditDishFailException;
import dao.exceptions.roleExceptions.CreateRoleFailException;
import dao.exceptions.roleExceptions.DeleteRoleFailException;
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

    public ArrayList<RoleModel> getAll() throws GetRoleFailException {
        listRoles = _repository.getAllRoles();
        return listRoles;
    }

    public ArrayList<RoleModel> getListRoles() throws GetRoleFailException {
        if (listRoles == null) listRoles = getAll();
        return listRoles;
    }

    public RoleModel getRole(String slug) throws GetRoleFailException {
        return _repository.getRole(slug);
    }

    public void createRole(RoleModel roleModel) throws CreateRoleFailException {
        _repository.addRole(roleModel);
    }

    public void editRole(RoleModel roleModel) throws EditDishFailException, AddPermissionFailException {
        _repository.editRole(roleModel);
    }

    public void deleteRole(String slug) throws DeleteRoleFailException {
        _repository.deleteRole(slug);
    }
}
