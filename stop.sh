rm interceptor-1.0.0/RUNNING_PID
kill -9 $(lsof -i:9070 -t)