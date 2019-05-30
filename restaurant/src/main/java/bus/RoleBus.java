package bus;

import dao.Repository;
import dao.exceptions.roleExceptions.GetRoleFailException;
import model.RoleModel;

import java.util.ArrayList;

public class RoleBus {
    private Repository _repository = Repository.getInstance();

    public ArrayList<RoleModel> getAll() throws GetRoleFailException {
        return _repository.getAllRole();
    }
}
