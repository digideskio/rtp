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

RUNTIME_DIR=/shared/technology/rtp/eclipse-3.6.2

mode=
repoDir=
repoName=

usage() {
  echo "Usage:"
  echo "  $0 repo-dir create <repo name>"
  echo "  $0 repo-dir add <child>"
  echo "  $0 repo-dir remove <child>"
  echo
  echo "Example:"
  echo "  $0 /path/to/repository create \"Sunflower Repository\""
  echo "  $0 /path/to/repository add M1-2025-01-06-1345"
}

fail() {
  echo Composite Repository Tool
  if [ $# -gt 0 ]; then
    echo "Error: $1"
  fi
  usage
  exit 1
}

if [ -z "$RUNTIME_DIR" ]; then
  fail "Missing RUNTIME_DIR, must point to an Eclipse installation"
fi

if [ ! -d "$RUNTIME_DIR/plugins" ]; then
  fail "Invalid RUNTIME_DIR: $RUNTIME_DIR, must point to an Eclipse installation"
fi

# Check command line
if [ $# -ne 3 ]; then
  fail "Wrong # of paramters"
fi

repoDir=$1
if [ ! -d "$repoDir" ]; then
  fail "Repository does not exist: $repoDir"
fi

mode=$2
if [ "$mode" != "create" -a "$mode" != "add" -a "$mode" != "remove" ]; then
  fail "Illegal mode: $mode"
fi

repoName=$3

# Find Equinox launcher
launcher=$RUNTIME_DIR/plugins/`ls -1 $RUNTIME_DIR/plugins 2> /dev/null | grep launcher_ | tail -n 1`
echo "Using Equinox launcher: $launcher"

cat > /tmp/comp-repo-rtp.xml <<EOM
<?xml version="1.0" encoding="UTF-8"?>
<project name="p2 composite repositories helper">

  <target name="create">
    <mkdir dir="${repoDir}"/>
    <p2.composite.repository>
      <repository compressed="true" name="${repoName}" location="${repoDir}" />
    </p2.composite.repository>
  </target>

  <target name="add">
    <p2.composite.repository>
      <repository compressed="true" location="${repoDir}" />
      <add>
        <repository location="${repoName}" />
      </add>
    </p2.composite.repository>
  </target>

  <target name="remove">
    <p2.composite.repository>
      <repository compressed="true" location="${repoDir}" />
      <remove>
        <repository location="${repoName}" />
      </remove>
    </p2.composite.repository>
  </target>

</project>
EOM

java -cp $launcher org.eclipse.core.launcher.Main \
    -application org.eclipse.ant.core.antRunner \
    -buildfile "/tmp/comp-repo-rtp.xml" \
    -DrepoDir="$repoDir" \
    -DrepoName="$repoName" \
    $mode \
  || fail