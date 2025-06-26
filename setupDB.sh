export PATH=$HOME/programs/mysql/$MYSQL_VERSION/bin:$PATH
MYSQL_SOCKET="$HOME/programs/mysql/$MYSQL_VERSION/data/mysql_"$(echo "$MYSQL_VERSION" | sed 's/\./_/g')".sock"
mysqladmin -P $MYSQL_PORT -u $MYSQL_USER -p$MYSQL_PASSWORD -S $MYSQL_SOCKET -f drop $MYSQL_DB_NAME > /dev/null 2>&1
mysqladmin -P $MYSQL_PORT -u $MYSQL_USER -p$MYSQL_PASSWORD -S $MYSQL_SOCKET create $MYSQL_DB_NAME > /dev/null 2>&1