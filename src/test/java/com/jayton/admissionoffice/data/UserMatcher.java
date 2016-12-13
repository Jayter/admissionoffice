package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.user.User;

import java.util.List;
import java.util.Map;

public class UserMatcher extends Matcher<User> {

    @Override
    public boolean equals(User first, User second) {
        if(!first.getName().equals(second.getName())) return false;
        if(!first.getLastName().equals(second.getLastName())) return false;
        if(!first.getAddress().equals(second.getAddress())) return false;
        if(!first.getEmail().equals(second.getEmail())) return false;
        if(!first.getPhoneNumber().equals(second.getPhoneNumber())) return false;
        if(!first.getBirthDate().equals(second.getBirthDate())) return false;
        if(first.getAverageMark() != second.getAverageMark()) return false;


        Map<Long, Short> secondResults = second.getResults();
        for(Map.Entry<Long, Short> firstSubjects: first.getResults().entrySet()) {
            Long firstKey = firstSubjects.getKey();
            Short firstValue = firstSubjects.getValue();

            if(!secondResults.containsKey(firstKey)) return false;
            if(!firstValue.equals(secondResults.get(firstKey))) return false;
        }
        return true;
    }

    @Override
    public boolean equals(List<User> first, List<User> second) {
        first.sort((u1, u2) -> (int)(u1.getId() - u2.getId()));
        second.sort((u1, u2) -> (int)(u1.getId() - u2.getId()));
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

    public boolean compareResults(Map<Long, Short> first, Map<Long, Short> second) {
        boolean flag = first.size() == second.size();
        if(flag) {
            for(Map.Entry<Long, Short> firstSubjects: first.entrySet()) {
                Long firstKey = firstSubjects.getKey();
                Short firstValue = firstSubjects.getValue();

                if(!second.containsKey(firstKey)) return false;
                if(!firstValue.equals(second.get(firstKey))) return false;
            }
        }
        return flag;
    }
}