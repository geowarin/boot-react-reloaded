import * as React from "react";
import {Component, ElementType} from "react";
import {render} from "react-dom";

declare var currentPage: string;

const pages: { [key: string]: Promise<{ default: ElementType }> } = {
  "pages/toto.tsx": import("./pages/toto")
};

let page = pages[currentPage] || Promise.reject(`Could not find ${currentPage}`);
page.then(module => {
  let Component = module.default;
  const rootElement = document.getElementById("root");
  render(<Component/>, rootElement);
});
