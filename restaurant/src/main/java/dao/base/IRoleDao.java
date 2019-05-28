package dao.base;

import dao.exceptions.AddPermissionFailException;
import dao.exceptions.RemovePermissionFailException;
import dao.exceptions.dishExceptions.EditDishFailException;
import dao.exceptions.roleException.CreateRoleFailException;
import dao.exceptions.roleException.DeleteRoleFailException;
import dao.exceptions.roleException.GetRoleFailException;
import model.RoleModel;
import model.enums.Permission;

import java.util.ArrayList;

public interface IRoleDao {
    RoleModel createRole(String token, String name, String slug, String description, ArrayList<Permission> permissions) throws CreateRoleFailException;

    RoleModel editRole(String token, String slug, String newSlug, String name, String description) throws EditDishFailException;

    RoleModel getRole(String token, String slug) throws GetRoleFailException;

    void deleteRole(String token, String slug) throws DeleteRoleFailException;

    RoleModel addPermission(String token, String slug, Permission permission) throws AddPermissionFailException;

    RoleModel removePermission(String token, String slug, Permission permission) throws RemovePermissionFailException;
}
