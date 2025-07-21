if ! test -f "dist/RUNNING_PID"
then
	./dist/bin/interceptor -jvm-debug "7070" -Dhttp.port=9070 > interceptor.log 2>&1 &
	# ./target/universal/stage/bin/interceptor -jvm-debug "7070" -Dhttp.port=9070 > interceptor.log 2>&1 &
fi