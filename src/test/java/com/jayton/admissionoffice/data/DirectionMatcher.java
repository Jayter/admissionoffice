package com.jayton.admissionoffice.data;

import com.jayton.admissionoffice.model.university.Direction;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DirectionMatcher extends Matcher<Direction> {

    @Override
    public boolean compare(Direction first, Direction second) {
        if(!first.getName().equals(second.getName())) return false;
        if(!scale(first.getAverageCoefficient()).equals(scale(second.getAverageCoefficient()))) return false;
        if(first.getCountOfStudents() != second.getCountOfStudents()) return false;
        if(first.getFacultyId() != second.getFacultyId()) return false;
        if(first.getEntranceSubjects().size() != second.getEntranceSubjects().size()) return false;

        Map<Long, BigDecimal> secondSubjects = second.getEntranceSubjects();
        for(Map.Entry<Long, BigDecimal> firstSubjects: first.getEntranceSubjects().entrySet()) {
            Long firstKey = firstSubjects.getKey();
            BigDecimal firstValue = firstSubjects.getValue();

            if(!secondSubjects.containsKey(firstKey)) return false;
            if(!scale(firstValue).equals(scale(secondSubjects.get(firstKey)))) return false;
        }
        return true;
    }

    public boolean compareLists(List<Direction> first, List<Direction> second) {
        first.sort((d1, d2) -> (int)(d1.getId() - d2.getId()));
        second.sort((d1, d2) -> (int)(d1.getId() - d2.getId()));
        boolean flag = first.size() == second.size();
        if(flag) {
            for(int i = 0; i < first.size(); i++) {
                if(!(compare(first.get(i), second.get(i)))) {
                    return false;
                }
            }
        }
        return flag;
    }

    public boolean compareSubjects(Map<Long, BigDecimal> first, Map<Long, BigDecimal> second) {
        boolean flag = first.size() == second.size();
        if(flag) {
            for(Map.Entry<Long, BigDecimal> firstSubjects: first.entrySet()) {
                Long firstKey = firstSubjects.getKey();
                BigDecimal firstValue = firstSubjects.getValue();

                if(!second.containsKey(firstKey)) return false;
                if(!scale(firstValue).equals(scale(second.get(firstKey)))) return false;
            }
        }
        return flag;
    }
}