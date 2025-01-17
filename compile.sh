VERSION=$(grep 'version := ' build.sbt | awk -F'"' '{print $2}')

rm -rf interceptor-$VERSION
sbt clean compile dist
unzip target/universal/interceptor-$VERSION.zip
mv interceptor-$VERSION dist

# sbt clean compile stage