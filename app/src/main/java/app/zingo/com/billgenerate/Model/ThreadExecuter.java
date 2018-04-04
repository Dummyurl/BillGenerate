package app.zingo.com.billgenerate.Model;

import java.util.concurrent.Executor;

/**
 * Created by root on 23/6/17.
 */

public class ThreadExecuter implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
