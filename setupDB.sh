export PATH=$HOME/programs/usql/$USQL_VERSION:$PATH
usql "mysql://$MYSQL_USER:$MYSQL_PASSWORD@${MYSQL_IP}:$MYSQL_PORT/mysql" -c "CREATE DATABASE \`$MYSQL_DB_NAME\`;" > /dev/null 2>&1