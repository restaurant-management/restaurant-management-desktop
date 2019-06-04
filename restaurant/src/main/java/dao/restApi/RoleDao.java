package dao.restApi;

import com.google.firebase.database.annotations.NotNull;
import dao.base.IRoleDao;
import dao.exceptions.AddPermissionFailException;
import dao.exceptions.RemovePermissionFailException;
import dao.exceptions.RequestFailException;
import dao.exceptions.dishExceptions.EditDishFailException;
import dao.exceptions.roleExceptions.CreateRoleFailException;
import dao.exceptions.roleExceptions.DeleteRoleFailException;
import dao.exceptions.roleExceptions.GetRoleFailException;
import model.RoleModel;
import model.enums.Permission;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoleDao implements IRoleDao {
    @Override
    public RoleModel createRole(String token, @NotNull String name, String slug, String description, ArrayList<Permission> permissions) throws CreateRoleFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("name", name));
        if (slug != null)
            body.add(new BasicNameValuePair("slug", slug));
        if (description != null)
            body.add(new BasicNameValuePair("description", description));
        if (permissions != null)
            for (int i = 0; i < permissions.size(); i++) {
                body.add(new BasicNameValuePair("permissions[" + i + "]", permissions.get(i).toString()));
            }

        try {
            String response = http.post("/api/roles", new Header[]{header}, body);
            return new RoleModel(new JSONObject(response));
        } catch (IOException | RequestFailException e) {
            throw new CreateRoleFailException(e.getMessage());
        }
    }

    @Override
    public RoleModel editRole(String token, String slug, String newSlug, String name, String description) throws EditDishFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        if (name != null)
            body.add(new BasicNameValuePair("name", name));
        if (newSlug != null && !newSlug.equals(slug))
            body.add(new BasicNameValuePair("slug", newSlug));
        if (description != null)
            body.add(new BasicNameValuePair("description", description));

        try {
            String response = http.put("/api/roles/" + slug, new Header[]{header}, body);
            return new RoleModel(new JSONObject(response));
        } catch (IOException | RequestFailException e) {
            throw new EditDishFailException(e.getMessage());
        }
    }

    @Override
    public RoleModel getRole(String token, String slug) throws GetRoleFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        try {
            String response = http.get("/api/roles/" + slug, new Header[]{header});
            return new RoleModel(new JSONObject(response));
        } catch (IOException | RequestFailException e) {
            throw new GetRoleFailException(e.getMessage());
        }
    }

    @Override
    public ArrayList<RoleModel> getAllRole(String token, Integer length, Integer offset) throws GetRoleFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        String uri = "/api/roles?";
        if (length != null) uri += "length=" + length;
        if (offset != null) uri += "&offset=" + offset;
        try {
            String response = http.get(uri, new Header[]{header});

            ArrayList<RoleModel> result = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response);
            for (Object json : jsonArray) {
                result.add(new RoleModel((JSONObject) json));
            }
            return result;
        } catch (IOException | RequestFailException e) {
            throw new GetRoleFailException(e.getMessage());
        }
    }

    @Override
    public void deleteRole(String token, String slug) throws DeleteRoleFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        try {
            http.delete("/api/roles/" + slug, new Header[]{header});
        } catch (IOException | RequestFailException e) {
            throw new DeleteRoleFailException(e.getMessage());
        }

    }

    @Override
    public RoleModel setPermission(String token, String slug, ArrayList<Permission> permissions) throws AddPermissionFailException {
        try {
            // Create body for request
            List<NameValuePair> body = new ArrayList<>();
            for (int i = 0; i < permissions.size(); i++) {
                body.add(new BasicNameValuePair("permissions[" + i + "]", permissions.get(i).toString()));
            }

            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.post("/api/roles/" + slug + "/permissions/set", new Header[]{header}, body);

            JSONObject json = new JSONObject(response);
            return new RoleModel(json);
        } catch (IOException | RequestFailException e) {
            throw new AddPermissionFailException(e.getMessage());
        }
    }

    @Override
    public RoleModel addPermission(String token, String slug, Permission permission) throws AddPermissionFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        try {
            String response = http.post("/api/roles/" + slug + "/permissions/" + permission, new Header[]{header}, new ArrayList<>());

            JSONObject json = new JSONObject(response);
            return new RoleModel(json);
        } catch (IOException | RequestFailException e) {
            throw new AddPermissionFailException(e.getMessage());
        }
    }

    @Override
    public RoleModel removePermission(String token, String slug, Permission permission) throws RemovePermissionFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        try {
            String response = http.delete("/api/roles/" + slug + "/permissions/" + permission, new Header[]{header});

            JSONObject json = new JSONObject(response);
            return new RoleModel(json);
        } catch (IOException | RequestFailException e) {
            throw new RemovePermissionFailException(e.getMessage());
        }
    }
}
