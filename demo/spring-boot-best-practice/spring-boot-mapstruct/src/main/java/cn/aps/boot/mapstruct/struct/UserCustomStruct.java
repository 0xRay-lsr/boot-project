package cn.aps.boot.mapstruct.struct;

import cn.aps.boot.mapstruct.dto.UserCustomDTO;
import cn.aps.boot.mapstruct.dto.UserExtDTO;
import cn.aps.boot.mapstruct.entity.UserDO;
import cn.aps.boot.mapstruct.entity.UserExtDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author : lishirui
 * 
 */
@Mapper(componentModel = "spring")
public interface UserCustomStruct {

    @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "regDate", expression = "java(org.apache.commons.lang3.time.DateFormatUtils.format(userDO.getRegDate(),\"yyyy-MM-dd HH:mm:ss\"))")
    @Mapping(source = "userExtDO", target = "userExtDTO")
    @Mapping(target = "memo", ignore = true)
    UserCustomDTO toUserCustomDTO(UserDO userDO);

    default UserExtDTO toUserExtDTO(UserExtDO userExtDO) {
        UserExtDTO userExtDTO = new UserExtDTO();
        userExtDTO.setKids(userExtDO.getKids());
        userExtDTO.setFavorite(userExtDO.getFavorite());

        // 覆盖这两个值
        userExtDTO.setRegSource("默认来源");
        userExtDTO.setSchool("默认学校");

        return userExtDTO;
    }

}
