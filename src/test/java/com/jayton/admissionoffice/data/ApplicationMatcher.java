package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.to.Application;

import java.math.BigDecimal;
import java.util.List;

public class ApplicationMatcher {

    public boolean compare(Application first, Application second) {
        boolean flag = first.getUserId() == second.getUserId();
        flag &= first.getDirectionId() == second.getDirectionId();
        flag &= first.getCreationTime().equals(second.getCreationTime());
        flag &= scale(first.getMark()).equals(scale(second.getMark()));
        return flag && first.getStatus() == second.getStatus();
    }

    public boolean compareLists(List<Application> first, List<Application> second) {
        boolean flag = first.size() == second.size();
        first.sort((d1, d2) -> (int)(d1.getId() - d2.getId()));
        second.sort((d1, d2) -> (int)(d1.getId() - d2.getId()));
        if(flag) {
            for(int i = 0; i < first.size(); i++) {
                if(!(compare(first.get(i), second.get(i)))) {
                    return false;
                }
            }
        }
        return flag;
    }

    protected BigDecimal scale(BigDecimal in) {
        return in.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}