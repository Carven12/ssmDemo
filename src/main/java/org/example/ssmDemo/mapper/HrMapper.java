package org.example.ssmDemo.mapper;

import org.example.ssmDemo.entity.Hr;
import org.mapstruct.Mapper;

/**
 * Created by sang on 2017/12/28.
 */
@Mapper
public interface HrMapper {

    Hr findHrByName(String name);
}
