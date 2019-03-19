package com.explore.service;

import com.explore.common.ServerResponse;
import com.explore.pojo.Class;
import java.util.List;

public interface IClassService {
    ServerResponse<List<Class>> getClasses();
    ServerResponse addClass(Class classes);
    ServerResponse deleteClass(Integer id);
    ServerResponse reviseClass(Class classes);
}
