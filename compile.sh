rm -rf interceptor-1.0.0
sbt clean compile dist
unzip target/universal/interceptor-1.0.0.zip

# sbt clean compile stage