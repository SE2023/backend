package com.se2023.backend.mapper;

import com.se2023.backend.entity.Icon.Icon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IconMapper {
    @Select("insert into icon(id, icon) values(#{id}, #{icon})")
    void addIcon(Icon icon);

    @Select("select icon from icon where id= #{id}")
    String queryIconById(Integer id);

    @Update("update icon set icon = #{icon} where id = #{id}")
    void updateIcon(Integer id,String icon);
}
