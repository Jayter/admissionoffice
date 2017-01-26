package com.jayton.admissionoffice.command.util;

public class CommandUtils {
    private CommandUtils() {
    }

    public static Long getTotalCountOfPages(Long totalCount, Long countPerPage) {
        if(totalCount < countPerPage) {
            return 1L;
        }
        long countOfPages = totalCount / countPerPage;
        long reminder = totalCount % countPerPage;
        if(reminder != 0) {
            countOfPages++;
        }
        return countOfPages;
    }
}
