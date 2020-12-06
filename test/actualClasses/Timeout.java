package actualClasses;

import java.util.concurrent.TimeUnit;

public @interface Timeout {

	int value();

	TimeUnit unit();

}


