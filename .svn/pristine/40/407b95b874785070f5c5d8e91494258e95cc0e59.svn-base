package com.herms.taskme.service;

import com.herms.taskme.model.Param;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.ParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ParamService {
    @Autowired
    private ParamRepository paramRepository;

    public ParamService() {
    }

    public String getParamValue(String code){
        Param param = paramRepository.findByCode(code);
        return param.getValue();
    }

    public Param addParam(Param param){
        return paramRepository.save(param);
    }

    public void updateParam(Long id, Param param) {
        paramRepository.save(param);
    }

    public void deleteParam(Long id){
        paramRepository.deleteById(id);
    }

}

