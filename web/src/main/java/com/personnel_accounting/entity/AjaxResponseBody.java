package com.personnel_accounting.entity;

import com.personnel_accounting.entity.dto.EmployeeDTO;

import java.util.List;

public class AjaxResponseBody {
    String msg;
    List<EmployeeDTO> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<EmployeeDTO> getResult() {
        return result;
    }

    public void setResult(List<EmployeeDTO> result) {
        this.result = result;
    }
}
