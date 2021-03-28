package org.example.ssmDemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.ssmDemo.entity.Hr;

/**
 * Created by sang on 2017/12/28.
 */
@Mapper
public interface HrMapper {

    Hr findHrByName(String name);
}
