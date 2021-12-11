import {
  POSTS_UPDATE
} from './actions';

const PostReducer = (state, action) => {
  switch(action.type) {
    case POSTS_UPDATE:
      return {
        ...state,
        data: action.payload
      }
    default:
      return state
  }
}

export default PostReducer