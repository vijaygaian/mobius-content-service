#!/bin/bash

# Define variables
SPRING_APPLICATION_NAME="${SPRING_APPLICATION_NAME}"
CRIU_DUMP_DIR="/mnt/data/${SPRING_APPLICATION_NAME}"

# Main script execution

# Step 1: Attempt to restore from an existing CRIU dump
if [ -d "${CRIU_DUMP_DIR}" ] && [ "$(ls -A ${CRIU_DUMP_DIR})" ]; then
    echo "Restoring from existing CRIU dump..."
    cp -r ${CRIU_DUMP_DIR}/tmp/ / && criu restore -D ${CRIU_DUMP_DIR} --tcp-close --shell-job #-v5
    RESTORE_STATUS=$?

    if [ $RESTORE_STATUS -eq 0 ]; then
        echo "Restore succeeded."
        exit 0  # Exit script after successful restore
    else
        echo "Restore failed. Attempting to create a new dump..."
        rm -rf ${CRIU_DUMP_DIR}/*
    fi
else
    echo "No existing CRIU dump found, starting fresh."
    rm -rf ${CRIU_DUMP_DIR}/*
fi

# Step 2: Start the Java process
echo "Starting Java process..."
/opt/zulu21.34.19-ca-crac-jdk21.0.3-linux_x64/bin/java $JVM_OPTIONS -jar ${SPRING_APPLICATION_NAME}.jar &
sleep 45  # Wait for the Java process to start
PID=$(jps | grep ${SPRING_APPLICATION_NAME}.jar | awk '{print $1}')
if [ -z "$PID" ]; then
    echo "Failed to start Java process."
    exit 1
fi
echo "Java process started with PID: $PID"

# Step 3: Create the CRIU dump directory if it doesn't exist
if [ ! -d "${CRIU_DUMP_DIR}" ]; then
    echo "Creating CRIU dump directory: ${CRIU_DUMP_DIR}"
    mkdir -p "${CRIU_DUMP_DIR}"
fi

# Step 4: Attempt to create a new CRIU dump until it succeeds
while true; do
    echo "Creating new CRIU dump..."
    criu dump -t "$PID" -D ${CRIU_DUMP_DIR} --shell-job --leave-running --tcp-established #-v5
    DUMP_STATUS=$?

    if [ $DUMP_STATUS -eq 0 ]; then
        echo "CRIU dump succeeded."
        cp -r /tmp ${CRIU_DUMP_DIR}
        ls ${CRIU_DUMP_DIR}
        break  # Exit loop on success
    else
        echo "CRIU dump failed. Retrying until successful..."
        sleep 10  # Optional: Add a delay before retrying to avoid tight loop
    fi
done

# Final message indicating that the script has completed successfully
echo "Script completed successfully."
exit 0
