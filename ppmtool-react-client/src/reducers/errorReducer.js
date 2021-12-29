import { GET_ERROR } from "../actions/types";

const initialState = {};
const errorReducer = (state = initialState, action) => {
  if (action.type === GET_ERROR) {
    return action.payload;
  }
  return state;
};
export default errorReducer;
