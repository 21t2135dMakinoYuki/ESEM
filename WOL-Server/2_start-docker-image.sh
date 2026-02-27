if [ $# -ne 1 ]; then
    echo Argument error! Please specify the mount destination using an absolute path. $*
    exit 1
fi
docker run --name woltool -p 8080:8080 --mount type=bind,src=$1,dst=/usr/local/tomcat/db -d wol:latest
echo docker started, run on $1
