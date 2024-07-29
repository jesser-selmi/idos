package com.IDOSdigital.userManagement.services.impl;

import com.IDOSdigital.userManagement.entities.Request;
import com.IDOSdigital.userManagement.entities.User;
import com.IDOSdigital.userManagement.repositories.RequestRepository;
import com.IDOSdigital.userManagement.repositories.UserRepository;
import com.IDOSdigital.userManagement.services.RequestService;
import com.IDOSdigital.userManagement.utils.EntityResponse;
import com.IDOSdigital.userManagement.utils.ErrorResponse;
import com.IDOSdigital.userManagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response getAllRequests() {
        List<Request> allRequests = requestRepository.findAll();
        List<Request> requests = new ArrayList<>();

        for (Request req : allRequests) {
            if (!req.isDeleted()) {
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = userRepository.findUserByEmail(((UserDetails)principal).getUsername()).orElse(null);

        Response res = new Response();
        EntityResponse entityResponse;
        List<ErrorResponse.ValidationError> errors = new ArrayList<>();

        try {
            request.setUserId(u.getId());
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = userRepository.findUserByEmail(((UserDetails) principal).getUsername()).orElse(null);

        try {
            Optional<Request> optionalRequest = requestRepository.findById(id);
            if (optionalRequest.isPresent()) {
                Request existingRequest = optionalRequest.get();

                String currentUserId = u.getId();

                if (!existingRequest.getUserId().equals(currentUserId) &&
                        !u.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))&&
                        !u.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RH"))) {
                    throw new RuntimeException("Unauthorized");
                }

                if (request.getType() != null) {
                    existingRequest.setType(request.getType());
                }
                if (request.getUserId() != null && !request.getUserId().isEmpty()) {
                    existingRequest.setUserId(request.getUserId());
                }

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

                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User u = userRepository.findUserByEmail(((UserDetails) principal).getUsername()).orElse(null);
                String currentUserId = u.getId();

                if (!existingRequest.getUserId().equals(currentUserId) &&
                        !u.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))&&
                        !u.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RH"))) {
                    throw new RuntimeException("Unauthorized");
                }

                existingRequest.setDeleted(true);
                Request updatedRequest = requestRepository.save(existingRequest);

                res.setData(updatedRequest);
                entityResponse = new EntityResponse(HttpStatus.OK.value(), "Request deleted successfully");
            } else {
                entityResponse = new EntityResponse(HttpStatus.NOT_FOUND.value(), "Request not found");
            }
        } catch (Exception e) {
            entityResponse = new EntityResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            errors.add(new ErrorResponse.ValidationError("Exception", e.getMessage()));
            entityResponse.setErrors(errors);
        }

        res.setEntityResponse(entityResponse);
        return res;
    }

}
