import {
  THREADS_UPDATE,
  THREAD_UPDATE,
  THREAD_ADD,
  THREAD_EDIT,
  THREAD_DELETE,
  THREAD_RESET
} from './actions';

const ThreadReducer = (state, action) => {
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
          // Credit: https://stackoverflow.com/a/34582848
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

export default ThreadReducer