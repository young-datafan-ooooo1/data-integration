package com.dp.de.run.management.plugin.dbInputservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tree<T> {

    private String title;

    private String value;

    private String text;

    private String[] script;




    private List<Tree<T>> children;


    private boolean hasParent = false;

    private boolean hasChildren = false;


    public void initChildren(){
        this.children = new ArrayList<>();
    }

}
