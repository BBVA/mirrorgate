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

const webpack = require('webpack');
const path = require('path');
const webpackMerge = require('webpack-merge');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const HtmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

// Webpack Config
var webpackConfig = {

  mode: process.env.PRODUCTION ? 'production' : 'development',

  entry: {
    'polyfills': './src/polyfills.ts',
    'main': './src/main.ts'
  },

  output: {
    filename: '[name].[chunkhash].js',
    sourceMapFilename: '[name].map',
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
  },

  plugins: [
    new webpack.ContextReplacementPlugin(
      /\@angular(\\|\/)core(\\|\/)esm5/,
      path.resolve(__dirname, './src')
    ),
    new webpack.ProvidePlugin({
      $: "jquery",
      jQuery: "jquery",
      "window.jQuery": "jquery",
      Popper: ['popper.js', 'default']
    }),
    new CopyWebpackPlugin([
        { from: 'src/assets' },
        { from: 'src/index.html' },
        { from: 'src/config.json' },
        { from: 'src/texts.json' },
        { from: 'node_modules/roboto-fontface/fonts/roboto/', to: 'vendor/roboto/fonts/roboto', flatten: true },
        { from: 'node_modules/roboto-fontface/css/roboto/*.css', to: 'vendor/roboto/css/roboto', flatten: true },
        { from: 'node_modules/@fortawesome/fontawesome-free/css/all.min.css', to: 'vendor/fontawesome/css', flatten: true },
        { from: 'node_modules/@fortawesome/fontawesome-free/webfonts', to: 'vendor/fontawesome/webfonts', flatten: true },
        { from: 'node_modules/bootstrap/dist/css/bootstrap.min.*', to: 'vendor/bootstrap', flatten: true },
        { from: 'node_modules/dragula/dist/*.min.css', to: 'vendor/dragula', flatten: true }
    ]),
    new MiniCssExtractPlugin({
      filename: "[name].css",
      chunkFilename: "[id].css"
    }),
    new HtmlWebpackPlugin({template: 'src/index.html'})
  ],

  module: {
    rules: [
      {
        test: /\.ts$/,
        use: ['awesome-typescript-loader', 'angular2-template-loader', 'angular2-router-loader']
      },
      {
        test: /\.css$/,
        use: [MiniCssExtractPlugin.loader, 'css-loader']
      },
      { test: /\.html$/, loader: 'html-loader' },
      {
        test: /\.scss$/,
        use: ['to-string-loader', 'css-loader' , 'sass-loader']
      },
      { test: /\.(woff2?|ttf|eot|svg)$/, loader: 'file-loader?name=fonts/[name].[ext]' }
    ]
  }

};


if (process.env.PRODUCTION) {

  var productionConfig = {
    devtool: 'source-map',

    optimization: {
      splitChunks: {
        chunks: 'all',
        minSize: 0
      },
      minimizer: [
        new UglifyJsPlugin({
          cache: true,
          parallel: true
        })
      ]
    }
  };

  webpackConfig = webpackMerge(productionConfig, webpackConfig);
}

module.exports = webpackConfig;
