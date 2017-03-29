#!/bin/bash

ACTION=$1;

function current_branch_name() {
    branch=$(git branch | sed -n -e 's/^\* \(.*\)/\1/p')
	echo $branch;
}

if [ "$ACTION" == "new-feature" ]; then
  echo "Current branch: $(current_branch_name)"
  FEATURE_NAME=$2;
  TARGET_VERSION=$3;
  DATE=`date +%Y%m%d`
  echo $DATE
  echo $FEATURE_NAME
  echo $TARGET_VERSION

  if [ "$(current_branch_name)" != "develop" ]; then
    force_flag=$4;
    if [ "$force_flag" != "--force" ]; then
        echo "ERROR: Feature branch should be created from develop branch. Use --force for ignore"
        exit
    fi
  fi

  if grep '^[-0-9a-zA-Z]*$' -v <<<$FEATURE_NAME ;
  then
    echo "ERROR: Feature name '$FEATURE_NAME' must match pattern: ^[-0-9a-zA-Z]*$"
    exit
  fi

  if grep -v -E '^[0-9]+\.[0-9]+\.[0-9]+$' <<<$TARGET_VERSION ;
  then
    echo "ERROR: Target version '$TARGET_VERSION' must match pattern: ^[0-9]+\.[0-9]+\.[0-9]+$"
    exit
  fi

  NEW_BRANCH_NAME='feature/'$FEATURE_NAME'_'$TARGET_VERSION'_'$DATE
  echo $NEW_BRANCH_NAME
  git checkout -b $NEW_BRANCH_NAME

  NEW_VERSION="$TARGET_VERSION-$FEATURE_NAME-SNAPSHOT"
  echo $NEW_VERSION

#TODO
#  find . -type f -name pom.xml -execdir awk "NR==1,/<version>.*<\/version>/{sub(/<version>.*<\/version>/, \"<version>$NEW_VERSION<\/version>\")} 1" {} > tmp \;

#  find . -type f -name pom.xml -execdir sh -c "awk \"NR==1,/<version>.*<\/version>/{sub(/<version>.*<\/version>/, \"<version>$NEW_VERSION<\/version>\")} 1\" {} > tmp" \;


#  find . -type f -name pom.xml -exec echo {}\;

#  find . -type f -name pom.xml -execdir sh -c "awk \"NR==1,/<version>.*<\/version>/{sub(/<version>.*<\/version>/, \"<version>$NEW_VERSION<\/version>\")} 1\" {} > tmp" \;



#  awk "NR==1,/<version>.*<\/version>/{sub(/<version>.*<\/version>/, \"<version>$NEW_VERSION<\/version>\")} 1" pom.xml > tmp
#  mv tmp pom.xml
#  rm tmp

#  sed -i "s/<version>.*<\/version>/<version>$NEW_VERSION<\/version>/" pom.xml

#  mvn -f jbb-bom versions:set -DnewVersion=$NEW_VERSION
#  mvn -f jbb-build-tools versions:set -DnewVersion=$NEW_VERSION
#  mvn versions:set -DnewVersion=$NEW_VERSION

fi