package com.geekguild.controllers;

import com.geekguild.models.Group;
import com.geekguild.models.User;
import com.geekguild.repositories.GroupRepository;
import jakarta.persistence.Id;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GroupController {

    private final GroupRepository groupDao;

    public GroupController(GroupRepository groupDao) {
        this.groupDao = groupDao;
    }


    @GetMapping("/groups")
    public String showGroups() {
        return "groups/groups";
    }

    @GetMapping("/group")
    public String showGroup() {
        return "groups/group";
    }

    @GetMapping("/group/{id}")
    public String viewGroup(@PathVariable("id") Long groupId, Model model) {
        //Retrieve the group with the giver Id from the database
        Group group = groupDao.findById(groupId).orElse(null);
        model.addAttribute("group", group);

        // CHeck if the group exists
        if (group == null) {
            return "redirect:/error";
        } else {
            return "groups/group";
        }



    }
}