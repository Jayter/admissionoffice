package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.to.Application;

import java.util.List;

public class ApplicationMatcher extends Matcher<Application> {

    @Override
    public boolean equals(Application first, Application second) {
        boolean flag = first.getUserId() == second.getUserId();
        flag &= first.getDirectionId() == second.getDirectionId();
        flag &= first.getCreationTime().equals(second.getCreationTime());
        flag &= scale(first.getMark()).equals(scale(second.getMark()));
        return flag && first.getStatus() == second.getStatus();
    }

    @Override
    public boolean equals(List<Application> first, List<Application> second) {
        first.sort((d1, d2) -> (int)(d1.getId() - d2.getId()));
        second.sort((d1, d2) -> (int)(d1.getId() - d2.getId()));
        boolean flag = first.size() == second.size();
        if(flag) {
            for(int i = 0; i < first.size(); i++) {
                if(!(equals(first.get(i), second.get(i)))) {
                    return false;
                }
            }
        }
        return flag;
    }
}