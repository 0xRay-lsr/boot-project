package cn.aps.boot.mapstruct.struct;

import cn.aps.boot.mapstruct.dto.UserMultiDTO;
import cn.aps.boot.mapstruct.entity.UserAddressDO;
import cn.aps.boot.mapstruct.entity.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author : lishirui
 * 
 */
@Mapper(componentModel = "spring")
public interface UserMultiStruct {

    @Mapping(source = "userDO.birthday", target = "birthday", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "userDO.regDate", expression = "java(org.apache.commons.lang3.time.DateFormatUtils.format(userDO.getRegDate(),\"yyyy-MM-dd HH:mm:ss\"))")
    @Mapping(source = "userAddressDO.postcode", target = "postcode")
    @Mapping(source = "userAddressDO.address", target = "address")
    @Mapping(target = "memo", ignore = true)
    UserMultiDTO toUserMultiDTO(UserDO userDO, UserAddressDO userAddressDO);

}
