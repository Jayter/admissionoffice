package com.jayton.admissionoffice.controller;

public enum CommandName {
    AUTHORIZE,
    LOGOUT,
    LOAD_MAIN,
    HANDLE_APPLICATIONS,
    ADMIN_PAGE,

    GET_UNIVERSITY,
    EDIT_UNIVERSITY,
    UPDATE_UNIVERSITY,
    CREATE_UNIVERSITY,
    DELETE_UNIVERSITY,

    GET_FACULTY,
    EDIT_FACULTY,
    UPDATE_FACULTY,
    CREATE_FACULTY,
    DELETE_FACULTY,

    GET_DIRECTION,
    EDIT_DIRECTION,
    UPDATE_DIRECTION,
    CREATE_DIRECTION,
    DELETE_DIRECTION,

    ADD_ENTRANCE_SUBJECT,
    DELETE_ENTRANCE_SUBJECT,

    CREATE_USER,
    GET_USER,
    EDIT_USER,
    UPDATE_USER,
    DELETE_USER,
    ADD_USER_RESULT,
    DELETE_USER_RESULT,
    USER_APPLY,
    USER_CANCEL_APPLICATION
}