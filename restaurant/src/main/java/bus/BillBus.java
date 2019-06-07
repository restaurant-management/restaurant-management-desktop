package bus;

import dao.Repository;
import dao.exceptions.billExceptions.CreateBillFailException;
import dao.exceptions.billExceptions.DeleteBillFailException;
import dao.exceptions.billExceptions.EditBillFailException;
import dao.exceptions.billExceptions.GetBillFailException;
import model.BillDetailModel;
import model.BillModel;

import java.util.ArrayList;
import java.util.Date;

public class BillBus {
    Repository _repository = Repository.getInstance();

    public ArrayList<BillModel> getByDay(Date day) throws GetBillFailException {
        return _repository.getBillByDay(day);
    }

    public BillModel getBill(int billId) throws GetBillFailException {
        return _repository.getBill(billId);
    }

    public void createBill(BillModel billModel) throws CreateBillFailException {
        _repository.createBill(billModel);
    }

    public void editBill(BillModel billModel) throws EditBillFailException {
        _repository.editBill(billModel);
    }

    public void deleteBill(int billId) throws DeleteBillFailException {
        _repository.deleteBill(billId);
    }

    public void addBillDetail(int billId, BillDetailModel billDetailModel) throws EditBillFailException {
        _repository.addBillDetail(billId, billDetailModel);
    }

    public void removeBillDetail(int billId, int dishId) throws EditBillFailException {
        _repository.removeBillDetail(billId, dishId);
    }
}
