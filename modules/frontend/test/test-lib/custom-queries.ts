import { queryHelpers, buildQueries } from '@testing-library/react'
import {Matcher, MatcherOptions} from "@testing-library/dom/matches";

const queryAllByName = (container: HTMLElement, id: Matcher, options?: MatcherOptions) =>
  queryHelpers.queryAllByAttribute('name', container, id, options);

const getMultipleError = (c: HTMLElement, name: Matcher) =>
  `Found multiple elements with the name: ${name}`;
const getMissingError = (c: HTMLElement, name: Matcher) =>
  `Unable to find an element with the name: ${name}`;

// noinspection JSUnusedGlobalSymbols
const [
  queryByName,
  getAllByName,
  getByName,
  findAllByName,
  findByName,
] = buildQueries(queryAllByName, getMultipleError, getMissingError);

export {
  queryByName,
  queryAllByName,
  getByName,
  getAllByName,
  findAllByName,
  findByName,
}
