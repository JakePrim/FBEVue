const { truncate } = require('fs');
const path = require('path');

const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    mode: 'development',
    devtool: 'none',
    entry: './src/index.js',
    output: {
        filename: 'main.js',
        path: path.resolve('dist')
    },
    devServer: {
        port: 3000,
        hot: true,
    },
    module: {
        rules: [
            {
                test: /\.js|jsx$/,
                exclude: /node_modules/,
                use: [
                    {
                        loader: 'babel-loader',
                        options: {
                            presets: ['@babel/preset-env','@babel/preset-react']
                        }
                    }
                ]
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: './src/index.html'
        })
    ]
}