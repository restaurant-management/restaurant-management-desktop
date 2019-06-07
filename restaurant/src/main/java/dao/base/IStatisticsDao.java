package dao.base;

import dao.exceptions.RequestFailException;
import dao.exceptions.roleExceptions.CreateRoleFailException;
import model.RoleModel;
import model.enums.Permission;
import org.json.JSONArray;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public interface IStatisticsDao {
    JSONArray countBill(String token, LocalDate startDate, LocalDate endDate) throws RequestFailException, IOException;
}
