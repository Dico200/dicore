package io.dico.dicore.util.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.function.Consumer;

public interface ExceptionHandler {
    void handle(Exception ex);
    
    default boolean isUnsafe() {
        return false;
    }
    
    static void log(Consumer<String> out, String action, Exception ex) {
        StringWriter msg = new StringWriter(1024);
        msg.append("Error occurred while ").append(action).append(':');
        if (ex instanceof SQLException) {
            SQLException sqlex = (SQLException) ex;
            msg.append('\n').append("Error code: ").append(Integer.toString(sqlex.getErrorCode()));
            msg.append('\n').append("SQL State: ").append(sqlex.getSQLState());
        }
        msg.append('\n').append("=======START STACK=======");
        try (PrintWriter pw = new PrintWriter(msg)) {
            ex.printStackTrace(pw);
        }
        msg.append('\n').append("========END STACK========");
        out.accept(msg.toString());
    }
    
    static ExceptionHandler withConsumer(Consumer<Exception> consumer) {
        return consumer::accept;
    }
    
    static ExceptionHandler log(String action) {
        return log(System.out, action);
    }
    
    static ExceptionHandler log(PrintStream out, String action) {
        return log(out::println, action);
    }
    
    static ExceptionHandler log(Consumer<String> out, String action) {
        return ex -> log(out, action, ex);
    }
    
    ExceptionHandler RUNTIME = new ExceptionHandler() {
        @Override
        public void handle(Exception ex) {
            throw (ex instanceof RuntimeException) ? (RuntimeException) ex : new RuntimeException(ex);
        }
        
        @Override
        public boolean isUnsafe() {
            return true;
        }
    };
    ExceptionHandler SUPPRESSED = ex -> {
    };
}