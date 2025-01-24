#!/usr/bin/env bash

#expected params $PACKAGE_NAME, ${PID_FILE}, $INSTALL_DIR, $LOG_DIR, ${CONFIG_INSTALL_DIR}, $BUILD_FOLDER_PATH, $CONFIG_DIR, $JAR_FOLDER_PATH, $ENVIRONMENT. $VERSION_PREFIX, $BUILD_NUMBER

# source of the properties:
#. "/build/build.properties"

	DEB_ROOT="${TEMP_PATH}/${PACKAGE_NAME}"
	echo "DEB_ROOT = ${DEB_ROOT}"

	TARGET_DIR="${TEMP_PATH}/debians"


	if [ -z $VERSION_PREFIX ]; then VERSION_PREFIX="1.0.0"; fi

	if [ -z $ARCH ]; then ARCH=all; fi

	VERSION_CONTROL=$VERSION_PREFIX.$BUILD_NUMBER
   	VERSION=$VERSION_PREFIX.$BUILD_NUMBER"_"$ARCH

	#Creating the required folders
	mkdir -p "${TARGET_DIR}"
	mkdir -p "${DEB_ROOT}"
	mkdir -p "${DEB_ROOT}/${INSTALL_DIR}"
	mkdir -p "${DEB_ROOT}/${LOG_DIR}"
	mkdir -p "${DEB_ROOT}/${CONFIG_INSTALL_DIR}"
	mkdir -p "${DEB_ROOT}/${CONFD_DIR}"
    mkdir -p "${DEB_ROOT}/${TEMPLATE_DIR}"

	#cd "${PROJECT_DIR}"

	echo "copying build folder contents to ${DEB_ROOT} folder"
	cp -a "${BUILD_FOLDER_PATH}/packaging/." "${DEB_ROOT}"
       
	echo "copying ${CONFIG_DIR}/${ENVIRONMENT} contents to ${DEB_ROOT}/${CONFIG_INSTALL_DIR}/ folder"
	cp -a "${CONFIG_DIR}/${ENVIRONMENT}/." "${DEB_ROOT}/${CONFIG_INSTALL_DIR}"

	echo "copying ${JAR_FOLDER_PATH} folder contents to $DEB_ROOT/${INSTALL_DIR} folder"
	cp -a "${JAR_FOLDER_PATH}/." "${DEB_ROOT}/${INSTALL_DIR}"

	echo "changing permisions of DEBIAN files"
    chmod 775 "${DEB_ROOT}/DEBIAN/control"
	chmod 775 "${DEB_ROOT}/DEBIAN/postinst"
	chmod 775 "${DEB_ROOT}/DEBIAN/postrm"
	chmod 775 "${DEB_ROOT}/DEBIAN/preinst"
	chmod 775 "${DEB_ROOT}/DEBIAN/prerm"


for file in "${DEB_ROOT}/DEBIAN/control" "${DEB_ROOT}/DEBIAN/postinst" "${DEB_ROOT}/DEBIAN/postrm" "${DEB_ROOT}/DEBIAN/preinst" "${DEB_ROOT}/DEBIAN/prerm" "${DEB_ROOT}/${INIT_PATH}"
do
echo $file
	sed -i -e "s:<PACKAGE_NAME>:${PACKAGE_NAME}:g" "${file}"
	sed -i -e "s:<INSTALL_DIR>:${INSTALL_DIR}:g" "${file}"
	sed -i -e "s:<LOG_DIR>:${LOG_DIR}:g" "${file}"
	sed -i -e "s:<INIT_PATH>:${INIT_PATH}:g" "${file}"
	sed -i -e "s:<ENVIRONMENT>:${ENVIRONMENT}:g" "${file}"
	sed -i -e "s:<CONFIG_INSTALL_DIR>:${CONFIG_INSTALL_DIR}:g" "${file}"
	sed -i -e "s:<CONFIG_LOCATION>:${CONFIG_LOCATION}:g" "${file}"
	sed -i -e "s:<PID_FILE>:${PID_FILE}:g" "${file}"
done


	#update the control file
	sed -i -e "s/<VERSION>/${VERSION_CONTROL}/g" "${DEB_ROOT}/DEBIAN/control"
	sed -i -e "s/<ARCH>/${ARCH}/g" "${DEB_ROOT}/DEBIAN/control"


	#cd "${DEB_ROOT}/.."
	echo "Creating debian on deb_root folder"
	dpkg-deb -b "${DEB_ROOT}"

	FILE_NAME="${PACKAGE_NAME}_${VERSION}.deb"
	rm -rf "${TARGET_DIR}/${PACKAGE_NAME}_*.deb"
	mv "${DEB_ROOT}/../${PACKAGE_NAME}.deb" "${TARGET_DIR}/${FILE_NAME}"
	echo "File name : ${FILE_NAME}"


    #cd "${TARGET_DIR}"
	#HOSTS="stage-wbuild1.ch.flipkart.com"
	openssl md5 "${TARGET_DIR}/${FILE_NAME}" | cut -f 2 -d " " > "${TARGET_DIR}/${FILE_NAME}.md5"

	echo "Upload the file and md5"


# for HOST in $HOSTS
# do
#   echo "Uploading ${FILE_NAME} to $HOST"
#   scp -p   "${TARGET_DIR}/${FILE_NAME}" $HOST:/var/data/ftp-all/apt/
#   scp -p   "${TARGET_DIR}/${FILE_NAME}.md5" $HOST:/var/data/ftp-all/apt/
#   echo "Disconnected from ftp server - $HOST. Upload Complete for ${FILE_NAME}"
