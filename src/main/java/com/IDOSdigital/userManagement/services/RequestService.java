package com.IDOSdigital.userManagement.services;

import com.IDOSdigital.userManagement.entities.Request;
import com.IDOSdigital.userManagement.utils.Response;


public interface RequestService {
    Response getAllRequests();
    Response getRequestById(String id);
    Response createRequest(Request request);
    Response updateRequest(Request request , String id);
    Response deleteRequest(String id);
}
