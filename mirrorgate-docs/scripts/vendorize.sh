#!/usr/bin/env bash

pushd bower_components
for pattern in '*.min.*' '*.eot' '*.svg' '*.ttf' '*.woff*'
do
    find */dist -name $pattern -exec cp --parents \{\} ../src/vendor \;
done
popd
