import {
  PAGE_UPDATE
} from './actions';

const PageReducer = (state, action) => {
  switch(action.type) {
    case PAGE_UPDATE:
      return {
        ...state,
        current: action.payload === 'sitename' ? 'forum' : action.payload
      }
    default:
      return state
  }
}

export default PageReducer