package cn.aps.boot.mapstruct.struct;

import cn.aps.boot.mapstruct.dto.UserShowDTO;
import cn.aps.boot.mapstruct.entity.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @Author : lishirui
 * 
 */
@Mapper(componentModel = "spring")
public interface UserExistStruct {

    @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "regDate", expression = "java(org.apache.commons.lang3.time.DateFormatUtils.format(userDO.getRegDate(),\"yyyy-MM-dd HH:mm:ss\"))")
    @Mapping(source = "userExtDO.regSource", target = "registerSource")
    @Mapping(source = "userExtDO.favorite", target = "favorite")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "memo", ignore = true)
    void toUserShowDTO(@MappingTarget UserShowDTO userShowDTO, UserDO userDO);

}
