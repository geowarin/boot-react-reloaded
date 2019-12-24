import {queries, render, RenderOptions} from '@testing-library/react'
import * as customQueries from './custom-queries'
import * as React from "react";
import userEvent from "@testing-library/user-event";
import waitForExpect from "wait-for-expect";

const customRender = (ui: React.ReactElement, options?: Omit<RenderOptions, 'queries'>) =>
  render(ui, {queries: {...queries, ...customQueries}, ...options});

// re-export everything
export * from '@testing-library/react'

// override render method
export {
  customRender as render,
  userEvent,
  waitForExpect
}
