#!/usr/bin/env bash
#
# Copyright (C) 2015-2016 Orange
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# http://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -ev

if [ "${TRAVIS_PULL_REQUEST}" = "false" -a "$TRAVIS_BRANCH" = "master" ]
then
	RELEASE_CANDIDATE_VERSION=$(cat RELEASE_CANDIDATE_VERSION)
	export REPO_NAME=$(expr ${TRAVIS_REPO_SLUG} : ".*\/\(.*\)")
	export TAG_NAME="releases/$RELEASE_CANDIDATE_VERSION"
	echo "TAG_NAME: $TAG_NAME"
	export RELEASE_NAME=$(expr "$RELEASE_CANDIDATE_VERSION" : "\(.*\)-SNAP.*")
	export TAG_DESC="Generated tag from TravisCI for build $TRAVIS_BUILD_NUMBER - $TAG_NAME. Binaries available as maven dependencies "'[ ![Set me Up !](https://img.shields.io/badge/JCenter Bintray-Set me Up !-orange.svg) ] (https://bintray.com/package/buildSettings?pkgPath=/elpaaso/maven/'"$REPO_NAME)and also here "'[ ![Download](https://img.shields.io/badge/Download-'$RELEASE_NAME'-blue.svg) ](https://bintray.com/elpaaso/maven/'"$REPO_NAME/$RELEASE_NAME/)"

	echo "Extracted Travis repo name: $REPO_NAME"

    JFROG_PROMOTION_URL=http://oss.jfrog.org/api/plugins/build/promote/snapshotsToBintray/$REPO_NAME/${TRAVIS_BUILD_NUMBER}
	echo "$JFROG_PROMOTION_URL">JFrogPromotion.url

	git status
	git checkout -b "release-candidate/$RELEASE_NAME"
	git add JFrogPromotion.url
	git commit -a -m "Update for release-candidate/$RELEASE_NAME"
	git status
    git push --set-upstream origin "release-candidate/$RELEASE_NAME"
	export GITHUB_DATA='{"tag_name":"'$TAG_NAME'","target_commitish":"release-candidate/'$RELEASE_NAME'","name":"'$RELEASE_NAME'","body":"'$TAG_DESC'","draft": true,"prerelease": true}'
	echo "Github data: $GITHUB_DATA"
	sleep 10
	curl --silent -X POST --data "$GITHUB_DATA" https://$GH_TOKEN@api.github.com/repos/orange-cloudfoundry/$REPO_NAME/releases
fi