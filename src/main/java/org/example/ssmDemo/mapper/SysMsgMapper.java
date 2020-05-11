package org.example.ssmDemo.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.ssmDemo.entity.Hr;
import org.example.ssmDemo.entity.MsgContent;
import org.example.ssmDemo.entity.SysMsg;

import java.util.List;

/**
 * Created by sang on 2018/2/2.
 */
public interface SysMsgMapper {

    int sendMsg(MsgContent msg);

    int addMsg2AllHr(@Param("hrs") List<Hr> hrs, @Param("mid") Long mid);

    List<SysMsg> getSysMsg(@Param("start") int start, @Param("size") Integer size, @Param("hrid") Long hrid);

    int markRead(@Param("flag") Long flag, @Param("hrid") Long hrid);
}
