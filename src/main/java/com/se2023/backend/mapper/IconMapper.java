package com.se2023.backend.mapper;

import com.se2023.backend.entity.Icon.Icon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IconMapper {
    @Select("insert into icon(id, icon) values(#{id}, #{icon})")
    void addIcon(Icon icon);

    @Select("select icon from icon where id= #{id}")
    String queryIconById(Integer id);
}
