const path = require("path");
const webpack = require("webpack");
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const bundleOutputDir = "../webapp/dist";
const srcDir = "./src";
const outPath = path.join(__dirname, bundleOutputDir);

module.exports = env => {
  const isDevBuild = !(env && env.prod);
  return [
    {
      stats: { modules: false },
      entry: { main: path.join(__dirname, srcDir, "index.tsx") },
      resolve: { extensions: [".js", ".jsx", ".ts", ".tsx"] },
      output: {
        path: outPath,
        filename: "[name].js",
        publicPath: "dist"
      },
      resolve: {
        extensions: [".ts", ".tsx", ".js", ".jsx", ".json"],
        modules: [
          path.join(__dirname, "node_modules"),
          path.join(__dirname, srcDir)
        ]
      },

      devServer: {
        port: 8787,
        quiet: false,
        noInfo: true,
        hot: false
      },
      module: {
        rules: [
          {
            test: /\.js$/,
            use: ["source-map-loader"],
            enforce: "pre"
          },
          {
            test: /\.tsx?$/,
            include: /src/,
            use: "ts-loader"
          },
          {
            test: /\.css$/,
            use: isDevBuild
              ? ["style-loader", "css-loader"]
              : ExtractTextPlugin.extract({ use: "css-loader?minimize" })
          },
          {
            test: /\.less$/,
            use: isDevBuild
              ? ["style-loader", "css-loader", "less-loader"]
              : ExtractTextPlugin.extract({
                  use: ["css-loader?minimize", "less-loader"]
                })
          },
          {
            test: /\.(png|svg|jpg|gif)$/,
            use: "file-loader"
          },
          {
            test: /\.(woff|woff2|eot|ttf)(\?|$)/,
            use: "url-loader?limit=100000"
          }
        ]
      },
      plugins: [
        new webpack.DllReferencePlugin({
          context: __dirname,
          manifest: require(path.join(outPath, "vendor-manifest.json"))
        })
      ].concat(
        isDevBuild
          ? [
              // Plugins that apply in development builds only
              new webpack.SourceMapDevToolPlugin({
                moduleFilenameTemplate: path.relative(outPath, "[resourcePath]") // Point sourcemap entries to the original file locations on disk
              })
            ]
          : [
              // Plugins that apply in production builds only
              new webpack.optimize.UglifyJsPlugin(),
              new ExtractTextPlugin("site.css")
            ]
      )
    }
  ];
};
