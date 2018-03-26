package com.vkyoungcn.learningtools.Models;

import java.util.List;

/**
 * Created by VkYoung16 on 2018/3/26 0026.
 */

public class Mission {
    private int db_id;
    private String name;
    private String description;
    private List<Integer> subGroups_ids;

    public Mission(String name) {
        this.name = name;
    }

    public int getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getSubGroups_ids() {
        return subGroups_ids;
    }

    public void setSubGroups_ids(List<Integer> subGroups_ids) {
        this.subGroups_ids = subGroups_ids;
    }
}
