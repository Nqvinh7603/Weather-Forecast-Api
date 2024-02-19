// window.onload = function() {
//   //<editor-fold desc="Changeable Configuration Block">

//   // the following lines will be replaced by docker/configurator, when it runs in a docker-container
//   window.ui = SwaggerUIBundle({
//     url: "weather-forecast-apis-v1.0.0.yml",
//     dom_id: '#swagger-ui',
//     deepLinking: true,
//     presets: [
//       SwaggerUIBundle.presets.apis,
//       SwaggerUIStandalonePreset
//     ],
//     plugins: [
//       SwaggerUIBundle.plugins.DownloadUrl
//     ],
//     layout: "StandaloneLayout"
//   });

//   //</editor-fold>
// };
window.onload = function() {
  // Tạo một đối tượng SwaggerUIBundle và gán nó vào #swagger-ui trong HTML
  const ui = SwaggerUIBundle({
    url: "weather-forecast-apis-v1.0.0.yml",
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  });
};
