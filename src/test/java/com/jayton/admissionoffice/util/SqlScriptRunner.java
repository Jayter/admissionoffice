package com.jayton.admissionoffice.util;

/*
 * Copyright (c) 2014 David Kaya
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlScriptRunner {

    public static final String DEFAULT_SCRIPT_DELIMETER = ";";

    private final boolean autoCommit;
    private final Connection connection;

    /**
     *
     * @param connection : Connection to database.
     * @param autoCommit : True - it will commit automatically, false - you have to commit manually.
     */
    public SqlScriptRunner(final Connection connection, final boolean autoCommit) {
        if (connection == null) {
            throw new RuntimeException("Connection is required");
        }
        this.connection = connection;
        this.autoCommit = autoCommit;
    }

    /**
     *
     * @param reader - file with your script
     */
    public void runScript(final Reader reader) throws SQLException {
        final boolean originalAutoCommit = this.connection.getAutoCommit();
        try {
            if (originalAutoCommit != this.autoCommit) {
                this.connection.setAutoCommit(autoCommit);
            }
            this.runScript(this.connection, reader);
        } finally {
            this.connection.setAutoCommit(originalAutoCommit);
        }
    }

    private void runScript(final Connection connection, final Reader reader) {

        for (String script : formatString(reader)) {
            PreparedStatement statement = null;

            try {
                statement = connection.prepareStatement(script);
                statement.execute();

                //If auto commit is enabled, then commit
                if (autoCommit) {
                    connection.commit();
                }

            } catch (SQLException ex) {
                ex.fillInStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                }
            }
        }

    }

    /**
     * Parses file into commands delimeted by ';'
     * @param reader
     * @return string[] - commands from file
     */
    private String[] formatString(final Reader reader) {
        String result = "";
        String line;
        final LineNumberReader lineReader = new LineNumberReader(reader);

        try {
            while ((line = lineReader.readLine()) != null) {
                if (line.startsWith("--") || line.startsWith("//") || line.startsWith("#")) {
                    //DO NOTHING - It is a commented line
                } else {
                    result += line;
                }
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }

        if (result == null) {
            throw new RuntimeException("Error while parsing or no scripts in file!");
        } else {
            return result.replaceAll("(?<!"+DEFAULT_SCRIPT_DELIMETER+")(\\r?\\n)+", "").split(DEFAULT_SCRIPT_DELIMETER);
        }
    }
}
