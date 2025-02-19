VERSION=$(grep 'version := ' build.sbt | awk -F'"' '{print $2}')
NAME=$(grep 'name := ' build.sbt | awk -F'"' '{print $2}')

rm -rf dist
sbt clean compile dist
unzip target/universal/$NAME-$VERSION.zip
mv $NAME-$VERSION dist

# sbt clean compile stage