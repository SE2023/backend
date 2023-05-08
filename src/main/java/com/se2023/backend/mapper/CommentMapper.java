package com.se2023.backend.mapper;


import com.se2023.backend.entity.Comment.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Select("insert into comment(user_id,facility_id,content,create_time) values (#{user_id}, #{facility_id}, #{content}, #{create_time})")
    void addComment(Comment comment);

    @Delete("delete from comment where user_id = #{user_id}")
    void deleteCommentByUser(Integer user_id);

    @Delete("delete from comment where id=#{id}")
    void deleteCommentById(Integer id);

    @Select("select * from comment where facility_id=#{facility_id}")
    List<Comment> queryCommentByFacility(Integer facility_id);

    @Select("select * from comment")
    List<Comment> queryAllComment();

    @Select("select * from comment where user_id=#{user_id}")
    List<Comment> queryCommentByUser(Integer user_id);


}
