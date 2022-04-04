#!/bin/bash
# USE https://github.com/ldbc/ldbc_snb_datagen_spark v0.3.3
rm -f params.ini
ln -s ./params-csv-composite-merge-foreign-30.ini params.ini
./run.sh
mkdir SF30_Composite_Merge_Foreign_Stream16
mv *.log social_network substitution_parameters SF30_Composite_Merge_Foreign_Stream16
rm -f params.ini
ln -s ./params-csv-composite-merge-foreign-100.ini params.ini
./run.sh
mkdir SF100_Composite_Merge_Foreign_Stream16
mv *.log social_network substitution_parameters SF100_Composite_Merge_Foreign_Stream16
