#!/bin/bash

DATE=`date +%Y%m%d`;
ACTION=$1;

function current_branch_name() {
    branch=$(git branch | sed -n -e 's/^\* \(.*\)/\1/p')
	echo $branch;
}

function update_version_in_pom() {
    awk "NR==1,/<version>.*<\/version>/{sub(/<version>.*<\/version>/, \"<version>$2<\/version>\")} 1" $1
}

export -f update_version_in_pom

if [ "$ACTION" == "new-feature" ]; then
  FEATURE_NAME=$2;
  TARGET_VERSION=$3;

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
  NEW_VERSION="$TARGET_VERSION-$FEATURE_NAME-SNAPSHOT"

  git checkout -b $NEW_BRANCH_NAME

  find . -type f -name pom.xml -execdir bash -c 'update_version_in_pom "$0" '$NEW_VERSION' > tmp && mv tmp $0' {} \;

  git commit -a -m "[gitflow] init branch $NEW_BRANCH_NAME"
  git push --set-upstream origin $NEW_BRANCH_NAME

fi