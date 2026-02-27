# Set the DB name (without extension) via environment variable, defaulting to "test"
DB_BASE_NAME="${DB_FILE_BASE:-test}"
DB_PATH="/usr/local/tomcat/db/${DB_BASE_NAME}.db"

# Export DB_PATH as an environment variable for the application
export DB_PATH

/usr/bin/sqlite3 "$DB_PATH" < /usr/local/tomcat/dbfile/CreateLogTable.sql
/usr/local/tomcat/bin/catalina.sh run