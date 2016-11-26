const path = require('path');
const webpack = require('webpack')
const merge = require('webpack-merge');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const PATHS = {
    app: path.join(__dirname, 'src', 'ui'),
    index: path.join(__dirname, 'src', 'ui', 'index.tpl'),
    output: path.join(__dirname, 'src', 'main', 'resources', 'public')
};

var finalConfig;

const common = {
    entry: {
        app: PATHS.app
    },
    output: {
        path: PATHS.output,
        filename: '[name].js'
    },
    plugins: [
        new HtmlWebpackPlugin({
            title: 'Shakin\' bet',
            template: PATHS.index
        })
    ],
    module: {
        loaders: [
            {
                test: /\.vue$/,
                loader: 'vue'
            },
        ]
    }
}

const devConfig = {
    // Use HMR in dev env
    devServer: {
        hot: true,
        inline: true,
        // Make sure, API requests are proxied to cherrypy server
        proxy: {
            '/v1': {
                target: 'http://localhost:8090',
                secure: false,
            }
        }
    },
    plugins: [
        new webpack.HotModuleReplacementPlugin()
    ]
}

switch(process.env.npm_lifecycle_event) {
    case 'build-dev':
        finalConfig = merge(common, devConfig)
        break
    case 'build':
    default:
        finalConfig = common;
}

module.exports = finalConfig;
