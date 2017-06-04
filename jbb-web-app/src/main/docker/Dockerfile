#######################################################################
#
# Dockerfile for build image with tomcat and jBB application deployment
#
#######################################################################

FROM tomcat:8.5.9-jre8

MAINTAINER project.jbb@gmail.com

# Update the repository sources list once more
RUN apt-get update

# Remove default ROOT application
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy artifact to tomcat webapps directory as ROOT.war
ADD *.war /usr/local/tomcat/webapps/ROOT.war

# Expose the default tomcat port
EXPOSE 8080

# Run tomcat
CMD ["catalina.sh", "run"]