set -x
java -jar ../out/artifacts/find_duplicates/find-duplicates.jar ./Album ./Pit ./Archive/ --all-files $@
