#!/bin/bash
################################################################################
# Copyright (c) 2011-2012 EclipseSource Inc. and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
# 
# Contributors:
#     hmalphettes - initial API and implementation
#     hstaduacher - ongoing development
################################################################################
# Called once the tycho build has completed.
# Takes care of a few shortcomings in the current tycho build.
set -e

CURRENT_DIR=`pwd`

#Change to the packages directory starting from the directory of this script:
cd `dirname $0`/..
PACKAGES_FOLDER=`pwd`

echo "Running post-tycho-release.sh from $PACKAGES_FOLDER"
cd $PACKAGES_FOLDER

# Look for the linux and macos products,
# Look for the *.sh files there and change their permission to executable.
# tar.gz those products.
# For example:
# org.eclipse.rtp.packages/target/products/org.eclipse.rtp.package/linux/gtk/x86/rt-incubation-0.1.0.v20110308-1242-N
# is such a folder where the manips must take place.
BUILT_PRODUCTS="$PACKAGES_FOLDER/org.eclipse.rtp.packages/target/products"


#We choose to be mention by name each one of the product
# It is tedious and a bit redundant but we want to make sure that the expected folders
# are where they are supposed to be.
# If that is not the case let's fail the build quickly.
RT_HEADLESS_LINUX32_PRODUCT="$BUILT_PRODUCTS/org.eclipse.rtp.package.headless/linux/gtk/x86"
[ ! -d "$RT_HEADLESS_LINUX32_PRODUCT" ] && { echo "ERROR: unable to locate a built product $RT_HEADLESS_LINUX32_PRODUCT is not a folder"; exit 42; }
# Reads the name of the top level folder.
RT_HEADLESS_FOLDER_NAME=`find $RT_HEADLESS_LINUX32_PRODUCT -maxdepth 1 -mindepth 1 -type d -exec basename {} \;`
#get the version number from the folder name. it looks like this:
# rt-headless-incubation-0.1.0.v20110308-1653-N


# Now change the permission: NOT useful anymore with TYCHO-566 partially fixed.
find $RT_HEADLESS_LINUX32_PRODUCT/$RT_HEADLESS_FOLDER_NAME -maxdepth 1 -name *.sh -exec chmod +x {} \;

#Remove the executable launcher specific to linux32
# Delete eclipse executable
RT_HEADLESS_EXEC=$RT_HEADLESS_LINUX32_PRODUCT/$RT_HEADLESS_FOLDER_NAME/eclipse
[ -f "$RT_HEADLESS_EXEC" ] && rm $RT_HEADLESS_EXEC || echo "INFO: no native launcher to delete $RT_HEADLESS_EXEC"
# Delete libcairo-swt.so
RT_HEADLESS_SWT_LIB=$RT_HEADLESS_LINUX32_PRODUCT/$RT_HEADLESS_FOLDER_NAME/libcairo-swt.so
[ -f "$RT_HEADLESS_SWT_LIB" ] && rm $RT_HEADLESS_SWT_LIB || echo "INFO: no native launcher to delete $RT_HEADLESS_SWT_LIB"
# Delete native launcher folder
RT_HEADLESS_NATIVE_LAUNCHER=$RT_HEADLESS_LINUX32_PRODUCT/$RT_HEADLESS_FOLDER_NAME/plugins/`ls -1 $RT_HEADLESS_LINUX32_PRODUCT/$RT_HEADLESS_FOLDER_NAME/plugins | grep org.eclipse.equinox.launcher.gtk.linux.x86 | tail -n 1`
#[ -d "$RT_HEADLESS_NATIVE_LAUNCHER" ] && rm -rf $RT_HEADLESS_NATIVE_LAUNCHER || echo "INFO: no native launcher to delete $RT_HEADLESS_NATIVE_LAUNCHER"

