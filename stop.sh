if test -f "target/universal/stage/RUNNING_PID"
then
	echo "Stopping Interceptor Server";
	kill -9 $(cat target/universal/stage/RUNNING_PID);
	rm target/universal/stage/RUNNING_PID;
fi