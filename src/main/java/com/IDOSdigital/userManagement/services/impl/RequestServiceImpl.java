package com.IDOSdigital.userManagement.services.impl;

import com.IDOSdigital.userManagement.entities.Request;
import com.IDOSdigital.userManagement.repositories.RequestRepository;
import com.IDOSdigital.userManagement.services.RequestService;
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
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public Response getAllRequests() {
        List<Request> allRequests = requestRepository.findAll();
        List<Request> requests = new ArrayList<>();

        for (Request req : allRequests) {
            if (!req.isDeleted()) { // Changed to check for non-deleted requests
                requests.add(req);
            }
        }
        Response response = new Response();
        EntityResponse entityResponse = new EntityResponse(200, "Requests found");
        response.setEntityResponse(entityResponse);
        response.setData(requests);
        return response;
    }


    @Override
    public Response getRequestById(String id) {
        Response res = new Response();
        EntityResponse entityResponse;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();

        try {
            Optional<Request> optionalRequest = requestRepository.findById(id);
            if (optionalRequest.isPresent()) {
                res.setData(optionalRequest.get());
                entityResponse = new EntityResponse(200, "Request found");
            } else {
                entityResponse = new EntityResponse(404, "Request not found");
            }
        } catch (Exception e) {
            errors.add(new ErrorResponse.ValidationError("Exception", e.getMessage()));
            entityResponse = new EntityResponse(500, "Internal Server Error");
            entityResponse.setErrors(errors);
        }
        res.setEntityResponse(entityResponse);
        return res;
    }

    @Override
    public Response createRequest(Request request) {
        Response res = new Response();
        EntityResponse entityResponse;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();

        try {
            Request createdRequest = requestRepository.save(request);
            res.setData(createdRequest);
            entityResponse = new EntityResponse(201, "Request created successfully");
        } catch (Exception e) {
            errors.add(new ErrorResponse.ValidationError("Exception", e.getMessage()));
            entityResponse = new EntityResponse(500, "Internal Server Error");
            entityResponse.setErrors(errors);
        }
        res.setEntityResponse(entityResponse);
        return res;
    }

    @Override
    public Response updateRequest(Request request, String id) {
        Response res = new Response();
        EntityResponse entityResponse;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();

        try {
            Optional<Request> optionalRequest = requestRepository.findById(id);
            if (optionalRequest.isPresent()) {
                Request existingRequest = optionalRequest.get();

                // Update only the attributes from request that are not null or not empty
                if (request.getType() != null) {
                    existingRequest.setType(request.getType());
                }
                if (request.getUserId() != null && !request.getUserId().isEmpty()) {
                    existingRequest.setUserId(request.getUserId());
                }

                // Save the updated request
                Request updatedRequest = requestRepository.save(existingRequest);

                res.setData(updatedRequest);
                entityResponse = new EntityResponse(HttpStatus.OK.value(), "Request updated successfully");
            } else {
                entityResponse = new EntityResponse(HttpStatus.NOT_FOUND.value(), "Request not found");
            }
        } catch (Exception e) {
            errors.add(new ErrorResponse.ValidationError("Exception", e.getMessage()));
            entityResponse = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
            entityResponse.setErrors(errors);
        }

        res.setEntityResponse(entityResponse);
        return res;
    }


    @Override
    public Response deleteRequest(String id) {
        Response res = new Response();
        EntityResponse entityResponse;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();

        try {
            Optional<Request> optionalRequest = requestRepository.findById(id);
            if (optionalRequest.isPresent()) {
                Request existingRequest = optionalRequest.get();
                existingRequest.setDeleted(true); // Soft delete by setting deleted to true
                Request updatedRequest = requestRepository.save(existingRequest);
                res.setData(updatedRequest); // Return the deleted data
                entityResponse = new EntityResponse(HttpStatus.OK.value(), "Request deleted successfully");
            } else {
                entityResponse = new EntityResponse(HttpStatus.NOT_FOUND.value(), "Request not found");
            }
        } catch (Exception e) {
            entityResponse = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("exception", e.getMessage()));
            entityResponse.setErrors(errors);
        }

        res.setEntityResponse(entityResponse);
        return res;
    }

}
