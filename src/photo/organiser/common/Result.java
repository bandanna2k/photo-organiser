package photo.organiser.common;

import java.util.function.Consumer;
import java.util.function.Function;

public class Result<L, R>
{
    private final L data;
    private final R error;

    private Result(L data, R errorMessage)
    {
        this.data = data;
        this.error = errorMessage;
    }

    public static <L, R> Result<L, R> failure(R errorMessage)
    {
        return new Result(null, errorMessage);
    }

    public static <L, R> Result<L, R> success(L data)
    {
        return new Result(data, null);
    }

    public static <L, R> Result<L, R> success()
    {
        return new Result(null, null);
    }

    public void fold(Consumer<L> success, Consumer<R> error)
    {
        ifSuccess(success);
        ifFailure(error);
    }

    public void ifSuccess(Consumer<L> successAction)
    {
        if(data != null)
        {
            successAction.accept(data);
        }
    }

    public void ifFailure(Consumer<R> failureAction)
    {
        if(isFailure())
        {
            failureAction.accept(error);
        }
    }

    public boolean isFailure()
    {
        return error != null;
    }

    public R failure()
    {
        return error;
    }

    public boolean isSuccess()
    {
        return error != null;
    }
}
