import {
  POSTS_UPDATE,
  POST_ADD,
  POST_DELETE
} from './actions';

const PostReducer = (state, action) => {
  switch(action.type) {
    case POSTS_UPDATE:
      return {
        ...state,
        data: action.payload.posts
      }
    case POST_ADD:
      return {
        ...state,
        data: [...state.data, action.payload]
      }
    case POST_DELETE:
      return {
        ...state,
        data: [
          // Credit: https://stackoverflow.com/a/34582848
          ...state.data.slice(0, state.data.indexOf(action.payload)),
          ...state.data.slice(state.data.indexOf(action.payload) + 1)
        ]
      }
    default:
      return state
  }
}

export default PostReducer