# Now pack the whole thing
cd $RT_HEADLESS_LINUX32_PRODUCT
# Delete the source bunldes
rm -rf $RT_HEADLESS_LINUX32_PRODUCT/$RT_HEADLESS_FOLDER_NAME/plugins/*.source_*.jar
#Also zip the product. It will look better than the zip produced by tycho that contains the native launcher.
zip -r $RT_HEADLESS_FOLDER_NAME.zip $RT_HEADLESS_FOLDER_NAME/
mv $RT_HEADLESS_FOLDER_NAME.zip $BUILT_PRODUCTS/../
cd $PACKAGES_FOLDER

BUILD_VERSION=$(echo "$RT_HEADLESS_FOLDER_NAME" | sed 's/^rt-headless-incubation-//')

#Same for normal package:
RT_LINUX32_PRODUCT="$BUILT_PRODUCTS/org.eclipse.rtp.package/linux/gtk/x86"
[ ! -d "$RT_LINUX32_PRODUCT" ] && { echo "ERROR: unable to locate a built product $RT_LINUX32_PRODUCT is not a folder"; exit 42; }
RT_FOLDER_NAME=`find $RT_LINUX32_PRODUCT -maxdepth 1 -mindepth 1 -type d -exec basename {} \;`
find $RT_LINUX32_PRODUCT/$RT_FOLDER_NAME -maxdepth 1 -name *.sh -exec chmod +x {} \;
# Delete eclipse executable
RT_EXEC=$RT_LINUX32_PRODUCT/$RT_FOLDER_NAME/eclipse
[ -f "$RT_EXEC" ] && rm $RT_EXEC || echo "INFO: no native launcher to delete $RT_EXEC"
# Delete libcairo-swt.so
RT_SWT_LIB=$RT_LINUX32_PRODUCT/$RT_FOLDER_NAME/libcairo-swt.so
[ -f "$RT_SWT_LIB" ] && rm $RT_SWT_LIB || echo "INFO: no native launcher to delete $RT_SWT_LIB"
# Delete native launcher folder
RT_NATIVE_LAUNCHER=$RT_LINUX32_PRODUCT/$RT_FOLDER_NAME/plugins/`ls -1 $RT_LINUX32_PRODUCT/$RT_FOLDER_NAME/plugins | grep org.eclipse.equinox.launcher.gtk.linux.x86 | tail -n 1`

cd $RT_LINUX32_PRODUCT
rm -rf $RT_LINUX32_PRODUCT/$RT_FOLDER_NAME/plugins/*.source_*.jar
zip -r $RT_FOLDER_NAME.zip $RT_FOLDER_NAME/
mv $RT_FOLDER_NAME.zip $BUILT_PRODUCTS/../
cd $PACKAGES_FOLDER

#If we have many more products let's consider something more generic.

# Move one linux zip archive of each product to
# a location on download.eclipse.org where they can be downloaded.
# Move the generated p2 repository to a location on download.eclipse.org
# where they can be consumed.
DOWNLOAD_FOLDER=/home/data/httpd/download.eclipse.org/rtp/incubation
if [ ! -d "$DOWNLOAD_FOLDER" ]; then
#we are not on the eclipse build machine. for testing, let's
#deploy the build inside the builds folder of org.eclipse.rtp.releng
   DOWNLOAD_FOLDER="$PACKAGES_FOLDER/org.eclipse.rtp.releng/builds/download.eclipse.org/rtp/incubation"
   mkdir -p $DOWNLOAD_FOLDER
fi

# The p2 repository is already taken care of by the build.
# Although we should definitly take care of mataining a symbolic link to the latest or update
# a composite repository and may delete the old builds.
# check that the build identifier is defined and well known.
BUILD_IDENTIFIER=`echo "$BUILD_VERSION" | sed 's/^.*\(.\)$/\1/'`
if [ "$BUILD_IDENTIFIER" == "N" ]; then
  DOWNLOAD_P2_FOLDER="$DOWNLOAD_FOLDER/updates/3.8-N-builds"
  BUILD_IDENTIFIER_LABEL="Nightly"
  DO_PURGE="true"
elif [ "$BUILD_IDENTIFIER" == "I" ]; then
  DOWNLOAD_P2_FOLDER="$DOWNLOAD_FOLDER/updates/3.8-I-builds"
  BUILD_IDENTIFIER_LABEL="Integration"
  DO_PURGE="true"
elif [ "$BUILD_IDENTIFIER" == "S" ]; then
  DOWNLOAD_P2_FOLDER="$DOWNLOAD_FOLDER/updates/3.8milestones"
  BUILD_IDENTIFIER_LABEL="Stable"
elif [ "$BUILD_IDENTIFIER" == "R" ]; then
  DOWNLOAD_P2_FOLDER="$DOWNLOAD_FOLDER/updates/3.8"
  BUILD_IDENTIFIER_LABEL="Release"
else
  echo "Unknown build identifier: the last character in the version $BUILD_VERSION is not 'N', 'I', 'S' or 'R'"
  echo "The build identifier was $BUILD_IDENTIFIER"
  exit 42
fi
mkdir -p $DOWNLOAD_P2_FOLDER

#remove the last 2 characters to get the version number without build identifier.
#This will make it easier to promote an N build to a I or S build.
BUILD_VERSION_NO_BUILD_IDENTIFIER=$(echo "$BUILD_VERSION" | sed 's/.\{2\}$//')

#Folder where the product archives are placed.
DOWNLOAD_PRODUCTS_FOLDER=$DOWNLOAD_P2_FOLDER/$BUILD_VERSION_NO_BUILD_IDENTIFIER

echo "Deploying the p2 repository in $DOWNLOAD_P2_FOLDER"
[ -d "$BUILT_PRODUCTS/../$BUILD_VERSION_NO_BUILD_IDENTIFIER" ] && rm -rf "$BUILT_PRODUCTS/../$BUILD_VERSION_NO_BUILD_IDENTIFIER"
[ -d "$DOWNLOAD_P2_FOLDER/$BUILD_VERSION_NO_BUILD_IDENTIFIER" ] && rm -rf "$DOWNLOAD_P2_FOLDER/$BUILD_VERSION_NO_BUILD_IDENTIFIER"
cp -r "$BUILT_PRODUCTS/../repository" "$BUILT_PRODUCTS/../$BUILD_VERSION_NO_BUILD_IDENTIFIER"
mv "$BUILT_PRODUCTS/../$BUILD_VERSION_NO_BUILD_IDENTIFIER" "$DOWNLOAD_P2_FOLDER"

#echo "Create the symbolic link 'current' to the p2 repository... 
#this does not work on eclipse server as the http server does not follow symbolic links."
#make sure that the symbolic link is a relative path. so it can be move arround, mirrored
#etc as long as the p2repo folder is also moved around and mirrored at the same time.
#cd $DOWNLOAD_P2_FOLDER
#[ -h "current"] && rm "current"
#ln -s $BUILD_VERSION_NO_BUILD_IDENTIFIER "current"
#back to the original directory before we exit:
#cd $CURRENT_DIR


echo "Deploying the archived products in $DOWNLOAD_PRODUCTS_FOLDER"
mkdir -p $DOWNLOAD_PRODUCTS_FOLDER
echo "$BUILT_PRODUCTS/../$RT_HEADLESS_FOLDER_NAME.zip"
cp $BUILT_PRODUCTS/../$RT_HEADLESS_FOLDER_NAME.zip $DOWNLOAD_PRODUCTS_FOLDER
echo "$BUILT_PRODUCTS/../$RT_FOLDER_NAME.zip"
cp $BUILT_PRODUCTS/../$RT_FOLDER_NAME.zip $DOWNLOAD_PRODUCTS_FOLDER

TIMESTAMP=`date +%s`
TIMESTAMP_FORMATTED=`date -d "1970-01-01 UTC + $TIMESTAMP seconds"`

echo "Generating the index.html for the p2 repository."
echo "<html>
  <head>
    <title>Eclipse RTP build $BUILD_VERSION_NO_BUILD_IDENTIFIER</title>
    <link rel=\"icon\" type=\"image/png\" href=\"http://eclipse.org/rtp/images/favicon.png\" />
  </head>
  <body>
    <h2>Eclipse RTP build $BUILD_VERSION_NO_BUILD_IDENTIFIER</h2>
    <p>This is a p2 repository built on $TIMESTAMP_FORMATTED.<br/>
    It contains the Eclipse Packages.
    Point PDE or p2-driector or maven-tycho at the current url to start installing products and features published here</p>
    <p>Product archives:
       <ul>
         <li><a href=\"$RT_HEADLESS_FOLDER_NAME.zip\">$RT_HEADLESS_FOLDER_NAME.zip</a></li>
         <li><a href=\"$RT_FOLDER_NAME.zip\">$RT_FOLDER_NAME.zip</a></li>
       </ul>
    </p>
    <p>See <a href=\"http://eclipse.org/rtp\">Eclipse RTP</a></p>
  </body>
</html>" > "$DOWNLOAD_PRODUCTS_FOLDER/index.html"

echo "Generating the composite repository in $DOWNLOAD_P2_FOLDER"
echo "<html>
  <head>
    <title>Eclipse RTP current $BUILD_IDENTIFIER_LABEL build</title>
    <link rel=\"icon\" type=\"image/png\" href=\"http://eclipse.org/rtp/images/favicon.png\" />
  </head>
  <body>
    <h2>Eclipse RTP current $BUILD_IDENTIFIER_LABEL build. </h2>
    <p>This is a composite p2 repository that point to the current $BUILD_IDENTIFIER_LABEL repository.<br/>
      It was updated on $TIMESTAMP_FORMATTED.
    </p>
    <p>
      The current $BUILD_IDENTIFIER_LABEL build and product archives are located here: <a href=\"$BUILD_VERSION_NO_BUILD_IDENTIFIER\">$BUILD_VERSION_NO_BUILD_IDENTIFIER</a>
    </p>
    <p>See <a href=\"http://eclipse.org/rtp\">Eclipse RTP</a></p>
  </body>
</html>" > "$DOWNLOAD_P2_FOLDER/index.html"
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<?compositeArtifactRepository version=\"1.0.0\"?>
<repository name=\"&quot;Eclipse RTP $BUILD_IDENTIFIER_LABEL&quot;\"
    type=\"org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository\" version=\"1.0.0\">
  <properties size=\"1\">
    <property name=\"p2.timestamp\" value=\"$TIMESTAMP\"/>
  </properties>
  <children size=\"1\">
    <child location=\"$BUILD_VERSION_NO_BUILD_IDENTIFIER\"/>
  </children>
</repository>" > "$DOWNLOAD_P2_FOLDER/artifacts.xml"
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<?compositeMetadataRepository version=\"1.0.0\"?>
<repository name=\"&quot;Eclipse RTP $BUILD_IDENTIFIER_LABEL&quot;\"
    type=\"org.eclipse.equinox.internal.p2.metadata.repository.CompositeMetadataRepository\" version=\"1.0.0\">
  <properties size=\"1\">
    <property name=\"p2.timestamp\" value=\"$TIMESTAMP\"/>
  </properties>
  <children size=\"1\">
    <child location=\"$BUILD_VERSION_NO_BUILD_IDENTIFIER\"/>
  </children>
</repository>" > "$DOWNLOAD_P2_FOLDER/content.xml"


if [ -n "$DO_PURGE" ]; then
  echo "Purging the old builds as this is an N or I build."
  cd $DOWNLOAD_P2_FOLDER
  nbFiles=`ls -l |wc -l`
  #This will keep only the 10 oldest builds
  while [ 10 -le $nbFiles ]
  do
    #fileToDelete=`ls -tr | head -1`
    fileToDelete=`ls -trd */ | head -1 | cut -f 1 -d /`
    echo "Purging the oldest file: $fileToDelete"
    rm -rf $fileToDelete
    nbFiles=`ls -l |wc -l`
  done
  cd $CURRENT_DIR
fi