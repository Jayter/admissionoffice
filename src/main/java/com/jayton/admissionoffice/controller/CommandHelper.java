package com.jayton.admissionoffice.controller;

import com.jayton.admissionoffice.command.Command;
import com.jayton.admissionoffice.command.impl.*;
import com.jayton.admissionoffice.command.impl.admin.*;
import com.jayton.admissionoffice.command.impl.direction.*;
import com.jayton.admissionoffice.command.impl.faculty.*;
import com.jayton.admissionoffice.command.impl.university.*;
import com.jayton.admissionoffice.command.impl.user.*;

import java.util.HashMap;
import java.util.Map;

public class CommandHelper {

    private Map<CommandName, Command> commands = new HashMap<>();

    private static final CommandHelper instance = new CommandHelper();

    private CommandHelper() {
        commands.put(CommandName.AUTHORIZE, new AuthorizeCommand());
        commands.put(CommandName.LOGOUT, new LogoutCommand());
        commands.put(CommandName.LOAD_MAIN, new LoadMainPageCommand());
        commands.put(CommandName.HANDLE_APPLICATIONS, new HandleApplicationsCommand());
        commands.put(CommandName.ADMIN_PAGE, new AdminPageCommand());
        commands.put(CommandName.EDIT_SESSION_TERMS, new EditSessionTermsCommand());
        commands.put(CommandName.UPDATE_SESSION_TERMS, new UpdateSessionTermsCommand());
        commands.put(CommandName.CREATE_SESSION_TERMS, new CreateSessionTermsCommand());

        commands.put(CommandName.GET_UNIVERSITY, new GetUniversityCommand());
        commands.put(CommandName.EDIT_UNIVERSITY, new EditUniversityCommand());
        commands.put(CommandName.UPDATE_UNIVERSITY, new UpdateUniversityCommand());
        commands.put(CommandName.CREATE_UNIVERSITY, new CreateUniversityCommand());
        commands.put(CommandName.DELETE_UNIVERSITY, new DeleteUniversityCommand());

        commands.put(CommandName.GET_FACULTY, new GetFacultyCommand());
        commands.put(CommandName.EDIT_FACULTY, new EditFacultyCommand());
        commands.put(CommandName.UPDATE_FACULTY, new UpdateFacultyCommand());
        commands.put(CommandName.CREATE_FACULTY, new CreateFacultyCommand());
        commands.put(CommandName.DELETE_FACULTY, new DeleteFacultyCommand());

        commands.put(CommandName.GET_DIRECTION, new GetDirectionCommand());
        commands.put(CommandName.EDIT_DIRECTION, new EditDirectionCommand());
        commands.put(CommandName.UPDATE_DIRECTION, new UpdateDirectionCommand());
        commands.put(CommandName.CREATE_DIRECTION, new CreateDirectionCommand());
        commands.put(CommandName.DELETE_DIRECTION, new DeleteDirectionCommand());
        commands.put(CommandName.ADD_ENTRANCE_SUBJECT, new AddEntranceSubjectCommand());
        commands.put(CommandName.DELETE_ENTRANCE_SUBJECT, new DeleteEntranceSubjectCommand());

        commands.put(CommandName.GET_USER, new GetUserCommand());
        commands.put(CommandName.CREATE_USER, new CreateUserCommand());
        commands.put(CommandName.UPDATE_USER, new UpdateUserCommand());
        commands.put(CommandName.EDIT_USER, new EditUserCommand());
        commands.put(CommandName.DELETE_USER, new DeleteUserCommand());
        commands.put(CommandName.USER_APPLY, new UserApplyCommand());
        commands.put(CommandName.USER_CANCEL_APPLICATION, new UserCancelApplicationCommand());
        commands.put(CommandName.ADD_USER_RESULT, new AddUserResultCommand());
        commands.put(CommandName.DELETE_USER_RESULT, new DeleteUserResultCommand());
    }

    public static CommandHelper getInstance() {
        return instance;
    }

    public Command getCommand(String name) {
        if(name == null || name.isEmpty()) {
            return null;
        }
        name = name.replace('-', '_').toUpperCase();

        try {
            return commands.get(CommandName.valueOf(name));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public CommandName getCommandName(String name) {
        if(name == null || name.isEmpty()){
            return null;
        }
        name = name.replace('-', '_');
        try{
            return CommandName.valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException e){
            return null;
        }
    }
}