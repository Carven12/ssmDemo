package org.example.ssmDemo.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.ssmDemo.entity.Department;

import java.util.List;

/**
 * Created by sang on 2018/1/7.
 */
public interface DepartmentMapper {
    void addDep(@Param("dep") Department department);

    void deleteDep(@Param("dep") Department department);

    List<Department> getDepByPid(Long pid);

    List<Department> getAllDeps();
}
