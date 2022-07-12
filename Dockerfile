FROM gradle:7.4.2-jdk11-alpine

EXPOSE 3001
WORKDIR /opt/workmotion
COPY ./ /opt/workmotion
CMD [ "gradle", "bootRun" ]
