#!/bin/bash
################################################################################
# Copyright (c) 2011-2012 EclipseSource Inc. and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
# 
# Contributors:
#     hstaduacher - initial API and implementation
################################################################################
#
# Tool to maintain composite repositories

SCRIPTS_DIR=$(dirname $(readlink -nm $0))
DOWNLOAD_DIR=/home/data/httpd/download.eclipse.org/rtp/incubation/updates

if [ -z "$RUNTIME_DIR" ]; then
  RUNTIME_DIR=/shared/technology/rtp/eclipse-3.6.2
fi

mode=
repoDir=
repoName=

fail() {
  echo Composite Repository Tool
  if [ $# -gt 0 ]; then
    echo "Error: $1"
  fi
  echo "Usage:"
  echo "  $0 repo-dir create <repo name>"
  echo "  $0 repo-dir add <child>"
  echo "  $0 repo-dir remove <child>"
  echo
  echo "Example:"
  echo "  $0 3.8milestones create \"RTP 1.0 Repository\""
  echo "  $0 3.8milestones 1.0.0.v20111216-1245 add M1"
  exit 1
}

# Check command line
if [ $# -ne 3 ]; then
  fail "Wrong # of paramters"
fi

repoDir=$1
if [ ${repoDir:0:1} != "/" ]; then
  repoDir="$DOWNLOAD_DIR/$repoDir"
fi
if [ ! -d "$repoDir" ]; then
  fail "Repository does not exist: $repoDir"
fi

mode=$2
if [ "$mode" == "create" ]; then
  repoName=$3
elif [ "$mode" == "add" -o "$mode" == "remove" ]; then
  repoName=$3
  if [ ${repoName:0:7} != "http://" -a ! -d "$repoDir/$repoName" ]; then
    fail "Child to add/remove does not exist: $repoDir/$repoName"
  fi
else
  fail "Illegal mode: $mode"
fi

# Find PDE build
pdeBuild=`ls -1 $RUNTIME_DIR/plugins | grep pde.build_ | tail -n 1`
echo "Using PDE Build: $pdeBuild"

# Find Equinox launcher
launcher=$RUNTIME_DIR/plugins/`ls -1 $RUNTIME_DIR/plugins | grep launcher_ | tail -n 1`
echo "Using Equinox launcher: $launcher"

java -cp $launcher org.eclipse.core.launcher.Main \
    -application org.eclipse.ant.core.antRunner \
    -buildfile "$SCRIPTS_DIR/comp-repo.xml" \
    -DrepoDir="$repoDir" \
    -DrepoName="$repoName" \
    $mode \
  || fail