import {
  USER_CREATE,
  USER_LOGIN,
  USER_LOGOUT,
  THREADS_UPDATE,
  THREAD_UPDATE,
  THREAD_ADD,
  THREAD_EDIT,
  THREAD_DELETE,
  THREAD_RESET,
  POSTS_UPDATE
} from './actions';

const authReducer = (state, action) => {
  switch(action.type) {
    case USER_CREATE:
      return {
        ...state,
        token: action.payload.token,
        user: action.payload.user,
      }
    case USER_LOGIN:
      localStorage.setItem('user', JSON.stringify(action.payload));
      return {
        ...state,
        token: action.payload.token,
        user: action.payload
      }
    case USER_LOGOUT:
      localStorage.removeItem('user');
      return {
        ...state,
        token: null,
        user: null
      }
    default:
      return state
  }
};

const threadReducer = (state, action) => {
  switch(action.type) {
    case THREADS_UPDATE:
      return {
        ...state,
        data: action.payload
      }
    case THREAD_UPDATE:
      return {
        ...state,
        singleThread: action.payload
      }
    case THREAD_ADD:
      return {
        ...state,
        data: [...state.data, action.payload]
      }
    case THREAD_EDIT:
      return {
        ...state,
        singleThread: action.payload
      }
    case THREAD_DELETE:
      return {
        ...state,
        data: [
          ...state.data.slice(0, state.data.indexOf(action.payload)),
          ...state.data.slice(state.data.indexOf(action.payload) + 1)
        ]
      }
    case THREAD_RESET:
      return {
        ...state,
        singleThread: {}
      }
    default:
      return state
  }
}

const postReducer = (state, action) => {
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

export { authReducer, threadReducer, postReducer };