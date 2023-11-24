package photo.organiser.actions;

import java.util.ArrayList;
import java.util.List;

public abstract class Action
{
    protected List<ActionError> errors = new ArrayList<>();

    abstract public void act();

    @Override
    public String toString()
    {
        return "Action{" +
                "errors=" + errors +
                '}';
    }

    public static class ActionError
    {
        final String errorMessage;
        final String exceptionMessage;

        public ActionError(String errorMessage, String exceptionMessage)
        {
            this.errorMessage = errorMessage;
            this.exceptionMessage = exceptionMessage;
        }

        @Override
        public String toString()
        {
            return "ActionError{" +
                    "errorMessage='" + errorMessage + '\'' +
                    ", exceptionMessage='" + exceptionMessage + '\'' +
                    '}';
        }
    }
}
