VERSION=$(grep 'version := ' build.sbt | awk -F'"' '{print $2}')
NAME=$(grep 'name := ' build.sbt | awk -F'"' '{print $2}')

rm -rf dist

while true; do
	sbt clean compile dist

	if [[ -e target/universal/$NAME-$VERSION.zip ]]; then
		unzip target/universal/$NAME-$VERSION.zip > /dev/null
		mv $NAME-$VERSION dist
		break
	fi
done

# sbt clean compile stage