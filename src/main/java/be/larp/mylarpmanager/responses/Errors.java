package be.larp.mylarpmanager.responses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Errors {

    private final Map<String, List<String>> validationErrors;
    private final List<String> globalErrors;

    public Errors() {
        this.validationErrors = new HashMap<>();
        this.globalErrors = new ArrayList<>();
    }

    public void addValidationError(String field, String message) {
        List<String> messageList;
        if(!validationErrors.containsKey(field)) {
            messageList = new ArrayList<>();
            validationErrors.put(field, messageList);
        } else {
            messageList = validationErrors.get(field);
        }
        messageList.add(message);
    }

    public void addGlobalError(String message) {
        globalErrors.add(message);
    }

    public Map<String, List<String>> getValidationErrors() {
        return validationErrors;
    }

    public List<String> getGlobalErrors() {
        return globalErrors;
    }

}
