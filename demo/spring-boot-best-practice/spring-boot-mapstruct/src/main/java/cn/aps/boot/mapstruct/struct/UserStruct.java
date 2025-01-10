package cn.aps.boot.mapstruct.struct;

import cn.aps.boot.mapstruct.dto.UserShowDTO;
import cn.aps.boot.mapstruct.entity.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author : lishirui
 * 
 */
@Mapper
public interface UserStruct {

    UserStruct INSTANCE = Mappers.getMapper(UserStruct.class);

    @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "regDate", expression = "java(org.apache.commons.lang3.time.DateFormatUtils.format(userDO.getRegDate(),\"yyyy-MM-dd HH:mm:ss\"))")
    @Mapping(source = "userExtDO.regSource", target = "registerSource")
    @Mapping(source = "userExtDO.favorite", target = "favorite")
    @Mapping(target = "memo", ignore = true)
    UserShowDTO toUserShowDTO(UserDO userDO);

    List<UserShowDTO> toUserShowDTOs(List<UserDO> userDOs);

}
