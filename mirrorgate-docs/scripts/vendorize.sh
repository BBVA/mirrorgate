#!/usr/bin/env bash

pushd node_modules

for library in 'jquery' 'bootstrap'
do
    for pattern in '*.min.*' '*.eot' '*.svg' '*.ttf' '*.woff*'
    do
        find */dist -name $library*$pattern -exec cp --parents \{\} ../src/vendor \;
    done
done

# Include Fortawesome dependencies
for pattern in '*.min.*' '*.eot' '*.svg' '*.ttf' '*.woff*'
do
    find @fortawesome -name $pattern -exec cp --parents \{\} ../src/vendor \;
done

popd
