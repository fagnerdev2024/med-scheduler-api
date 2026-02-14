FROM ubuntu:latest
LABEL authors="fagne"

ENTRYPOINT ["top", "-b"]