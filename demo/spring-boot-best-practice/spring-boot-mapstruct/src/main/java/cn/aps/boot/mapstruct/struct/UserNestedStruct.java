package cn.aps.boot.mapstruct.struct;

import cn.aps.boot.mapstruct.dto.UserNestedDTO;
import cn.aps.boot.mapstruct.entity.UserNestedDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author : lishirui
 * 
 */
@Mapper(componentModel = "spring")
public interface UserNestedStruct {

    @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "regDate", expression = "java(org.apache.commons.lang3.time.DateFormatUtils.format(userNestedDO.getRegDate(),\"yyyy-MM-dd HH:mm:ss\"))")
    @Mapping(source = "userAddressDO", target = ".")
    @Mapping(source = "userExtDO", target = ".")
    @Mapping(source = "userExtDO.memo", target = "memo")
    UserNestedDTO toUserNestedDTO(UserNestedDO userNestedDO);

}
