/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var webpack = require('webpack');
var path = require('path');
var webpackMerge = require('webpack-merge');
var CopyWebpackPlugin = require('copy-webpack-plugin');

// Webpack Config
var webpackConfig = {
  entry: {
    'main': './src/main.browser.aot.ts',
  },

  output: {
    publicPath: '',
    path: path.resolve(__dirname, './dist'),
  },

  plugins: [
    new webpack.optimize.UglifyJsPlugin({
      sourceMap: true
    }),
    new webpack.ContextReplacementPlugin(
      // The (\\|\/) piece accounts for path separators in *nix and Windows
      /angular(\\|\/)core(\\|\/)@angular/,
      path.resolve(__dirname, './src'),
      {
        // your Angular Async Route paths relative to this root directory
      }
    ),
    new webpack.ProvidePlugin({
      jQuery: 'jquery',
      $: 'jquery',
      jquery: 'jquery'
    }),
    new CopyWebpackPlugin([
        { from: 'src/assets' },
        { from: 'src/index.html' },
        { from: 'src/config.json' },
    ])
  ],

  module: {
    loaders: [
      // .ts files for TypeScript
      {
        test: /\.ts$/,
        loaders: [
          'awesome-typescript-loader?configFileName=tsconfig.aot.json',
          'angular2-template-loader',
          'angular2-router-loader'
        ]
      },
      { test: /\.css$/, loaders: ['to-string-loader', 'css-loader'] },
      { test: /\.html$/, loader: 'raw-loader' },
      {
        test: /\.scss$/,
        use: [{
            loader: "to-string-loader" // creates style nodes from JS strings
        }, {
            loader: "css-loader" // translates CSS into CommonJS
        }, {
            loader: "sass-loader" // compiles Sass to CSS
        }]
      },
      { test: /\.(woff2?|ttf|eot|svg)$/, loader: 'file-loader?name=fonts/[name].[ext]' },
      { test: /bootstrap\/dist\/js\/umd\//, loader: 'imports-loader?jQuery=jquery' }
    ]
  }

};


// Our Webpack Defaults
var defaultConfig = {
  devtool: 'source-map',

  output: {
    filename: '[name].bundle.js',
    sourceMapFilename: '[name].map',
    chunkFilename: '[id].chunk.js'
  },

  resolve: {
    extensions: [ '.ts', '.js' ],
    modules: [ path.resolve(__dirname, 'node_modules') ]
  },

  devServer: {
    contentBase: [path.resolve(__dirname, 'src'),path.resolve(__dirname, 'test/mocks')],
    historyApiFallback: true,
    watchOptions: { aggregateTimeout: 300, poll: 1000 },
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
      "Access-Control-Allow-Headers": "X-Requested-With, content-type, Authorization"
    }
  },

  node: {
    global: true,
    crypto: 'empty',
    __dirname: true,
    __filename: true,
    process: true,
    Buffer: false,
    clearImmediate: false,
    setImmediate: false
  }
};


module.exports = webpackMerge(defaultConfig, webpackConfig);
