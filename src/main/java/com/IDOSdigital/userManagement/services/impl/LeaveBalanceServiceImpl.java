package com.IDOSdigital.userManagement.services.impl;

import com.IDOSdigital.userManagement.entities.LeaveBalance;
import com.IDOSdigital.userManagement.repositories.LeaveBalanceRepository;
import com.IDOSdigital.userManagement.services.LeaveBalanceService;
import com.IDOSdigital.userManagement.utils.EntityResponse;
import com.IDOSdigital.userManagement.utils.Response;
import com.IDOSdigital.userManagement.utils.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Override
    public Response getAllLeaveBalances() {
        List<LeaveBalance> allLeaveBalances = leaveBalanceRepository.findAll();
        List<LeaveBalance> leaveBalances = new ArrayList<>();

        for (LeaveBalance lb : allLeaveBalances) {
            if (!lb.isDeleted()) {
                leaveBalances.add(lb);
            }
        }
        Response response = new Response();
        EntityResponse entityResponse = new EntityResponse(200, "Leave balances found");
        response.setEntityResponse(entityResponse);
        response.setData(leaveBalances);
        return response;
    }


    @Override
    public Response getLeaveBalanceById(String id) {
        Response response = new Response();
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        EntityResponse entityResponse;

        try {
            Optional<LeaveBalance> optionalLeaveBalance = leaveBalanceRepository.findById(id);
            if (optionalLeaveBalance.isPresent()) {
                response.setData(optionalLeaveBalance.get());
                entityResponse = buildResponseValidator("Leave balance found", HttpStatus.OK, errors);
            } else {
                entityResponse = buildResponseValidator("Leave balance not found", HttpStatus.NOT_FOUND, errors);
            }
        } catch (Exception e) {
            entityResponse = buildResponseValidator("Error retrieving leave balance", HttpStatus.INTERNAL_SERVER_ERROR, errors);
        }

        response.setEntityResponse(entityResponse);
        return response;
    }

    @Override
    public Response createLeaveBalance(LeaveBalance leaveBalance) {
        Response response = new Response();
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        EntityResponse entityResponse;

        try {
            LeaveBalance createdLeaveBalance = leaveBalanceRepository.save(leaveBalance);
            response.setData(createdLeaveBalance);
            entityResponse = buildResponseValidator("Leave balance created successfully", HttpStatus.CREATED, errors);
        } catch (Exception e) {
            entityResponse = buildResponseValidator("Error creating leave balance", HttpStatus.INTERNAL_SERVER_ERROR, errors);
        }

        response.setEntityResponse(entityResponse);
        return response;
    }

    @Override
    public Response updateLeaveBalance(LeaveBalance leaveBalance, String id) {
        Response response = new Response();
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        EntityResponse entityResponse;

        try {
            Optional<LeaveBalance> optionalLeaveBalance = leaveBalanceRepository.findById(id);
            if (optionalLeaveBalance.isPresent()) {
                LeaveBalance existingLeaveBalance = optionalLeaveBalance.get();

                // Update only the attributes from leaveBalance that are not null or not empty
                if (leaveBalance.getBalance() != 0) {
                    existingLeaveBalance.setBalance(leaveBalance.getBalance());
                }

                if (leaveBalance.getUserId() != null && !leaveBalance.getUserId().isEmpty()) {
                    existingLeaveBalance.setUserId(leaveBalance.getUserId());
                }

                // Save the updated leave balance
                LeaveBalance updatedLeaveBalance = leaveBalanceRepository.save(existingLeaveBalance);

                response.setData(updatedLeaveBalance);
                entityResponse = buildResponseValidator("Leave balance updated successfully", HttpStatus.OK, errors);
            } else {
                entityResponse = buildResponseValidator("Leave balance not found", HttpStatus.NOT_FOUND, errors);
            }
        } catch (Exception e) {
            errors.add(new ErrorResponse.ValidationError("Exception", e.getMessage()));
            entityResponse = buildResponseValidator("Error updating leave balance", HttpStatus.INTERNAL_SERVER_ERROR, errors);
        }

        response.setEntityResponse(entityResponse);
        return response;
    }


    @Override
    public Response deleteLeaveBalance(String id) {
        Response response = new Response();
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        EntityResponse entityResponse;

        try {
            Optional<LeaveBalance> optionalLeaveBalance = leaveBalanceRepository.findById(id);
            if (optionalLeaveBalance.isPresent()) {
                LeaveBalance existingLeaveBalance = optionalLeaveBalance.get();
                existingLeaveBalance.setDeleted(true); // Soft delete by setting deleted to true
                LeaveBalance updatedLeaveBalance = leaveBalanceRepository.save(existingLeaveBalance); // Save the updated leave balance
                response.setData(updatedLeaveBalance); // Return the deleted data
                entityResponse = buildResponseValidator("Leave balance deleted successfully", HttpStatus.OK, errors);
            } else {
                entityResponse = buildResponseValidator("Leave balance not found", HttpStatus.NOT_FOUND, errors);
            }
        } catch (Exception e) {
            entityResponse = buildResponseValidator("Error deleting leave balance", HttpStatus.INTERNAL_SERVER_ERROR, errors);
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            entityResponse.setErrors(errors);
        }

        response.setEntityResponse(entityResponse);
        return response;
    }


    private EntityResponse buildResponseValidator(String message, HttpStatus status, List<ErrorResponse.ValidationError> errors) {
        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setMessage(message);
        entityResponse.setStatus(status.value());
        entityResponse.setErrors(errors);
        return entityResponse;
    }
}
