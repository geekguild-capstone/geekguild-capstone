package com.geekguild.repositories;

import com.geekguild.models.Group;
import com.geekguild.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMembersContaining(User user);

    // Add the method to get the count of members in a specific group
    @Query("SELECT COUNT(m) FROM Group g JOIN g.members m WHERE g.id = :groupId")
    int getGroupMembersCount(@Param("groupId") Long groupId);


    @Query("SELECT u FROM Group g JOIN g.members u WHERE g.id = :groupId AND u.id = :userId")
    User findMemberById(@Param("groupId") Long groupId, @Param("userId") Long userId);


    @Query("SELECT g FROM Group g WHERE :user NOT MEMBER OF g.members")
    List<Group> findGroupsNotContainingMember(@Param("user") User user);
}
