import {
  POSTS_UPDATE,
  POST_UPDATE,
  POST_ADD,
  POST_EDIT,
  POST_DELETE,
  POST_RESET
} from './actions';

const PostReducer = (state, action) => {
  switch(action.type) {
    case POSTS_UPDATE:
      return {
        ...state,
        data: action.payload.posts
      }
    case POST_UPDATE:
      return {
        ...state,
        singlePost: action.payload
      }
    case POST_ADD:
      return {
        ...state,
        data: [...state.data, action.payload]
      }
    case POST_EDIT:
      return {
        ...state,
        singlePost: action.payload
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
    case POST_RESET:
      return {
        ...state,
        singlePost: {}
      }
    default:
      return state
  }
}

export default PostReducer