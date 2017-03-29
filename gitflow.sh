#!/bin/bash

DATE=`date +%Y%m%d`;

MASTER_BRANCH_NAME="master";
DEVELOP_BRANCH_NAME="develop";

NEW_FEATURE_ACTION="new-feature";
NEW_HOTFIX_ACTION="new-hotfix";
NEW_RELEASE_ACTION="new-release";

FORCE_FLAG="--force";

ACTION=$1;

function current_branch_name() {
    branch=$(git branch | sed -n -e 's/^\* \(.*\)/\1/p')
	echo $branch;
}

# $1 - pom.xml absolute path
# $2 - new version value
function update_version_in_pom() {
    awk "NR==1,/<version>.*<\/version>/{sub(/<version>.*<\/version>/, \"<version>$2<\/version>\")} 1" $1
}
export -f update_version_in_pom

# $1 - new branch name
function create_new_branch() {
    git checkout -b $1
}

# $1 - branch name
function make_init_commit() {
    git commit -a -m "[gitflow] init branch $1"
}

# $1 - branch name
function push_origin() {
    git push --set-upstream origin $1
}

# $1 - new pom project version value
function replace_all_poms_project_version() {
    find . -type f -name pom.xml -execdir bash -c 'update_version_in_pom "$0" '$1' > tmp && mv tmp $0' {} \;
}

function validate_branching_from_current_branch_possibility() {
  local SOURCE_BRANCH_NAME=$1;
  local FORCE_FLAG_VALUE=$2;

  if [ "$(current_branch_name)" != "$SOURCE_BRANCH_NAME" ]; then
    if [ "$FORCE_FLAG_VALUE" != "$FORCE_FLAG" ]; then
        echo "ERROR: Branch should be created from $SOURCE_BRANCH_NAME branch. Use --force $FORCE_FLAG for forcing creation from current branch"
        exit
    fi
  fi
}

# $1 - feature name
function validate_feature_name() {
  if grep '^[-0-9a-zA-Z]*$' -v <<<$1 ;
  then
    echo "ERROR: Feature name '$1' must match pattern: ^[-0-9a-zA-Z]*$"
    exit
  fi
}

# $1 - hotfix name
function validate_hotfix_name() {
  if grep -v -E '^[0-9]+$' <<<$1 ;
  then
    echo "ERROR: Hotfix name '$1' must match pattern: ^[0-9]+$"
    exit
  fi
}

# $1 - version value
function validate_semantic_version() {
  if grep -v -E '^[0-9]+\.[0-9]+\.[0-9]+$' <<<$1 ;
  then
    echo "ERROR: Version '$1' must match pattern: ^[0-9]+\.[0-9]+\.[0-9]+$"
    exit
  fi
}

function prepare_new_branch_for_development() {
  local NEW_BRANCH_NAME=$1;
  local NEW_VERSION=$2;

  create_new_branch $NEW_BRANCH_NAME
  replace_all_poms_project_version $NEW_VERSION
  make_init_commit $NEW_BRANCH_NAME
  push_origin $NEW_BRANCH_NAME
}

function new_feature_branch() {
  local FEATURE_NAME=$1;
  local TARGET_VERSION=$2;
  local FORCE_FLAG_VALUE=$3;

  validate_branching_from_current_branch_possibility $DEVELOP_BRANCH_NAME $FORCE_FLAG_VALUE

  validate_feature_name $FEATURE_NAME
  validate_semantic_version $TARGET_VERSION

  NEW_BRANCH_NAME='feature/'$FEATURE_NAME'_'$TARGET_VERSION'_'$DATE
  NEW_VERSION="$TARGET_VERSION-$FEATURE_NAME-SNAPSHOT"

  prepare_new_branch_for_development $NEW_BRANCH_NAME $NEW_VERSION
}

function new_hotfix_branch() {
  local HOTFIX_NAME=$1;
  local TARGET_VERSION=$2;
  local FORCE_FLAG_VALUE=$3;

  validate_branching_from_current_branch_possibility $MASTER_BRANCH_NAME $FORCE_FLAG_VALUE

  validate_hotfix_name $HOTFIX_NAME
  validate_semantic_version $TARGET_VERSION

  NEW_BRANCH_NAME='hotfix/'$HOTFIX_NAME'_'$TARGET_VERSION'_'$DATE
  NEW_VERSION="$TARGET_VERSION-FIX-$HOTFIX_NAME-SNAPSHOT"

  prepare_new_branch_for_development $NEW_BRANCH_NAME $NEW_VERSION
}

function new_release_branch() {
  local RELEASE_VERSION=$1;
  local FORCE_FLAG_VALUE=$2;

  validate_branching_from_current_branch_possibility $DEVELOP_BRANCH_NAME $FORCE_FLAG_VALUE

  validate_semantic_version $RELEASE_VERSION

  NEW_BRANCH_NAME='release/'$RELEASE_VERSION'_'$DATE
  NEW_VERSION="$RELEASE_VERSION-SNAPSHOT"

  prepare_new_branch_for_development $NEW_BRANCH_NAME $NEW_VERSION
}

function run_action() {
    if [ "$ACTION" == "$NEW_FEATURE_ACTION" ]; then
      FEATURE_NAME=$1;
      TARGET_VERSION=$2;
      FORCE_FLAG_VALUE=$3;
      new_feature_branch $FEATURE_NAME $TARGET_VERSION $FORCE_FLAG_VALUE

    elif [ "$ACTION" == "$NEW_HOTFIX_ACTION" ]; then
      HOTFIX_NAME=$1;
      TARGET_VERSION=$2;
      FORCE_FLAG_VALUE=$3;
      new_hotfix_branch $HOTFIX_NAME $TARGET_VERSION $FORCE_FLAG_VALUE

    elif [ "$ACTION" == "$NEW_RELEASE_ACTION" ]; then
      RELEASE_VERSION=$1;
      FORCE_FLAG_VALUE=$2;
      new_release_branch $RELEASE_VERSION $FORCE_FLAG_VALUE

    else
      echo "ERROR: Unknown action: $ACTION"
    fi
}

run_action $2 $3 $4