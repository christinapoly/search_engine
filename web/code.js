/* jshint esversion: 6 */

const performSearch = () => {
  fetch("/search?q=" + document.getElementById("searchbox").value)
    .then((response) => response.json())
    .then((data) => {
      if (data.length === 0) {
        document.getElementById("responsesize").innerHTML =
          "<p>No web page contains the query word.</p>";
      } else {
        document.getElementById("responsesize").innerHTML =
          "<p>" + data.length + " websites retrieved</p>";
      }
      let results = data
        .map((page) => `<li><a href="${page.url}">${page.title}</a></li>`)
        .join("\n");
      document.getElementById("urllist").innerHTML = `<ul>${results}</ul>`;
    });
};

document.getElementById("searchbutton").onclick = performSearch;

document.getElementById("searchbox").onkeydown = (event) => {
  if (event.key === "Enter") {
    performSearch();
  }
};
