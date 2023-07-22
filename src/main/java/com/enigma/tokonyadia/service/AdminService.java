package com.enigma.tokonyadia.service;

import com.enigma.tokonyadia.entity.Admin;

public interface AdminService {

    Admin create(Admin admin);
    Admin getById(String id);
    Admin update(Admin admin);

}
